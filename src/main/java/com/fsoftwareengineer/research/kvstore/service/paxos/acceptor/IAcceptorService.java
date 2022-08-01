package com.fsoftwareengineer.research.kvstore.service.paxos.acceptor;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptResponse;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseResponse;

import java.util.Optional;

public interface IAcceptorService {


  Optional<PromiseResponse> promise(PromiseRequest promiseRequest);
  Optional<AcceptResponse> accept(AcceptRequest acceptRequest);
  DataModel commit(AcceptRequest acceptRequest);
}
