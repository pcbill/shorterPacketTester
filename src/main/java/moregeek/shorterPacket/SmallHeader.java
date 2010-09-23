package moregeek.shorterPacket;

import com.moregeek.blaze.net.DataInput;
import com.moregeek.blaze.net.DataOutput;
import com.moregeek.blaze.net.NeuronPacketHeader;
import com.moregeek.blaze.net.PacketHeader;

public class SmallHeader implements PacketHeader {
	private static final long serialVersionUID = 3739372503937610610L;

	private byte opType;
	private Integer userId;
	
	
	public SmallHeader(byte opType, Integer userId) {
		this.opType = opType;
		this.userId = userId;
	}
	
	public Integer getUserId() {
		return userId;
	}

	@Override
	public void deserializeFrom(DataInput input) {
		opType = input.readByte();
		userId = input.readInt();
	}

	@Override
	public void serializeTo(DataOutput output) {
		output.writeByte(opType);
		output.writeInt(userId);
	}

	@Override
	public byte getHeaderFlag() {
		return NeuronPacketHeader.SMALL_HEADER;
	}
	
}
