package com.fsoftwareengineer.research.kvstore.controller;

import com.fsoftwareengineer.research.kvstore.model.DataModel;
import com.fsoftwareengineer.research.kvstore.service.IGetService;
import com.fsoftwareengineer.research.kvstore.service.IPutService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class KvController {

  @Autowired
  private final IGetService getService; // TODO: Use consensus-based get instead of direct read from persistence.

  @Autowired
  private final IPutService putService;

  @GetMapping("/get")
  public @ResponseBody DataModel get(@RequestBody String key) {
    return getService.get(key);
  }

  @PostMapping("/put")
  public @ResponseBody DataModel put(@RequestBody DataModel data) {
    return putService.put(data.getKey(), data.getValue());
  }
}
