package com.demo.im.netty.processor;


import com.demo.im.common.enums.IMConversationType;
import com.demo.im.util.SpringContextHolder;


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
