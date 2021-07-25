package org.roybond007;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;
import org.springframework.data.mongodb.core.schema.JsonSchemaProperty;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.data.mongodb.core.query.Collation.Alternate;
import org.springframework.data.mongodb.core.query.Collation.CaseFirst;
import org.springframework.data.mongodb.core.query.Collation.ComparisonLevel;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;

import static org.springframework.data.mongodb.core.query.Criteria.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.roybond007.model.entity.UserEntity;
import org.roybond007.model.helper.EntityReferenceWithTimestamp;
import org.roybond007.model.helper.RatingReference;


@EnableEurekaClient
@SpringBootApplication
public class ImdbCloneUserserviceApplication implements CommandLineRunner{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(ImdbCloneUserserviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		
		createUserEntity(mongoTemplate);
		createAdminUser(mongoTemplate);
		
	}

	private void createAdminUser(MongoTemplate mongoTemplate2) {
		Query query = new Query(where("_id").regex("^Admin@"));
		
		UserEntity userEntity = mongoTemplate.findOne(query, UserEntity.class);
		
		if(userEntity == null) {
			UserEntity target = new UserEntity();
			target.setActive(true);
			target.setEmailId("adminuser199907@gmail.com");
			target.setFollowerList(new ArrayList<EntityReferenceWithTimestamp>());
			target.setFollowingList(new ArrayList<EntityReferenceWithTimestamp>());
			target.setId("Admin@" + System.currentTimeMillis());
			target.setPassword(passwordEncoder.encode("admin3289@4556"));
			target.setProfilePictureLink("profiles/default.jpg");
			target.setRatingList(new ArrayList<RatingReference>());
			target.setReviewList(new ArrayList<String>());
			target.setRoles(new String[] {"ADMIN"});
			target.setUserId("adminuser21353");
			target.setWatchList(new ArrayList<EntityReferenceWithTimestamp>());
			target.setWishList(new ArrayList<EntityReferenceWithTimestamp>());
			
			mongoTemplate.save(target);
		}
		
	}

	private void createUserEntity(MongoTemplate mongoTemplate2) {
		if(!mongoTemplate.collectionExists(UserEntity.class)) {
			
			Collation userEntityCollation = Collation
												.of(Locale.ENGLISH)
												.alternate(Alternate.shifted().space())
												.backwards(false)
												.caseFirst(CaseFirst.off())
												.caseLevel(false)
												.normalization(false)
												.numericOrdering(false)
												.strength(ComparisonLevel.secondary());
			
			MongoJsonSchema userEntitySchema = MongoJsonSchema
													.builder()
														.required("_id", "userId", "password", "roles"
																, "isActive", "emailId", "profilePictureLink"
																, "noOfFollowers", "noOfFollowings", "noOfMovieReviewed"
																, "noOfMovieRated", "wishListLength", "watchListLength"
																, "followerList", "followingList", "ratingList"
																, "reviewList", "wishList", "watchList"
														)
														.properties(
																JsonSchemaProperty.string("_id"),
																JsonSchemaProperty.string("userId").minLength(10).maxLength(30),
																JsonSchemaProperty.string("password").minLength(10),
																JsonSchemaProperty.array("roles")
																	.items(JsonSchemaObject.string()
																	.possibleValues(Arrays.asList("USER", "ADMIN"))),
																JsonSchemaProperty.bool("isActive"),
																JsonSchemaProperty.string("emailId"),
																JsonSchemaProperty.string("profilePictureLink"),
																JsonSchemaProperty.int64("noOfFollowers").gte(0),
																JsonSchemaProperty.int64("noOfFollowings").gte(0),
																JsonSchemaProperty.int64("noOfMovieReviewed").gte(0),
																JsonSchemaProperty.int64("noOfMovieRated").gte(0),
																JsonSchemaProperty.int64("wishListLength").gte(0),
																JsonSchemaProperty.int64("watchListLength").gte(0),
																JsonSchemaProperty.array("followerList")
																	.items(JsonSchemaObject.object()
																				.required("_id", "timestamp")
																				.properties(JsonSchemaProperty.string("_id"),
																							JsonSchemaProperty.int64("timestamp")
																				)
																	),
																JsonSchemaProperty.array("followingList")
																	.items(JsonSchemaObject.object()
																				.required("_id", "timestamp")
																				.properties(JsonSchemaProperty.string("_id"),
																							JsonSchemaProperty.int64("timestamp")
																				)
																	),
																JsonSchemaProperty.array("ratingList")
																	.items(JsonSchemaObject.object()
																				.required("_id", "rating", "timestamp")
																				.properties(JsonSchemaProperty.string("_id"),
																							JsonSchemaProperty.float64("rating"),
																							JsonSchemaProperty.int64("timestamp")
																				)
																	),
																JsonSchemaProperty.array("reviewList")
																	.items(JsonSchemaObject.object()
																				.required("_id", "timestamp")
																				.properties(JsonSchemaProperty.string("_id")
																						, JsonSchemaProperty.int64("timestamp")
																				)
																	),
																JsonSchemaProperty.array("wishList")
																	.items(JsonSchemaObject.object()
																				.required("_id", "timestamp")
																				.properties(JsonSchemaProperty.string("_id"),
																							JsonSchemaProperty.int64("timestamp")
																				)
																	),
																JsonSchemaProperty.array("watchList")
																	.items(JsonSchemaObject.object()
																				.required("_id", "timestamp")
																				.properties(JsonSchemaProperty.string("_id"),
																							JsonSchemaProperty.int64("timestamp")
																				)
																	)
														)
														.description("root schema mismatch")
													.build();
			
			mongoTemplate.createCollection(UserEntity.class
					, CollectionOptions.empty()
					.collation(userEntityCollation)
					.schema(userEntitySchema)
					.schemaValidationAction(ValidationAction.ERROR)
					.schemaValidationLevel(ValidationLevel.STRICT)
			);
			
			
			mongoTemplate.indexOps(UserEntity.class)
				.ensureIndex(
						new Index().collation(userEntityCollation)
						.named("userEntityUserIdIndex").on("userId", Direction.DESC)
						.unique()
				);
			
			mongoTemplate.indexOps(UserEntity.class)
				.ensureIndex(
						new Index().collation(userEntityCollation)
						.named("userEntityEmailIdIndex").on("emailId", Direction.DESC)
						.unique()
				);
			
			mongoTemplate.indexOps(UserEntity.class)
			.ensureIndex(
					new Index().collation(userEntityCollation)
					.named("userEntityFollowerListUserIdIndex").on("followerList._id", Direction.DESC)
					.unique()
			);
			
			mongoTemplate.indexOps(UserEntity.class)
			.ensureIndex(
					new Index().collation(userEntityCollation)
					.named("userEntityFollowingListUserIdIndex").on("followingList._id", Direction.DESC)
					.unique()
			);
			
			mongoTemplate.indexOps(UserEntity.class)
			.ensureIndex(
					new Index().collation(userEntityCollation)
					.named("userEntityWishListUserIdIndex").on("wishList._id", Direction.DESC)
					.unique()
			);
			
			mongoTemplate.indexOps(UserEntity.class)
			.ensureIndex(
					new Index().collation(userEntityCollation)
					.named("userEntityWatchListUserIdIndex").on("watchList._id", Direction.DESC)
					.unique()
			);
			
			mongoTemplate.indexOps(UserEntity.class)
			.ensureIndex(
					new Index().collation(userEntityCollation)
					.named("userEntityRatingListUserIdIndex").on("ratingList._id", Direction.DESC)
					.unique()
			);
		}
		
	}
	
}
