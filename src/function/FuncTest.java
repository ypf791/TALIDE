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

import talide.core.*;


/*****************
   public class   
*****************/
public class FuncTest extends Func {
	public FuncTest() {
		super("Test");
	}
	
	public void actionPerformed(ActionEvent e) {
		_talideInstance.showExecFrame(
			new Tape("TEST"),
			new Circuit("TEST")
		);
	}
}