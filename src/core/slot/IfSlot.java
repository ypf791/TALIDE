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
	protected int _imgIdx;
	// fields end
	
	
	// constructors
	public IfSlot(String str) throws CreateSlotException {
		_code = "if:" + str;
		String[] args = str.split(":", 2);
		switch (args[0]) {
			case "0": _condition = TapeConst.TC_0; _imgIdx = 5; break;
			case "1": _condition = TapeConst.TC_1; _imgIdx = 7; break;
			case "x": _condition = TapeConst.TC_X; _imgIdx = 9; break;
			default: throw new CreateSlotException(1, args[0]);
		}
		switch (args[1]) {
			case "-": _direction = Direction.DIR_NEG; _imgIdx -= 3; break;
			case "+": _direction = Direction.DIR_POS; _imgIdx += 3; break;
			default: throw new CreateSlotException(2, args[1]);
		}
		_NTEList.add(new NextToExec(_direction.toInt(), 0, ExecResult.NIL));
	}
	// constructors end


	// methods
	public NextToExec exec(TapeConst tc) {
		if (tc!=_condition) return _defaultNTE;
		return _NTEList.firstElement();
	}
	
	public java.awt.Image getImage(boolean isExec) {
		return _img_slot[_imgIdx + (isExec ? 1 : 0)];
	}
	// methods end
}