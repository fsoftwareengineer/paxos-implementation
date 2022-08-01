package com.fsoftwareengineer.research.kvstore.model.paxos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AcceptResponse {

  private final boolean accepted;
}
