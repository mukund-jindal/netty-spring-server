package com.socket.server.domain;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.NonNull;

/**
 * Client Domain Object
 */
public class Client {
    public static final AttributeKey<Client> CLIENT_ATTRIBUTE_KEY = AttributeKey.newInstance("USER");

    @Getter
    private final String clientName;
    private final Channel channel;

    private Client(String clientName, Channel channel){
        this.clientName = clientName;
        this.channel = channel;
    }

    public static Client of(@NonNull String loginCommand, @NonNull Channel channel) {
        if (loginCommand.startsWith("login ")) {
            return new Client(loginCommand.trim().substring("login ".length()),channel );
        }

        throw new IllegalArgumentException("loginCommand ["+loginCommand+"] can not be accepted");
    }

    public void login(ChannelRepository channelRepository, Channel channel) {
        channel.attr(CLIENT_ATTRIBUTE_KEY).set(this);
        channelRepository.put(this.clientName, channel);
    }

    public void logout(ChannelRepository channelRepository, Channel channel) {
        channel.attr(CLIENT_ATTRIBUTE_KEY).getAndSet(null);
        channelRepository.remove(this.clientName);
    }

    public static Client current(Channel channel) {
        Client client = channel.attr(CLIENT_ATTRIBUTE_KEY).get();
        if ( client == null ){
            throw new ClientLoggedOutException();
        }
        return client;
    }

    public void tell(Channel targetChannel, @NonNull String username, @NonNull String message) {
        if (targetChannel != null) {
            targetChannel.write(this.clientName);
            targetChannel.write(">");
            targetChannel.writeAndFlush(message + "\n\r");
            this.channel.writeAndFlush("The message was sent to ["+username+"] successfully.\r\n");
        }else{
            this.channel.writeAndFlush("No user named with ["+ username +"].\r\n");
        }
    }
}
