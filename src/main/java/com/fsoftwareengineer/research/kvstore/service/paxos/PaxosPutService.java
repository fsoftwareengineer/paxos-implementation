package com.fsoftwareengineer.research.kvstore.service.paxos;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptResponse;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseResponse;
import com.fsoftwareengineer.research.kvstore.service.IPutService;
import com.fsoftwareengineer.research.kvstore.service.paxos.proposer.IProposerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class PaxosPutService implements IPutService {

  @Autowired
  private final IProposerService proposerService;

  @Override
  public DataModel put(String key, String value) {
    DataModel dataModel = new DataModel(key, value);

    // 1. propose the data model
    Optional<PromiseResponse> oPromiseResponse = proposerService.propose(dataModel);

    if(oPromiseResponse.isEmpty()) {
      throw new RuntimeException("Request was not fulfilled after retries");
    }

    // let's ask for accept
    PromiseResponse response = oPromiseResponse.get();
    AcceptRequest acceptRequest = new AcceptRequest(response.getPromisedGeneration(), response.getAcceptedValue());

    Optional<AcceptResponse> acceptResponse = proposerService.accept(acceptRequest);

    // commit
    log.info("Committing {}", acceptResponse.get());

    return proposerService.commit(acceptRequest).get();

  }
}
