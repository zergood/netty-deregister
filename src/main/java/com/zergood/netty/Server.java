package com.zergood.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup readGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup writeGroup = new NioEventLoopGroup(1);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        BusinessLogicEmulator businessLogicEmulator = new BusinessLogicEmulator(writeGroup);

        ServerBootstrap server = serverBootstrap.group(bossGroup, readGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ServerInitializer(businessLogicEmulator));
                    }
                });

        try {
            ChannelFuture func = server.bind(8000).sync();
            func.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            readGroup.shutdownGracefully();
            writeGroup.shutdownGracefully();
        }
    }
}
