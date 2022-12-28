package org.shahryarkiani;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class BasicHttpHandler extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest req) {

            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
            String path = decoder.path();
            path = path.substring(1);
            //Parse the path from the request

            //Create Hello, {path} response and convert it to a ByteBuf
            ByteBuf resContent = Unpooled.wrappedBuffer(("Hello, " + path).getBytes());

            FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), OK, resContent);

            res.headers()
                    .set(CONTENT_TYPE, TEXT_PLAIN)
                    .set(CONNECTION, CLOSE)
                    .setInt(CONTENT_LENGTH, res.content().readableBytes());

            //
            ChannelFuture f = ctx.write(res);

            //Closes the channel after all write operations are complete
            f.addListener(ChannelFutureListener.CLOSE);

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
