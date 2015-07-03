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
public class TapePanel extends JPanel {
	// fields
	protected Tape _tape;
	// fields end


	// constructors
	public TapePanel(Tape t) { _tape = t; }
	// constructors end
}