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
import java.awt.Image;
import java.awt.Graphics;
import java.util.TreeMap;
import java.util.Vector;


/**************
   interface   
**************/
@FunctionalInterface
interface SlotFactory {
	// methods
	Slot createSlot(String str) throws CreateSlotException;
	// methods end
}


/*****************
   public class   
*****************/
public abstract class Slot {
	// static fields
	private static TreeMap<String, SlotFactory> _factoryMap;
	
	protected static NextToExec _defaultNTE;
	
	protected static Image[] _img_slot;
	protected static Image[] _img_slot_p;
	// static fields end
	
	
	// fields
	protected Vector<NextToExec> _NTEList = new Vector<>();
	protected Vector<Bridge> _bridgeList = null;
	protected String _code;
	// fields end
	
	
	// nested enumerations
	protected static enum Direction {
		DIR_POS(1), DIR_NEG(-1);
		
		private int _value;
		
		private Direction(int v) { _value = v; }
		
		public int toInt() { return _value; }
	}
	// nested enumerations end


	// static methods
	public static void setImageReference(Image[] i, Image[] j) {
		_img_slot = i;
		_img_slot_p = j;
	}
	
	private static void showErrMsg(Exception ex, String msg) {
		System.err.println("[ ERR ] " + ex.getStackTrace()[0].toString());
		System.err.println("[ MSG ] " + msg);
		System.err.println("[ RTN ] EmptySlot instead");
	}
	
	public static Slot parseSlot(String str) {
		if (str==null) str = "";
		
		String[] tokens = str.split(":", 2); // [type, option]
		String type = "";
		String option = "";
		String errMsg = "";
		
		switch (tokens.length) {
			case 2: option = tokens[1];
			case 1: type   = tokens[0]; break;
		}

		SlotFactory factory = _factoryMap.get(type);
		try {
			return factory.createSlot(option);
		} catch (CreateSlotException ex) {
			showErrMsg(ex, "Unrecognized argument $" + ex._idx + ": " + ex._arg);
			return new EmptySlot();
		} catch (NullPointerException ex) {
			showErrMsg(ex, "No factory matches the given string: " + str);
			return new EmptySlot();
		} catch (Exception ex) {
			showErrMsg(ex, "Unpredictable exception");
			return new EmptySlot();
		}
	}
	
	public static void regFactory(String str, SlotFactory fac) {
		_factoryMap.put(str, fac);
	}
	// static methods end


	// methods
	public NextToExec exec(TapeConst tc) { return _NTEList.firstElement(); }
	
	public String toCode() { return _code; }
	
	public abstract Image getImage(boolean b);
	
	public Vector<Bridge> getBridges() {
		if (_bridgeList==null) {
			_bridgeList = new Vector<Bridge>();
			for (NextToExec nte : _NTEList) {
				if (nte._diff_row==0 && nte._diff_col==1) continue;
				_bridgeList.add(new Bridge(nte._diff_row, nte._diff_col));
			}
		}
		return _bridgeList;
	}
	// methods end


	// static block
	static {
		_defaultNTE = new NextToExec(0, 1, ExecResult.NIL);
		_factoryMap = new TreeMap<String, SlotFactory>();
		
		// register factories
		regFactory("emp", EmptySlot::new);
		regFactory("sh", ShiftSlot::new);
		regFactory("if", IfSlot::new);
		regFactory("ovrd", OverrideSlot::new);
		regFactory("j", JumpSlot::new);
		/*
		regFactory("sh_p", PrsShiftSlot::new);
		regFactory("if_p", PrsIfSlot::new);
		regFactory("ovrd_p", PrsOverrideSlot::new);
		regFactory("j_p", PrsJumpSlot::new);
		*/
	}
	// static block end
}