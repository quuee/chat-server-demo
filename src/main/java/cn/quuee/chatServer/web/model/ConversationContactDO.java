package cn.quuee.chatServer.web.model;

import lombok.Data;

@Data
public class ConversationContactDO {

    private Long conversationId;

    private Long contactUserId;

    private Long currentUserId;
}
