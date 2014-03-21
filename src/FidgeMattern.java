/* 
Author : Ravi Teja Karumajji
Date : 02/10/2014 
Purpose: Advanced Operating Systems - project 1
*/

import java.util.*;
import java.net.*;
import java.io.*;

public class FidgeMattern extends Thread{

	private ServerSocket LocServersock;
	 vector_clock local;
	 

	
	public FidgeMattern(int port, int n, int id) throws IOException
		{
		LocServersock = new ServerSocket(port);
		local = new vector_clock(n,id);
		}
	
	public void run(){
		int counter = 0;
		int byc = 0;
		while(true){
				try{
					System.out.println("\n\nServer ready for connections on port: " +LocServersock.getLocalPort());
					Socket server = LocServersock.accept();
					byte[] buf;
					DataInputStream in = new DataInputStream(server.getInputStream());
					int[] rem = new int[local.num_nodes];
					String msg = in.readUTF();
					System.out.println("\nNew message arrived: " +msg);
					for(int i=0;i<local.num_nodes;i++){
						rem[i] = in.readInt();	
					}
					
					local.merge(rem);
					server.close();
					if (msg.charAt(0) == 'B' && msg.charAt(1) == 'y' && msg.charAt(2) == 'e'){
						byc++;
						System.out.println("\nNumber of Bye messages arrived: " +byc);
						if(byc >= 4){
							try{
								System.out.println("\n**************System received Bye messages from all nodes***********");
								System.out.println("\n\t\tClock value after receiving the last special message");
								vector_clock.printClock();
								System.out.println("**************System terminating gracefully************\n\t\tClosing server connection");
								LocServersock.close();
								System.out.println("\t\tServer connection closed");
								
								}
								catch(IOException e){
									e.printStackTrace();
								}
							break;
						}
					}
					
				}
				catch(IOException e)
		         {
		            e.printStackTrace();
		            break;
		         }
		
			counter++;
		}
		
	}
	
	
	
	public static void main(String [] args){
		int num_nodes = 0;
		HashMap<String,nodein> nodes = new HashMap<String,nodein>();
		int my_id;
		my_id = Integer.parseInt(args[0]);
		//System.out.println("I am at reading first line");
		
		try{
			
			Scanner rfile = new Scanner(new File("Config.txt"));
			String rline;
			int valcn = 0;
			while(rfile.hasNextLine()){
				// System.out.println("I am at reading first line");
				
				//	System.out.println("I am reading real line");
					rline = rfile.nextLine();
					if(rline.charAt(0) != '#'){
						// System.out.println(rline.charAt(0));
						if (valcn == 0){
							num_nodes = Character.getNumericValue(rline.charAt(0));
							// System.out.println("" +rline.charAt(0));
							System.out.println("Number of nodes = " +num_nodes);
							valcn = 1;
							rline = rfile.nextLine();
							
							}
						String[] params = rline.split(" ");
						System.out.println(" " +params[0] +" " +params[1] +" " +params[2]);
						nodein temp = new nodein(params[0],params[1],params[2]);
						nodes.put(params[0],temp);
					
						}
					
				
				}
			int port = Integer.parseInt(nodes.get(Integer.toString(my_id)).port);
			FidgeMattern T1 = new FidgeMattern(port,num_nodes,my_id);
			T1.start();
			FidgeIntern T2 = new FidgeIntern(nodes,my_id);
			T2.start();
			
			
		
			}
		
		catch (IOException e){
			e.printStackTrace();
			}
		
		
	}
}

class nodein {
	String id;
	String hostname;
	String port;
	
	public nodein(String i, String h, String p){
		this.id = i;
		this.hostname = h;
		this.port = p;
		
	}
	
	public void print(){
		System.out.println("");
		System.out.print(" " +this.id + " " +this.hostname + " " +this.port);
	}
}

class vector_clock{
	public static int num_nodes;
	public int id;
	public static int[] clock;
	
	vector_clock(int n, int it){
		num_nodes = n;
		id = it;
		clock = new int[n];
		for(int i=0;i<n;i++){
			clock[i] = 0;
		}
	}
	
	vector_clock(int[] c){
		clock = c;
	}
	
	public synchronized  void merge(int[] m){
		for(int i=0;i<num_nodes;i++){
			clock[i] = Math.max(this.clock[i], m[i]);
		}
		clock[id] = clock[id] + 1;
		System.out.println("New clock value after reading the last arrived message: ");
		vector_clock.printClock();
	}
	
	public synchronized static void change(int id){
		clock[id] = clock[id] + 1;
		System.out.println("Clock changed internally, new clock value:  ");
		vector_clock.printClock();
	
	}
		
	public synchronized static void printClock(){
		System.out.println();
		for(int i=0;i<num_nodes;i++){
			System.out.print(" " +clock[i]);
		}
		System.out.println();
	}
}

class FidgeIntern extends Thread {
	private Socket client;
	String hostname;
	int port;
	HashMap<String,nodein> nodes;
	int rno1,rno2,rno3;
	int id;
	
	FidgeIntern(HashMap<String,nodein> n, int id){
		nodes = n;
		this.id = id;
		}
	
	
	public void run(){
		int counter = 0;
		int num_nodes = vector_clock.num_nodes;
		System.out.println("Client ready to send");
		while(true){
			
			Random ran = new Random();
			rno1 = ran.nextInt(3);	
			if(rno1 != 0){
				try{
					
						try{
							if(counter < 30){
								rno2 = ran.nextInt(num_nodes);
								if(rno2 != id){
									hostname = nodes.get(Integer.toString(rno2)).hostname;
									port = Integer.parseInt(nodes.get(Integer.toString(rno2)).port);
									rno3 = ran.nextInt(3000);
									Thread.sleep(rno3);
									System.out.println("\n\nConnecting to host " +hostname + " on port: " +port);
									Socket client = new Socket(hostname,port);
									vector_clock.change(id);
									counter++;
									System.out.println("System internal counter: " +counter);
								
								
				         			DataOutputStream out = new DataOutputStream(client.getOutputStream());
									out.writeUTF("Hello from " +nodes.get(Integer.toString(id)).hostname +", This is my message number: " +counter);
									// System.out.println("\nConnected and message sent successfully");
									for(int i=0;i<num_nodes;i++){
										out.writeInt(vector_clock.clock[i]);
									}
									System.out.println("\nConnected and message sent successfully");
									client.close();
								}
							}
							if(counter >= 30){
								System.out.println("*************System stopping generation of internal events and preparing to send exit messages***********");
								System.out.println("\n\t\tClock value before sending the first special message");
								vector_clock.printClock();
								for(int k =0;k<num_nodes;k++){
										if (k != id){
											hostname = nodes.get(Integer.toString(k)).hostname;
											port = Integer.parseInt(nodes.get(Integer.toString(k)).port);
											System.out.println("Sending special message to " +hostname + " on port: " +port);
											
											Socket client = new Socket(hostname,port);
											//System.out.println("System internal counter: " +counter);
											
											//OutputStream outToServer = client.getOutputStream();
						         			DataOutputStream out = new DataOutputStream(client.getOutputStream());
						         			out.writeUTF("Bye from "+hostname);
											// System.out.println("\nConnected and message sent successfully");
						         			vector_clock.change(id);
											for(int j=0;j<vector_clock.num_nodes;j++){
												out.writeInt(vector_clock.clock[j]);
											}
										}
								}
								System.out.println("***************Special messages sent successfully, closing the client connection");
								break;	
							}
							
						}
						catch(IOException e){
							e.printStackTrace();
						}
					
				}
		
				catch(InterruptedException e)
				{
					counter++;
					System.out.println("\nSystem internal counter: " +counter);
					e.printStackTrace();
				}
			}
			
			else{
				counter++;
				System.out.println("Generating internal event");
				System.out.println("\nSystem internal counter for send and internal events: " +counter);
				vector_clock.change(id);
			}
			
		}
	}
}