import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;


public class FidgeMattern extends Thread{

	private ServerSocket LocServersock;
	// int num_nodes;

	
	public FidgeMattern(int port, int n, int id) throws IOException
		{
		LocServersock = new ServerSocket(port);
		//num_nodes = n;
		local = new vector_clock(n,id);
		}
	
	public void run(){
		while(true){
			try{
				Socket server = LocServersock.accept();
				byte[] buf;
				DataInputStream in = new DataInputStream(server.getInputStream());
				int[] rem = new int[local.num_nodes];
				
				for(int i=0;i<local.num_nodes;i++){
					rem[i] = in.readInt();	
				}
				vector_clock remote = new vector_clock(rem);
				
				local = local.merge(remote);
				
			}
			catch(IOException e)
	         {
	            e.printStackTrace();
	            break;
	         }
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
						//int id = Integer.parseInt(params[0]);
						System.out.println(" " +params[0] +" " +params[1] +" " +params[2]);
						nodein temp = new nodein(params[0],params[1],params[2]);
						nodes.put(params[0],temp);
						System.out.println(nodes.get(params[0]).port);
						}
					
				
				}
			
		//	vector_clock loc_clock = new vector_clock(num_nodes,my_id);
			// System.out.println("id: " +my_id);
			// my_id = 1;
			int port = Integer.parseInt(nodes.get(Integer.toString(my_id)).port);
			
			// System.out.println(port);
			
			
			
			
				
			
			
			
			
			/*BufferedReader rfile = new BufferedReader(new FileReader("Config.txt"));
			String rline = null;
		
			
			while((rline = rfile.readLine()) != null && rfile.readLine().charAt(0) != '#'){
				
				numb_nodes = */
			}
		
		catch (IOException e){
			e.printStackTrace();
			}
		
		
		
		/* System.out.println("I am at printing final");
		for(int i=0;i<num_nodes;i++){
			//System.out.println("I am at printing final");
			nodes.get(i).print();
		} */
		
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
	public int num_nodes;
	public int id;
	public int[] clock;
	
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
	
	public vector_clock merge(vector_clock c){
		for(int i=0;i<num_nodes;i++){
			this.clock[i] = Math.max(this.clock[i], c.clock[i]);
		}
		this.clock[id] = this.clock[id] + 1;
		return this;
	}
	
	public void printClock(){
		System.out.println();
		for(int i=0;i<num_nodes;i++){
			System.out.print(" " +clock[i]);
		}
	}
}

class FidgeIntern extends Thread {
	private Socket client;
	String hostname;
	int port;
	HashMap<String,nodein> nodes;
	int rno1,rno2;
	
	FidgeIntern(HashMap<String,nodein> n, vector_clock l){
		nodes = n;
		
		
		}
	
	
	public void run(){
		while(true){
			
			Random ran = new Random();
			rno1 = ran.nextInt(1);
			
			if(rno1 == 1){
			try{
			Socket client = new Socket(hostname,port);
			}
		
			catch(IOException e)
			{
			e.printStackTrace();
			}
			}
			
		}
	}
}