import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.math.*;

/**
  * Rook is used to validate for rook similar movement
  * @author Nicholas Joerger
  * @author Tim Chisholm
  * @author Shane Rand
  * @author Hassan Robinson
 */

public class Rook implements Piece{
	private int firstPosition,secondPosition;
	
	/**
	  * Rook is used to create a new blank Rook
	 */
	
	public Rook(){
		
	}
	
	/**
	  * setPieces is used to set the pieces that need to be validated as a valid movement
	  * @param _firstPosition the first button that was clicked position
	  * @param _secondPosition the second button that was clicked position
	 */
	
	public void setPieces(int _firstPosition,int _secondPosition){
		firstPosition = _firstPosition;
		secondPosition = _secondPosition;
	}
	
	/**
	  * validateMove checks to see if the two positions that were passed are 
	  * @return valid (boolean) returns if the move is valid or not
	 */
	
	public boolean validateMove(){
		boolean valid = false;
		
		if(Board.playerNumber==0){//validation for the first player movement
			int distance = secondPosition-firstPosition;
			//gets the distance of the move and gets the floor/ceiling(i.e. 45 floor 6 ceil of 5)
			int ceil= (int)Math.ceil((double)firstPosition/8);
			int floor= (int)Math.floor((double)firstPosition/8+1);
			
			//finds the left distance and then checks to see if the second position is on the same row as the first
			if(distance<0&&distance>-8){
				int dist=(int)Math.ceil(secondPosition/8);
				if(dist==ceil)
					{ valid= false; }
				else
					{ valid = true;	}
			}
			//finds the right distance and then checks to see if the second position on the same row as the first
			else if (distance>0&&distance<8){
				int dist=(int)Math.floor(secondPosition/8);
				if(dist==floor)
					{ valid= false; }
				else
					{ valid = true; }
			}
		
			//checks the vertical positions 
			int numuberOfUpCounts =ceil+1;//checks upwards
			int numberOfDownCounts =8-ceil;//checks all the pieces below
		
			for(int i = numuberOfUpCounts; i >=0; i--){//checks to see if the current looking position is equal to the second postion
				if(secondPosition == (firstPosition-(8*i))){
					valid = true;
					break;
				}
			}
			//checks to see if the current looking position is equal to the second postion
			for(int i = 0; i <= numberOfDownCounts; i++){
				if(secondPosition == firstPosition+(8*i)){
					valid = true;
					break;
				}
			}
		}
		else if (Board.playerNumber==1){//checks for the second player
			int distance = secondPosition-firstPosition;
			//finds the distance between the two pieces then finds the lower row and the current row(i.e. 45 floor-6, ceil-5)
			int ceil= (int)Math.ceil((double)firstPosition/8);
			int floor= (int)Math.floor((double)firstPosition/8+1);
		
			if(distance<0&&distance>-8){//checks movement to the left and also checks to see if the current piece is also in the same row
				int dist=(int)Math.ceil(secondPosition/8);
				if(dist==ceil)
					{ valid= false; }
				else
					{ valid = true; }
			}
			else if (distance>0&&distance<8){//checks movement to the right and also checks to see if the current piece is also in the same row
				int dist=(int)Math.floor(secondPosition/8);
				if(dist==floor)
					{ valid= false; }
				else
					{ valid = true; }
			}
		
			//finds the number of rows to look upward
			int numberOfUpCounts =ceil;
			//finds the number of rows to look downward
			int numberOfDownCounts =8-ceil;
			
			//checks to see if the current position being look into is the same as the second position
			for(int i = 0; i <=numberOfUpCounts; i++){
				if(secondPosition == (firstPosition-(8*i))){
					valid = true;
					break;
				}
			}
			//checks to see if the current position being look into is the same as the second position
			for(int i =0; i<=numberOfDownCounts;i++){
				if(secondPosition == firstPosition+(8*i)){
					valid = true;
					break;
				}
			}
		}
		return valid;
	}
}
