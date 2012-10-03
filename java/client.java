import java.io.*;
import java.net.*;

public class client {
	public static void main(String[] args) throws UnknownHostException, IOException {

	    for(int i=0;i<10;i++) {
	    	sendValues(i, 240, 1, 0);
	    }
	}
	
	public static void sendValues(int leftSpeed, int rightSpeed, int leftDirection, int rightDirection) throws UnknownHostException, IOException {
		Socket socket;
		socket = new Socket("127.0.0.1", 8888);
		socket.setSendBufferSize(4096);
		PrintStream output;
		String message = Integer.toString(leftSpeed) + "," + Integer.toString(rightSpeed) + "," +
				Integer.toString(leftDirection) + "," + Integer.toString(rightDirection);

		try {
			output = new PrintStream(socket.getOutputStream());
			output.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		   
	}
}