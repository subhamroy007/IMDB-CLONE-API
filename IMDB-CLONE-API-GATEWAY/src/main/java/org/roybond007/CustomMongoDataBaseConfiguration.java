package org.roybond007;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
public class CustomMongoDataBaseConfiguration extends AbstractReactiveMongoConfiguration {

	@Override
	protected String getDatabaseName() {

		return "imdb-clone-db";
	}

	@Override
	protected MongoClientSettings mongoClientSettings() {
		ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

		MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.build();
		return clientSettings;
	}

	@Override
	protected MongoClient createReactiveMongoClient(MongoClientSettings settings) {

		return MongoClients.create(settings);
	}

	@Override
	public ReactiveMongoDatabaseFactory reactiveMongoDbFactory() {
		ReactiveMongoDatabaseFactory factory = new SimpleReactiveMongoDatabaseFactory
				(createReactiveMongoClient(mongoClientSettings()), getDatabaseName());
		
		
		
		return factory;
	}

	@Override
	public ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory databaseFactory,
			MappingMongoConverter mongoConverter) {
		
		ReactiveMongoTemplate template = new ReactiveMongoTemplate(databaseFactory, mongoConverter);
		template.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
		
		return template;
	}
	
}
