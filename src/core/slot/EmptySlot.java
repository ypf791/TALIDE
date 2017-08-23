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
public final class EmptySlot extends Slot {
	// constructors
	public EmptySlot() {}
	
	public EmptySlot(String str) { _code = "emp"; }
	// constructors end


	// methods
	public NextToExec exec(TapeConst tc) { return _defaultNTE; }
	
	public java.awt.Image getImage(boolean isExec) {
		return _img_slot[isExec ? 1 : 0];
	}
	// methods end
}