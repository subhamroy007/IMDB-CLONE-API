package org.roybond007.repositories;

import org.roybond007.model.dto.ReactUploadResponseBody;
import org.roybond007.model.entity.ReplyEntity;

public interface ReplyEntityRepositoryCustom {
    void uploadReplyToReview(ReplyEntity target);
    ReactUploadResponseBody uploadReactToReply(String userId, String replyId);
}
