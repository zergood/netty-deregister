package com.zergood.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private BusinessLogicEmulator businessLogicEmulator;

    public ServerInitializer(BusinessLogicEmulator businessLogicEmulator) {
        this.businessLogicEmulator = businessLogicEmulator;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ServerHandler(businessLogicEmulator));
    }
}
