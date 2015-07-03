/*******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2015/6/19
  VERSION : v1.0
*******************************************************************************/


/************
   package   
************/
package talide.function;


/***********
   import   
***********/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/*****************
   public class   
*****************/
public abstract class Func extends AbstractAction {
	// static fields
	protected static talide.Talide _talideInstance;
	// static fields end


	// constructors
	public Func() { super(); }
	public Func(String name) { super(name); }
	public Func(String name, Icon icon) { super(name, icon); }
	// constructors end


	// static methods
	public static void setTalideInstance(talide.Talide t) {
		_talideInstance = t;
	}
	// static methods end
}