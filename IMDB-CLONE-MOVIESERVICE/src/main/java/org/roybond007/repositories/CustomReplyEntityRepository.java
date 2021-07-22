package org.roybond007.repositories;

import org.roybond007.model.entity.ReplyEntity;

public interface CustomReplyEntityRepository {
    void uploadReplyToReview(ReplyEntity target);
}
