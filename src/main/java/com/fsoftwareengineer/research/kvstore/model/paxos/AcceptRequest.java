package com.fsoftwareengineer.research.kvstore.model.paxos;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AcceptRequest {
  private final Generation generation;
  private final DataModel proposedValue;

  @Override
  public String toString() {
    return "AcceptRequest{" +
        "generation=" + generation.toString() +
        ", proposedValue=" + proposedValue.toString() +
        '}';
  }
}
