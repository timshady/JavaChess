import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JPanel implements ServerInformation, Runnable {
	// JFrame myFrame = new JFrame("Chat");
	
	private JTextArea jtaChat = new JTextArea(4,25);
	private JTextArea jtaType = new JTextArea(4,25);
	private JTextArea jtaOnlineUsers = new JTextArea("Online Users:\n",15,15);
	private Socket s;
	
	JButton jbSend = new JButton("Send");
	
	WriteThread wts =null;
	
	/**
		Parameterized constructor for Client
		@param _ip the IP address to connect to
	*/
	public Client(String _ip){
		try{
			s = new Socket(_ip,CHAT_PORT);
			wts=new WriteThread(jbSend);
			wts.start();
			gui();
			
			//start read thread
			ReadThread rt = new ReadThread(s);
			rt.start();
		}
		catch(UnknownHostException uhe) 
			{ JOptionPane.showMessageDialog(null,"Server isnt present","Error",JOptionPane.ERROR_MESSAGE); }
		catch(IOException ie) 
			{ JOptionPane.showMessageDialog(null, "Sorry there is was an I/O error", "I/O error", JOptionPane.ERROR_MESSAGE); }
		catch(Exception e)
			{ JOptionPane.showMessageDialog(null,"An Error occured","Error",JOptionPane.ERROR_MESSAGE); }
	}
	
	/**
		Buids the Graphical User Interface
	*/
	private void gui(){
		setLayout(new BorderLayout(5,5));
		
		jtaChat.setLineWrap(true);
		jtaType.setLineWrap(true);
		jtaOnlineUsers.setLineWrap(true);
		
		//uses DefaultCaret for text scrolling
		DefaultCaret caret = (DefaultCaret)jtaChat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		jtaChat.setWrapStyleWord(true);
		jtaType.setWrapStyleWord(true);
		jtaOnlineUsers.setWrapStyleWord(true);
		
		jtaChat.setEditable(false);
		jtaOnlineUsers.setEditable(false);
		
		JPanel jpSouth = new JPanel();//add scroll
		jpSouth.add(new JScrollPane(jtaType));
		jpSouth.add(jbSend);
		
		JPanel jpEast = new JPanel();
		jpEast.add(new JScrollPane(jtaOnlineUsers));
				
		add(jpSouth, BorderLayout.SOUTH);
		add(new JScrollPane(jtaChat), BorderLayout.CENTER);
		add(jpEast, BorderLayout.EAST);
	}
	
	/**
		run method for Client
	*/
	public void run() {
	
	}
	
	/**
		Class for writing messages to clients
	*/
	class WriteThread extends Thread implements ServerInformation{
		private String userName,userMsg;
		private JButton jbSend;
		private PrintWriter out = null;
		
		/**
			Parameterized constructor for WriteThread
			@param _jbSend the JButton that's used to send the messsages
		*/
		public WriteThread(JButton _jbSend){
			jbSend = _jbSend;
			
			userName = JOptionPane.showInputDialog("Enter your user name");
			//System.out.println(userName);
		}
		
		/**
			Main running method
			sends message then dies
		*/
		public void run(){
			// setup the socket connection to the host
			try {
				out = new PrintWriter(s.getOutputStream());
				
				out.println(userName);
				out.flush();
				
				jbSend.addActionListener(new ActionListener(){
					//action listener for jButton
					public void actionPerformed(ActionEvent ae){
							out.println(jtaType.getText());
							out.flush();
					}
				});
				
				jtaType.addKeyListener(new KeyListener(){
					public void keyTyped(KeyEvent e){
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							out.println(jtaType.getText());
							out.flush();
							jtaType.setText("");
							jtaType.requestFocus();
						}							
					}
					
					/**
						keyPressed event from KeyListener
							if the enter key is pressed send the message
						@param e KeyEvent
					*/
					public void keyPressed(KeyEvent e){
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							out.println(jtaType.getText());
							out.flush();
							jtaType.setText("");
							jtaType.requestFocus();
						}
					}
					
					/**
						keyReleased event from KeyListener
							if the enter key is pressed send the message
						@param e KeyEvent
					*/
					public void keyReleased(KeyEvent e){
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							out.println(jtaType.getText());
							out.flush();
							jtaType.setText("");
							jtaType.requestFocus();
						}
					}
				});
				
			}
			catch(UnknownHostException uhe) 
				{ System.out.println("The server is not present"); }
			catch(IOException ie) 
				{ System.out.println("The was a connection problem"); }
			catch(Exception e)
				{ JOptionPane.showMessageDialog(null,"An Error occured","Error",JOptionPane.ERROR_MESSAGE); }
		}
	}
	
	/**
		ReadThread constantly runs to gather the messages from clients
	*/
	class ReadThread extends Thread implements ServerInformation{
		private String userName,userMsg;
		private JButton jbSend;
		private BufferedReader in = null;
		
		/**
			Parameterized constructor accepts a socket
			@param _s the socket from which to be reading
		*/
		public ReadThread(Socket _s){
			s = _s;
		}
		
		
		/**
			Main running method runs constantly to read the messages
		*/
		public void run(){
			// setup the socket connection to the host
			try {
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));

				String msg = in.readLine();
				jtaChat.append(msg + "\n");
				
				while(true){
					String chat = in.readLine();
						
					int users= chat.indexOf("Online Users:");
					String online=chat.substring(users);
					String text = chat.substring(0, users);
					
					jtaChat.append(text + "\n");
					
					String [] user=online.split("~");
					
					String toPrint="";
					for(int counter =0; counter<user.length; counter++){
						toPrint+=user[counter]+"\n";
					}
					
					jtaOnlineUsers.setText(toPrint);
				}
			}
			catch(UnknownHostException uhe) 
				{ JOptionPane.showMessageDialog(null,"Server isnt present","Error",JOptionPane.ERROR_MESSAGE); }
			catch(IOException ie)
				{ JOptionPane.showMessageDialog(null,"Sorry there is was an I/O error","Error",JOptionPane.ERROR_MESSAGE); }
		}
	}
}
