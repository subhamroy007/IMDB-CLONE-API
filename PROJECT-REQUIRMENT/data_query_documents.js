//user info query
let userInfoQuery = [
  {
    $match: {
      userId: "targetId",
    },
  },
  {
    $project: {
      _id: 1,
      userId: 1,
      profilePictureLink: 1,
      isAdmin: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$roles",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem", "ADMIN"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
    },
  },
];

//query to get the profile of a user
let profileDataQuery = [
  {
    $match: {
      userId: "targetId",
    },
  },
  {
    $set: {
      isFollowing: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$followingList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "sourceId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
      isAdmin: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$roles",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem", "ADMIN"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
    },
  },
  {
    $project: {
      _id: 1,
      userId: 1,
      isAdmin: 1,
      profilePictureLink: 1,
      noOfFollowers: 1,
      noOfFollowings: 1,
      noOfMovieReviewed: 1,
      noOfMovieRated: 1,
      wishListLength: 1,
      watchListLength: 1,
      isFollowing: 1,
    },
  },
];

//query to get the paginated user wishlist of movies
let wishlistQuery = [
  {
    $match: {
      userId: "roybond007",
    },
  },
  {
    $project: {
      _id: "$userId",
      wishListPage: {
        $slice: ["$wishList", 1, 5],
      },
    },
  },
  {
    $unwind: {
      path: "$wishListPage",
      preserveNullAndEmptyArrays: true,
    },
  },
  {
    $lookup: {
      from: "movieEntity",
      let: {
        movieId: "$wishListPage._id",
      },
      pipeline: [
        {
          $match: {
            $expr: {
              $eq: ["$_id", "$$movieId"],
            },
          },
        },
        {
          $set: {
            rating: {
              $first: {
                $filter: {
                  input: "$ratingList",
                  as: "elem",
                  cond: {
                    $eq: ["$$elem._id", "roybond007"],
                  },
                },
              },
            },
          },
        },
        {
          $project: {
            _id: 1,
            title: 1,
            description: 1,
            genres: 1,
            poster: 1,
            avgRating: {
              $cond: {
                if: {
                  $eq: ["$totalRating", NumberLong(0)],
                },
                then: 0,
                else: {
                  $toDouble: {
                    $divide: [
                      { $toDouble: "$totalRating" },
                      { $toDouble: "$noOfRatings" },
                    ],
                  },
                },
              },
            },
            noOfRatings: 1,
            userRating: {
              $ifNull: ["$rating.rating", 0],
            },
          },
        },
      ],
      as: "movieObject",
    },
  },
  {
    $group: {
      _id: "$_id",
      result: {
        $push: {
          $first: "$movieObject",
        },
      },
    },
  },
  {
    $project: {
      _id: 0,
      result: 1,
    },
  },
];

//following is the query for rating movie list in paginated format
let ratingListQuery = [
  {
    $match: {
      userId: "userId",
    },
  },
  {
    $project: {
      _id: "$userId",
      ratingListPage: {
        $slice: ["$ratingList", "index", "length"],
      },
    },
  },
  {
    $unwind: {
      path: "$ratingListPage",
      preserveNullAndEmptyArrays: true,
    },
  },
  {
    $lookup: {
      from: "movieEntity",
      let: {
        rating: "$ratingListPage.rating",
        movieId: "$ratingListPage._id",
      },
      pipeline: [
        {
          $match: {
            $expr: {
              $eq: ["$_id", "$$movieId"],
            },
          },
        },
        {
          $project: {
            _id: 1,
            title: 1,
            description: 1,
            genres: 1,
            poster: 1,
            avgRating: {
              $cond: {
                if: {
                  $eq: ["$totalRating", NumberLong(0)],
                },
                then: 0,
                else: {
                  $toDouble: {
                    $divide: [
                      { $toDouble: "$totalRating" },
                      { $toDouble: "$noOfRatings" },
                    ],
                  },
                },
              },
            },
            noOfRatings: 1,
            userRating: "$$rating",
          },
        },
      ],
      as: "movieObject",
    },
  },
  {
    $group: {
      _id: "$_id",
      result: {
        $push: {
          $first: "$movieObject",
        },
      },
    },
  },
  {
    $project: {
      _id: 0,
      result: 1,
    },
  },
];

//fetch a single reply given the id
let fetchSingleReplyQuery = [
  {
    $match: {
      _id: "Reply@1627407513779",
    },
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
                      $eq: ["$$elem._id", "roybond007"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
    },
  },
  {
    $lookup: {
      from: "userEntity",
      let: {
        userId: "$userId",
      },
      pipeline: [
        {
          $match: {
            $expr: {
              $eq: ["$userId", "$$userId"],
            },
          },
        },
        {
          $project: {
            _id: "$userId",
            profilePictureLink: 1,
          },
        },
      ],
      as: "targetUser",
    },
  },
  {
    $project: {
      _id: 1,
      timestamp: 1,
      content: 1,
      userObject: {
        $first: "$targetUser",
      },
      noOfLikes: 1,
      userReact: 1,
    },
  },
];

//fetch a single review given the id
let fetchSingleReviewQuery = [
  {
    $match: {
      _id: "Review@1627384988698",
    },
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
                      $eq: ["$$elem._id", "roybond007"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
      replyListPage: {
        $slice: ["$replyList", 0, 5],
      },
    },
  },
  {
    $unwind: {
      path: "$replyListPage",
      preserveNullAndEmptyArrays: true,
    },
  },
  {
    $lookup: {
      from: "replyEntity",
      let: {
        replyId: "$replyListPage._id",
      },
      pipeline: [
        {
          $match: {
            $expr: {
              $eq: ["$_id", "$$replyId"],
            },
          },
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
                            $eq: ["$$elem._id", "roybond007"],
                          },
                        },
                      },
                    },
                    1,
                  ],
                },
                then: true,
                else: false,
              },
            },
          },
        },
        {
          $lookup: {
            from: "userEntity",
            let: {
              userId: "$userId",
            },
            pipeline: [
              {
                $match: {
                  $expr: {
                    $eq: ["$userId", "$$userId"],
                  },
                },
              },
              {
                $project: {
                  _id: "$userId",
                  profilePictureLink: 1,
                },
              },
            ],
            as: "targetUser",
          },
        },
        {
          $project: {
            _id: 1,
            timestamp: 1,
            content: 1,
            userObject: {
              $first: "$targetUser",
            },
            noOfLikes: 1,
            userReact: 1,
          },
        },
      ],
      as: "target",
    },
  },
  {
    $group: {
      _id: "$_id",
      timestamp: {
        $first: "$timestamp",
      },
      userId: {
        $first: "$userId",
      },
      content: {
        $first: "$content",
      },
      noOfLikes: {
        $first: "$noOfLikes",
      },
      noOfReplies: {
        $first: "$noOfReplies",
      },
      userReact: {
        $first: "$userReact",
      },
      list: {
        $push: {
          $first: "$target",
        },
      },
    },
  },
  {
    $lookup: {
      from: "userEntity",
      let: {
        userId: "$userId",
      },
      pipeline: [
        {
          $match: {
            $expr: {
              $eq: ["$userId", "$$userId"],
            },
          },
        },
        {
          $project: {
            _id: "$userId",
            profilePictureLink: 1,
          },
        },
      ],
      as: "targetUser",
    },
  },
  {
    $project: {
      _id: 1,
      timestamp: 1,
      userObject: {
        $first: "$targetUser",
      },
      content: 1,
      noOfLikes: 1,
      noOfReplies: 1,
      userReact: 1,
      replyList: {
        result: "$list",
      },
    },
  },
];

let fetchReplyListQuery = [
  {
    $match: {
      _id: "Review@1627384988698",
    },
  },
  {
    $project: {
      _id: 1,
      replyListPage: {
        $slice: ["$replyList", 0, 5],
      },
    },
  },
  {
    $unwind: {
      path: "$replyListPage",
      preserveNullAndEmptyArrays: true,
    },
  },
  {
    $lookup: {
      from: "replyEntity",
      let: {
        replyId: "$replyListPage._id",
      },
      pipeline: [
        {
          $match: {
            $expr: {
              $eq: ["$_id", "$$replyId"],
            },
          },
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
                            $eq: ["$$elem._id", "roybond007"],
                          },
                        },
                      },
                    },
                    1,
                  ],
                },
                then: true,
                else: false,
              },
            },
          },
        },
        {
          $lookup: {
            from: "userEntity",
            let: {
              userId: "$userId",
            },
            pipeline: [
              {
                $match: {
                  $expr: {
                    $eq: ["$userId", "$$userId"],
                  },
                },
              },
              {
                $project: {
                  _id: "$userId",
                  profilePictureLink: 1,
                },
              },
            ],
            as: "targetUser",
          },
        },
        {
          $project: {
            _id: 1,
            timestamp: 1,
            content: 1,
            userObject: {
              $first: "$targetUser",
            },
            noOfLikes: 1,
            userReact: 1,
          },
        },
      ],
      as: "target",
    },
  },
  {
    $group: {
      _id: "$_id",
      result: {
        $push: {
          $first: "$target",
        },
      },
    },
  },
  {
    $project: {
      _id: 0,
      result: 1,
    },
  },
];

let fetchReviewListQuery = [
  {
    $match: {
      _id: "Movie@1627369717204",
    },
  },
  {
    $project: {
      _id: 1,
      reviewListPage: {
        $slice: ["$reviewList", 0, 5],
      },
    },
  },
  {
    $unwind: {
      path: "$reviewListPage",
      preserveNullAndEmptyArrays: true,
    },
  },
  {
    $lookup: {
      from: "reviewEntity",
      let: {
        reviewId: "$reviewListPage._id",
      },
      pipeline: [
        {
          $match: {
            $expr: {
              $eq: ["$_id", "$$reviewId"],
            },
          },
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
                            $eq: ["$$elem._id", "roybond007"],
                          },
                        },
                      },
                    },
                    1,
                  ],
                },
                then: true,
                else: false,
              },
            },
            replyListPage: {
              $slice: ["$replyList", 0, 5],
            },
          },
        },
        {
          $unwind: {
            path: "$replyListPage",
            preserveNullAndEmptyArrays: true,
          },
        },
        {
          $lookup: {
            from: "replyEntity",
            let: {
              replyId: "$replyListPage._id",
            },
            pipeline: [
              {
                $match: {
                  $expr: {
                    $eq: ["$_id", "$$replyId"],
                  },
                },
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
                                  $eq: ["$$elem._id", "roybond007"],
                                },
                              },
                            },
                          },
                          1,
                        ],
                      },
                      then: true,
                      else: false,
                    },
                  },
                },
              },
              {
                $lookup: {
                  from: "userEntity",
                  let: {
                    userId: "$userId",
                  },
                  pipeline: [
                    {
                      $match: {
                        $expr: {
                          $eq: ["$userId", "$$userId"],
                        },
                      },
                    },
                    {
                      $project: {
                        _id: "$userId",
                        profilePictureLink: 1,
                      },
                    },
                  ],
                  as: "targetUser",
                },
              },
              {
                $project: {
                  _id: 1,
                  timestamp: 1,
                  content: 1,
                  userObject: {
                    $first: "$targetUser",
                  },
                  noOfLikes: 1,
                  userReact: 1,
                },
              },
            ],
            as: "target",
          },
        },
        {
          $group: {
            _id: "$_id",
            timestamp: {
              $first: "$timestamp",
            },
            userId: {
              $first: "$userId",
            },
            content: {
              $first: "$content",
            },
            noOfLikes: {
              $first: "$noOfLikes",
            },
            noOfReplies: {
              $first: "$noOfReplies",
            },
            userReact: {
              $first: "$userReact",
            },
            list: {
              $push: {
                $first: "$target",
              },
            },
          },
        },
        {
          $lookup: {
            from: "userEntity",
            let: {
              userId: "$userId",
            },
            pipeline: [
              {
                $match: {
                  $expr: {
                    $eq: ["$userId", "$$userId"],
                  },
                },
              },
              {
                $project: {
                  _id: "$userId",
                  profilePictureLink: 1,
                },
              },
            ],
            as: "targetUser",
          },
        },
        {
          $project: {
            _id: 1,
            timestamp: 1,
            userObject: {
              $first: "$targetUser",
            },
            content: 1,
            noOfLikes: 1,
            noOfReplies: 1,
            userReact: 1,
            replyList: {
              result: "$list",
            },
          },
        },
      ],
      as: "target",
    },
  },
  {
    $group: {
      _id: "$_id",
      result: {
        $push: {
          $first: "$target",
        },
      },
    },
  },
  {
    $project: {
      _id: 0,
      result: 1,
    },
  },
];

let movieQuery = [
  {
    $match: {
      _id: "3421",
    },
  },
  {
    $set: {
      rating: {
        $first: {
          $filter: {
            input: "$ratingList",
            as: "rating",
            cond: {
              $eq: ["$$rating._id", "_id"],
            },
          },
        },
      },
      isWishListed: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$wishList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "userId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
      isWatchListed: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$watchList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "userId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
      avgRating: {
        $cond: {
          if: {
            $eq: ["$totalRating", NumberLong(0)],
          },
          then: 0,
          else: {
            $toDouble: {
              $divide: [
                { $toDouble: "$totalRating" },
                { $toDouble: "$noOfRatings" },
              ],
            },
          },
        },
      },
    },
  },
  {
    $project: {
      _id: 1,
      title: 1,
      description: 1,
      posterLink: 1,
      trailerLink: 1,
      timestamp: 1,
      noOfRatings: 1,
      avgRating: 1,
      genres: 1,
      userRating: {
        $ifNull: ["$rating.rating", 0],
      },
      isWatchListed: 1,
      isWishListed: 1,
    },
  },
];

let fetchSearchQuery = [
  {
    $match: {
      $text: {
        $search: "query",
      },
    },
  },
  {
    $sort: {
      searchPreference: {
        $meta: "textScore",
      },
    },
  },
  {
    $skip: "index",
  },
  {
    limit: "length",
  },
  {
    $set: {
      rating: {
        $first: {
          $filter: {
            input: "$ratingList",
            as: "rating",
            cond: {
              $eq: ["$$rating._id", "userId"],
            },
          },
        },
      },
      isWishListed: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$wishList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "userId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
      isWatchListed: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$watchList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "userId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
    },
  },
  {
    $project: {
      _id: 1,
      title: 1,
      length: 1,
      posterLink: 1,
      noOfRatings: 1,
      avgRating: {
        $cond: {
          if: {
            $eq: ["$totalRating", NumberLong(0)],
          },
          then: 0,
          else: {
            $toDouble: {
              $divide: [
                { $toDouble: "$totalRating" },
                { $toDouble: "$noOfRatings" },
              ],
            },
          },
        },
      },
      genres: 1,
      userRating: {
        $ifNull: ["$rating", 0],
      },
      isWatchListed: 1,
      isWishListed: 1,
    },
  },
];

let fetchRecentlyAddedMovieQuery = [
  {
    $sort: {
      timestamp: -1,
      totalRating: -1,
    },
  },
  {
    $skip: "index",
  },
  {
    $limit: "length",
  },
  {
    $set: {
      rating: {
        $first: {
          $filter: {
            input: "$ratingList",
            as: "rating",
            cond: {
              $eq: ["$$rating._id", "_id"],
            },
          },
        },
      },
      isWishListed: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$wishList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "userId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
      isWatchListed: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$watchList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "userId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
    },
  },
  {
    $project: {
      _id: 1,
      title: 1,
      length: 1,
      posterLink: 1,
      noOfRatings: 1,
      avgRating: {
        $cond: {
          if: {
            $eq: ["$totalRating", NumberLong(0)],
          },
          then: 0,
          else: {
            $toDouble: {
              $divide: [
                { $toDouble: "$totalRating" },
                { $toDouble: "$noOfRatings" },
              ],
            },
          },
        },
      },
      genres: 1,
      userRating: {
        $ifNull: ["$rating.rating", 0],
      },
      isWatchListed: 1,
      isWishListed: 1,
    },
  },
];

let fetchTopRatedMovieQuery = [
  {
    $sort: {
      totalRating: -1,
    },
  },
  {
    $skip: "index",
  },
  {
    $limit: "length",
  },
  {
    $set: {
      rating: {
        $first: {
          $filter: {
            input: "$ratingList",
            as: "rating",
            cond: {
              $eq: ["$$rating._id", "_id"],
            },
          },
        },
      },
      isWishListed: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$wishList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "userId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
      isWatchListed: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$watchList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "userId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
    },
  },
  {
    $project: {
      _id: 1,
      title: 1,
      length: 1,
      posterLink: 1,
      noOfRatings: 1,
      avgRating: {
        $cond: {
          if: {
            $eq: ["$totalRating", NumberLong(0)],
          },
          then: 0,
          else: {
            $toDouble: {
              $divide: [
                { $toDouble: "$totalRating" },
                { $toDouble: "$noOfRatings" },
              ],
            },
          },
        },
      },
      genres: 1,
      userRating: {
        $ifNull: ["$rating.rating", 0],
      },
      isWatchListed: 1,
      isWishListed: 1,
    },
  },
];

let fetchLeastRatedMovieQuery = [
  {
    $sort: {
      totalRating: 1,
    },
  },
  {
    $skip: "index",
  },
  {
    $limit: "length",
  },
  {
    $set: {
      rating: {
        $first: {
          $filter: {
            input: "$ratingList",
            as: "rating",
            cond: {
              $eq: ["$$rating._id", "_id"],
            },
          },
        },
      },
      isWishListed: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$wishList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "userId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
      isWatchListed: {
        $cond: {
          if: {
            $eq: [
              {
                $size: {
                  $filter: {
                    input: "$watchList",
                    as: "elem",
                    cond: {
                      $eq: ["$$elem._id", "userId"],
                    },
                  },
                },
              },
              1,
            ],
          },
          then: true,
          else: false,
        },
      },
    },
  },
  {
    $project: {
      _id: 1,
      title: 1,
      length: 1,
      posterLink: 1,
      noOfRatings: 1,
      avgRating: {
        $cond: {
          if: {
            $eq: ["$totalRating", NumberLong(0)],
          },
          then: 0,
          else: {
            $toDouble: {
              $divide: [
                { $toDouble: "$totalRating" },
                { $toDouble: "$noOfRatings" },
              ],
            },
          },
        },
      },
      genres: 1,
      userRating: {
        $ifNull: ["$rating.rating", 0],
      },
      isWatchListed: 1,
      isWishListed: 1,
    },
  },
];
