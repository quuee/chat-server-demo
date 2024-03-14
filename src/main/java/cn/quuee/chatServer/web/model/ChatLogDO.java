package cn.quuee.chatServer.web.model;

import lombok.Data;

@Data
public class ChatLogDO {
    private Long logId;

    private Long conversationId;

    private Long groupId;

    private Long currentUserId;

    private Long senderId;

    private Integer contentType;

    private String content;

    private String contentTime;

    private String imageUrl;
    private Double imageWidth;
    private Double imageHeight;
    private Double imageFileSize;

    private String soundUrl;
    private Double soundDateSize;
    private Double duration;
}
