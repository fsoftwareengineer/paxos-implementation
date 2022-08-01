package com.fsoftwareengineer.research.kvstore.service.paxos.message;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
public class HttpMessenger<RQ, RP> implements IMessenger<RQ, RP> {

  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
  private final String url;

  @Override
  public RP send(RQ rq, Class<RP> responseType) throws IOException {
    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    RequestBody body = RequestBody.create(gson.toJson(rq), JSON);
    Request request = new Request.Builder()
        .url(url)
        .post(body)
        .build();
    try (Response response = client.newCall(request).execute()) {
      return gson.fromJson(Objects.requireNonNull(response.body()).string(), responseType);
    }
  }

}
