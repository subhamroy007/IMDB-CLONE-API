package org.roybond007.repositories;

import org.roybond007.model.dto.UserInfoResponseBody;
import org.roybond007.model.entity.UserEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEntityRepository extends MongoRepository<UserEntity, String>{

	
	@Meta(allowDiskUse = true)
	@Aggregation(pipeline = {
			"{\r\n"
			+ "    $match: {\r\n"
			+ "      userId: ?0,\r\n"
			+ "    },\r\n"
			+ "  }",
			"{\r\n"
			+ "    $project: {\r\n"
			+ "      _id: 1,\r\n"
			+ "      userId: 1,\r\n"
			+ "      profilePictureLink: 1,\r\n"
			+ "      isAdmin: {\r\n"
			+ "        $cond: {\r\n"
			+ "          if: {\r\n"
			+ "            $eq: [\r\n"
			+ "              {\r\n"
			+ "                $size: {\r\n"
			+ "                  $filter: {\r\n"
			+ "                    input: \"$roles\",\r\n"
			+ "                    as: \"elem\",\r\n"
			+ "                    cond: {\r\n"
			+ "                      $eq: [\"$$elem\", \"ADMIN\"],\r\n"
			+ "                    },\r\n"
			+ "                  },\r\n"
			+ "                },\r\n"
			+ "              },\r\n"
			+ "              1,\r\n"
			+ "            ],\r\n"
			+ "          },\r\n"
			+ "          then: true,\r\n"
			+ "          else: false,\r\n"
			+ "        },\r\n"
			+ "      },\r\n"
			+ "    },\r\n"
			+ "  }"
	})
	UserInfoResponseBody findUserInfo(String userId);
	
}
