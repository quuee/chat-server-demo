package cn.quuee.chatServer.netty.processor;


import cn.quuee.chatServer.common.enums.IMConversationType;
import cn.quuee.chatServer.util.SpringContextHolder;


public class ProcessorFactory {

    public static AbstractMessageProcessor createProcessor(IMConversationType cmd) {
        AbstractMessageProcessor processor = null;
        switch (cmd) {
//            case LOGIN:
//                processor = SpringContextHolder.getApplicationContext().getBean(LoginProcessor.class);
//                break;
            case HEART_BEAT:
                processor = SpringContextHolder.getApplicationContext().getBean(HeartbeatProcessor.class);
                break;
            case PRIVATE_MESSAGE:
                processor = SpringContextHolder.getApplicationContext().getBean(PrivateMessageProcessor.class);
                break;
            case GROUP_MESSAGE:
                processor = SpringContextHolder.getApplicationContext().getBean(GroupMessageProcessor.class);
                break;
            default:
                break;
        }
        return processor;
    }

}
