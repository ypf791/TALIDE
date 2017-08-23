/*******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2015/6/14
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


/*****************
   public class   
*****************/
public class MyToolBar extends JToolBar {
	// constructors
	public MyToolBar() {
		add(new JButton(Talide._func_new));
		add(new JButton(Talide._func_open));
		add(new JButton(Talide._func_test));
	}
	// constructors end
}