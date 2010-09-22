package moregeek.shorterPacket;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.moregeek.blaze.net.interserver.E2eBuffer;
import com.moregeek.blaze.net.interserver.E2eDecoder;
import com.moregeek.blaze.net.interserver.E2eSmashClient;
import com.moregeek.blaze.net.interserver.E2eSmashClientCommand;

public class Client extends E2eSmashClient{
	
	private Channel channel;
	
	public Client(String loginServerIp, int loginServerPort) {
		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(), 
						Executors.newCachedThreadPool()));
		
		bootstrap.getPipeline().addLast("decoder", new E2eDecoder());
		bootstrap.getPipeline().addLast("handler", new Handler(new E2eSmashClientCommand() {
			
			@Override
			protected void send(E2eBuffer buffer) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected void callback(E2eBuffer buffer) {
				// TODO Auto-generated method stub
				
			}
		}));
		
		
		
		ChannelFuture connect = bootstrap.connect(new InetSocketAddress(loginServerIp, loginServerPort));
		connect.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					channel = future.getChannel();
				}
			}
		});
	}
	
	public Channel getChannel() {
		while (channel == null) {}
		return channel;
	}
}
