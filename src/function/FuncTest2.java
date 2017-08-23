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
import java.util.Hashtable;

import talide.core.*;


/*****************
   public class   
*****************/
public class FuncTest2 extends Func {
	// fields
	MyFrame _frame;
	// fields end
	
	
	// constructors
	public FuncTest2() { super("Test2"); }
	// constructors end
	
	
	// nested classes
	class MyFrame extends JFrame {
		JButton _decBtn;
		JButton _incBtn;
		JLabel _field;
		int _value;
		
		public MyFrame() {
			super();
			setLayout(new FlowLayout());
			_value = 40;
			_decBtn = new JButton("-");
			_incBtn = new JButton("+");
			_field = new JLabel(String.valueOf(_value));
			_decBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					--_value;
					_field.setText(String.valueOf(_value));
				}
			});
			_incBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					++_value;
					_field.setText(String.valueOf(_value));
				}
			});
			add(_decBtn);
			add(_field);
			add(_incBtn);
			pack();
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);
		}
	}
	// nested classes end
	
	
	// methods
	public void actionPerformed(ActionEvent e) {
		_frame = new MyFrame();
	}
	// methods end
}