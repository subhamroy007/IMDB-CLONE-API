//query to het the paginated user wishlist of movies
let wishlistQuery = [
    {
        $match: {
            _id: "userId"
        }
    },
    {
        $project: {
            _id: 1,
            wishListPage: {
                $slice: ["$wishList", "index", "length"]
            }
        }
    },
    {
        $unwind: "$wishListPage"
    },
    {
        $lookup: {
            from: "movieEntity",
            let: {
                movieId: "$wishListPage._id"
            },
            pipeline: [
                {
                    $match: {
                        $expr: {
                            $eq: ["$_id", "$$movieId"]
                        }
                    }
                },
                {
                    $set: {
                        rating: {
                            $first: {
                                $filter: {
                                    input: "$ratingList",
                                    as: "elem",
                                    cond: {
                                        $eq: ["$$elem._id", "userId"]
                                    }
                                }
                            }
                        }
                    }
                },
                {
                    $project: {
                        _id: 1,
                        title: 1,
                        description: 1,
                        genres: 1,
                        poster: 1,
                        totalRating: 1,
                        noOfRatings: 1,
                        userRating: {
                            $ifNull: ["$rating.rating", 0]
                        }
                    }
                }
            ],
            as: "movieObject"
        }
    },
    {
        $group: {
            _id: "$_id",
            result: {
                $push: {
                    $first: "$movieObject"
                }
            }
        }
    },
    {
        $project: {
            _id: 0,
            result: 1
        }
    }
]

//following is the query for rating movie list in paginated format
let ratingListQuery = [
    {
        $match: {
            _id: "userId"
        }
    },
    {
        $project: {
            _id: 1,
            ratingListPage: {
                $slice: ["$ratingList", "index", "length"]
            }
        }
    },
    {
        $unwind: "$ratingListPage"
    },
    {
        $lookup: {
            from: "movieEntity",
            let: {
                rating: "$ratingListPage.rating",
                movieId: "$ratingListPage._id"
            },
            pipeline: [
                {
                    $match: {
                        $expr: {
                            $eq: ["$_id", "$$movieId"]
                        }
                    }
                },
                {
                    $project: {
                        _id: 1,
                        title: 1,
                        description: 1,
                        genres: 1,
                        poster: 1,
                        totalRating: 1,
                        noOfRatings: 1,
                        userRating: "$$rating"
                    }
                }
            ],
            as: "movieObject"
        }
    },
    {
        $group: {
            _id: "$_id",
            result: {
                $push: {
                    $first: "$movieObject"
                }
            }
        }
    },
    {
        $project: {
            _id: 0,
            result: 1
        }
    }
]


let fetchSingleReplyQuery = [
    {
        $match: {
            _id: "replyId"
        }
    },
    {
        $set: {
            userReact: {
                $cond: {
                    if: {
                        $eq: [
                            {
                                $size: {
                                    $filter: {
                                        input: "$likeList",
                                        as: "elem",
                                        cond: {
                                            $eq: ["$$elem._id", "userId"]
                                        }
                                    }
                                }
                            },
                            1
                        ]
                    },
                    then: true,
                    else: false
                }
            }
        }
    },
    {
        from: "UserEntity",
        let: {
            userId: "$userId"
        },
        pipeline: [
            {
                $match: {
                    $expr: {
                        $eq: ["$userId", "$$userId"]
                    }
                }
            },
            {
                _id: "$userId",
                profilePictureLink: 1
            }
        ],
        as: "target"
    },
    {
        _id: 1,
        timestamp: 1,
        content: 1,
        userObject: {
            $first: "$target"
        },
        noOfLikes: 1,
        userReact: 1
    }
]


let fetchSingleReviewQuery = [
    {
        $match: {
            _id: "reviewId"
        }
    },
    {
        $project: {
            _id: 1,
            timestamp: 1,
            userId: 1,
            content: 1,
            noOfLikes: 1,
            noOfReplies: 1,
            userReact: {
                $cond: {
                    if: {
                        $eq: [
                            {
                                $size: {
                                    $filter: {
                                        input: "$likeList",
                                        as: "elem",
                                        cond: {
                                            $eq: ["$$elem._id", "userId"]
                                        }
                                    }
                                }
                            },
                            1
                        ]
                    },
                    then: true,
                    else: false
                }
            },
            replyListPage: {
                $slice: ["$replyList", "index", "length"]
            }
        }
    },
    {
        $unwind: "$replyListPage"
    },
    {
        $lookup: {
            from: "replyEntity",
            let: {
                replyId: "$replyListPage._id"
            },
            pipeline: [,
                {
                    $match: {
                        $expr: {
                            $eq: ["$_id", "$$replyId"]
                        }
                    }
                },
                {
                    $set: {
                        userReact: {
                            $cond: {
                                if: {
                                    $eq: [
                                        {
                                            $size: {
                                                $filter: {
                                                    input: "$likeList",
                                                    as: "elem",
                                                    cond: {
                                                        $eq: ["$$elem._id", "userId"]
                                                    }
                                                }
                                            }
                                        },
                                        1
                                    ]
                                },
                                then: true,
                                else: false
                            }
                        }
                    }
                },
                {
                    from: "UserEntity",
                    let: {
                        userId: "$userId"
                    },
                    pipeline: [
                        {
                            $match: {
                                $expr: {
                                    $eq: ["$userId", "$$userId"]
                                }
                            }
                        },
                        {
                            _id: "$userId",
                            profilePictureLink: 1
                        }
                    ],
                    as: "target"
                },
                {
                    _id: 1,
                    timestamp: 1,
                    content: 1,
                    userObject: {
                        $first: "$target"
                    },
                    noOfLikes: 1,
                    userReact: 1
                }
            ],
            as: "target"
        }
    },
    {
        $group: {
            _id: "$_id",
            timestamp: {
                $first: "$timestamp"
            },
            userId: {
                $first: "$userId"
            },
            content: {
                $first: "$content"
            },
            noOfLikes: {
                $first: "$noOfLikes"
            },
            noOfReplies: {
                $first: "noOfReplies"
            },
            userReact: {
                $first: "$userReact"
            },
            replyList: {
                result: {
                    $push: {
                        $first: "$target"
                    }
                }
            }
        }
    },
    {
        from: "UserEntity",
        let: {
            userId: "$userId"
        },
        pipeline: [
            {
                $match: {
                    $expr: {
                        $eq: ["$userId", "$$userId"]
                    }
                }
            },
            {
                _id: "$userId",
                profilePictureLink: 1
            }
        ],
        as: "target"
    },
    {
        $project: {
            _id: 1,
            timestamp: 1,
            userObject: {
                $first: "$target"
            },
            content: 1,
            noOfLikes: 1,
            noOfReplies: 1,
            userReact: 1,
            replyList: 1
        }
    }
]

let fetchReplyListQuery = [
    {
        $match: {
            _id: "reviewId"
        }
    },
    {
        $project: {
            _id: 1,
            replyListPage: {
                $slice: ["$replyList", "index", "length"]
            }
        }
    },
    {
        $unwind: "$replyListPage"
    },
    {
        $lookup: {
            from: "replyEntity",
            let: {
                replyId: "$replyListPage._id"
            },
            pipeline: [
                {
                    $match: {
                        $expr: {
                            $eq: ["$_id", "$$replyId"]
                        }
                    }
                },
                {
                    $set: {
                        userReact: {
                            $cond: {
                                if: {
                                    $eq: [
                                        {
                                            $size: {
                                                $filter: {
                                                    input: "$likeList",
                                                    as: "elem",
                                                    cond: {
                                                        $eq: ["$$elem._id", "userId"]
                                                    }
                                                }
                                            }
                                        },
                                        1
                                    ]
                                },
                                then: true,
                                else: false
                            }
                        }
                    }
                },
                {
                    from: "UserEntity",
                    let: {
                        userId: "$userId"
                    },
                    pipeline: [
                        {
                            $match: {
                                $expr: {
                                    $eq: ["$userId", "$$userId"]
                                }
                            }
                        },
                        {
                            _id: "$userId",
                            profilePictureLink: 1
                        }
                    ],
                    as: "target"
                },
                {
                    _id: 1,
                    timestamp: 1,
                    content: 1,
                    userObject: {
                        $first: "$target"
                    },
                    noOfLikes: 1,
                    userReact: 1
                }
            ],
            as: "target"
        }
    },
    {
        $group: {
            _id: "$_id",
            result: {
                $push: {
                    $first: "$target"
                }
            }
        }
    },
    {
        $project: {
            _id: 0,
            result: 1
        }
    }
]


let fetchReviewListQuery = [
    {
        $match: {
            _id: "movieId"
        }
    },
    {
        $project: {
            _id: 1,
            reviewListPage: {
                $slice: ["$reviewList", "index", "length"]
            }
        }
    },
    {
        $unwind: "$reviewListPage"
    },
    {
        $lookup: {
            from: "reviewEntity",
            let: {
                reviewId: "$reviewListPage._id"
            },
            pipeline: [
                {
                    $match: {
                        $expr: {
                            $eq: ["$_id", "$$reviewId"]
                        }
                    }
                },
                {
                    $project: {
                        _id: 1,
                        timestamp: 1,
                        userId: 1,
                        content: 1,
                        noOfLikes: 1,
                        noOfReplies: 1,
                        userReact: {
                            $cond: {
                                if: {
                                    $eq: [
                                        {
                                            $size: {
                                                $filter: {
                                                    input: "$likeList",
                                                    as: "elem",
                                                    cond: {
                                                        $eq: ["$$elem._id", "userId"]
                                                    }
                                                }
                                            }
                                        },
                                        1
                                    ]
                                },
                                then: true,
                                else: false
                            }
                        },
                        replyListPage: {
                            $slice: ["$replyList", 0, "length"]
                        }
                    }
                },
                {
                    $unwind: "$replyListPage"
                },
                {
                    $lookup: {
                        from: "replyEntity",
                        let: {
                            replyId: "$replyListPage._id"
                        },
                        pipeline: [,
                            {
                                $match: {
                                    $expr: {
                                        $eq: ["$_id", "$$replyId"]
                                    }
                                }
                            },
                            {
                                $set: {
                                    userReact: {
                                        $cond: {
                                            if: {
                                                $eq: [
                                                    {
                                                        $size: {
                                                            $filter: {
                                                                input: "$likeList",
                                                                as: "elem",
                                                                cond: {
                                                                    $eq: ["$$elem._id", "userId"]
                                                                }
                                                            }
                                                        }
                                                    },
                                                    1
                                                ]
                                            },
                                            then: true,
                                            else: false
                                        }
                                    }
                                }
                            },
                            {
                                from: "UserEntity",
                                let: {
                                    userId: "$userId"
                                },
                                pipeline: [
                                    {
                                        $match: {
                                            $expr: {
                                                $eq: ["$userId", "$$userId"]
                                            }
                                        }
                                    },
                                    {
                                        _id: "$userId",
                                        profilePictureLink: 1
                                    }
                                ],
                                as: "target"
                            },
                            {
                                _id: 1,
                                timestamp: 1,
                                content: 1,
                                userObject: {
                                    $first: "$target"
                                },
                                noOfLikes: 1,
                                userReact: 1
                            }
                        ],
                        as: "target"
                    }
                },
                {
                    $group: {
                        _id: "$_id",
                        timestamp: {
                            $first: "$timestamp"
                        },
                        userId: {
                            $first: "$userId"
                        },
                        content: {
                            $first: "$content"
                        },
                        noOfLikes: {
                            $first: "$noOfLikes"
                        },
                        noOfReplies: {
                            $first: "noOfReplies"
                        },
                        userReact: {
                            $first: "$userReact"
                        },
                        replyList: {
                            result: {
                                $push: {
                                    $first: "$target"
                                }
                            }
                        }
                    }
                },
                {
                    from: "UserEntity",
                    let: {
                        userId: "$userId"
                    },
                    pipeline: [
                        {
                            $match: {
                                $expr: {
                                    $eq: ["$userId", "$$userId"]
                                }
                            }
                        },
                        {
                            _id: "$userId",
                            profilePictureLink: 1
                        }
                    ],
                    as: "target"
                },
                {
                    $project: {
                        _id: 1,
                        timestamp: 1,
                        userObject: {
                            $first: "$target"
                        },
                        content: 1,
                        noOfLikes: 1,
                        noOfReplies: 1,
                        userReact: 1,
                        replyList: 1
                    }
                }
            ],
            as: "target"
        }
    },
    {
        $group: {
            _id: "$_id",
            result: {
                $push: {
                    $first: "$target"
                }
            }
        }
    },
    {
        $project: {
            _id: 0,
            result: 1
        }
    }
]