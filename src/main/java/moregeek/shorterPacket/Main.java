package moregeek.shorterPacket;

public class Main {
	public static void main(String[] args) {
		assert args.length == 2;
		
        String host = (String)args[0];
        int port = Integer.parseInt((String)args[1]);
		
        Client client = new Client(host, port);
		
        
	}
}
