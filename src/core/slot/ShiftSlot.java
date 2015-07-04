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
public class ShiftSlot extends Slot {
	// fields
	protected Direction _direction;
	// fields end
	
	
	// constructors
	public ShiftSlot(String str) throws FailToCreateSlotException {
		switch (str) {
			case "-": _direction = Direction.DIR_NEG; break;
			case "+": _direction = Direction.DIR_POS; break;
			default: throw new FailToCreateSlotException(1, str);
		}
	}
	// constructors end


	// methods
	public String toCode() {
		StringBuffer rtn = new StringBuffer("sh:");
		switch (_direction) {
			case DIR_POS: rtn.append('+'); break;
			case DIR_NEG: rtn.append('-'); break;
		}
		return new String(rtn);
	}
	
	public NextToExec exec(TapeConst tc) {
		ExecResult er = ExecResult.NIL;
		switch (_direction) {
			case DIR_POS: er = ExecResult.SH_R; break;
			case DIR_NEG: er = ExecResult.SH_L; break;
		}
		return new NextToExec(0, 1, er);
	}
	// methods end
}