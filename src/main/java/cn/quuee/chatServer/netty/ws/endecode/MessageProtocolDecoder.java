package cn.quuee.chatServer.netty.ws.endecode;

import cn.quuee.chatServer.model.IMMessageWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

public class MessageProtocolDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame, List<Object> list) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        IMMessageWrapper sendInfo = objectMapper.readValue(textWebSocketFrame.text(), IMMessageWrapper.class);
        list.add(sendInfo);
    }
}
