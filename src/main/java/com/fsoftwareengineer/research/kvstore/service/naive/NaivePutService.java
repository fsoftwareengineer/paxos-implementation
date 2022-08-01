package com.fsoftwareengineer.research.kvstore.service.naive;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import com.fsoftwareengineer.research.kvstore.persistence.IDataModelPersistence;
import com.fsoftwareengineer.research.kvstore.service.IPutService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NaivePutService implements IPutService {

  @Autowired
  private final IDataModelPersistence persistence;

  @Override
  public DataModel put(String key, String value) {
    return persistence.save(new DataModel(key, value));
  }
}
