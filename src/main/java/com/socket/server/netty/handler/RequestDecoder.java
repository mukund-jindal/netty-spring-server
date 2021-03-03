package com.socket.server.netty.handler;

import com.socket.server.domain.StartTransactionRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<StartTransactionRequest> {

    private final Charset charset = Charset.forName("UTF-8");

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {
 
        StartTransactionRequest data = new StartTransactionRequest();
       // data.setIntValue(in.readInt());
        int strLen = in.readInt();
         String string= in.readCharSequence(strLen, charset).toString();
        System.out.println(string);
        out.add(in.readInt());
    }
}