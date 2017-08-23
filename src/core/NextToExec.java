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


/*****************
   public class   
*****************/
class NextToExec {
	// fields
	public final int _diff_row;
	public final int _diff_col;
	public final ExecResult _result;
	// fields end


	// constructors
	public NextToExec(int r, int c, ExecResult er) {
		_diff_row = r;
		_diff_col = c;
		_result = er;
	}
	// constructors end
}