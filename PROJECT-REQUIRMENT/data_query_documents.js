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
                userId: "$_id",
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
                                        $eq: ["$$elem.id", "$$userId"]
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
    }
]

//following is the quesry for rating movie list in paginated format
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
    }
]
