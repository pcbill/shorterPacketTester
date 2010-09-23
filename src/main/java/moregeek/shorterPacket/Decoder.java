package moregeek.shorterPacket;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.moregeek.blaze.exception.NeuronException;
import com.moregeek.blaze.net.NeuronPacketHeader;
import com.moregeek.blaze.net.interserver.CallbackPacket;

public class Decoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext context, 
							Channel channel,
							ChannelBuffer buffer) throws Exception 
	{
		if (buffer.readableBytes() < 3) {
			return null;
		}
		
		buffer.markReaderIndex();
		
		int packetSize = 0;
		byte opType = 0;
		int userId = 0;
		byte headerFlag = buffer.readByte();
		if (headerFlag == NeuronPacketHeader.MHEADER) {
			packetSize = buffer.readShort();
//			logger.trace("--------packet size: "+ packetSize);
			if (buffer.readableBytes() < packetSize - 3) {
				buffer.resetReaderIndex();
				return null;
			}
			packetSize -= 3;
			
		} else if (headerFlag == NeuronPacketHeader.RHEADER) {
			packetSize = buffer.readShort();
//			logger.trace("--------packet size: "+ packetSize);
			if (buffer.readableBytes() < packetSize) {
				buffer.resetReaderIndex();
				return null;
			}
		} else if (headerFlag == NeuronPacketHeader.SMALL_HEADER) {
			opType = buffer.readByte();
			userId = buffer.readInt();
			
			if (opType == 59) { ///< attack 
				packetSize = 40;
//				logger.trace("--------packet size: "+ packetSize);
				if (buffer.readableBytes() < packetSize) {
					buffer.resetReaderIndex();
					return null;
				}
			} else if (opType == 64) { ///< broadcasting
				packetSize = 114;
				if (buffer.readableBytes() < packetSize) {
					buffer.resetReaderIndex();
					return null;
				}
			}
			
		} else {
			String msg = "[Error] Wrong Message Header Flag. - "+ headerFlag;
			throw new NeuronException(msg);
		}

		
		ChannelBuffer readBytes = buffer.readBytes(packetSize);
		return new CallbackPacket(headerFlag, channel, readBytes, opType, userId);
	}

}
