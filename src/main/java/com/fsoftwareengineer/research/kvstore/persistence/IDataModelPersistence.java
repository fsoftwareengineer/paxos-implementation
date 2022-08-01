package com.fsoftwareengineer.research.kvstore.persistence;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDataModelPersistence extends JpaRepository<DataModel, String> {
}
