package com.zergood.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BusinessLogicEmulator {
    class BusinessTask implements Runnable{
        private ChannelHandlerContext ctx;

        public BusinessTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            makeMoney();
            DefaultFullHttpResponse response = createHelloResponse();

            try {
                //Block here
                ctx.deregister().sync();
                writeGroup.register(ctx.channel()).sync();

                ctx.writeAndFlush(response);
                ctx.close();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void makeMoney() {
            int i = 0;
            while(i < 100000){
                i = i + 1;
            }
        }

        private DefaultFullHttpResponse createHelloResponse() {
            ByteBuf helloBytes = Unpooled.wrappedBuffer("hello".getBytes());
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, helloBytes);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
            return response;
        }
    }

    public BusinessLogicEmulator(NioEventLoopGroup writeGroup) {
        this.writeGroup = writeGroup;
    }

    private NioEventLoopGroup writeGroup;
    private Executor executor = Executors.newSingleThreadExecutor();

    public void start(ChannelHandlerContext ctx) {
        executor.execute(new BusinessTask(ctx));
    }
}
