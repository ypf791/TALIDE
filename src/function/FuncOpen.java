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
public class FuncOpen extends Func {
	public FuncOpen() {
		super("Open");
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("open function executing...");
	}
}