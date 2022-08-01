package com.fsoftwareengineer.research.kvstore.service.paxos.acceptor;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptResponse;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseResponse;
import com.fsoftwareengineer.research.kvstore.persistence.IDataModelPersistence;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AcceptorService implements IAcceptorService {

  @Autowired
  private final IAcceptorStateService acceptorStateService;

  @Autowired
  private final IDataModelPersistence persistence;

  @Override
  public Optional<PromiseResponse> promise(PromiseRequest promiseRequest) {

    // do I already have an accepted value?
    Optional<AcceptRequest> existingAccept =
        acceptorStateService.getExistingAcceptedRequest(promiseRequest.getProposedValue().getKey());

    if (existingAccept.isPresent()) {
      // since I already have accepted value, just promise and return the accepted value.
      AcceptRequest accepted = existingAccept.get();
      log.info("Found existing accepted request {}", accepted);
      acceptorStateService.savePromiseRequest(promiseRequest); // save as is.
      return Optional.of(PromiseResponse.builder()
          .promisedGeneration(accepted.getGeneration())
          .acceptedValue(accepted.getProposedValue())
          .promised(true)
          .build());
    }

    // do I already have a request I promised for this key?
    Optional<PromiseRequest> existingRequest =
        acceptorStateService.getExistingProposeRequest(promiseRequest.getProposedValue().getKey());

    if (existingRequest.isPresent()) {
      // I already have it, is the request generation greater than mine?
      log.info("Found existing proposeRequest that it promised {}", existingRequest.get());
      if (promiseRequest.getGeneration().isGreaterThan(existingRequest.get().getGeneration()) ||
          promiseRequest.getGeneration().isEqual(existingRequest.get().getGeneration())) {
        // replace the existing value because I am going to promise this.
        log.info("The new request {} generation is greater than the old request {}",
            promiseRequest,
            existingRequest.get());
        acceptorStateService.savePromiseRequest(promiseRequest);
        return Optional.of(PromiseResponse.builder()
            .promised(true)
            .promisedGeneration(promiseRequest.getGeneration())
            .build());
      } else {
        log.info("The new request {} generation is not greater than the old request {}, do NOT promise.",
            promiseRequest,
            existingRequest.get());
       return Optional.of(PromiseResponse.builder()
            .promised(false)
            .build());
      }
    }

    log.info("Didn't find any existing promise or accepted value, promise this request {}", promiseRequest);
    // default promise.
    acceptorStateService.savePromiseRequest(promiseRequest);
    return Optional.of(PromiseResponse.builder()
        .promised(true)
        .promisedGeneration(promiseRequest.getGeneration())
        .build());
  }

  @Override
  public Optional<AcceptResponse> accept(AcceptRequest acceptRequest) {

    // Did I promise this value?
    Optional<PromiseRequest> existingRequest =
        acceptorStateService.getExistingProposeRequest(acceptRequest.getProposedValue().getKey());

    // Does generation match with lastly proposed generation?
    if(existingRequest.isPresent() && existingRequest.get().getGeneration().isEqual(acceptRequest.getGeneration())) {
      log.info("Found promised request {} and new accept request {}. Note that their values may differ.",
          existingRequest.get(),
          acceptRequest);
      acceptorStateService.saveAcceptedRequest(acceptRequest);
      acceptorStateService.removePromiseRequest(acceptRequest.getProposedValue().getKey());
      return Optional.of(AcceptResponse.builder().accepted(true).build());
    }

    log.info("The system did not promise this request generation {}", acceptRequest);
    return Optional.of(AcceptResponse.builder().accepted(false).build());
  }

  @Override
  public DataModel commit(AcceptRequest acceptRequest) {
    persistence.save(acceptRequest.getProposedValue());
    acceptorStateService.removePromiseRequest(acceptRequest.getProposedValue().getKey());
    return persistence.getById(acceptRequest.getProposedValue().getKey());
  }
}
