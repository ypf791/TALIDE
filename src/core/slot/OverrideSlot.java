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
public class OverrideSlot extends Slot {
	// fields
	protected TapeConst _toOverride;
	protected int _imgIdx;
	// fields end
	
	
	// constructors
	public OverrideSlot(String str) throws CreateSlotException {
		_code = "ovrd:" + str;
		ExecResult er = ExecResult.NIL;
		switch (str) {
			case "0":
				_toOverride = TapeConst.TC_0;
				er = ExecResult.OVRD_0;
				_imgIdx = 20;
				break;
			case "1":
				_toOverride = TapeConst.TC_1;
				er = ExecResult.OVRD_1;
				_imgIdx = 22;
				break;
			case "x":
				_toOverride = TapeConst.TC_X;
				er = ExecResult.OVRD_X;
				_imgIdx = 24;
				break;
			default: throw new CreateSlotException(1, str);
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