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
public class IfSlot extends Slot {
	// fields
	protected TapeConst _condition;
	protected Direction _direction;
	// fields end
	
	
	// constructors
	public IfSlot(String str) throws FailToCreateSlotException {
		String[] args = str.split(":", 2);
		switch (args[0]) {
			case "0": _condition = TapeConst.TC_0; break;
			case "1": _condition = TapeConst.TC_1; break;
			case "x": _condition = TapeConst.TC_X; break;
			default: throw new FailToCreateSlotException(1, args[0]);
		}
		switch (args[1]) {
			case "-": _direction = Direction.DIR_NEG; break;
			case "+": _direction = Direction.DIR_POS; break;
			default: throw new FailToCreateSlotException(2, args[1]);
		}
	}
	// constructors end


	// methods
	public String toCode() {
		StringBuffer rtn = new StringBuffer("if:");
		switch (_condition) {
			case TC_0: rtn.append('0'); break;
			case TC_1: rtn.append('1'); break;
			case TC_X: rtn.append('x'); break;
		}
		switch (_direction) {
			case DIR_POS: rtn.append('+'); break;
			case DIR_NEG: rtn.append('-'); break;
		}
		return new String(rtn);
	}
	
	public NextToExec exec(TapeConst tc) {
		if (tc!=_condition) return _defaultNTE;
		return new NextToExec(_direction.toInt(), 0, ExecResult.NIL);
	}
	// methods end
}