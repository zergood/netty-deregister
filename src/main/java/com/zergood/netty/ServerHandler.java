package com.zergood.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class ServerHandler extends SimpleChannelInboundHandler<Object>{
    public BusinessLogicEmulator businessLogicEmulator;

    public ServerHandler(BusinessLogicEmulator businessLogicEmulator) {
        this.businessLogicEmulator = businessLogicEmulator;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest) msg;

            if(request.getMethod() == HttpMethod.GET){
                businessLogicEmulator.start(ctx);
            } else {
                createNotFoundResponse();
            }
        }
    }

    private void createNotFoundResponse() {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.NOT_FOUND);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
    }
}
