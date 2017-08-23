/*******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2015/6/20
  VERSION : v1.0
*******************************************************************************/


/************
   package   
************/
package talide;


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
public class CircuitPanel extends JPanel {
	// fields
	protected Circuit _circuit;
	// fields end


	// constructors
	public CircuitPanel(Circuit c) { _circuit = c; }
	// constructors end
}