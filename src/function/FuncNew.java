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
public class FuncNew extends Func {
	// constructors
	public FuncNew() { super("New"); }
	// constructors end
	
	
	// methods
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(
			_talideInstance,
			"new function executing..."
		);
	}
	// methods end
}