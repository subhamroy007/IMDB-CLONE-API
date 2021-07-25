package org.roybond007;

import java.util.Locale;import org.roybond007.model.entity.MovieEntity;
import org.roybond007.model.entity.ReplyEntity;
import org.roybond007.model.entity.ReviewEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Collation.Alternate;
import org.springframework.data.mongodb.core.query.Collation.CaseFirst;
import org.springframework.data.mongodb.core.query.Collation.ComparisonLevel;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;
import org.springframework.data.mongodb.core.schema.JsonSchemaProperty;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;

@SpringBootApplication
public class ImdbCloneMovieserviceApplication implements CommandLineRunner{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(ImdbCloneMovieserviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		createMovieEntity();
		createReviewEntity();
		createReplyEntity();
		
	}

	private void createMovieEntity() {
		
		if(!mongoTemplate.collectionExists(MovieEntity.class)) {
			Collation movieEntityCollation = Collation
					.of(Locale.ENGLISH)
					.alternate(Alternate.shifted().space())
					.backwards(false)
					.caseFirst(CaseFirst.off())
					.caseLevel(false)
					.normalization(false)
					.numericOrdering(false)
					.strength(ComparisonLevel.secondary());
			
			
			MongoJsonSchema movieEntitySchema = MongoJsonSchema
													.builder()
													.required("_id", "title", "description", "timestamp", "length"
															, "posterLink", "trailerLink", "noOfReviews", "noOfRatings"
															, "totalRating", "genres", "ratingList", "reviewList")
													.properties(JsonSchemaProperty.string("_id")
													, JsonSchemaProperty.string("title").minLength(10).maxLength(200)
													, JsonSchemaProperty.string("description").minLength(10).maxLength(2000)
													, JsonSchemaProperty.int64("timestamp"), JsonSchemaProperty.int64("length").gt(0)
													, JsonSchemaProperty.string("posterLink"), JsonSchemaProperty.string("trailerLink")
													, JsonSchemaProperty.int64("noOfReviews").gte(0), JsonSchemaProperty.int64("noOfRatings").gte(0)
													, JsonSchemaProperty.int64("totalRating").gte(0)
													, JsonSchemaProperty.string("genres").minLength(3)
													, JsonSchemaProperty.array("ratingList")
														.items(JsonSchemaObject.object()
																.required("_id", "rating", "timestamp")
																.properties(JsonSchemaProperty.string("_id")
																, JsonSchemaProperty.int64("rating").gte(1).lte(5)
																, JsonSchemaProperty.int64("timestamp")
																)
														)
													, JsonSchemaProperty.array("reviewList")
														.items(JsonSchemaObject.object()
																.required("_id", "timestamp")
																.properties(JsonSchemaProperty.string("_id")
																, JsonSchemaProperty.int64("timestamp")
																)
														)
													
													)
													.description("movie json schema is not satisfied")
													.build();
			
			mongoTemplate
				.createCollection(MovieEntity.class
						, CollectionOptions.empty()
						.collation(movieEntityCollation)
						.schema(movieEntitySchema)
						.schemaValidationAction(ValidationAction.ERROR)
						.schemaValidationLevel(ValidationLevel.STRICT)
				);
			
			
			mongoTemplate
				.indexOps(MovieEntity.class)
				.ensureIndex(new Index()
						.collation(movieEntityCollation)
						.named("movieEntityRatingListUserIdIndex")
						.on("ratingList._id", Direction.DESC)
				);
			
			mongoTemplate
				.indexOps(MovieEntity.class)
				.ensureIndex(TextIndexDefinition
						.builder()
						.onFields("title", "description")
						.named("movieEntityTextSearchIndex")
						.withSimpleCollation()
						.build()
				);

		}
	}

	private void createReviewEntity() {
		
		if(!mongoTemplate.collectionExists(ReviewEntity.class)) {
			Collation reviewEntityCollation = Collation
					.of(Locale.ENGLISH)
					.alternate(Alternate.shifted().space())
					.backwards(false)
					.caseFirst(CaseFirst.off())
					.caseLevel(false)
					.normalization(false)
					.numericOrdering(false)
					.strength(ComparisonLevel.secondary());
			
			MongoJsonSchema reviewEntitySchema = MongoJsonSchema
													.builder()
													.required("_id", "timestamp", "userId", "movieId", "content"
															, "noOfLikes", "noOfReplies", "likeList", "replyList"
													)
													.properties(JsonSchemaProperty.string("_id"), JsonSchemaProperty.int64("timestamp")
															, JsonSchemaProperty.string("content").minLength(1)
															, JsonSchemaProperty.string("userId"), JsonSchemaProperty.string("movieId")
															, JsonSchemaProperty.int64("noOfLikes").gte(0)
															, JsonSchemaProperty.int64("noOfReplies").gte(0)
															, JsonSchemaProperty.array("likeList")
																.items(JsonSchemaObject.object()
																		.required("_id", "timestamp")
																		.properties(JsonSchemaProperty.string("_id")
																				, JsonSchemaProperty.int64("timestamp")
																		)
																)
															, JsonSchemaProperty.array("replyList")
															.items(JsonSchemaObject.object()
																	.required("_id", "timestamp")
																	.properties(JsonSchemaProperty.string("_id")
																			, JsonSchemaProperty.int64("timestamp")
																	)
															)
													)
													.description("review json schema is not satisfied")
													.build();
			
			mongoTemplate
				.createCollection(ReviewEntity.class
						, CollectionOptions.empty()
						.collation(reviewEntityCollation)
						.schema(reviewEntitySchema)
						.schemaValidationAction(ValidationAction.ERROR)
						.schemaValidationLevel(ValidationLevel.STRICT)
				);
			
			mongoTemplate
				.indexOps(ReviewEntity.class)
				.ensureIndex(new Index()
						.collation(reviewEntityCollation)
						.named("reviewEntityLikeListUserIdIndex")
						.on("likeList._id", Direction.DESC).unique()
				);
		}
		
	}

	private void createReplyEntity() {
		
		if(!mongoTemplate.collectionExists(ReplyEntity.class)) {
			Collation replyEntityCollation = Collation
					.of(Locale.ENGLISH)
					.alternate(Alternate.shifted().space())
					.backwards(false)
					.caseFirst(CaseFirst.off())
					.caseLevel(false)
					.normalization(false)
					.numericOrdering(false)
					.strength(ComparisonLevel.secondary());

			MongoJsonSchema replyEntitySchema = MongoJsonSchema
									.builder()
									.required("_id", "timestamp", "content", "userId"
											, "reviewId", "noOfLikes", "likeList"
									)
									.properties(JsonSchemaProperty.string("_id"), JsonSchemaProperty.int64("timestamp")
											, JsonSchemaProperty.string("content").minLength(1)
											, JsonSchemaProperty.string("userId"), JsonSchemaProperty.string("reviewId")
											, JsonSchemaProperty.int64("noOfLikes").gte(0)
											, JsonSchemaProperty.array("likeList")
												.items(JsonSchemaObject.object()
														.required("_id", "timestamp")
														.properties(JsonSchemaProperty.string("_id")
																, JsonSchemaProperty.int64("timestamp")
														)
												)
									)
									.description("reply json schema is not satisfied")
									.build();
			
			mongoTemplate
			.createCollection(ReplyEntity.class
			, CollectionOptions
			.empty()
			.collation(replyEntityCollation)
			.schema(replyEntitySchema)
			.schemaValidationAction(ValidationAction.ERROR)
			.schemaValidationLevel(ValidationLevel.STRICT)
			);
			
			mongoTemplate
			.indexOps(ReplyEntity.class)
			.ensureIndex(new Index().collation(replyEntityCollation)
			.named("replyEntityLikeListUserIdIndex")
			.on("likeList._id", Direction.DESC).unique()
			);
		}
		
	}
	
	
	
}
