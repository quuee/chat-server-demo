package cn.quuee.chatServer.common;

public final class IMRedisKey {

    private IMRedisKey() {}

    /**
     * im-server最大id,从0开始递增
     */
    public static final String  IM_MAX_SERVER_ID = "im<max_server_id>";
    /**
     * 用户ID所连接的IM-server的ID
     * 目前主要用来给用户在线状态续命
     */
    public static final String  IM_USER_SERVER_ID = "im<server_id>:user:terminal";
    /**
     * 未读私聊消息队列
     * 等待接收方用户上线推送
     */
    public static final String IM_MESSAGE_PRIVATE_UNREAD_QUEUE = "im:message:private:unread";
    /**
     * 只是单纯作为Map的key，用户取user
     */
    public static final String IM_USER = "im:user";
    /**
     * 未读群聊消息队列
     */
    public static final String IM_MESSAGE_GROUP_UNREAD_QUEUE = "im:message:group:unread";
    /**
     * 私聊消息发送结果队列
     */
    public static final String IM_RESULT_PRIVATE_QUEUE = "im:result:private";
    /**
     * 群聊消息发送结果队列
     */
    public static final String IM_RESULT_GROUP_QUEUE = "im:result:group";

}
