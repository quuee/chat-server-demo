package com.demo.im.netty.processor;

import io.netty.channel.ChannelHandlerContext;

public interface AbstractMessageProcessor<T> {

    public void process(ChannelHandlerContext ctx, T data);

//    public void process(T data);

    public T transForm(Object o);


}
