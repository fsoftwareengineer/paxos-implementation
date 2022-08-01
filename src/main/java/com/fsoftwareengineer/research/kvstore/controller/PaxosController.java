package com.fsoftwareengineer.research.kvstore.controller;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.AcceptResponse;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseRequest;
import com.fsoftwareengineer.research.kvstore.model.paxos.PromiseResponse;
import com.fsoftwareengineer.research.kvstore.service.paxos.acceptor.IAcceptorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Internal controller to process paxos consensus.
 */
@RestController
@AllArgsConstructor
@RequestMapping("consensus")
public class PaxosController {

  private final IAcceptorService acceptorService;


  @PostMapping("/propose")
  public @ResponseBody PromiseResponse propose(@RequestBody PromiseRequest promiseRequest) {

    // right now just promise all values for testing APIs.
    return acceptorService.promise(promiseRequest).get();
  }

  @PostMapping("/accept")
  public @ResponseBody AcceptResponse accept(@RequestBody AcceptRequest acceptRequest) {

    // right now just promise all values for testing APIs.
    return acceptorService.accept(acceptRequest).get();
  }

  @PostMapping("/commit")
  public @ResponseBody DataModel commit(@RequestBody AcceptRequest acceptRequest) {

    // right now just promise all values for testing APIs.
    return acceptorService.commit(acceptRequest);
  }
}
