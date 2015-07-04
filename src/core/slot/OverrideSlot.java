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
	// fields end
	
	
	// constructors
	public OverrideSlot(String str) throws FailToCreateSlotException {
		switch (str) {
			case "0": _toOverride = TapeConst.TC_0; break;
			case "1": _toOverride = TapeConst.TC_1; break;
			case "x": _toOverride = TapeConst.TC_X; break;
			default: throw new FailToCreateSlotException(1, str);
		}
	}
	// constructors end


	// methods
	public String toCode() {
		StringBuffer rtn = new StringBuffer("ovrd:");
		switch (_toOverride) {
			case TC_0: rtn.append('0'); break;
			case TC_1: rtn.append('1'); break;
			case TC_X: rtn.append('x'); break;
		}
		return new String(rtn);
	}
	
	public NextToExec exec(TapeConst tc) {
		ExecResult er = ExecResult.NIL;
		switch (_toOverride) {
			case TC_0: er = ExecResult.OVRD_0; break;
			case TC_1: er = ExecResult.OVRD_1; break;
			case TC_X: er = ExecResult.OVRD_X; break;
		}
		return new NextToExec(0, 1, er);
	}
	// methods end
}