package com.fsoftwareengineer.research.kvstore.service.paxos.proposer;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptResponse;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseResponse;

import java.util.Optional;

public interface IProposerService {
  Optional<PromiseResponse> propose(DataModel dataModel);
  Optional<AcceptResponse> accept(AcceptRequest acceptRequest);
  Optional<DataModel> commit(AcceptRequest acceptRequest);
}
