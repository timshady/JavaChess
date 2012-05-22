import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
  * Piece is used to give the general rules that are needed to be considered a piece in chess
  * @author Nicholas Joerger
  * @author Tim Chisholm
  * @author Shane Rand
  * @author Hassan Robinson
 */

public interface Piece{
	
	/**
		sets the pieces to their respective places
	*/
	abstract void setPieces(int _firstPosition,int _secondPosition);
	/**
		Validates the given move
	*/
	abstract boolean validateMove();
	
}
