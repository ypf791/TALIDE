/*******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2015/7/4
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
public final class CreateSlotException extends Exception {
	// fields
	public int _idx;
	public String _arg;
	// fields end
	
	
	// constructors
	public CreateSlotException(int idx, String arg) {
		_idx = idx;
		_arg = arg;
	}
	// constructors end
}