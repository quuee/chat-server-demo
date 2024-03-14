package cn.quuee.chatServer.web.model;

import lombok.Data;

import java.util.List;

@Data
public class ConversationDO {

    private Long conversationId;

    private Long groupId; // 群id

    private Long contactId; // 联系人id

    private String conversationName; // 群聊名 或 联系人名

    private Integer conversationType;

    private Integer currentUserId;

    private String conversationThumb;

    private Integer contentType;

    private String lastTime;

    private String lastMessage;

    private List<UserDO> contactMembers; // 联系人
}
