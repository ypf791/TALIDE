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
	protected int _imgIdx;
	// fields end
	
	
	// constructors
	public ShiftSlot(String str) throws CreateSlotException {
		_code = "sh:" + str;
		ExecResult er = ExecResult.NIL;
		switch (str) {
			case "-":
				_direction = Direction.DIR_NEG;
				er = ExecResult.SH_L;
				_imgIdx = 26;
				break;
			case "+":
				_direction = Direction.DIR_POS;
				er = ExecResult.SH_R;
				_imgIdx = 28;
				break;
			default:
				throw new CreateSlotException(1, str);
		}
		_NTEList.add(new NextToExec(0, 1, er));
	}
	// constructors end


	// methods
	public java.awt.Image getImage(boolean isExec) {
		return _img_slot[_imgIdx + (isExec ? 1 : 0)];
	}
	// methods end
}