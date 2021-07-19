package org.roybond007.repositories;

import java.util.Optional;

import org.roybond007.model.dto.MovieInfoResponseBody;
import org.roybond007.model.entity.MovieEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieEntityRepository extends PagingAndSortingRepository<MovieEntity, String> {
	
	@Meta(allowDiskUse = true)
	@Aggregation(pipeline = {
			"{\r\n"
			+ "    $match: {\r\n"
			+ "      _id: ?0\r\n"
			+ "    }\r\n"
			+ "  }",
			"{\r\n"
			+ "    $project: {\r\n"
			+ "      _id: 1,\r\n"
			+ "      title: 1,\r\n"
			+ "      description: 1,\r\n"
			+ "      genres: 1,\r\n"
			+ "      posterLink: 1,\r\n"
			+ "      trailerLink: 1,\r\n"
			+ "      avgRating: 1,\r\n"
			+ "      timestamp: 1,\r\n"
			+ "      noOfRatings: 1,\r\n"
			+ "      rating: {\r\n"
			+ "        $first: {\r\n"
			+ "          $filter: {\r\n"
			+ "            input: \"$ratingList\",\r\n"
			+ "            as: \"rating\",\r\n"
			+ "            cond: {\r\n"
			+ "              $eq: [\"$$rating._id\", ?1]\r\n"
			+ "            }\r\n"
			+ "          }\r\n"
			+ "        }\r\n"
			+ "      } \r\n"
			+ "    }\r\n"
			+ "  }",
			"{\r\n"
			+ "    $project: {\r\n"
			+ "      _id: 1,\r\n"
			+ "      title: 1,\r\n"
			+ "      description: 1,\r\n"
			+ "      genres: 1,\r\n"
			+ "      posterLink: 1,\r\n"
			+ "      trailerLink: 1,\r\n"
			+ "      avgRating: 1,\r\n"
			+ "      timestamp: 1,\r\n"
			+ "      noOfRatings: 1,\r\n"
			+ "      userRating: {\r\n"
			+ "        $ifNull: [\"$rating.rating\", 0]\r\n"
			+ "      }\r\n"
			+ "    }\r\n"
			+ "  }"
	})
	Optional<MovieInfoResponseBody> findMovieInfo(String movieId,@Nullable String userId);
	
}
