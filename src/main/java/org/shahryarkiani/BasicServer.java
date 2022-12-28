package org.shahryarkiani;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class BasicServer {


    private final int port;
    public static void main(String[] args) throws Exception {
        new BasicServer(8080).start();
    }

    public BasicServer(int port)
    {
        this.port = port;
    }

    public void start() throws Exception{

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new BasicHttpInitializer());
            Channel ch = b.bind(port).sync().channel();

            System.out.println("Server is up and listening on http://127.0.0.1:" + port);

            ch.closeFuture().sync();

        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}