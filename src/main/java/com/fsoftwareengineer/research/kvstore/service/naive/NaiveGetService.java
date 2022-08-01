package com.fsoftwareengineer.research.kvstore.service.naive;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import com.fsoftwareengineer.research.kvstore.persistence.IDataModelPersistence;
import com.fsoftwareengineer.research.kvstore.service.IGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NaiveGetService implements IGetService {

  @Autowired
  @Qualifier("filePersistence")
  private final IDataModelPersistence persistence;

  public NaiveGetService(IDataModelPersistence persistence) {
    this.persistence = persistence;
  }

  @Override
  public DataModel get(String key) {
    return persistence.findById(key).orElse(null);
  }
}
