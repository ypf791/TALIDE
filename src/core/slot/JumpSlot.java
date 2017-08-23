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
		_code = "j:" + str;
		try {
			_distance = Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			throw new CreateSlotException(1, str);
		}
		_NTEList.add(new NextToExec(0, _distance, ExecResult.NIL));
	}
	// constructors end


	// methods
	public java.awt.Image getImage(boolean isExec) {
		int rtnIdx = 0;
		switch (_distance) {
			case -2: rtnIdx = 14; break;
			case -3: rtnIdx = 16; break;
			case -4: rtnIdx = 18; break;
		}
		return _img_slot[isExec ? rtnIdx+1 : rtnIdx];
	}
	// methods end
}