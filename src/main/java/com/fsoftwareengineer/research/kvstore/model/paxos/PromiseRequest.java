package com.fsoftwareengineer.research.kvstore.model.paxos;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PromiseRequest {
  // Can you please promise this generation?
  private Generation generation;
  private DataModel proposedValue;

  @Override
  public String toString() {
    return "PromiseRequest{" +
        "generation=" + generation +
        ", proposedValue=" + proposedValue +
        '}';
  }
}
