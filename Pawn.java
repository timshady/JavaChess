import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
  * Pawn is used to validate movement for pawn like objects
  * @author Nicholas Joerger
  * @author Tim Chisholm
  * @author Shane Rand
  * @author Hassan Robinson
 */

public class Pawn implements Piece{
	private int firstPosition,secondPosition;
	private boolean firstMove=true;
	
	/**
	  * Pawn is used to create a blank pawn
	 */
	
	public Pawn(){
		
	}
	
	/**
	  * setPieces is used to set the two locations to check between
	  * @param _firstPosition is the first button that was clicked
	  * @param _secondPosition is the second button position that was clicked
	 */
	
	public void setPieces(int _firstPosition,int _secondPosition){
		firstPosition = _firstPosition;
		secondPosition = _secondPosition;
	}
	
	/**
	  * validateMove is used to validate the movement of the pawn
	  * @return valid(boolean) is true if the second position is within the limits of the first position
	 */
	
	public boolean validateMove(){
		JButton firstButton = Board.boardButtons.get(firstPosition);
		JButton secondButton=Board.boardButtons.get(secondPosition);
		boolean valid = false;
		
		//if the first player is trying to move their pawn
		if(Board.playerNumber==0){
			//if the first position of the button is within the seventh line then it passes as a first move jump
			//no other testing is needed since pawns can not travel backwards
			if((47<firstPosition)&&(firstPosition<57))
				{firstMove=true;}
			
			//if the second button clicked was blank 
			//or it checks to see if the disance is within 8 places or if its the first move within 16
			if(secondButton.getActionCommand().equals("Blank")){
				int distance = firstPosition - 8;
				if(secondPosition == distance)
					valid = true;
				else if ((secondPosition == (distance - 8))&&firstMove)
					valid = true;
			}
			//other wise it checks to see if its an attack then it checks to see if the position is on the 'wings' of the pawn
			else if(secondButton.getActionCommand().charAt(0)=='E'){
				int distance = firstPosition - 8;
				if(secondPosition == distance-1 || secondPosition == distance+1)
					{ valid = true; }
			}
		}
		//if it is the second player that is trying to move then
		else if(Board.playerNumber==1){
			//if the first position of the button is within the second line then it passes as a first move jump
			//no other testing is needed since pawns can not travel backwards
			if((7<firstPosition)&&(firstPosition<16))
				{firstMove=true;}
			
			//if the second button clicked was blank 
			//or it checks to see if the disance is within 8 places or if its the first move within 16
			if(secondButton.getActionCommand().equals("Blank")){
				int distance = firstPosition + 8;
				if(secondPosition == distance)
					valid = true;
				else if((secondPosition == distance + 8)&&firstMove)
					valid = true;
			}
			//other wise it checks to see if its an attack then it checks to see if the position is on the 'wings' of the pawn
			else if(secondButton.getActionCommand().charAt(0)=='F'){
				int distance = firstPosition +8;
				if(secondPosition == distance-1 || secondPosition == distance+1){
					valid = true;
				}
			}
		}
		return valid;
	}
}
