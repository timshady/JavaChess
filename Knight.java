import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.math.*;

/**
  * Knight is used to validate knight like movement
  * @author Nicholas Joerger
  * @author Tim Chisholm
  * @author Shane Rand
  * @author Hassan Robinson
 */

public class Knight implements Piece{
	//holds first positions
	private int firstPosition,secondPosition;
	
	//holds possible conflict areas
	private Hashtable<String,Integer> col1= new Hashtable<String,Integer>();
	private Hashtable<String,Integer> col2= new Hashtable<String,Integer>();
	private Hashtable<String,Integer> col3= new Hashtable<String,Integer>();
	private Hashtable<String,Integer> col4= new Hashtable<String,Integer>();
	
	/**
	  * Knight creates a blank knight
	 */
	
	public Knight(){
		
	}
	
	/**
	  * setPieces that are needed to be validated 
	  * @param _firstPosition (int) holds the first position that was picked
	  * @param _secondPosition (int) holds the second position that was picked
	 */
	
	public void setPieces(int _firstPosition,int _secondPosition){
		firstPosition = _firstPosition;
		secondPosition = _secondPosition;
	}
	
	/**
	  * validateMove is used to validate the movement
	  * @return (boolean) if the movement is possible
	 */
	
	public boolean validateMove(){
		boolean valid = false;
		
		//gets the row of the first rows min and max number (i.e. 45/8 ceil is 5, floor is 6)
		//actual distance
		int ceil = (int)Math.ceil(secondPosition/8);
		int floor = (int)Math.floor(secondPosition/8);
		int distance = firstPosition-secondPosition;
		
		//adds trouble values
		addHashTable();
		
		//validates the first trouble col.
		if(col1.containsValue(firstPosition)){
			//check right side
			if(distance ==-6)//checks upper second L
				{ valid=true; }
			else if(distance ==-15)//checks upper first L
				{ valid=true; }
			else if(distance==10)//checks lower first L
				{ valid=true; }
			else if(distance==17)//checks lower second L
				{ valid=true; }
		}
		else if (col2.containsValue(firstPosition)){
			//check one left from first position
			if(distance==17)
				{ valid=true;}
			else if(distance==-10)//lower third right
				{ valid=true;}
			//check right side
			else if(distance ==6)//upper right L
				{ valid=true; }
			else if(distance ==-15)//lower right L 
				{ valid=true; }
			else if(distance==15)//upper right L
				{ valid=true; }
			else if(distance==-7)//lower left space
				{ valid=true; }
			else if(distance==-17)//lower left third L
				{ valid=true; }
		}
		else if(col3.containsValue(firstPosition)){
			//check first right side
			if(distance==15)//upper right
				{ valid=true;}
			else if(distance==17)//upper left
				{ valid=true; }
			else if(distance==-17)//lower left
				{ valid=true; } 
			//check left side
			else if(distance==10)//far left
				{ valid=true; }
			else if(distance==-6)//far left
				{ valid=true; }
			else if(distance==-15)//far left
				{ valid=true; }
		}
		else if(col4.containsValue(firstPosition)){
			//check left side
			if(distance==10)//far upper left
				{ valid=true; }
			else if(distance==-6)//far lower left
				{ valid=true; }
			else if(distance==17)//upper left
				{ valid=true; }
			else if(distance==-15)//lower left
				{ valid=true; }
		}
		else{
			//normal movement
			if(distance==6)//far upper right
				{ valid=true; }
			else if(distance==10)//far upper left
				{ valid=true; }
			else if(distance==15)//upper right
				{ valid=true; }
			else if(distance==17)//upper left
				{ valid=true; }
			else if(distance==-6)//far lower left
				{ valid=true; }
			else if(distance==-10)//far lower right
				{ valid=true; }
			else if(distance==-15)//lower left
				{ valid=true; }
			else if(distance==-17)//lower right
				{ valid=true; }
		}
		return valid;
	}
	/**
		Adds data to hashtable for validation
	*/
	private void addHashTable(){
		//positions that have different move validation
		col1.put("First Col, First Row", new Integer(0));
		col1.put("First Col, Second Row", new Integer(8));
		col1.put("First Col, Third Row", new Integer(16));
		col1.put("First Col, Fourth Row", new Integer(24));
		col1.put("First Col, Fifth Row", new Integer(32));
		col1.put("First Col, sixth Row", new Integer(40));
		col1.put("First Col, Seventh Row", new Integer(48));
		col1.put("First Col, Eighth Row", new Integer(56));
		
		//positions that have different move validation
		col2.put("Second Col, First Row", new Integer(1));
		col2.put("Second Col, Second Row", new Integer(9));
		col2.put("Second Col, Third Row", new Integer(17));
		col2.put("Second Col, Fourth Row", new Integer(25));
		col2.put("Second Col, Fifth Row", new Integer(33));
		col2.put("Second Col, Sixth Row", new Integer(41));
		col2.put("Second Col, Seventh Row", new Integer(49));
		col2.put("Second Col, Eighth Row", new Integer(57));
		
		//positions that have different move validation
		col3.put("Third Col, First Row", new Integer(6));
		col3.put("Third Col, Second Row", new Integer(14));
		col3.put("Third Col, Third Row", new Integer(22));
		col3.put("Third Col, Fourth Row", new Integer(30));
		col3.put("Third Col, Fifth Row", new Integer(38));
		col3.put("Third Col, Sixth Row", new Integer(46));
		col3.put("Third Col, Seventh Row", new Integer(54));
		col3.put("Third Col, Eighth Row", new Integer(62));
		
		//positions that have different move validation
		col4.put("Forth Col, First Row", new Integer(7));
		col4.put("Forth Col, Second Row", new Integer(15));
		col4.put("Forth Col, Third Row", new Integer(23));
		col4.put("Forth Col, Fourth Row", new Integer(31));
		col4.put("Forth Col, Fifth Row", new Integer(39));
		col4.put("Forth Col, Sixth Row", new Integer(47));
		col4.put("Forth Col, Seventh Row", new Integer(55));
		col4.put("Forth Col, Eighth Row", new Integer(63));
	}
}
