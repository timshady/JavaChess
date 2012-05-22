import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.math.*;

/**
  * Queen is used to validate pieces that have queen like movement patterns
  * @author Nicholas Joerger
  * @author Tim Chisholm
  * @author Shane Rand
  * @author Hassan Robinson
 */

public class Queen implements Piece{
	private int firstPosition,secondPosition;
	//holds the locations that could change how the queen can validate
	private Hashtable<String,Integer> col1= new Hashtable<String,Integer>();
	private Hashtable<String,Integer> col2= new Hashtable<String,Integer>();
	
	/**
	  * Queen is used to create a new blank queen
	 */
	
	public Queen(){
		
	}
	
	/**
	  * setPieces sets the positions that need to be checked for correct movement
	  * @param _firstPosition the first position that was cliecked
	  * @param _secondPosition the second position that was clicked
	 */
	
	public void setPieces(int _firstPosition,int _secondPosition){
		firstPosition = _firstPosition;
		secondPosition = _secondPosition;
	}
	
	/**
	  * validateMove the move ability is tried to be moved
	  * @return valid (boolean) this returns if movement location is able to move
	 */
	
	public boolean validateMove(){
		boolean valid = false;
		
		//adds trouble location to hashtables
		addHashTable();
		
		//checks for specific movement for the first player
		if(Board.playerNumber==0){
			int distance = secondPosition-firstPosition;
			
			//checks the last position in the row and the highest for the row (i.e. 45/8 floor=6 ceil=5)
			int ceil= (int)Math.ceil((double)firstPosition/8);
			int floor= (int)Math.floor((double)firstPosition/8+1);
			
			//checks the horiz. movement to the left if the space between is less than 8 and in the same row
			if(distance<0&&distance>-8){
				int dist=(int)Math.ceil(secondPosition/8);
				if(dist==ceil)
					{ valid= false; }
				else
					{ valid = true; }
			}
			//checks the horiz. movement to the right if the space between is less than 8 and in the same row
			else if (distance>0&&distance<8){
				int dist=(int)Math.floor(secondPosition/8);
				if(dist==floor)
					{ valid= false; }
				else
					{ valid = true; }
			}
			
			//gives number of rows up to check
			int numberOfUpCounts =ceil;
			//gives the number of rows down to check
			int numberOfDownCounts =8-ceil;
			
			//checks vertical upwards possible movement
			for(int i = 0; i <=numberOfUpCounts; i++){
				if(secondPosition == (firstPosition-(8*i))){
					valid = true;
				}
			}
			//checks vertical downwards possible movement
			for(int i = 0; i <= numberOfDownCounts; i++){					
				if(secondPosition ==(firstPosition+((8*i))))
					{ valid=true; }
			}
			//checks the diagonal movement to the upper right
			for(int counter=0;counter<col2.size();counter++){
				if(!(col2.containsValue(firstPosition))){//sees if the piece is not in the second col of values that are odd
					if((firstPosition-7*counter)>0){//then if the current place being checked is greater than 0
						if(secondPosition ==(firstPosition-(7*counter))){ //check if the curent position is the second posiion, if so exit with a valid
							valid=true; 
							break;
						}
					}
					else//otherwise break
						{ break; }
				}
				else//otherwise break
					{ break; }
			}
			//checks diagonal to the upper left
			//checks to see if current item being checked is within the confines of the first hashtable of possible values that have different validation
			for(int counter=0;counter<col1.size();counter++){
				if(!(col1.containsValue(firstPosition))){//if the value is not present in the first col1
					if((firstPosition-(9*counter))>0){//checks to see if the value being checked is larger than 0
						if(secondPosition ==(firstPosition-(9*counter))){ //then sees if the current location is the secondposition
							valid=true;
							break;
						}
					}
					else//otherwise break
						{ break; }
				}
				else//otherwise break
					{ break; }
			}
			//checks diagonal to the left
			//checks to see if the current item being checked is within the confines of the first col1 hastable of possbile values that have different validation
			for(int counter=0;counter<col1.size();counter++){
				if(!(col1.containsValue(firstPosition))){//if the value is not within the first col1
					if((firstPosition+(7*counter))<64){//see if the current place being checked is less than 0
						if((secondPosition == firstPosition+(7*counter))){//if the current location being checked is equal to the current place then a true is returned
							valid = true; 
							break;
						}
					}
					else//otherwise break
						{ break; }
				}
				else//otherwise break
					{ break; }
			}
			//checks diagonal to the lower right
			//checks to see if the current item being checked is within the confines of the first col1 hashtable of possible values that have different validation
			for(int counter=0;counter<col2.size();counter++){
				if(!(col1.containsValue(firstPosition))){
					if(firstPosition+(7*counter)<64){//checks to see if the current position being checked is less than 64
						if(secondPosition ==(firstPosition+((9*counter)))){ //then checks to see if that position is the current position
							valid=true;
							break;
						}
					}
					else//otherwise break
						{ break; }
				}
				else//otherwise break
					{ break; }
			}
		}
		//checks the requirements if its the second player
		else if (Board.playerNumber==1){
			int distance = secondPosition-firstPosition;
			//gets the distance then gets the row the same as above
			int ceil= (int)Math.ceil((double)firstPosition/8);
			int floor= (int)Math.floor((double)firstPosition/8+1);

			//checks the left distance and row to keep the piece within the current row
			if(distance<0&&distance>-8){
				int dist=(int)Math.ceil(secondPosition/8);
				if(dist==ceil)//checks to make sure the movement spot is within the current row
					{ valid= false; }
				else
					{ valid = true; }
			}
			else if (distance>0&&distance<8){//checks the right distance and row to keep the piece within the current row
				int dist=(int)Math.floor(secondPosition/8);
				if(dist==floor)//checks to make sure the movement spot is within the current row
					{ valid= false; }
				else
					{ valid = true; }
			}
			
			//gets the vertical moves to check
			int numberOfUpCounts =ceil;
			int numberOfDownCounts =8-ceil;
			
			//checks to see if the current verical move is the second position
			for(int i = 0; i <=numberOfUpCounts; i++){
				if(secondPosition == (firstPosition-(8*i))){
					valid = true;
				}
			}
			//checks to see if the current verical move is the second position
			for(int i = 0; i <= numberOfDownCounts; i++){					
				if(secondPosition ==(firstPosition+((8*i))))
					{ valid=true; }
			}
			//checks diagonal to the right
			//checks to see if the position starting is in the first col otherwise it checks
			for(int counter=0;counter<col2.size();counter++){
				if(!(col2.containsValue(firstPosition))){
					if((firstPosition-7*counter)>0){//checks to see if the current value is positive
						if(secondPosition ==(firstPosition-(7*counter))){ //checks to see if the current value is the second position
							valid=true; 
							break;
						}
					}
					else
						{ break; }//otherwise break
				}
				else
					{ break; }//otherwise break
			}
			//checks diagonal to the left
			//checks to see if the position is not within the first col
			for(int counter=0;counter<col1.size();counter++){
				if(!(col1.containsValue(firstPosition))){
					if((firstPosition-(9*counter))>0){//checks to see if the value being checked is above 0
						if(secondPosition ==(firstPosition-(9*counter))){ //checks to see if the second position is within the range
							valid=true;
							break;
						}
					}
					else//otherwise break
						{ break; }
				}
				else//otherwise break
					{ break; }
			}
			//checks for lower diagnoal left movement
			for(int counter=0;counter<col1.size();counter++){
				if(!(col1.containsValue(firstPosition))){//checks to see if the value is in the first col1
					if((firstPosition+(7*counter))<64){//checks to make sure the value is less than 64
						if((secondPosition == firstPosition+(7*counter))){//checks for lower right diagonal
							valid = true; 
							break;
						}
					}
					else//otherwise break
						{ break; }
				}
				else//otherwise break
					{ break; }
			}
			//check for lower right diagnoal movement
			for(int counter=0;counter<col2.size();counter++){
				if(!(col1.containsValue(firstPosition))){//checks to see if the first position is not in the left col
					if(firstPosition+(7*counter)<64){//checks to see if the piece is less than 64
						if(secondPosition ==(firstPosition+((9*counter)))){ //checks to see if the secondposition is within the diagonal
							valid=true;
							break;
						}
					}
					else//otherwise break
						{ break; }
				}
				else//otherwise break
					{ break; }
			}
		}
		return valid;
	}
	
	/**
		Adds information to a hashtable for movement validation
	*/
	private void addHashTable(){
		//possible values that could hinder validation
		col1.put("first col first row",0);
		col1.put("first col second row", 8);
		col1.put("first col third row", 16);
		col1.put("first col fourth row", 24);
		col1.put("first col fifth row", 32);
		col1.put("first col sixth row", 40);
		col1.put("first col seventh row", 48);
		col1.put("first col eighth row", 54);
		
		//possible values that could hinder validation
		col2.put("second col first row",7);
		col2.put("second col second row",15);
		col2.put("second col third row", 23);
		col2.put("second col fourth row", 31);
		col2.put("second col fifth row", 39);
		col2.put("second col sixth row", 47);
		col2.put("second col seventh row", 55);
		col2.put("second col eighth row", 63);
	}
}
