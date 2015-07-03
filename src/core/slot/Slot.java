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
	Slot createSlot(String str);
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


	// static methods
	public static Slot parseSlot(String str) {
		String[] tokens = str.split(":", 2); // [type, option]
		String type = tokens[0];
		String option = "";
		
		if (tokens.length==2) option = tokens[1];
		SlotFactory factory = _factoryMap.get(type);
		if (factory!=null) {
			return factory.createSlot(option);
		} else {
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
	}
	// static block end
}