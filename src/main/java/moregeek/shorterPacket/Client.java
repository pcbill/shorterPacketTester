package moregeek.shorterPacket;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.moregeek.blaze.net.interserver.E2eSmashClient;

public class Client extends E2eSmashClient{
	
	private Channel channel;
	
	public Client(String loginServerIp, int loginServerPort, final Main main) {
		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(), 
						Executors.newCachedThreadPool()));
		
		bootstrap.getPipeline().addLast("decoder", new Decoder());
		bootstrap.getPipeline().addLast("handler", new Handler(main));
		
		ChannelFuture connect = bootstrap.connect(new InetSocketAddress(loginServerIp, loginServerPort));
		connect.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					System.out.println("connection successfully....");
					channel = future.getChannel();
//					main.run(channel);
					new Thread(main).start();
//					main.login(channel);
				}
			}
		});
	}
	
	public Channel getChannel() {
		while (channel == null) {}
		return channel;
	}
}
