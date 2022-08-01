package com.fsoftwareengineer.research.kvstore.service.paxos.message;

import java.io.IOException;

public interface IMessenger<RQ, RS>{

  RS send(RQ request, Class<RS> responseType) throws IOException;
}
