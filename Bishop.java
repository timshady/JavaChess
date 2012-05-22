import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.math.*;

/**
  * The Bishop class is used to check the validation of movement for Bishop like pieces
  * @author Nicholas Joerger
  * @author Tim Chisholm
  * @author Shane Rand
  * @author Hassan Robinson
 */

public class Bishop implements Piece{
	private int firstPosition,secondPosition;
	 
	 /**
	   * Bishop creates an empty Bishop
	  */
	public Bishop(){
		
	}
	
	/**
	  * setPieces is used to set the first and second position
	  * @param _firstPosition (int) holds the first button pressed location
	  * @param _secondPosition (int) holds the second button pressed location
	 */
	
	public void setPieces(int _firstPosition,int _secondPosition){
		firstPosition = _firstPosition;
		secondPosition = _secondPosition;
	}
	
	/**
	  * validateMove validates the movement of a Bishop piece
	  * @return (boolean)is used to return if the move is valid
	 */
	
	public boolean validateMove(){
		JButton firstButton = Board.boardButtons.get(firstPosition);	
		JButton secondButton = Board.boardButtons.get(secondPosition);
		boolean valid = false;
		
		//gets the distance between the two pieces
		int distance = firstPosition - secondPosition;
		
		//the distance for the diagonals is constant
		//so the diagonals can be found in this way
		if(distance % 9 == 0|| distance % 7 == 0)
			{ valid = true; }
		
		return valid;
	}
}
