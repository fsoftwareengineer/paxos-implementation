package com.fsoftwareengineer.research.kvstore.service.paxos.proposer;

import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseRequest;

import java.util.Optional;

public interface IProposalStateService {
  Optional<PromiseRequest> find(String key);
  void save(PromiseRequest promiseRequest);
  void remove(PromiseRequest promiseRequest);
}
