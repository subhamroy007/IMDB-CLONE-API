package org.roybond007.configs;

import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class CustomMongoDataBaseConfiguration extends AbstractMongoClientConfiguration{

	@Override
	protected String getDatabaseName() {
		
		return "imdb-clone-db";
	}

	@Override
	protected MongoClientSettings mongoClientSettings() {
		
		ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
		
		MongoClientSettings clientSettings = MongoClientSettings
												.builder()
												.applyConnectionString(connectionString)
												.build();
		return clientSettings;
	}
	
	@Override
	protected MongoClient createMongoClient(MongoClientSettings settings) {
		
		return MongoClients.create(settings);
	}
	
	@Override
	public MongoDatabaseFactory mongoDbFactory() {
		MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(
				createMongoClient(mongoClientSettings()), getDatabaseName());
		
		
		
		return factory;
	}
	
	@Override
	public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
		
		MongoTemplate template = new MongoTemplate(databaseFactory, converter);
		template.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
		
		return template;
	}
	
}
