package org.roybond007.repositories;

import org.roybond007.model.entity.ReplyEntity;
import org.roybond007.model.helper.ReplyDataFetchObject;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReplyEntityRepository
		extends MongoRepository<ReplyEntity, String>, ReplyEntityRepositoryCustom {

	@Meta(allowDiskUse = true)
	@Aggregation(pipeline = {
			"{\r\n"
			+ "        $match: {\r\n"
			+ "            _id: ?1\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $set: {\r\n"
			+ "            userReact: {\r\n"
			+ "                $cond: {\r\n"
			+ "                    if: {\r\n"
			+ "                        $eq: [\r\n"
			+ "                            {\r\n"
			+ "                                $size: {\r\n"
			+ "                                    $filter: {\r\n"
			+ "                                        input: \"$likeList\",\r\n"
			+ "                                        as: \"elem\",\r\n"
			+ "                                        cond: {\r\n"
			+ "                                            $eq: [\"$$elem._id\", ?0]\r\n"
			+ "                                        }\r\n"
			+ "                                    }\r\n"
			+ "                                }\r\n"
			+ "                            },\r\n"
			+ "                            1\r\n"
			+ "                        ]\r\n"
			+ "                    },\r\n"
			+ "                    then: true,\r\n"
			+ "                    else: false\r\n"
			+ "                }\r\n"
			+ "            }\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $lookup: {\r\n"
			+ "            from: \"userEntity\",\r\n"
			+ "            let: {\r\n"
			+ "                userId: \"$userId\"\r\n"
			+ "            },\r\n"
			+ "            pipeline: [\r\n"
			+ "                {\r\n"
			+ "                    $match: {\r\n"
			+ "                        $expr: {\r\n"
			+ "                            $eq: [\"$userId\", \"$$userId\"]\r\n"
			+ "                        }\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                {\r\n"
			+ "                    $project: {\r\n"
			+ "                        _id: \"$userId\",\r\n"
			+ "                        profilePictureLink: 1\r\n"
			+ "                    }\r\n"
			+ "                }\r\n"
			+ "            ],\r\n"
			+ "            as: \"targetUser\"\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $project: {\r\n"
			+ "            _id: 1,\r\n"
			+ "            timestamp: 1,\r\n"
			+ "            content: 1,\r\n"
			+ "            userObject: {\r\n"
			+ "                $first: \"$targetUser\"\r\n"
			+ "            },\r\n"
			+ "            noOfLikes: 1,\r\n"
			+ "            userReact: 1\r\n"
			+ "        }\r\n"
			+ "    }"
	})
	ReplyDataFetchObject findSingleReply(String userId, String replyId);

	
	
	
}
