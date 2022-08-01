package com.fsoftwareengineer.research.kvstore.service.paxos.proposer;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptResponse;
import com.fsoftwareengineer.research.kvstore.model.paxos.Generation;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseResponse;
import com.fsoftwareengineer.research.kvstore.service.paxos.message.HttpMessenger;
import com.fsoftwareengineer.research.kvstore.service.paxos.message.IMessenger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProposerService implements IProposerService {

  private static final List<IMessenger<PromiseRequest, PromiseResponse>> proposalMessengers = new ArrayList<>();
  private static final List<IMessenger<AcceptRequest, AcceptResponse>> acceptMessengers = new ArrayList<>();
  private static final List<IMessenger<AcceptRequest, DataModel>> commitMessenger = new ArrayList<>();

  static {
    proposalMessengers.add(new HttpMessenger<>("http://127.0.0.1:8080/consensus/propose"));
    proposalMessengers.add(new HttpMessenger<>("http://127.0.0.1:8081/consensus/propose"));


    acceptMessengers.add(new HttpMessenger<>("http://127.0.0.1:8080/consensus/accept"));
    acceptMessengers.add(new HttpMessenger<>("http://127.0.0.1:8081/consensus/accept"));


    commitMessenger.add(new HttpMessenger<>("http://127.0.0.1:8080/consensus/commit"));
    commitMessenger.add(new HttpMessenger<>("http://127.0.0.1:8081/consensus/commit"));
  }

  @Autowired
  private final IProposalStateService proposalStateRetriever;

  private static final long MAX_RETRY = 3;
  private static final long QUORUM = 2;

  @Override
  public Optional<PromiseResponse> propose(DataModel dataModel) {
    int retry = 0;
    while (retry < MAX_RETRY) {

      PromiseRequest promiseRequest = buildPromiseRequest(dataModel);

      // now I need to send to all acceptors.
      List<PromiseResponse> promiseResponses = sendPromiseRequest(promiseRequest);

      // responses that promised my generation
      List<PromiseResponse> promisedResponses = quorumResponses(promiseResponses);


      if (promisedResponses.size() < QUORUM) {
        retry++;
        continue; // keep retrying with the higher version.
      }

      // I have a promise now. I need to check whether any promise response has accepted value
      // generation of which is greater than the proposed request.
      // then replace that with the final response

      // there is a value that's already been accepted.
      // promised responses might already have accepted value.
      PromiseResponse accepted = getAcceptedValue(promisedResponses);
      PromiseResponse finalResponse = null;
      if(accepted != null) {
        // so replace with the accepted value before sending accept request.
        // but keep the current generation since I am the highest.
        log.info("Found already accepted version {}", accepted);
        finalResponse = PromiseResponse.builder()
            .promisedGeneration(promiseRequest.getGeneration())
            .acceptedValue(accepted.getAcceptedValue())
            .promised(true)
            .build();
      } else {
        log.info("Not found already accepted version, proceeding with the current request.");
        finalResponse = PromiseResponse.builder()
            .promisedGeneration(promiseRequest.getGeneration())
            .acceptedValue(promiseRequest.getProposedValue())
            .promised(true)
            .build();
      }

      // Need to save the state in case accept fails and new request comes in.
      proposalStateRetriever.save(PromiseRequest.builder()
              .generation(finalResponse.getPromisedGeneration())
              .proposedValue(finalResponse.getAcceptedValue())
          .build());

      log.info("final response {}", finalResponse);
      return Optional.of(finalResponse);
    }

    return Optional.empty();
  }

  @Override
  public Optional<AcceptResponse> accept(AcceptRequest acceptRequest) {
    List<AcceptResponse> responses =  acceptMessengers.stream().map(node -> {
      try {
        return node.send(acceptRequest, AcceptResponse.class);
      } catch (IOException e) {
        return null;
      }
    }).filter(Objects::nonNull).collect(Collectors.toList());

    return responses.stream().filter(AcceptResponse::isAccepted).findFirst();
  }

  @Override
  public Optional<DataModel> commit(AcceptRequest acceptRequest) {
    List<DataModel> dataModels = commitMessenger.stream().map(node -> {
      try {
        return node.send(acceptRequest, DataModel.class);
      } catch (IOException e) {
        return null;
      }
    }).filter(Objects::nonNull)
        .filter(m -> m.getValue().equals(acceptRequest.getProposedValue().getValue()))
        .collect(Collectors.toList());

    if(dataModels.size() >= QUORUM) {
      return Optional.of(dataModels.get(0));
    }

    throw new RuntimeException("Unable to commit the request " + acceptRequest.toString());
  }

  private PromiseRequest buildPromiseRequest(final DataModel dataModel) {
    Optional<PromiseRequest> oRequest = proposalStateRetriever.find(dataModel.getKey());
    PromiseRequest promiseRequest;
    // if I already have a request, then I need to increase the generation number.
    // otherwise create a new one.

    if (oRequest.isPresent()) {
      // I already have a proposed value in my system.
      Generation generation = oRequest.get().getGeneration();
      generation.setGeneration(generation.getGeneration() + 1); // increase the generation
      oRequest.get().setProposedValue(dataModel); // set the proposed value as new value.
      promiseRequest = oRequest.get();
    } else {
      promiseRequest = PromiseRequest.builder()
          .generation(Generation.builder()
              .generation(1)
              .serverId(1) // TODO: <FixMe> get from the arg input
              .serverName("Server1") // TODO: <FixMe> get from the arg input
              .build())
          .proposedValue(dataModel)
          .build();
      proposalStateRetriever.save(promiseRequest);
    }

    return promiseRequest;
  }

  private List<PromiseResponse> sendPromiseRequest(final PromiseRequest promiseRequest) {
    // synchronous for now..
    // TODO: use an execution pool
    return proposalMessengers.stream().map(node -> {
      try {
        return node.send(promiseRequest, PromiseResponse.class);
      } catch (IOException e) {
        return null;
      }
    }).filter(Objects::nonNull).collect(Collectors.toList());
  }

  private List<PromiseResponse> quorumResponses(final List<PromiseResponse> responses) {

   return responses.stream().filter(PromiseResponse::isPromised).collect(Collectors.toList());
  }


  private PromiseResponse getAcceptedValue(final List<PromiseResponse> responses) {

    List<PromiseResponse> responsesWithAcceptedValue = responses.stream()
        .filter(r -> r.getAcceptedValue() != null)
        .collect(Collectors.toList());

    if(responsesWithAcceptedValue.size() == 0) {
      return null;
    }

    PromiseResponse highestGenerationResponse = responsesWithAcceptedValue.get(0);
    for (PromiseResponse response : responsesWithAcceptedValue) {
      if (response.getPromisedGeneration().isGreaterThan(highestGenerationResponse.getPromisedGeneration())) { // only if this is accepted.
        highestGenerationResponse = response;
      }
    }

    return highestGenerationResponse;
  }
}
