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
public class JumpSlot extends Slot {
	// fields
	protected int _distance;
	// fields end
	
	
	// constructors
	public JumpSlot(String str) throws CreateSlotException {
		try {
			_distance = Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			throw new CreateSlotException(1, str);
		}
	}
	// constructors end


	// methods
	public String toCode() {
		return "j:" + _distance;
	}
	
	public NextToExec exec(TapeConst tc) {
		return new NextToExec(0, _distance, ExecResult.NIL);
	}
	// methods end
}