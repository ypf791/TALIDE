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
public final class FailToCreateSlotException extends Exception {
	// fields
	private int _idx;
	private String _arg;
	// fields end
	
	
	// constructors
	public FailToCreateSlotException(int idx, String arg) {
		_idx = idx;
		_arg = arg;
	}
	// constructors end


	// methods
	public void printMyMessage() {
		System.err.println("[ ERR ] " + getStackTrace()[0].toString());
		System.err.println("[ MSG ] Unrecognized argument $" + _idx + ": " + _arg);
	}
	// methods end
}