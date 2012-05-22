import java.net.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

/**
  * BoardServer
  * @author Nicholas Joerger
  * @author Tim Chisholm
  * @author Shane Rand
  * @author Hassan Robinson
 */

public class BoardServer extends Thread implements ServerInformation{
	//holds users and their output streams
	private static Vector<BoardThread> boardList = new Vector<BoardThread>();
	private static Vector<PrintWriter> outputStreams = new Vector<PrintWriter>();
	
	//holds basic information
	private String move;
	private ServerSocket bsocket;
	private Socket bs=null;
	private Socket cs=null;
	
	//visuals that can be manipulated
	JFrame myFrame;
	JTextArea jtaLog;
	JLabel jlUsers;
	JLabel jlGames;
	
	/**
	  * main starts the server
	  * @param args (String[]) no purpose in this application, just needed to function
	 */
	
	public static void main(String[] args){
 		new BoardServer();
 	}
	
	/**
	  * BoardServer creates a new Server and begins saving connections for new games
	 */
	public BoardServer(){
		try{
			System.out.println("getLocalHost: "+InetAddress.getLocalHost() );
			System.out.println("getByName:    "+InetAddress.getByName("localhost") );
			
			//creates new server sockets
			bsocket = new ServerSocket(BOARD_PORT);
			
			
			
			start();//starts gui and chat server as well
			
			while(true){
				//creates client socket
				bs = bsocket.accept();
				
				//also starts new board thread
				BoardThread st = new BoardThread(bs);
				boardList.add(st);
			}
		}	
		catch(BindException be){
			System.out.println("Server already running, closing this instance");
		}
		catch(IOException ioe){
			System.out.println("IO Error");
			ioe.printStackTrace();
		}
		
	}//end Constructor
	
	/**
		run method for the thread
	*/
	public void run(){
		gui();//starts the gui for the server
		new Main();//starts the chat server
	}
	
	/**
		Builds the Graphical User Interface
	*/
	private void gui(){//creates the visuals for the server information
		myFrame=new JFrame("Server");
		myFrame.setLayout(new BorderLayout());
		
		JPanel jpSouth = new JPanel();
		JLabel jlUsersInfo= new JLabel("Number of Users Online: ");
		JLabel jlGamesInfo= new JLabel("Number of Games Occuring: ");
		
		jlUsers= new JLabel(" ");
		jlGames= new JLabel(" ");
		
		jpSouth.add(jlUsersInfo);
		jpSouth.add(jlUsers);
		jpSouth.add(jlGamesInfo);
		jpSouth.add(jlGames);
		
		jtaLog=new JTextArea(10,10);
		jtaLog.setLineWrap(true);
		jtaLog.setWrapStyleWord(true);
		JScrollPane jspScrolling = new JScrollPane(jtaLog);
		
		//Adds in the ServerIP and stuff to the jtaLog
		try{	
			jtaLog.append("GetLocalHost: "+InetAddress.getLocalHost() );
			jtaLog.append("\nGetByName:    "+InetAddress.getByName("localhost") );
		}
		catch(Exception e){}
		
		myFrame.add(jspScrolling, BorderLayout.CENTER);
		myFrame.add(jpSouth, BorderLayout.SOUTH);
		
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setSize(450,250);
		myFrame.setVisible(true);
	}
	
	/**
	  * BoardThread is used to accept a second client to start a new game it also starts to listen for new movements
	  * @author Nicholas Joerger
	  * @author Tim Chisholm
	  * @author Shane Rand
	  * @author Hassan Robinson
	 */
	
	class BoardThread extends Thread{
		private Socket bs;
		private Socket cs;
		private BufferedReader br;
		
		/**
			Parameterized constructor accepts a socket object and uses it to play a game
			@param _bs the socket that's used to connect
		*/
		public BoardThread(Socket _bs){
			this.bs = _bs;
			//br = new BufferedReader(new InputStreamReader(bs.getInputStream());
			
			try{
				cs = bsocket.accept();
				jtaLog.append("\nconnected\n");
				
				start();
				jtaLog.append("started");
			}
			catch(IOException ioe)
				{ioe.printStackTrace();}
			catch(NullPointerException npe){ }
			
		}
		
		/**
			run method for BoardThread
			creates new readThreads
		*/
		public void run(){
			//the appropriate socket and the player number
			new ReadThread(bs,0);
			new ReadThread(cs,1);
		}
		
	}//end BoardThread
	
	/**
	  * ReadThread creates a new reading from client thread to get the movement of pieces
	  * @author Nicholas Joerger
	  * @author Tim Chisholm
	  * @author Shane Rand
	  * @author Hassan Robinson
	 */
	
	class ReadThread extends Thread{
		private Socket cs;
		private BufferedReader in;
		private PrintWriter out;
		private int playerNumber;
		private int runCounter=0;
		
		/**
		  * ReadThread is used to create a new readthread from a socket and player number
		  * @param _cs (Socket)
		  * @param _playerNumber (int)
		 */
		
		public ReadThread(Socket _cs, int _playerNumber){
			this.cs = _cs;
			playerNumber=_playerNumber;
			start();
		}
		
		/**
		  * run is used to read in movements from the client to push to ther other client after first recieving the type of piece
		 */
		
		public void run(){
			try{
				in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
				out = new PrintWriter(cs.getOutputStream());
				
				//adds piece to output stream vector
				outputStreams.add(out);
				
				//sends the player number to the client
				out.println(playerNumber);
				out.flush();
				
				//gets the blank move
				while(true){
					String move=" ";
					try{//goes until it retrieves a null
						if(!(move=in.readLine()).equals(null)){
							jtaLog.append("\n"+move);//shows server console move and writes to other client
					
							new WriteThread(out,move);
						}//otherwise it tries to remove the user
						else{
							removeUser();//removes the current user
						}
					}
					catch(NullPointerException npe){//removes the current user
						removeUser();
					}
					catch(IOException ioe){}
				}
			}//end try
			
			catch(SocketException se){//removes this object and its location in the chat server
				int _position=removeUser();
				Main.removeThisUser(_position);
			}
			catch(IOException ioe)
				{ioe.printStackTrace();}
			finally{//always closes connections
				try{
					out.close();
					in.close();
				}
				catch(IOException ioe)
					{ioe.printStackTrace();}
			}
			
		}//end run
		
		/**
			Removes a user from chat/game
		*/
		private int removeUser(){
			try{
				//sends win message to other client
				new WriteThread(out,"Win");
				
				try//allows enough time for the message to be sent
					{ Thread.sleep(4000); }
				catch(InterruptedException ie)
					{ ie.printStackTrace();}
				
				int position =-1;//finds the output stream to kill
				for(int i=0; i<outputStreams.size(); i++){
					if(out.equals(outputStreams.get(i))){
						if((i<outputStreams.size())&&(i % 2 == 0)){
							outputStreams.get(i).close();//closes this stream and the other stream
							outputStreams.remove(i);
					
							outputStreams.get(i).close();
							outputStreams.remove(i);
					
							position=i;//saves the position that was removed
						}
						else{
							outputStreams.get(--i).close();//closes this streams
							outputStreams.remove(i);
					
							outputStreams.get(i).close();//closes the other stream associated with this one
							outputStreams.remove(i);
					
							position=i;//saves the position at which this one was removed
						}
						break;
					}
				}
				
				if(position>-1){//removes the boardlist associated with this position
					boardList.remove(position);
				}
				
				//closes the sockets and output streams that are inheirent of this theard
				out.close();
				in.close();
				this.cs.close();
				
				//returns the position that was removed
				return position;
			}
			catch(IOException ioe){}
//				{ioe.printStackTrace();}
			return -1;//returns a false
		}
	}//end ReadThread
	
	/**
	  * WriteThread is used to write the move of one opponenet to the other
	  * @author Nicholas Joerger
	  * @author Tim Chisholm
	  * @author Shane Rand
	  * @author Hassan Robinson
	 */
	
	class WriteThread extends Thread{
		//the move to be printed and the outputStream
		private String move;
		private PrintWriter out;
		
		/**
		  * WriteThread is used to intake the output stream and the move
		  * @param _out (PrintWriter)
		  * @param _move (String)
		 */
		
		public WriteThread(PrintWriter _out,String _move){
			this.out = _out;
			this.move = _move;
			start();
		}
		
		/**
		  * run is used to print to the other client
		 */
		
		public void run(){
			//finds the number of players and the number of games being played
			jlUsers.setText(Integer.toString(outputStreams.size()));
			jlGames.setText(Integer.toString((outputStreams.size()/2)));
			
			//tries to find this outputstream in the arraylist then goes one above if the index is even or down one if the index is odd to send this clients connection message
			for(int i=0; i<outputStreams.size(); i++){
				if(out.equals(outputStreams.get(i))){
					if((i<outputStreams.size())&&(i % 2 == 0)){//if even, augments the counter by one and then prints to stream
						outputStreams.get(++i).println(move);
						outputStreams.get(i).flush();
					}
					else{
						outputStreams.get(--i).println(move);//if odd, removes one from the counter and then prints to stream
						outputStreams.get(i).flush();
					}
					break;
				}
			}
		}
		
	}//end WriteThread
	
}//end BoardServer
