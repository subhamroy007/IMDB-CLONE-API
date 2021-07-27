package org.roybond007.repository;

import java.util.Optional;

import org.roybond007.model.dto.EntityListUpdatedResponseBody;
import org.roybond007.model.dto.MoviePageObject;
import org.roybond007.model.entity.UserEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends PagingAndSortingRepository<UserEntity, String>, CustomUserEntityRepository{

	@Meta(allowDiskUse = true)
	@Query(fields = "{_id: 1, userId: 1, password: 1, emailId: 1, roles: 1, isActive: 1}")
	Optional<UserEntity> findByUserId(String userId);

	
	@Meta(allowDiskUse = true)
	@Aggregation(pipeline = {
			"{\r\n"
			+ "        $match: {\r\n"
			+ "            userId: ?0\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $project: {\r\n"
			+ "            _id: \"$userId\",\r\n"
			+ "            wishListPage: {\r\n"
			+ "                $slice: [\"$wishList\", ?1, ?2]\r\n"
			+ "            }\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $unwind: {\r\n"
			+ "            path: \"$wishListPage\",\r\n"
			+ "            preserveNullAndEmptyArrays: true\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $lookup: {\r\n"
			+ "            from: \"movieEntity\",\r\n"
			+ "            let: {\r\n"
			+ "                movieId: \"$wishListPage._id\"\r\n"
			+ "            },\r\n"
			+ "            pipeline: [\r\n"
			+ "                {\r\n"
			+ "                    $match: {\r\n"
			+ "                        $expr: {\r\n"
			+ "                            $eq: [\"$_id\", \"$$movieId\"]\r\n"
			+ "                        }\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                {\r\n"
			+ "                    $set: {\r\n"
			+ "                        rating: {\r\n"
			+ "                            $first: {\r\n"
			+ "                                $filter: {\r\n"
			+ "                                    input: \"$ratingList\",\r\n"
			+ "                                    as: \"elem\",\r\n"
			+ "                                    cond: {\r\n"
			+ "                                        $eq: [\"$$elem._id\", ?0]\r\n"
			+ "                                    }\r\n"
			+ "                                }\r\n"
			+ "                            }\r\n"
			+ "                        }\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                {\r\n"
			+ "                    $project: {\r\n"
			+ "                        _id: 1,\r\n"
			+ "                        title: 1,\r\n"
			+ "                        description: 1,\r\n"
			+ "                        genres: 1,\r\n"
			+ "                        poster: 1,\r\n"
			+ "                        totalRating: 1,\r\n"
			+ "                        noOfRatings: 1,\r\n"
			+ "                        userRating: {\r\n"
			+ "                            $ifNull: [\"$rating.rating\", 0]\r\n"
			+ "                        }\r\n"
			+ "                    }\r\n"
			+ "                }\r\n"
			+ "            ],\r\n"
			+ "            as: \"movieObject\"\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $group: {\r\n"
			+ "            _id: \"$_id\",\r\n"
			+ "            result: {\r\n"
			+ "                $push: {\r\n"
			+ "                    $first: \"$movieObject\"\r\n"
			+ "                }\r\n"
			+ "            }\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $project: {\r\n"
			+ "            _id: 0,\r\n"
			+ "            result: 1\r\n"
			+ "        }\r\n"
			+ "    }"
	})
	MoviePageObject findWishListMovies(String userId, int index, int length);


	@Meta(allowDiskUse = true)
	@Aggregation(pipeline = {
			"{\r\n"
					+ "        $match: {\r\n"
					+ "            userId: ?0\r\n"
					+ "        }\r\n"
					+ "    }",
					"{\r\n"
					+ "        $project: {\r\n"
					+ "            _id: \"$userId\",\r\n"
					+ "            watchListPage: {\r\n"
					+ "                $slice: [\"$watchList\", ?1, ?2]\r\n"
					+ "            }\r\n"
					+ "        }\r\n"
					+ "    }",
					"{\r\n"
					+ "        $unwind: {\r\n"
					+ "            path: \"$watchListPage\",\r\n"
					+ "            preserveNullAndEmptyArrays: true\r\n"
					+ "        }\r\n"
					+ "    }",
					"{\r\n"
					+ "        $lookup: {\r\n"
					+ "            from: \"movieEntity\",\r\n"
					+ "            let: {\r\n"
					+ "                movieId: \"$watchListPage._id\"\r\n"
					+ "            },\r\n"
					+ "            pipeline: [\r\n"
					+ "                {\r\n"
					+ "                    $match: {\r\n"
					+ "                        $expr: {\r\n"
					+ "                            $eq: [\"$_id\", \"$$movieId\"]\r\n"
					+ "                        }\r\n"
					+ "                    }\r\n"
					+ "                },\r\n"
					+ "                {\r\n"
					+ "                    $set: {\r\n"
					+ "                        rating: {\r\n"
					+ "                            $first: {\r\n"
					+ "                                $filter: {\r\n"
					+ "                                    input: \"$ratingList\",\r\n"
					+ "                                    as: \"elem\",\r\n"
					+ "                                    cond: {\r\n"
					+ "                                        $eq: [\"$$elem._id\", ?0]\r\n"
					+ "                                    }\r\n"
					+ "                                }\r\n"
					+ "                            }\r\n"
					+ "                        }\r\n"
					+ "                    }\r\n"
					+ "                },\r\n"
					+ "                {\r\n"
					+ "                    $project: {\r\n"
					+ "                        _id: 1,\r\n"
					+ "                        title: 1,\r\n"
					+ "                        description: 1,\r\n"
					+ "                        genres: 1,\r\n"
					+ "                        poster: 1,\r\n"
					+ "                        totalRating: 1,\r\n"
					+ "                        noOfRatings: 1,\r\n"
					+ "                        userRating: {\r\n"
					+ "                            $ifNull: [\"$rating.rating\", 0]\r\n"
					+ "                        }\r\n"
					+ "                    }\r\n"
					+ "                }\r\n"
					+ "            ],\r\n"
					+ "            as: \"movieObject\"\r\n"
					+ "        }\r\n"
					+ "    }",
					"{\r\n"
					+ "        $group: {\r\n"
					+ "            _id: \"$_id\",\r\n"
					+ "            result: {\r\n"
					+ "                $push: {\r\n"
					+ "                    $first: \"$movieObject\"\r\n"
					+ "                }\r\n"
					+ "            }\r\n"
					+ "        }\r\n"
					+ "    }",
					"{\r\n"
					+ "        $project: {\r\n"
					+ "            _id: 0,\r\n"
					+ "            result: 1\r\n"
					+ "        }\r\n"
					+ "    }"
	})
	MoviePageObject findWatchListMovies(String userId, int index, int length);

	
	@Meta(allowDiskUse = true)
	@Aggregation(pipeline = {
			"{\r\n"
			+ "        $match: {\r\n"
			+ "            userId: ?0\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $project: {\r\n"
			+ "            _id: \"$userId\",\r\n"
			+ "            ratingListPage: {\r\n"
			+ "                $slice: [\"$ratingList\", ?1, ?2]\r\n"
			+ "            }\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $unwind: {\r\n"
			+ "            path: \"$ratingListPage\",\r\n"
			+ "            preserveNullAndEmptyArrays: true\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $lookup: {\r\n"
			+ "            from: \"movieEntity\",\r\n"
			+ "            let: {\r\n"
			+ "                rating: \"$ratingListPage.rating\",\r\n"
			+ "                movieId: \"$ratingListPage._id\"\r\n"
			+ "            },\r\n"
			+ "            pipeline: [\r\n"
			+ "                {\r\n"
			+ "                    $match: {\r\n"
			+ "                        $expr: {\r\n"
			+ "                            $eq: [\"$_id\", \"$$movieId\"]\r\n"
			+ "                        }\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                {\r\n"
			+ "                    $project: {\r\n"
			+ "                        _id: 1,\r\n"
			+ "                        title: 1,\r\n"
			+ "                        description: 1,\r\n"
			+ "                        genres: 1,\r\n"
			+ "                        poster: 1,\r\n"
			+ "                        totalRating: 1,\r\n"
			+ "                        noOfRatings: 1,\r\n"
			+ "                        userRating: \"$$rating\"\r\n"
			+ "                    }\r\n"
			+ "                }\r\n"
			+ "            ],\r\n"
			+ "            as: \"movieObject\"\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $group: {\r\n"
			+ "            _id: \"$_id\",\r\n"
			+ "            result: {\r\n"
			+ "                $push: {\r\n"
			+ "                    $first: \"$movieObject\"\r\n"
			+ "                }\r\n"
			+ "            }\r\n"
			+ "        }       \r\n"
			+ "    }",
			"{\r\n"
			+ "        $project: {\r\n"
			+ "            _id: 0,\r\n"
			+ "            result: 1\r\n"
			+ "        }\r\n"
			+ "    }"
	})
	MoviePageObject findRatingListMovies(String userId, int index, int length);

	
}
