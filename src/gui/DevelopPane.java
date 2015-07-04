/*******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2015/6/19
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
public class DevelopPane extends JTabbedPane {
	// constructors
	public DevelopPane() {
		setPreferredSize(new Dimension(900, 600));
		
		// testing components
		JPanel jpnl = new JPanel();
		jpnl.setBackground(Color.WHITE);
		addTab("Tab 1", jpnl);
		
		jpnl = new JPanel();
		jpnl.setBackground(Color.RED);
		addTab("Tab 2", jpnl);
		
		jpnl = new JPanel();
		jpnl.setBackground(Color.BLACK);
		addTab("Tab 3", jpnl);
	}
	// constructors end
}