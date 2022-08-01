package com.fsoftwareengineer.research.kvstore.service.paxos.acceptor;

import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseRequest;

import java.util.Optional;

public interface IAcceptorStateService {

  Optional<AcceptRequest> getExistingAcceptedRequest(String key);
  void saveAcceptedRequest(AcceptRequest acceptRequest);
  Optional<PromiseRequest> getExistingProposeRequest(String key);
  void savePromiseRequest(PromiseRequest promiseRequest);
  void removeAcceptedRequest(String key);
  void removePromiseRequest(String key);
}
