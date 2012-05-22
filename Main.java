import java.net.*;
import java.util.*;
import java.io.*;

/**
  * Main is the chat server
  * @author Nicholas Joerger
  * @author Tim Chisholm
  * @author Shane Rand
  * @author Hassan Robinson
 */

public class Main implements ServerInformation{
	//holds users and their output streams and their user names
	private static Vector<UserThread> threadList= new Vector<UserThread>();
	private static Vector<PrintWriter> chatOutputStreams = new Vector<PrintWriter>();
	private static Vector<String> userList = new Vector<String>();
	private String userName;
	private String userMsg;
	private Thread tmp;
	
	/**
	  * Main is used to start and run the chat server
	 */
	
	public Main(){
		try{
			//creates new server sockets
			ServerSocket csocket = new ServerSocket(CHAT_PORT);
			
			//creates client socket
			Socket cs=null;
			
			//int players = 0;
		
			
			//infinite loop to accept clients and add them to the chat
			while(true){
			
				cs = csocket.accept();	

				UserThread st = new UserThread(cs);
				threadList.add(st);
			}
			
			
		}
		catch(BindException be)
			{ System.out.println("Server already running, closing this instance"); }
		catch(IOException ioe)
			{ System.out.println("IO Error"); }
	}//end main
	/**
	  * removeThisUser is used to remove the user at the position in the corresponding arraylist
	  * @param _position (int) the position to remove
	 */
	public static void removeThisUser(int _position){
		chatOutputStreams.remove(_position);
		threadList.remove(_position);
		userList.remove(_position);
		System.out.println("User left");
		//catch user leaving
	}
	
	/**
	  * UserThread is used to hold a client connection and information
	  * @author Nicholas Joerger
	  * @author Tim Chisholm
	  * @author Shane Rand
	  * @author Hassan Robinson
	 */
	
	class UserThread extends Thread{
		//holds client socket for input in the read thread
		Socket cs;
		//holds the output for later use in the write thread
		PrintWriter out=null;
		
		//brings in the client socket and tries to create printWriter
		/**
		  * UserThread is used to create a new client connection with the use of a filled socket
		  * @param _cs is used to hold the client socket connection
		 */
		public UserThread(Socket _cs){
			this.cs = _cs;
			start();
		}
		/**
		  * run is used to create an independent read thread
		 */
		public void run(){
			new ReadThread(cs);			
		}//end run
		
		/**
		  * writeMessage is used to write a message to a client
		  * @param opw (PrintWriter) writes a message to one client
		  * @param msg (String) the message to write
		 */
		
		public void writeMessage(PrintWriter opw, String msg){
			opw.println(msg);
			opw.flush();
		}
		
	}//end user thread
	
	/**
	  * ReadThread is used to read messages from the clients to later rebroadcast to others
	  * @author Nicholas Joerger
	  * @author Tim Chisholm
	  * @author Shane Rand
	  * @author Hassan Robinson
	 */
	
	class ReadThread extends Thread{
		private Socket cs;
		private BufferedReader br;
		private PrintWriter opw;
		private String name;
		
		/**
		  * ReadThread is used to create a new reading method
		  * @param _cs (Socket) is the client socket that is currently connected
		 */
		 
		public ReadThread(Socket _cs){
			this.cs = _cs;
			start();
		}	
		
		/**
		  * run is used to read information to rebroadcast to other clients
		 */
		public void run(){
			try{
				//creates new read streams and adds them to read threadlist
				br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
				opw = new PrintWriter(cs.getOutputStream());
				chatOutputStreams.add(opw);
											
				//gets the user name and stores it and sets it
				userName = br.readLine();
				name= userName;
				setUserName(name);
				
				//creates an independent write thread
				new WriteThread(cs);
				
				while(true){
					//keeps reading in messages
					userMsg = br.readLine();
					
					//gets the first portion and then tries to make a move thread
					if(userMsg.substring(0,2).equals("~")){
						System.out.println(userMsg);
						new BoardMove(cs,userMsg);
					}
					//otherwise send a list of all users and the message with the name of the user
					else{
						String allUsers=" Online Users:~";
						for(int counter =0; counter<userList.size();counter++){
							allUsers+=userList.get(counter)+"~";
						}			
						new WriteThread(cs,userMsg+allUsers,name);
					}
				}
			}//end try
			catch(SocketException se){
				//removes the name from output stream, threadlist, and userlist
				int position=-1;

				for(int counter=0; counter<chatOutputStreams.size();counter++){
					if(opw.equals(chatOutputStreams.get(counter))){
						position=counter;
						chatOutputStreams.remove(counter);
					}
				}
				if(position>-1){
					threadList.remove(position);
					userList.remove(position);
				}
				System.out.println("User left");
				//catch user leaving
			}
			catch(IOException ioe){
				ioe.printStackTrace();
			}
			catch(StringIndexOutOfBoundsException ste){}
		}//end run
		
		/**
		  * getUserName is used to get the thread's user name
		  * @return name(String) is the name of the client
		 */
		 
		public String getUserName(){
			return name;
		}
		
		/**
		  * setUserName is used to set the clients nick name
		  * @param _name is the client nick name
		 */
		 
		public void setUserName(String _name){
			userList.add(_name);
		}
	}//end ReadThread
	
	/**
	  * WriteThread writes information out to the other connected users
	  * @author Nicholas Joerger
	  * @author Tim Chisholm
	  * @author Shane Rand
	  * @author Hassan Robinson
	 */
	
	class WriteThread extends Thread{
		private PrintWriter opw;
		private Socket cs;
		private String userMsg;
		private String name;
		
		/**
		  * WriteThread the write thread is used to write information to other cleitns
		  * @param _cs (Socket) is used to write to other clients
		 */
		 
		public WriteThread(Socket _cs){
			this.cs = _cs;
			try{
				//creates new out and prints greeting message
				opw = new PrintWriter(cs.getOutputStream());
				
				opw.println("Welcome, you joined the chat");
				opw.flush();
				
			}
			catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		
		/**
		  * WriteThread is used to bring in an already created connection
		  * @param _cs (Socket) the socket of the user
		  * @param _userMsg (String) the message the user wants to send
		  * @param _name (String) the name of the client
		 */
		 
		public WriteThread(Socket _cs,String _userMsg,String _name){
			this.cs = _cs;
			userMsg = _userMsg;
			name = _name;
			start();
		}
		
		/**
		  * run is used to write out the information to the other users
		 */
		
		public void run(){
			for(int i=0; i<threadList.size(); i++){
				UserThread ut = threadList.get(i);
				
				opw = chatOutputStreams.get(i);
				ut.writeMessage(opw,name + ": " + userMsg);
			}
		}//end run
	
	}//end WriteThread
	
	/**
	  * BoardMove is used to send moves to other clients
	  * @author Nicholas Joerger
	  * @author Tim Chisholm
	  * @author Shane Rand
	  * @author Hassan Robinson
	 */ 
	
	class BoardMove extends Thread{
		private PrintWriter opw;
		private Socket cs;
		private String userMsg;
		private ArrayList<String> moves = new ArrayList<String>();
		
		/**
		  * BoardMove is used to bring in a message to send to a client
		  * @param _cs (Socket) the socket to try to connect and send a message to
		  * @param _userMsg (String) the message to send
		 */
		 
		public BoardMove(Socket _cs,String _userMsg){
			this.cs = _cs;
			userMsg = _userMsg;
			if(moves.size() < 2){
				moves.add(userMsg);
			}
			start();
		}
		
		/**
			Run method for BoardMove 
			used to send movements
		*/
		public void run(){
			//goes through all the users and makes it a userthread
			for(int i=0; i<threadList.size(); i++){
				UserThread ut = threadList.get(i);
				//then it sends the information to all the output streams
				for(int j=0; j<moves.size(); j++){
					opw = chatOutputStreams.get(i);
					ut.writeMessage(opw,moves.get(j));
				}
			}
		}//end run
	
	}//end WriteThread
}
