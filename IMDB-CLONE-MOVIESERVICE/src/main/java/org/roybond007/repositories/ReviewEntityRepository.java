package org.roybond007.repositories;

import org.roybond007.model.dto.ReplyDataFetchResponseBody;
import org.roybond007.model.entity.ReviewEntity;
import org.roybond007.model.helper.ReviewDataFetchObject;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewEntityRepository 
		extends MongoRepository<ReviewEntity, String>, ReviewEntityRepositoryCustom {

	@Meta(allowDiskUse = true)
	@Aggregation(pipeline = {
			"{\r\n"
			+ "        $match: {\r\n"
			+ "            _id: ?1\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $project: {\r\n"
			+ "            _id: 1,\r\n"
			+ "            timestamp: 1,\r\n"
			+ "            userId: 1,\r\n"
			+ "            content: 1,\r\n"
			+ "            noOfLikes: 1,\r\n"
			+ "            noOfReplies: 1,\r\n"
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
			+ "            },\r\n"
			+ "            replyListPage: {\r\n"
			+ "                $slice: [\"$replyList\", ?2, ?3]\r\n"
			+ "            }\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $unwind: {\r\n"
			+ "            path: \"$replyListPage\",\r\n"
			+ "            preserveNullAndEmptyArrays: true\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $lookup: {\r\n"
			+ "            from: \"replyEntity\",\r\n"
			+ "            let: {\r\n"
			+ "                replyId: \"$replyListPage._id\"\r\n"
			+ "            },\r\n"
			+ "            pipeline: [\r\n"
			+ "                {\r\n"
			+ "                    $match: {\r\n"
			+ "                        $expr: {\r\n"
			+ "                            $eq: [\"$_id\", \"$$replyId\"]\r\n"
			+ "                        }\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                {\r\n"
			+ "                    $set: {\r\n"
			+ "                        userReact: {\r\n"
			+ "                            $cond: {\r\n"
			+ "                                if: {\r\n"
			+ "                                    $eq: [\r\n"
			+ "                                        {\r\n"
			+ "                                            $size: {\r\n"
			+ "                                                $filter: {\r\n"
			+ "                                                    input: \"$likeList\",\r\n"
			+ "                                                    as: \"elem\",\r\n"
			+ "                                                    cond: {\r\n"
			+ "                                                        $eq: [\"$$elem._id\", ?0]\r\n"
			+ "                                                    }\r\n"
			+ "                                                }\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        1\r\n"
			+ "                                    ]\r\n"
			+ "                                },\r\n"
			+ "                                then: true,\r\n"
			+ "                                else: false\r\n"
			+ "                            }\r\n"
			+ "                        }\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                {\r\n"
			+ "                    $lookup: {\r\n"
			+ "                        from: \"userEntity\",\r\n"
			+ "                        let: {\r\n"
			+ "                            userId: \"$userId\"\r\n"
			+ "                        },\r\n"
			+ "                        pipeline: [\r\n"
			+ "                            {\r\n"
			+ "                                $match: {\r\n"
			+ "                                    $expr: {\r\n"
			+ "                                        $eq: [\"$userId\", \"$$userId\"]\r\n"
			+ "                                    }\r\n"
			+ "                                }\r\n"
			+ "                            },\r\n"
			+ "                            {\r\n"
			+ "                                $project: {\r\n"
			+ "                                    _id: \"$userId\",\r\n"
			+ "                                    profilePictureLink: 1\r\n"
			+ "                                }\r\n"
			+ "                            }\r\n"
			+ "                        ],\r\n"
			+ "                        as: \"targetUser\"\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                {\r\n"
			+ "                    $project: {\r\n"
			+ "                        _id: 1,\r\n"
			+ "                        timestamp: 1,\r\n"
			+ "                        content: 1,\r\n"
			+ "                        userObject: {\r\n"
			+ "                            $first: \"$targetUser\"\r\n"
			+ "                        },\r\n"
			+ "                        noOfLikes: 1,\r\n"
			+ "                        userReact: 1\r\n"
			+ "                    }\r\n"
			+ "                }\r\n"
			+ "            ],\r\n"
			+ "            as: \"target\"\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $group: {\r\n"
			+ "            _id: \"$_id\",\r\n"
			+ "            timestamp: {\r\n"
			+ "                $first: \"$timestamp\"\r\n"
			+ "            },\r\n"
			+ "            userId: {\r\n"
			+ "                $first: \"$userId\"\r\n"
			+ "            },\r\n"
			+ "            content: {\r\n"
			+ "                $first: \"$content\"\r\n"
			+ "            },\r\n"
			+ "            noOfLikes: {\r\n"
			+ "                $first: \"$noOfLikes\"\r\n"
			+ "            },\r\n"
			+ "            noOfReplies: {\r\n"
			+ "                $first: \"$noOfReplies\"\r\n"
			+ "            },\r\n"
			+ "            userReact: {\r\n"
			+ "                $first: \"$userReact\"\r\n"
			+ "            },\r\n"
			+ "            list: {\r\n"
			+ "                $push: {\r\n"
			+ "                    $first: \"$target\"\r\n"
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
			+ "            userObject: {\r\n"
			+ "                $first: \"$targetUser\"\r\n"
			+ "            },\r\n"
			+ "            content: 1,\r\n"
			+ "            noOfLikes: 1,\r\n"
			+ "            noOfReplies: 1,\r\n"
			+ "            userReact: 1,\r\n"
			+ "            replyList: {\r\n"
			+ "                result: \"$list\"\r\n"
			+ "            }\r\n"
			+ "        }\r\n"
			+ "    }"
	})
	ReviewDataFetchObject findSingleReview(String userId, String reviewId, int index, int length);

	@Meta(allowDiskUse = true)
	@Aggregation(pipeline = {
			"{\r\n"
			+ "        $match: {\r\n"
			+ "            _id: ?1\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $project: {\r\n"
			+ "            _id: 1,\r\n"
			+ "            replyListPage: {\r\n"
			+ "                $slice: [\"$replyList\", ?2, ?3]\r\n"
			+ "            }\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $unwind: {\r\n"
			+ "            path: \"$replyListPage\",\r\n"
			+ "            preserveNullAndEmptyArrays: true\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $lookup: {\r\n"
			+ "            from: \"replyEntity\",\r\n"
			+ "            let: {\r\n"
			+ "                replyId: \"$replyListPage._id\"\r\n"
			+ "            },\r\n"
			+ "            pipeline: [\r\n"
			+ "                {\r\n"
			+ "                    $match: {\r\n"
			+ "                        $expr: {\r\n"
			+ "                            $eq: [\"$_id\", \"$$replyId\"]\r\n"
			+ "                        }\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                {\r\n"
			+ "                    $set: {\r\n"
			+ "                        userReact: {\r\n"
			+ "                            $cond: {\r\n"
			+ "                                if: {\r\n"
			+ "                                    $eq: [\r\n"
			+ "                                        {\r\n"
			+ "                                            $size: {\r\n"
			+ "                                                $filter: {\r\n"
			+ "                                                    input: \"$likeList\",\r\n"
			+ "                                                    as: \"elem\",\r\n"
			+ "                                                    cond: {\r\n"
			+ "                                                        $eq: [\"$$elem._id\", ?0]\r\n"
			+ "                                                    }\r\n"
			+ "                                                }\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        1\r\n"
			+ "                                    ]\r\n"
			+ "                                },\r\n"
			+ "                                then: true,\r\n"
			+ "                                else: false\r\n"
			+ "                            }\r\n"
			+ "                        }\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                {\r\n"
			+ "                    $lookup: {\r\n"
			+ "                        from: \"userEntity\",\r\n"
			+ "                        let: {\r\n"
			+ "                            userId: \"$userId\"\r\n"
			+ "                        },\r\n"
			+ "                        pipeline: [\r\n"
			+ "                            {\r\n"
			+ "                                $match: {\r\n"
			+ "                                    $expr: {\r\n"
			+ "                                        $eq: [\"$userId\", \"$$userId\"]\r\n"
			+ "                                    }\r\n"
			+ "                                }\r\n"
			+ "                            },\r\n"
			+ "                            {\r\n"
			+ "                                $project: {\r\n"
			+ "                                    _id: \"$userId\",\r\n"
			+ "                                    profilePictureLink: 1\r\n"
			+ "                                }\r\n"
			+ "                            }\r\n"
			+ "                        ],\r\n"
			+ "                        as: \"targetUser\"\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                {\r\n"
			+ "                    $project: {\r\n"
			+ "                        _id: 1,\r\n"
			+ "                        timestamp: 1,\r\n"
			+ "                        content: 1,\r\n"
			+ "                        userObject: {\r\n"
			+ "                            $first: \"$targetUser\"\r\n"
			+ "                        },\r\n"
			+ "                        noOfLikes: 1,\r\n"
			+ "                        userReact: 1\r\n"
			+ "                    }\r\n"
			+ "                }\r\n"
			+ "            ],\r\n"
			+ "            as: \"target\"\r\n"
			+ "        }\r\n"
			+ "    }",
			"{\r\n"
			+ "        $group: {\r\n"
			+ "            _id: \"$_id\",\r\n"
			+ "            result: {\r\n"
			+ "                $push: {\r\n"
			+ "                    $first: \"$target\"\r\n"
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
	ReplyDataFetchResponseBody findReplyList(String userId, String reviewId, int index, int length);

	
}
