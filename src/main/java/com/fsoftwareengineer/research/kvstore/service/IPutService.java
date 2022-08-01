package com.fsoftwareengineer.research.kvstore.service;

import com.fsoftwareengineer.research.kvstore.model.DataModel;

public interface IPutService {

  DataModel put(String key, String value);
}
