package com.fsoftwareengineer.research.kvstore.service;

import com.fsoftwareengineer.research.kvstore.model.DataModel;

public interface IGetService {

  DataModel get(String key);
}
