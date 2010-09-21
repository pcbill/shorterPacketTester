package moregeek.shorterPacket;

public class Main {
	public static void main(String[] args) {
		assert args.length == 2;
		
		// Parse options.
        String host = (String)args[0];
        int port = Integer.parseInt((String)args[1]);
		new Client(host, port);
	}
}
