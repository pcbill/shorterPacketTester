package moregeek.shorterPacket;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.moregeek.blaze.net.OpType;
import com.moregeek.blaze.net.interserver.CallbackPacket;
import com.moregeek.blaze.net.interserver.E2eBuffer;

@ChannelPipelineCoverage("all")
public class Handler extends SimpleChannelHandler{

	private CallbackPacket callbackPacket;
	private Main main;

	public Handler(Main main) {
		this.main = main;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	throws Exception 
	{
		this.callbackPacket = (CallbackPacket)e.getMessage();
		
		E2eBuffer buffer = callbackPacket.getBuffer();
		print(buffer);
		handleCallback(buffer); 
	}

	private void handleCallback(E2eBuffer buffer) {
		if (!callbackPacket.isOperationSucceed()) {
			return;
		}
		if (callbackPacket.getOpType() == OpType.login) {
			loginCallback(buffer);
		} else if (callbackPacket.getOpType() == OpType.createMatch) {
			createMatch(buffer);
		} else if (callbackPacket.getOpType() == OpType.changeMatchState) {
			changeMatchState(buffer);
		} else if (callbackPacket.getOpType() == OpType.joinMatch) {
			joinMatch(buffer);
		} else if (callbackPacket.getOpType() == 46) { ///< gameStart
			gameStart(buffer);
		} else if (callbackPacket.getOpType() == 58) { ///< reborn
			rebornCallback(buffer);
		} else if (callbackPacket.getOpType() == OpType.broadcasting) {
			gameTimeCallback(buffer);
		} else if (callbackPacket.getOpType() == 59) { ///< attack
			attackCallback(buffer);
		}  
	}

	private void attackCallback(E2eBuffer buffer) {
		StringBuffer result = new StringBuffer("attackCallback :{");

		short matchId = buffer.readShort();
		result.append("matchId:").append(matchId).append(",");
		
		byte dir = buffer.readByte();
		result.append("dir:").append(dir).append(",");

		short x = buffer.readShort();
		result.append("x:").append(x).append(",");

		short y = buffer.readShort();
		result.append("y:").append(y).append(",");

		byte skillLevel = buffer.readByte();
		result.append("skillLevel:").append(skillLevel).append(",");

		byte attackMode = buffer.readByte();
		result.append("attackMode:").append(attackMode).append(",");
		
		for (int i = 0; i < 6; i++) {
			result.append("\n\tvictim:{");

			byte targetId = buffer.readByte();
			result.append("targetId:").append(targetId).append(",");

			int decHp = buffer.readInt();
			result.append("decHp:").append(decHp).append(",");
			
			result.append("}, ");
		}
		
		byte deadBits = buffer.readByte();
		result.append("deadBits:").append(deadBits).append(",");

		result.append("}");
		System.out.println(result);
	}

	private void gameTimeCallback(E2eBuffer buffer) {
		StringBuffer result = new StringBuffer("gameTimeCallback :{");
		
		byte deadHumanCount = buffer.readByte();
		result.append("deadHumanCount:").append(deadHumanCount).append(",");
		
		byte deadJunkfoodianCount = buffer.readByte();
		result.append("deadJunkfoodianCount:").append(deadJunkfoodianCount).append(",");

		short matchId = buffer.readShort();
		result.append("matchId:").append(matchId).append(",");
		
		for (int i = 0; i < 10; i++) {
			result.append("\n\tuser:{");
			
			byte ticketId = buffer.readByte();
			result.append("ticketId:").append(ticketId).append(",");

			byte action = buffer.readByte();
			result.append("action:").append(action).append(",");

			byte dir = buffer.readByte();
			result.append("dir:").append(dir).append(",");

			short x = buffer.readShort();
			result.append("x:").append(x).append(",");

			short y = buffer.readShort();
			result.append("y:").append(y).append(",");

			int hp = buffer.readInt();
			result.append("hp:").append(hp).append(",");
			result.append("}, ");
		}

		result.append("}");
		System.out.println(result);
	}

	private void rebornCallback(E2eBuffer buffer) {
		StringBuffer result = new StringBuffer("rebornCallback :{");

		short matchId = buffer.readShort();
		result.append("matchId:").append(matchId).append(",");

		byte ticketId = buffer.readByte();
		result.append("ticketId:").append(ticketId).append(",");
		
		byte dir = buffer.readByte();
		result.append("dir:").append(dir).append(",");

		short x = buffer.readShort();
		result.append("x:").append(x).append(",");

		short y = buffer.readShort();
		result.append("y:").append(y).append(",");
		
		result.append("}");
		System.out.println(result);
	}

	private void gameStart(E2eBuffer buffer) {
		StringBuffer result = new StringBuffer("gameStart :{");
		
		short matchId = buffer.readShort();
		result.append("matchId:").append(matchId).append(",");
		
		for (int i = 0; i < 10; i++) {
			result.append("\n\tuser:{");
			
			byte ticketId = buffer.readByte();
			result.append("ticketId:").append(ticketId).append(",");
			
			byte dir = buffer.readByte();
			result.append("dir:").append(dir).append(",");

			short x = buffer.readShort();
			result.append("x:").append(x).append(",");

			short y = buffer.readShort();
			result.append("y:").append(y).append(",");

			int hp = buffer.readInt();
			result.append("hp:").append(hp).append(",");
			result.append("}, ");
		}
		
		result.append("}");
		System.out.println(result);
	}

	private void joinMatch(E2eBuffer buffer) {
		StringBuffer result = new StringBuffer("joinMatch :{");

		Byte userCount = buffer.readByte();
		result.append("userCount:").append(userCount).append(",");

		for (int i = 0; i < userCount; i++) {
			result.append("\n\tuser:{");
			Byte isMaster = buffer.readByte();
			result.append("isMaster:").append(isMaster).append(",");
			
			String userNickName = buffer.readString();
			result.append("userNickName:").append(userNickName).append(",");

			String pic = buffer.readString();
			result.append("pic:").append(pic).append(",");
			
			String locale = buffer.readString();
			result.append("locale:").append(locale).append(",");

			Byte ticketId = buffer.readByte();
			result.append("ticketId:").append(ticketId).append(",");

			Short life = buffer.readShort();
			result.append("life:").append(life).append(",");
			
			Short att = buffer.readShort();
			result.append("att:").append(att).append(",");

			Short def = buffer.readShort();
			result.append("def:").append(def).append(",");

			Short speed = buffer.readShort();
			result.append("speed:").append(speed).append(",");

			Byte raceId = buffer.readByte();
			result.append("raceId:").append(raceId).append(",");

			Byte color = buffer.readByte();
			result.append("color:").append(color).append(",");

			Byte head = buffer.readByte();
			result.append("head:").append(head).append(",");

			Byte hair = buffer.readByte();
			result.append("hair:").append(hair).append(",");

			Byte body = buffer.readByte();
			result.append("body:").append(body).append(",");

			Byte hand1 = buffer.readByte();
			result.append("hand1:").append(hand1).append(",");

			Byte hand2 = buffer.readByte();
			result.append("hand2:").append(hand2).append(",");

			Byte hand3 = buffer.readByte();
			result.append("hand3:").append(hand3).append(",");
			
			Byte foot1 = buffer.readByte();
			result.append("foot1:").append(foot1).append(",");

			Byte foot2 = buffer.readByte();
			result.append("foot2:").append(foot2).append(",");

			Byte glove = buffer.readByte();
			result.append("glove:").append(glove).append(",");

			Byte backpack = buffer.readByte();
			result.append("backpack:").append(backpack).append(",");

			Byte glasses = buffer.readByte();
			result.append("glasses:").append(glasses).append(",");

			Byte cap = buffer.readByte();
			result.append("cap:").append(cap).append(",");

			Byte weapon = buffer.readByte();
			result.append("weapon:").append(weapon).append(",");

			Byte level = buffer.readByte();
			result.append("level:").append(level).append(",");

			Byte action = buffer.readByte();
			result.append("action:").append(action).append(",");
			result.append("}, ");
		}
		
		result.append("}");
		System.out.println(result);
	}

	private void changeMatchState(E2eBuffer buffer) {
		StringBuffer result = new StringBuffer("changeMatchState :{");
		int matchId = buffer.readInt();
		result.append("matchId:").append(matchId).append(",");

		byte action = buffer.readByte();
		result.append("action:").append(action).append(",");

		result.append("}");
		System.out.println(result);
	}

	private void createMatch(E2eBuffer buffer) {
		StringBuffer result = new StringBuffer("createMatch :{");
		
		byte dataSize = buffer.readByte();
		result.append("dataSize:").append(dataSize).append(",");
		
		int matchId = buffer.readInt();
		main.setMatchId(matchId);
		result.append("matchId:").append(matchId).append(",");
		
		byte ticketId = buffer.readByte();
		main.setTicketId(ticketId);
		result.append("ticketId:").append(ticketId).append(",");
		
		result.append("}");
		System.out.println(result);
	}

	private void loginCallback(E2eBuffer buffer) {
		StringBuffer result = new StringBuffer("login:{");
		
		buffer.readByte(); /// returnValueSize
		
		int userId = buffer.readInt();
		result.append("userId:").append(userId).append(",");
		
		byte sessionKeySize = buffer.readByte();
		byte[] sessionKeys = new byte[sessionKeySize];
		for (int i = 0; i < sessionKeySize; i++) {
			sessionKeys[i] = buffer.readByte();
		}
		result.append("sessionKeys:").append(new String(sessionKeys)).append(",");

		byte hostSize = buffer.readByte();
		byte[] host = new byte[hostSize];
		for (int i = 0; i < hostSize; i++) {
			host[i] = buffer.readByte();
		}
		result.append("host:").append(new String(host)).append(",");
		
		int port = buffer.readInt();
		result.append("port:").append(port).append(",");

		byte lastVersion = buffer.readByte();
		result.append("lastVersion:").append(lastVersion).append(",");
		
		result.append("}");
		System.out.println(result);
	}

	private void print(E2eBuffer buffer) {
		/// print result
		StringBuffer result = new StringBuffer("[");
		for (int i = 0; i < callbackPacket.getPacketSize(); i++) {
			result.append(buffer.getByte(i)).append(",");
		}
		result.append("]");
		
		System.out.println(result);
	}
}
