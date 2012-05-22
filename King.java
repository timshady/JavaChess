import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.math.*;

/**
  * King is used to validate the movement of a king piece
  * @author Nicholas Joerger
  * @author Tim Chisholm
  * @author Shane Rand
  * @author Hassan Robinson
 */

public class King implements Piece{
	//holds first position
	private int firstPosition,secondPosition;
	
	//holds the positions that would give different movement validation
	private Hashtable<String, Integer> col1 = new Hashtable<String,Integer>();
	private Hashtable<String, Integer> col2 = new Hashtable<String, Integer>();
	
	/**
	  * King() is used to create an empty King
	 */
	 
	public King(){
		
	}
	
	/**
	  * setPieces is used to set the movement of pieces using the first position as the first button clicked and the second as the second place clicked
	  * @param _firstPosition (int) holds the first button clicked
	  * @param _secondPosition (int) holds the second button clicked
	 */
	
	public void setPieces(int _firstPosition,int _secondPosition){
		firstPosition = _firstPosition;
		secondPosition = _secondPosition;
	}
	
	/**
	  * validateMove is used to validate king pieces
	  * @return (boolean) is used to see if the move is valid
	 */
	public boolean validateMove(){
		boolean valid=false;
		addHashTable();
		
		//holds the difference of the location
		int distance =firstPosition-secondPosition;
		
		//checks for the first possible problem column
		if(col1.containsValue(firstPosition)){
			if(distance ==8){ valid=true; }//checks up one
			
			else if(distance ==-8)//checks down one
				{ valid=true; }
			else if(distance == 7)//checks the first diagonal
				{ valid=true; }
			else if(distance == -9)//checks the second diagonal
				{ valid=true;}
			else if(distance == -1)//checks the one to the left
				{ valid=true; }
		}
		//checks for the second possible problem column
		else if(col2.containsValue(firstPosition)){
			if(distance ==8)//checks one up
				{ valid=true; }
			else if(distance ==-8)//checks one down
				{ valid=true; }
			else if(distance ==-7)//checks one down diagonal
				{ valid=true; }
			else if(distance == 9)//checks one up diagonal
				{ valid=true;}
			else if(distance == 1)//checks one sideways
				{ valid=true; }
		}
		else{
			if(distance ==8)//checks one up
				{ valid=true; }
			else if(distance ==-8)//checks one down
				{ valid=true; }
			else if(distance == 7)//checks one up diagonal
				{ valid=true; }
			else if(distance == -9)//checks one down diagonal
				{ valid=true;}
			else if(distance == -1)//checks one back
				{ valid=true; }
			else if(distance == -7)//checks one down diagonal
				{ valid=true; }
			else if(distance == 9)//checks one up diagonal
				{ valid=true;}
			else if(distance == 1)//checks one right diagonal
				{ valid=true; }
		}
		return valid;
	}
	
	/**
		Adds data to hashtable for validation
	*/
	private void addHashTable(){
		//positions that could mess up
		col1.put("First Col, First Row", new Integer(0));
		col1.put("First Col, Second Row", new Integer(8));
		col1.put("First Col, Third Row", new Integer(16));
		col1.put("First Col, Fourth Row", new Integer(24));
		col1.put("First Col, Fifth Row", new Integer(32));
		col1.put("First Col, sixth Row", new Integer(40));
		col1.put("First Col, Seventh Row", new Integer(48));
		col1.put("First Col, Eighth Row", new Integer(56));
		
		//positions that could mess up
		col2.put("Forth Col, Second Row", new Integer(15));
		col2.put("Forth Col, Third Row", new Integer(23));
		col2.put("Forth Col, Fourth Row", new Integer(31));
		col2.put("Forth Col, Fifth Row", new Integer(39));
		col2.put("Forth Col, Sixth Row", new Integer(47));
		col2.put("Forth Col, Seventh Row", new Integer(55));
		col2.put("Forth Col, Eighth Row", new Integer(63));
	}
}
