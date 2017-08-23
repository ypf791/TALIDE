/*******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2015/6/21
  VERSION : v1.0
*******************************************************************************/


/************
   package   
************/
package talide.core;


/***********
   import   
***********/
import java.util.TreeMap;


/*****************
   public class   
*****************/
class NextToExec {
	// fields
	public int _diff_row;
	public int _diff_col;
	public ExecResult _result;
	// fields end


	// constructors
	public NextToExec(int r, int c, ExecResult er) {
		_diff_row = r;
		_diff_col = c;
		_result = er;
	}
	// constructors end
}