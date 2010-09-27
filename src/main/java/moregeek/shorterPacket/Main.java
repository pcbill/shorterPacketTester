package moregeek.shorterPacket;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.Channel;

import com.moregeek.blaze.engine.Platform;
import com.moregeek.blaze.net.MessageHeader;
import com.moregeek.blaze.net.NeuronPacketHeader;
import com.moregeek.blaze.net.OpType;
import com.moregeek.blaze.net.interserver.E2eBuffer;

public class Main implements Runnable{
	private String sessionKey = "ce9c2e2f3bb534decbf2a4881864fef5";
	private int matchId = 0;
	private byte ticketId;
	private Client client;
	
	public static void main(String[] args) {
		assert args.length == 2;
		
        String host = (String)args[0];
        int port = Integer.parseInt((String)args[1]);
		
       new Main(host, port);
        
	}
	
	public Main(String host, int port) {
		Client client = new Client(host, port, this);
		this.client = client;
	}

	public void run(Channel channel) {
//		login(channel);		
      loginGameServer(channel);
      createMatch(channel);
      
//      while (matchId == 0) {
    	  try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//			System.out.println("matchId:"+matchId);
//      }
      
      changeMatchState(channel);
//      startfight(channel);
//      
//      try {
//			TimeUnit.SECONDS.sleep(3);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//      
//      attack(channel);

	}
	private void attack(Channel channel) {
		/// attack
        E2eBuffer buffer = new E2eBuffer();
        buffer.writeByte(NeuronPacketHeader.MHEADER);
		buffer.markWriterIndex();
		buffer.writeShort((short)0); ///< skip for total size 
//		new MessageHeader((byte)59, (byte)0, Platform.PC, "555", sessionKey).serializeTo(buffer);
		new SmallHeader((byte)59, 555).serializeTo(buffer);
		
		buffer.writeShort((short)matchId);
//		buffer.writeInt(Integer.parseInt(((String)infos.getUserId(account))));
		buffer.writeByte((byte) 0); ///< dir
		buffer.writeShort((short) 0); ///< x
		buffer.writeShort((short) 0); ///< y
		buffer.writeByte((byte) 1); // /< attackLevel
		buffer.writeByte((byte) 0); // /< attackMode

		for (int i = 0; i < 6; i++) {
			buffer.writeByte((byte)0);
			buffer.writeInt(0); ///< losed hp, but client fill 0
//			buffer.writeByte((byte) 0); // /< isDeads
		}
		buffer.writeByte((byte)0); ///< deadBits
		
		buffer.updateSize();
		
		channel.write(buffer.getBuffer());

		buffer.destroy();
	}

	private void startfight(Channel channel) {
		///startfight
        E2eBuffer buffer = new E2eBuffer();
        buffer.writeByte(NeuronPacketHeader.MHEADER);
		buffer.markWriterIndex();
		buffer.writeShort((short)0); ///< skip for total size 
		new MessageHeader(OpType.startFight, (byte)0, Platform.PC, "555", sessionKey).serializeTo(buffer);
		buffer.writeInt(matchId);

		buffer.updateSize();
		
		channel.write(buffer.getBuffer());

		buffer.destroy();
	}

	/// changeMatchState
	private void changeMatchState(Channel channel) {
        E2eBuffer buffer = new E2eBuffer();
        buffer.writeByte(NeuronPacketHeader.MHEADER);
		buffer.markWriterIndex();
		buffer.writeShort((short)0); ///< skip for total size 
		new MessageHeader(OpType.changeMatchState, (byte)0, Platform.PC, "555", sessionKey).serializeTo(buffer);

		buffer.writeInt(matchId);
		buffer.writeByte((byte) 0);
		buffer.updateSize();
		
		channel.write(buffer.getBuffer());

		buffer.destroy();
	}

	/// create match
	private  void createMatch(Channel channel) {
        E2eBuffer buffer = new E2eBuffer();
        buffer.writeByte(NeuronPacketHeader.MHEADER);
		buffer.markWriterIndex();
		buffer.writeShort((short)0); ///< skip for total size 
		new MessageHeader(OpType.createMatch, (byte)0, Platform.PC, "555", sessionKey).serializeTo(buffer);

		byte[] titleArray = new byte[20];
		Arrays.fill(titleArray, (byte)0);
		byte[] bytes = (""+System.currentTimeMillis()).getBytes();
		for (int i = 0; i < bytes.length; i++) {
			titleArray[i] = bytes[bytes.length - 1 - i];
		}
		buffer.writeBytes(titleArray);
		
		byte[] descriptionArray = new byte[80];
		Arrays.fill(descriptionArray, (byte)0);
		bytes = "des".getBytes();
		for (int i = 0; i < bytes.length; i++) {
			descriptionArray[i] = bytes[i];
		}
		buffer.writeBytes(descriptionArray);
		
		
		byte[] passwordArray = new byte[20];
		Arrays.fill(passwordArray, (byte)0);
		bytes = "pass".getBytes();
		for (int i = 0; i < bytes.length; i++) {
			passwordArray[i] = bytes[i];
		}
		buffer.writeBytes(passwordArray);
		
		buffer.writeByte((byte)1); ///< mapIndex
		buffer.writeByte((byte)0); ///< militaryRank
		buffer.writeByte((byte)1); ///< gameLevel		
		buffer.updateSize();
		
		channel.write(buffer.getBuffer());

		buffer.destroy();
	}

	/// login game server
	private  void loginGameServer(Channel channel) {
		E2eBuffer buffer = new E2eBuffer();
        buffer.writeByte(NeuronPacketHeader.MHEADER);
		buffer.markWriterIndex();
		buffer.writeShort((short)0); ///< skip for total size 
		new MessageHeader(OpType.login, (byte)0, Platform.PC, "555", sessionKey).serializeTo(buffer);
		buffer.updateSize();
		
		channel.write(buffer.getBuffer());
		buffer.destroy();
	}

	/// login
	public  void login(Channel channel) {
		E2eBuffer buffer = new E2eBuffer();
        buffer.writeByte(NeuronPacketHeader.MHEADER);
		buffer.markWriterIndex();
		buffer.writeShort((short)0); ///< skip for total size 
		new MessageHeader(OpType.login, (byte)0, Platform.PC, "robot555", "robotpwd").serializeTo(buffer);
		buffer.updateSize();
		
		channel.write(buffer.getBuffer());

		buffer.destroy();
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public void setTicketId(byte ticketId) {
		this.ticketId = ticketId;
	}

	public void setSessionKey(String sessionKey2) {
		this.sessionKey = sessionKey2;
	}

	@Override
	public void run() {
		run(client.getChannel());
	}

	
}
