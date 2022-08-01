package com.fsoftwareengineer.research.kvstore;

import com.fsoftwareengineer.research.kvstore.persistence.DataModelFilePersistence;
import com.fsoftwareengineer.research.kvstore.persistence.IDataModelPersistence;
import com.fsoftwareengineer.research.kvstore.service.IGetService;
import com.fsoftwareengineer.research.kvstore.service.IPutService;
import com.fsoftwareengineer.research.kvstore.service.naive.NaiveGetService;
import com.fsoftwareengineer.research.kvstore.service.paxos.PaxosPutService;
import com.fsoftwareengineer.research.kvstore.service.paxos.proposer.IProposerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@SpringBootApplication
public class KvstoreApplication {

	@Value("${serverName}")
	private String serverName;

	public static void main(String[] args) {
		SpringApplication.run(KvstoreApplication.class, args);
	}

	@Primary
	@Bean(name="filePersistence")
	IDataModelPersistence getDataModelPersistence() {
		return new DataModelFilePersistence(serverName);
	}

	@Primary
	DataModelFilePersistence getDataModelFilePersistence() {
		return new DataModelFilePersistence(serverName);
	}

	@Primary
	@Bean
	IPutService getPutService(IProposerService proposerService) {
		return new PaxosPutService(proposerService);
	}

	@Primary
	@Bean
	IGetService getGetService() {
		return new NaiveGetService(getDataModelPersistence());
	}

}
