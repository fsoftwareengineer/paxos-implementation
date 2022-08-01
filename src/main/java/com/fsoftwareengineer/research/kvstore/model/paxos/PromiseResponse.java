package com.fsoftwareengineer.research.kvstore.model.paxos;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PromiseResponse {
  // I promise or promised this generation.
  private final Generation promisedGeneration;
  // I already accepted this value with promisedGeneration above.
  private final DataModel acceptedValue;
  // I promised your request or I didn't
  private final boolean promised;
}
