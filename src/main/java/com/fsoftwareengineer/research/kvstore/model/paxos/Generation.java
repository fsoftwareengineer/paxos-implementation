package com.fsoftwareengineer.research.kvstore.model.paxos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Generation {
  private long generation;
  private final long serverId;
  private final String serverName; // for debugging.

  @Override
  public String toString() {
    return "Generation{" +
        "generation=" + generation +
        ", serverId=" + serverId +
        ", serverName='" + serverName + '\'' +
        '}';
  }

  public boolean isGreaterThan(final Generation generation) {
    if(this.generation > generation.getGeneration()) return true;
    if(this.generation == generation.getGeneration() && this.serverId > generation.getServerId()) return true;

    return false;
  }

  public boolean isEqual(final Generation generation) {
    return generation.getGeneration() == this.generation && generation.getServerId() == this.getServerId();
  }
}
