package moregeek.shorterPacket;

import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.moregeek.blaze.net.interserver.E2eDecoder;
import com.moregeek.blaze.net.interserver.E2eSmashClient;

public class Client extends E2eSmashClient{
	
	public Client(String loginServerIp, int loginServerPort) {
		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(), 
						Executors.newCachedThreadPool()));
		
		bootstrap.getPipeline().addLast("decoder", new E2eDecoder());
		bootstrap.getPipeline().addLast("handler", new Handler());
//		innerHandler = handler;
	}
}
