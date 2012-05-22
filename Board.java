import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.math.*;

/**
 * Board is used on the client side to start game play and to start chat communication
 * @author Nicholas Joerger
 * @author Tim Chisholm
 * @author Shane Rand
 * @author Hassan Robinson
 */

public class Board extends Thread implements ActionListener, ServerInformation{
	public static int playerNumber=-1;//player
	public static boolean moveTurn;//turn
	public static ArrayList <JButton> boardButtons = new ArrayList <JButton>();//holds board and pieces

	//holds the image icons for the pieces
	ImageIcon blankIcon, blackIcon, blackRookIcon, blackKnightIcon, blackBishopIcon, blackPawnIcon, blackKingIcon, blackQueenIcon,
	whiteRookIcon, whiteKnightIcon, whiteBishopIcon, whitePawnIcon, whiteKingIcon, whiteQueenIcon;

	//gets the piece from within the folder									  
	java.net.URL blankIconURL = Board.class.getResource("blank.png");
	java.net.URL blackIconURL = Board.class.getResource("black.png");
	java.net.URL whiteIconURL = Board.class.getResource("white.png");

	java.net.URL blackRookIconURL = Board.class.getResource("Black Rook.png");
	java.net.URL blackKnightIconURL = Board.class.getResource("Black Knight.png");
	java.net.URL blackBishopIconURL = Board.class.getResource("Black Bishop.png");
	java.net.URL blackPawnIconURL = Board.class.getResource("Black Pawn.png");
	java.net.URL blackKingIconURL = Board.class.getResource("Black King.png");
	java.net.URL blackQueenIconURL = Board.class.getResource("Black Queen.png");

	java.net.URL whiteRookIconURL = Board.class.getResource("White Rook.png");
	java.net.URL whiteKnightIconURL = Board.class.getResource("White Knight.png");
	java.net.URL whiteBishopIconURL = Board.class.getResource("White Bishop.png");
	java.net.URL whitePawnIconURL = Board.class.getResource("White Pawn.png");
	java.net.URL whiteKingIconURL = Board.class.getResource("White King.png");
	java.net.URL whiteQueenIconURL = Board.class.getResource("White Queen.png");

	//holds all of the image icons
	ArrayList<URL> urls = new ArrayList<URL>();

	//Jframe used to hold GUI
	JFrame myFrame = new JFrame();

	//buttons used to switch pieces on local side
	JButton firstButton,secondButton;
	int turn =0;//first button click, second button click
	char move;

	//read in and out
	BufferedReader in;
	PrintWriter out;

	//holds the piece to validate
	ArrayList<Piece> pieces = new ArrayList<Piece>();

	//holds socket of player and its icon, also chat
	Socket board = null;
	Client client;
	ImageIcon thisPlayersIcon;

	/**
	 * main starts the program
	 * @param args not used within this application
	 */

	public static void main(String[] args){
		new Board();
	}

	/**
	 * Board starts the game
	 */

	public Board(){//starts its thread
		start();
	}

	/**
	 * run is used to create the initial gui and server connection
	 */

	public void run(){
		//adds all the images to the url arraylist
		urls.add(blankIconURL);
		urls.add(blackIconURL);

		urls.add(blackRookIconURL);
		urls.add(blackKnightIconURL);
		urls.add(blackBishopIconURL);
		urls.add(blackPawnIconURL);
		urls.add(blackKingIconURL);
		urls.add(blackQueenIconURL);

		urls.add(whiteRookIconURL);
		urls.add(whiteKnightIconURL);
		urls.add(whiteBishopIconURL);
		urls.add(whitePawnIconURL);
		urls.add(whiteKingIconURL);
		urls.add(whiteQueenIconURL);

		boolean allIconsPresent = true;

		//checks to see if all the pieces exist
		for(int i = 0; i < urls.size(); i++){
			if(urls.get(i) == null){
				allIconsPresent = false;
				System.out.println("Problem with icons");
			}
		}
		//then adds the icons to the imageicons
		if (allIconsPresent) {
			ImageIcon blankIcon = new ImageIcon(blankIconURL);
			ImageIcon blackIcon = new ImageIcon(blackIconURL);

			blackRookIcon = new ImageIcon(blackRookIconURL);
			blackKnightIcon = new ImageIcon(blackKnightIconURL);
			blackBishopIcon = new ImageIcon(blackBishopIconURL);
			blackPawnIcon = new ImageIcon(blackPawnIconURL);
			blackKingIcon = new ImageIcon(blackKingIconURL);
			blackQueenIcon = new ImageIcon(blackQueenIconURL);

			blackKingIcon.setDescription("Black.King");

			whiteRookIcon = new ImageIcon(whiteRookIconURL);
			whiteKnightIcon = new ImageIcon(whiteKnightIconURL);
			whiteBishopIcon = new ImageIcon(whiteBishopIconURL);
			whitePawnIcon = new ImageIcon(whitePawnIconURL);
			whiteKingIcon = new ImageIcon(whiteKingIconURL);
			whiteQueenIcon = new ImageIcon(whiteQueenIconURL);

			whiteKingIcon.setDescription("White.King");
		}

		//creates Client GUI
		gui();

		//creates new read thread for client
		new ReadThread(in);
	}

	/**
		calls a method to return an image icon chosen by the user
	 */
	private ImageIcon getPiece(){//gets the clients piece want
		return setTeamIcons("1");
	}

	/**
		Gets the IP address from the user to connect to the server
	 */
	private void getIP(){//gets the ip address to the server the player wants to try to connect to
		try{
			String ip =" ";
			while(ip.equals(" ")){
				ip=JOptionPane.showInputDialog(null, "Please Input the Server IP Address", "Information Message", JOptionPane.INFORMATION_MESSAGE);
			}
			//saves board socket and ip with details and creates a new client with an ip address
			board = new Socket(ip,BOARD_PORT);//ip //SERVERIP
			client = new Client(ip);

			//creates the in and out streams to the server
			out = new PrintWriter(board.getOutputStream());
			in = new BufferedReader(new InputStreamReader(board.getInputStream()));
		}
		//tries to get IP again and again until it gets it
		catch(UnknownHostException uhe)
		{ getIP(); }
		catch(IOException ioe)
		{ getIP(); }
		catch(Exception e)
		{ getIP(); }
	}

	/**
		Builds the Graphical User Interface
	 */
	private void gui(){
		myFrame.setLayout(new BorderLayout(5,5));

		//tries to get the ip address
		getIP();

		//starts a new thread for the client
		new Thread(client).start();

		//gui addidtion
		JMenu file = new JMenu("File");
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					out.close();
					in.close();
					board.close();
				}
				catch(IOException ioe)
				{ JOptionPane.showMessageDialog(null,"Server went down","Error",JOptionPane.ERROR_MESSAGE); }
				finally
				{System.exit(0);}
			}
		});
		JMenu help = new JMenu("Help");
		JMenuItem rules = new JMenuItem("Rules");
		help.add(rules);
		rules.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{//reads in rules from file and shows user the rules 
					JFrame tempFrame = new JFrame("Rules");

					JTextArea jtaRules = new JTextArea(30,60);
					jtaRules.setWrapStyleWord(true);
					jtaRules.setLineWrap(true);

					JScrollPane jspRules = new JScrollPane(jtaRules);

					tempFrame.add(jspRules);

					BufferedReader fileIn = new BufferedReader(new FileReader("README.TXT"));
					String totalText=" ";
					String temp= " ";

					while((temp=fileIn.readLine())!=null){
						totalText+=temp+"\n";
					}

					jtaRules.setText(totalText);

					tempFrame.pack();
					tempFrame.setVisible(true);
					tempFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				}
				catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		});
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(file);
		menuBar.add(help);
		myFrame.add(client,BorderLayout.EAST);
		myFrame.setJMenuBar(menuBar);

		//gets the icon of this player
		thisPlayersIcon=getPiece();

		//sends it to the server
		out.println(thisPlayersIcon.getDescription());
		out.flush();

		//adds the buttons to the board and colors it appropriately
		addButtons();
		colorBoard();

		//sets frame needs
		myFrame.setSize(1050,600);
		myFrame.setLocation(200,50);
		myFrame.setResizable(false);
		myFrame.setVisible(true);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
		Adds buttons to the game client
	 */
	private void addButtons(){
		JPanel jpCenter = new JPanel();//creates jpanel to add all the buttons to
		jpCenter.setLayout(new GridLayout(8,8));
		for(int i=0; i<64; i++){	
			boardButtons.add(new JButton());
			boardButtons.get(i).setBorderPainted(false);
			boardButtons.get(i).setName("" + i + "");

			boardButtons.get(i).setIcon(new ImageIcon("blank.png"));//sets all pieces to blank originally

			//dependent on when the piece was created depends on what type of piece it will become
			if(i == 0 || i == 7)
				boardButtons.get(i).setActionCommand("ERook");
			else if(i == 1 || i == 6)
				boardButtons.get(i).setActionCommand("EKnight");
			else if(i == 2 || i == 5)
				boardButtons.get(i).setActionCommand("EBishop");
			else if(i == 3)
				boardButtons.get(i).setActionCommand("EQueen");
			else if(i == 4)
				boardButtons.get(i).setActionCommand("EKing");
			else if(i >=8 && i<=15)
				boardButtons.get(i).setActionCommand("EPawn");
			else if(i >=16 && i<=47)
				boardButtons.get(i).setActionCommand("Blank");
			else if(i >=48 && i <= 55)
				boardButtons.get(i).setActionCommand("FPawn");
			else if(i == 56 || i == 63)
				boardButtons.get(i).setActionCommand("FRook");
			else if(i == 57 || i == 62)
				boardButtons.get(i).setActionCommand("FKnight");
			else if(i == 58 || i == 61)
				boardButtons.get(i).setActionCommand("FBishop");
			else if(i == 60)
				boardButtons.get(i).setActionCommand("FQueen");
			else if(i == 59)
				boardButtons.get(i).setActionCommand("FKing");

			//then adds an action listener and adds it to the board
			boardButtons.get(i).addActionListener(this);
			jpCenter.add(boardButtons.get(i));
		}
		myFrame.add(jpCenter,BorderLayout.CENTER);//adds the frame to the board
	}
	/*
	 * colorBoard is used to color the board in the white black fashion
	 */
	private void colorBoard(){
		//used to keep track of position and color
		int colorSwitch=0;
		int rowCount=0;
		int turnThrough=0;

		for(int counter=0; counter<boardButtons.size();counter++){
			if(colorSwitch==0){//used to keep original white black pattern
				if(rowCount%2==0){
					boardButtons.get(counter).setBackground(Color.WHITE);
					colorSwitch++;
				}
				else{
					boardButtons.get(counter).setBackground(Color.BLACK);
					colorSwitch++;
				}
			}
			else{//used to create the black white pattern
				if(rowCount%2==0){
					boardButtons.get(counter).setBackground(Color.BLACK);
					colorSwitch=0;
				}
				else{
					boardButtons.get(counter).setBackground(Color.WHITE);
					colorSwitch=0;
				}
			}

			if(turnThrough==7)//goes to the next 'row' when its the seventh place
			{ rowCount++; }
			else if(turnThrough==8)//resets on the 8th time through
			{ turnThrough=0; }

			turnThrough++;
		}
	}
	/**
		Sets the team icons
		@param description the description of the piece
			this description comes from the ImageIcon's description
			uses this to get what color the piece is
	 */
	private void setOtherTeam(String description){
		int indexOfSpace = description.indexOf("."); // gets the first part of the description, black/white
		String otherTeamColor = description.substring(0,indexOfSpace);

		String thisTeamColorName = thisPlayersIcon.getDescription();
		indexOfSpace=thisTeamColorName.indexOf(".");
		String thisTeamColor = thisTeamColorName.substring(0, indexOfSpace);

		if(playerNumber==0){ //if you're the first player
			if(otherTeamColor.equals("Black")){ // sets your icons if the other team is black
				for(int counter =0; counter<64;counter++){
					if(counter == 0 || counter == 7)
						boardButtons.get(counter).setIcon(blackRookIcon);
					else if(counter == 1 || counter == 6)
						boardButtons.get(counter).setIcon(blackKnightIcon);
					else if(counter == 2 || counter == 5)
						boardButtons.get(counter).setIcon(blackBishopIcon);
					else if(counter == 3)
						boardButtons.get(counter).setIcon(blackQueenIcon);
					else if(counter == 4)
						boardButtons.get(counter).setIcon(blackKingIcon);
					else if(counter >=8 && counter<=15)
						boardButtons.get(counter).setIcon(blackPawnIcon);
					else if(counter >=16 && counter<=47)
						boardButtons.get(counter).setIcon(blankIcon);
					else if(counter >=48 && counter <= 55)
						boardButtons.get(counter).setIcon(whitePawnIcon);
					else if(counter == 56 || counter == 63)
						boardButtons.get(counter).setIcon(whiteRookIcon);
					else if(counter == 57 || counter == 62)
						boardButtons.get(counter).setIcon(whiteKnightIcon);
					else if(counter == 58 || counter == 61)
						boardButtons.get(counter).setIcon(whiteBishopIcon);
					else if(counter == 60)
						boardButtons.get(counter).setIcon(whiteQueenIcon);
					else if(counter == 59)
						boardButtons.get(counter).setIcon(whiteKingIcon);
				}
			}

			else{ // other wise it's white
				for(int counter =0; counter<64;counter++){
					if(counter == 0 || counter == 7)
						boardButtons.get(counter).setIcon(whiteRookIcon);
					else if(counter == 1 || counter == 6)
						boardButtons.get(counter).setIcon(whiteKnightIcon);
					else if(counter == 2 || counter == 5)
						boardButtons.get(counter).setIcon(whiteBishopIcon);
					else if(counter == 3)
						boardButtons.get(counter).setIcon(whiteQueenIcon);
					else if(counter == 4)
						boardButtons.get(counter).setIcon(whiteKingIcon);
					else if(counter >=8 && counter<=15)
						boardButtons.get(counter).setIcon(whitePawnIcon);
					else if(counter >=16 && counter<=47)
						boardButtons.get(counter).setIcon(blankIcon);
					else if(counter >=48 && counter <= 55)
						boardButtons.get(counter).setIcon(blackPawnIcon);
					else if(counter == 56 || counter == 63)
						boardButtons.get(counter).setIcon(blackRookIcon);
					else if(counter == 57 || counter == 62)
						boardButtons.get(counter).setIcon(blackKnightIcon);
					else if(counter == 58 || counter == 61)
						boardButtons.get(counter).setIcon(blackBishopIcon);
					else if(counter == 60)
						boardButtons.get(counter).setIcon(blackQueenIcon);
					else if(counter == 59)
						boardButtons.get(counter).setIcon(blackKingIcon);
				}
			}
		}
		else if(playerNumber ==1){ //if you're the second player
			if(otherTeamColor.equals("White")){ //if the other team is white must set your icons to black
				for(int counter =0; counter<64;counter++){
					if(counter == 0 || counter == 7)
						boardButtons.get(counter).setIcon(blackRookIcon);
					else if(counter == 1 || counter == 6)
						boardButtons.get(counter).setIcon(blackKnightIcon);
					else if(counter == 2 || counter == 5)
						boardButtons.get(counter).setIcon(blackBishopIcon);
					else if(counter == 3)
						boardButtons.get(counter).setIcon(blackQueenIcon);
					else if(counter == 4)
						boardButtons.get(counter).setIcon(blackKingIcon);
					else if(counter >=8 && counter<=15)
						boardButtons.get(counter).setIcon(blackPawnIcon);
					else if(counter >=16 && counter<=47)
						boardButtons.get(counter).setIcon(blankIcon);
					else if(counter >=48 && counter <= 55)
						boardButtons.get(counter).setIcon(whitePawnIcon);
					else if(counter == 56 || counter == 63)
						boardButtons.get(counter).setIcon(whiteRookIcon);
					else if(counter == 57 || counter == 62)
						boardButtons.get(counter).setIcon(whiteKnightIcon);
					else if(counter == 58 || counter == 61)
						boardButtons.get(counter).setIcon(whiteBishopIcon);
					else if(counter == 60)
						boardButtons.get(counter).setIcon(whiteQueenIcon);
					else if(counter == 59)
						boardButtons.get(counter).setIcon(whiteKingIcon);
				}
			}

			else{ // otherwise the other team is black and your icons are white
				for(int counter =0; counter<64;counter++){
					if(counter == 0 || counter == 7)
						boardButtons.get(counter).setIcon(whiteRookIcon);
					else if(counter == 1 || counter == 6)
						boardButtons.get(counter).setIcon(whiteKnightIcon);
					else if(counter == 2 || counter == 5)
						boardButtons.get(counter).setIcon(whiteBishopIcon);
					else if(counter == 3)
						boardButtons.get(counter).setIcon(whiteQueenIcon);
					else if(counter == 4)
						boardButtons.get(counter).setIcon(whiteKingIcon);
					else if(counter >=8 && counter<=15)
						boardButtons.get(counter).setIcon(whitePawnIcon);
					else if(counter >=16 && counter<=47)
						boardButtons.get(counter).setIcon(blankIcon);
					else if(counter >=48 && counter <= 55)
						boardButtons.get(counter).setIcon(blackPawnIcon);
					else if(counter == 56 || counter == 63)
						boardButtons.get(counter).setIcon(blackRookIcon);
					else if(counter == 57 || counter == 62)
						boardButtons.get(counter).setIcon(blackKnightIcon);
					else if(counter == 58 || counter == 61)
						boardButtons.get(counter).setIcon(blackBishopIcon);
					else if(counter == 60)
						boardButtons.get(counter).setIcon(blackQueenIcon);
					else if(counter == 59)
						boardButtons.get(counter).setIcon(blackKingIcon);
				}
			}
		}
	}
	/*
	 * setTeamIcons gets the image icon that this player wants to use
	 * @param playerSide (String) is used to find what item to display first
	 * @return teamIcon (ImageIcon) the icon that the user choose
	 */
	private ImageIcon setTeamIcons(String playerSide){
		ImageIcon teamIcon= new ImageIcon();//keeps the teamicon

		try{
			//gets the option to choose then displays the message and stores the icon to return
			ImageIcon[] gamePieces = {whiteKingIcon,blackKingIcon};
			String message = ("Please choose your piece");
			JOptionPane dialogMessage = new JOptionPane();

			teamIcon = (ImageIcon)JOptionPane.showInputDialog(null, message, "Choose your icon.", JOptionPane.OK_CANCEL_OPTION, null, gamePieces, gamePieces[0]);
		}
		catch(NullPointerException npe)
		{ setTeamIcons(playerSide); }
		catch(Exception e)
		{ setTeamIcons(playerSide); }
		return teamIcon;
	}

	/**
	 * actionPerformed used for movement
	 * @param e used to find which button was pressed
	 */

	public void actionPerformed(ActionEvent e){
		if(moveTurn){//if its this clients turn then it allows clicking to be recorded else no clicking for movement
			move(" ",e);
		}
		else{
			JOptionPane.showMessageDialog(null, "It is not your turn","Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}//end ActionPerformed
	/*
	 * firstPiece  is used to find what piece the user first clicked
	 * @return boolean if the piece is true
	 */
	private boolean firstPiece(){
		//gets the type of piece and checks for what type of piece is it is
		String command = firstButton.getActionCommand().substring(1);
		char check = firstButton.getActionCommand().substring(0,1).charAt(0);

		//checks to see if the button is the same as this one and creates it
		if(move==check){
			if(command.equals("Pawn"))
				pieces.add(new Pawn());
			else if(command.equals("Rook"))
				pieces.add(new Rook());
			else if(command.equals("Knight"))
				pieces.add(new Knight());
			else if(command.equals("Bishop"))
				pieces.add(new Bishop());
			else if(command.equals("King"))
				pieces.add(new King());
			else if(command.equals("Queen"))
				pieces.add(new Queen());
			if(pieces.size()>0)
			{ return true; }
		}
		//else kick out a false
		return false;
	}
	/**
	 * checkAttackSelf is used to see if the user is trying to attack one of its own pieces
	 * @return boolean of if the user is trying to attack itself
	 */
	private boolean checkAttackSelf(){//checks to see if the client is trying to attack itself
		//gets the first and second piece action commands and checks to see if they are the same compares and returns the appropriate
		String checkSecond= secondButton.getActionCommand();
		char checkSecondCommand = checkSecond.charAt(0);

		String checkFirst = firstButton.getActionCommand();
		char checkFirstCommand = checkFirst.charAt(0);

		if (checkSecondCommand==checkFirstCommand)
		{ return true; }
		else
		{ return false; }
	}
	/**
	 * changePieces changes the pieces to the right icons
	 */
	private void changePieces(){
		//switches the two pieces
		Icon firstButtonIcon = firstButton.getIcon();
		Icon secondButtonIcon = secondButton.getIcon();

		if(secondButton.getActionCommand().equals("Blank")){//if blank then straight switch
			firstButton.setIcon(secondButtonIcon);
			secondButton.setIcon(firstButtonIcon);
		}
		else{//else remove the first and replace it with a blank button
			secondButton.setIcon(firstButtonIcon);
			firstButton.setIcon(new ImageIcon("blank.png"));
		}
		//switches the action commands
		String firstCommand = firstButton.getActionCommand();
		String secondCommand = secondButton.getActionCommand();

		firstButton.setActionCommand(secondCommand);
		secondButton.setActionCommand(firstCommand);
	}
	/**
	 * setEnemyPieceAndMove sets the enemy pieces and alows movement
	 * @param _firstPosition the first place to move from
	 * @param _secondPosition the place to move to
	 * @param _move the string of movement
	 */

	private void setEnemyPiecesAndMove(int _firstPosition, int _secondPosition, String _move){
		char moveType = _move.charAt(0);//gets the type ove

		firstButton = boardButtons.get(_firstPosition);//finds the buttons
		secondButton = boardButtons.get(_secondPosition);

		String firstCommand = firstButton.getActionCommand();//gets their action command
		String secondCommand = secondButton.getActionCommand();

		Icon firstButtonIcon = firstButton.getIcon();//gets their icon
		Icon secondButtonIcon = secondButton.getIcon();

		if((moveType == 'F')||(moveType =='E')){//if its an attack move it switches the first button to blank then places the values of the second to the temps

			firstButton.setActionCommand("Blank");
			firstButton.setIcon(new ImageIcon("blank.png"));

			secondButton.setActionCommand(firstCommand);
			secondButton.setIcon(firstButtonIcon);
		}
		else{//other wise a straight switch
			firstButton.setActionCommand(secondCommand);
			secondButton.setActionCommand(firstCommand);

			firstButton.setIcon(secondButtonIcon);
			secondButton.setIcon(firstButtonIcon);
		}
		//replaces pieces
		boardButtons.set(_secondPosition,secondButton);
		boardButtons.set(_firstPosition,firstButton);
	}
	/**
	 * move movement for both the first and second player
	 * @param _move (String) the pieces and movement type
	 * @param e (ActionEvent) the actionevent that occured
	 */

	private void move(String _move,ActionEvent e){
		if(_move.equals(" ")){//if its the first players move
			if(turn == 0){//if its the first button click
				firstButton = (JButton)e.getSource();//save button

				if(firstButton.getActionCommand().charAt(0)=='B'){//check to make sure the button is not blank
					firstButton=null;	
				}
				else{//other wise its saved and the first piece method is called and the second button will be used
					if(firstPiece())
					{ turn++; }
					else{//other wise dont save
						turn=0; 
						JOptionPane.showMessageDialog(null, "Not your piece", "Information", JOptionPane.INFORMATION_MESSAGE); 
					}
				}
			}

			else if(turn == 1){//if it is the second button click
				secondButton = (JButton)e.getSource();//save button

				if(checkAttackSelf()){//check to see if its attacking it self...if so restart to first click
					firstButton=null;
					secondButton=null;
					turn=0;
					moveTurn=true;
					JOptionPane.showMessageDialog(null,"You can not attack your own piece please try another", "Information",JOptionPane.INFORMATION_MESSAGE);
				}
				else{//other wise switch pieces and change turn to the other player
					moveTurn=false;
					turn = 0;

					//saves the position of the buttons
					int firstPosition = Integer.parseInt(firstButton.getName());
					int secondPosition = Integer.parseInt(secondButton.getName());

					//checks to see if pieces has a value in it 
					if(pieces.size() > 0){
						pieces.get(0).setPieces(firstPosition,secondPosition);//sets the positions to check

						if(pieces.get(0).validateMove() == true){//checks to see if the movement is valid
							//switches pieces
							changePieces();

							//creates message to send
							String curMove = Integer.toString(firstPosition) + "," + Integer.toString(secondPosition);

							//gets the type of move and appends it and sends it out
							String moveType = firstButton.getActionCommand().substring(0,1);

							out.println(moveType + curMove);
							out.flush();

							pieces.remove(0);	
						}
						else{
							JOptionPane.showMessageDialog(null,"Invalid Move","ERROR",JOptionPane.ERROR_MESSAGE);
							moveTurn=true;
							turn=0;
							pieces.remove(0);	
						}
					}
				}//end		
			}
		}//end if
		else{//if its second players move
			moveTurn=true;
			turn = 0;

			int comma = _move.indexOf(",");
			int firstPosition=0;
			int secondPosition = 0;

			//gets the position
			firstPosition = Integer.parseInt(_move.substring(1,comma));
			secondPosition = Integer.parseInt(_move.substring(comma+1));

			//sets the enemy pieces and moves them as needed
			setEnemyPiecesAndMove(firstPosition, secondPosition, _move);
		}
		if(checkWin())//checks to see if the client won yet
		{ loser(); }

	}
	/**
	 * checkWin is used to see if this client won or lost
	 * @return boolean that if the user still has pieces on the board
	 */
	private boolean checkWin(){//checks to see if all the pieces are gone
		char player='O';
		boolean alive=false;

		if (playerNumber==0)//dependent on the player number it checks to see if there any of its own pieces left
		{ player='F'; }
		else if(playerNumber ==1)
		{ player= 'E'; }

		char pieceType;

		for(int counter=0; counter<boardButtons.size(); counter++){//gets the current piece type its checking then compares
			pieceType=boardButtons.get(counter).getActionCommand().charAt(0);//if one of the players pieces is left then it is 'alive'
			if(player==pieceType){ 
				alive =true;
				break;
			}
		}
		if(alive) //returns the opposite of alive 
		{ return false;}
		else
		{ return true; }
	}
	/**
	 * winner is used to perform operation before close to tell the user that they won
	 */
	private void winner(){//tells user that they won
		JOptionPane.showMessageDialog(null,"You WON!!!!!!\nGame closing in 4 seconds","Win!", JOptionPane.INFORMATION_MESSAGE);

		try{//closes all sockets/streams
			in.close();
			out.close();
			board.close();

			//sleeps for 4seconds then exits
			Thread.sleep(4000);
			System.exit(1);
		}
		catch(IOException ioe)
		{ JOptionPane.showMessageDialog(null,"Server went down","Error",JOptionPane.ERROR_MESSAGE); }
		catch(InterruptedException ie)
		{ JOptionPane.showMessageDialog(null,"Sorry something broke the game","Error",JOptionPane.ERROR_MESSAGE); }
		catch(Exception e)
		{ JOptionPane.showMessageDialog(null,"An Error occured, winner","Error",JOptionPane.ERROR_MESSAGE); }	
	}
	/**
	 * loser is used to tell the user that they are loser and perform operation before close
	 */
	private void loser(){//tells user that their opponent won
		JOptionPane.showMessageDialog(null,"Other Client Won\nGame closing in 4 seconds","Loser", JOptionPane.INFORMATION_MESSAGE);

		//sends out the message
		out.println("Win");
		out.flush();

		try{//closes all sockets
			in.close();
			out.close();
			board.close();

			Thread.sleep(4000);//sleeps for 4 seconds then closes
			System.exit(1);
		}
		catch(IOException ioe)
		{ JOptionPane.showMessageDialog(null,"Server went down","Error",JOptionPane.ERROR_MESSAGE); }
		catch(InterruptedException ie)
		{ JOptionPane.showMessageDialog(null,"Sorry something broke the game","Error",JOptionPane.ERROR_MESSAGE); }
		catch(Exception e)
		{ JOptionPane.showMessageDialog(null,"An Error occured, loser","Error",JOptionPane.ERROR_MESSAGE); }
	}

	/**
	 * ReadThread
	 * @author Nicholas Joerger
	 * @author Tim Chisholm
	 * @author Shane Rand
	 * @author Hassan Robinson
	 */

	class ReadThread extends Thread{
		private BufferedReader in;
		private String playerMove;

		/**
		 * ReadThread is used to start the reading process
		 * @param _in used to read from the server
		 */

		public ReadThread(BufferedReader _in){
			this.in = _in;
			start();
		}

		/**
		 * run is used to get player information from the server and then later opponent moves
		 */

		public void run(){
			try{//gets the player number (first or second player)
				playerNumber= Integer.parseInt(in.readLine());

				if(playerNumber==0){
					moveTurn=true;
					move='F';
				}
				else if(playerNumber ==1){
					moveTurn =false;
					move='E';
				}

				//gives the jframe the player number
				myFrame.setTitle("Player "+(playerNumber+1));

				//gets other clients description
				String description = in.readLine();
				setOtherTeam(description);

				while(true){//gets the move
					playerMove = in.readLine();//gets the opponents move
					ActionEvent e = null;

					if(playerMove.equals("Other Client Won")){//checks for lose or win
						loser();
					}
					else if(playerMove.equals("Win")){
						winner();
					}
					else{//other wise normal movement
						move(playerMove,e);
					}
				}
			}
			catch(SocketException se)//if there is a socket exception then this client is considered the winer
			{ winner(); }
			catch(IOException ioe)
			{ JOptionPane.showMessageDialog(null,"Server went down","Error",JOptionPane.ERROR_MESSAGE); }
			catch(Exception e){ 
				JOptionPane.showMessageDialog(null,"An Error occured and you have been declared the winner, Read in","Error",JOptionPane.ERROR_MESSAGE); 
				winner();
			}
		}	
	}//end ReadThread
}//end Board
