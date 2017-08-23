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
	
	public EmptySlot(String str) {}
	// constructors end


	// methods
	public String toCode() { return "emp"; }
	
	public NextToExec exec(TapeConst tc) { return _defaultNTE; }
	// methods end
}