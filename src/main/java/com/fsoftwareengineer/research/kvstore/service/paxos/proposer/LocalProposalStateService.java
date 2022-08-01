package com.fsoftwareengineer.research.kvstore.service.paxos.proposer;

import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LocalProposalStateService implements IProposalStateService {

  // need to store all proposed data mode in memory to track different keys in transit.
  // in the future, probably need to move to local storage or database.
  private static final Map<String, PromiseRequest> proposeMap = new HashMap<>();

  @Override
  public Optional<PromiseRequest> find(String key) {
    return Optional.ofNullable(proposeMap.get(key));
  }

  @Override
  public void save(PromiseRequest promiseRequest) {
    String key = promiseRequest.getProposedValue().getKey();
    proposeMap.put(key, promiseRequest);
  }

  @Override
  public void remove(PromiseRequest promiseRequest) {
    proposeMap.remove(promiseRequest.getProposedValue().getKey());
  }
}
