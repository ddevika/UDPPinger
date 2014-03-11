import java.io.*;
import java.net.*;
import java.util.*;

/*
* Server to process ping requests over UDP. java PingClient host port
*/
public class PingClient
{

public static void main(String[] args) throws Exception
{
	/* To test to compile in Eclipse IDE*/
	/*String [] args1 = new String[2];
	args1[0] = "127.0.0.1";
	args1[1] = "1024";
	System.out.println("argument 0: " + args1[0]);
	// Get command line argument.
	if (args1.length != 2) {
		System.out.println("Required arguments: host port");
		return;
	}

	String ServerName =args1[0];
	int port = Integer.parseInt(args1[1]);*/
	 /*End Test*/
	
	// Get command line argument.
	if (args.length != 2) {
		System.out.println("Required arguments: host port");
		return;
	}

	String ServerName =args[0];
	int port = Integer.parseInt(args[1]);
	
	// Create a datagram socket for receiving and sending UDP packets
	// through the port specified on the command line.
	DatagramSocket socket = new DatagramSocket();
	InetAddress IPAddress =InetAddress.getByName(ServerName);
	// Processing loop.

	//array to store each sendTime 
	Long [] sendTime = new Long[10];
	
	// Send 10 ping requests
	for(int i=0; i<10; i++){

		long SendTime = System.currentTimeMillis();
		String Message = "Ping "+ i + " " + SendTime + "\r\n";
		
		//saving SendTime to the array
		sendTime[i] = SendTime;

		DatagramPacket request = new DatagramPacket(Message.getBytes(), Message.length(),IPAddress,port );
		socket.send(request);
		DatagramPacket reply = new DatagramPacket(new byte[1024], 1024);

		socket.setSoTimeout(1000);

		try {
			socket.receive(reply);
		}
		catch(IOException E)
		{
			System.out.println("Exception Caught " + E.getMessage());
		}

		Thread.sleep(1000);

	}
	
	//calculates min, max, avg RTT
	long MAX =0 , MIN=0, SUM=0;
	long smallest = Long.MAX_VALUE;
	long biggest = Long.MIN_VALUE;
	
	System.out.println("test "+ smallest);
	
	for (int i=0; i<sendTime.length; i++)
	{
		System.out.println("Send Time 2 "+ sendTime[i]);
		//find MIN RTT
		if (sendTime[i] < smallest)
		{
		smallest = sendTime[i];
		}
		MIN = smallest;
		
		//find MAX RTT
		if (sendTime[i] > smallest)
		{
		biggest = sendTime[i];
		}
		MAX = biggest;
		
		//find SUM of RTT
		SUM += sendTime[i];
	}
	
	System.out.println("Maximum RTT is " + MAX);
	System.out.println("Minimum RTT is " + MIN);
	System.out.println("Average RTT is " + SUM/10);
	//end of calculation
}


/* 
 * Print ping data to the standard output stream.
 */
private static void printData(DatagramPacket request) throws Exception
{
   // Obtain references to the packet's array of bytes.
   byte[] buf = request.getData();

   // Wrap the bytes in a byte array input stream,
   // so that you can read the data as a stream of bytes.
   ByteArrayInputStream bais = new ByteArrayInputStream(buf);

   // Wrap the byte array output stream in an input stream reader,
   // so you can read the data as a stream of characters.
   InputStreamReader isr = new InputStreamReader(bais);

   // Wrap the input stream reader in a bufferred reader,
   // so you can read the character data a line at a time.
   // (A line is a sequence of chars terminated by any combination of \r and \n.) 
   BufferedReader br = new BufferedReader(isr);

   // The message data is contained in a single line, so read this line.
   String line = br.readLine();

   // Print host address and data received from it.
   System.out.println(
      "Received from " + 
      request.getAddress().getHostAddress() + 
      ": " +
      new String(line));
}
}