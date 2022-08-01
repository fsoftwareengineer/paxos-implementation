package com.fsoftwareengineer.research.kvstore.service.paxos.acceptor;

import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LocalAcceptorStateService implements IAcceptorStateService{
  private static final Map<String, PromiseRequest> promisedRequests = new HashMap<>();
  private static final Map<String, AcceptRequest> acceptedRequests = new HashMap<>();

  @Override
  public Optional<AcceptRequest> getExistingAcceptedRequest(String key) {
    return Optional.ofNullable(acceptedRequests.get(key));
  }

  @Override
  public Optional<PromiseRequest> getExistingProposeRequest(String key) {
    return Optional.ofNullable(promisedRequests.get(key));
  }

  @Override
  public void saveAcceptedRequest(AcceptRequest acceptRequest) {
    acceptedRequests.put(acceptRequest.getProposedValue().getKey(), acceptRequest);
  }

  @Override
  public void savePromiseRequest(PromiseRequest promiseRequest) {
    promisedRequests.put(promiseRequest.getProposedValue().getKey(), promiseRequest);
  }

  @Override
  public void removeAcceptedRequest(String key) {
    acceptedRequests.remove(key);
  }

  @Override
  public void removePromiseRequest(String key) {
    promisedRequests.remove(key);
  }
}
