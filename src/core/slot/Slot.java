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
import java.util.TreeMap;


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
	protected static NextToExec _defaultNTE;
	private static TreeMap<String, SlotFactory> _factoryMap;
	// static fields end
	
	
	// nested enumerations
	protected enum Direction {
		DIR_POS(1), DIR_NEG(-1);
		
		private int _value;
		
		private Direction(int v) { _value = v; }
		
		public int toInt() { return _value; }
	}
	// nested enumerations end


	// static methods
	private static void showErrMsg(Exception ex, String msg) {
		System.err.println("[ ERR ] " + ex.getStackTrace()[0].toString());
		System.err.println("[ MSG ] " + msg);
		System.err.println("[ RTN ] EmptySlot instead");
	}
	public static Slot parseSlot(String str) {
		String[] tokens = str.split(":", 2); // [type, option]
		String type = tokens[0];
		String option = "";
		String errMsg = "";
		
		if (tokens.length==2) option = tokens[1];
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
	public abstract NextToExec exec(TapeConst tc);
	
	public abstract String toCode();
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
	}
	// static block end
}