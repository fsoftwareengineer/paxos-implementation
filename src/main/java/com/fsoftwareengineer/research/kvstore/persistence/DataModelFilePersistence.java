package com.fsoftwareengineer.research.kvstore.persistence;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Simple file persistence. Key is the file name and Value is the content.
 */
@Repository
@Slf4j
public class DataModelFilePersistence implements IDataModelPersistence {

  private final String path;

  public DataModelFilePersistence(@Value("${serverName}") String partition) {

    ClassLoader classLoader = getClass().getClassLoader();
    this.path = Objects.requireNonNull(classLoader.getResource(".")).getPath() + partition;
    // Create a partition
    File file = new File(this.path);
    if (!file.exists()){
      log.info("Creating partition {}", partition);
      file.mkdir();
      // If you require it to make the entire directory path including parents,
      // use directory.mkdirs(); here instead.
    }

    log.info("Initializing File Persistence with root path {}, partition path{}",
        classLoader.getResource(".").getPath(),
        this.path);

  }

  @Override
  public List<DataModel> findAll() {
    return null;
  }

  @Override
  public List<DataModel> findAll(Sort sort) {
    return null;
  }

  @Override
  public List<DataModel> findAllById(Iterable<String> strings) {
    return null;
  }

  @Override
  public <S extends DataModel> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends DataModel> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends DataModel> List<S> saveAllAndFlush(Iterable<S> entities) {
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<DataModel> entities) {

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<String> strings) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public DataModel getOne(String s) {
    return null;
  }

  @Override
  public DataModel getById(String s) {
    Path filePath = Path.of(this.path + "/" + s);
    try {
      String content = Files.readString(filePath);
      return new DataModel(s, content);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public DataModel getReferenceById(String s) {
    return null;
  }

  @Override
  public <S extends DataModel> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends DataModel> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public Page<DataModel> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public <S extends DataModel> S save(S entity) {
    DataModel data = entity;
    log.info("retrieving data from path {}", this.path);
    Path path = Paths.get(this.path + "/" + data.getKey());

    // Try block to check for exceptions
    try {
      // Now calling Files.writeString() method
      // with path , content & standard charsets
      Files.writeString(path, data.getValue(), StandardCharsets.UTF_8);
    }

    // Catch block to handle the exception
    catch (IOException ex) {
      // Print messqage exception occurred as
      // invalid. directory local path is passed
      System.out.print("Invalid Path");
      throw new RuntimeException("Unable to commit");
    }

    return entity;
  }

  @Override
  public Optional<DataModel> findById(String s) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(String s) {
    return false;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(String s) {

  }

  @Override
  public void delete(DataModel entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends String> strings) {

  }

  @Override
  public void deleteAll(Iterable<? extends DataModel> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public <S extends DataModel> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends DataModel> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends DataModel> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends DataModel> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends DataModel, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }
}
