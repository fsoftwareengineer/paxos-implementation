package com.fsoftwareengineer.research.kvstore.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@Data
@Entity
public class DataModel {
  @Id
  @Column(unique=true)
  private final String key;
  private final String value; // for the simplicity, make it a string for now.

  @Override
  public String toString() {
    return "DataModel{" +
        "key='" + key + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
