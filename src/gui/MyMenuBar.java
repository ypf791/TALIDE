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

import talide.function.*;


/*****************
   public class   
*****************/
public class MyMenuBar extends JMenuBar {
	public MyMenuBar() {
		//file
		JMenu jmFile = new JMenu("File");
		jmFile.add(new JMenuItem(Talide._func_new));
		jmFile.add(new JMenuItem(Talide._func_open));
		jmFile.add(new JMenuItem("Save"));
		jmFile.add(new JMenuItem("Save As"));
		jmFile.addSeparator();
		jmFile.add(new JMenuItem("Close"));
		//edit
		JMenu jmEdit = new JMenu("Edit");
		jmEdit.add(new JMenuItem("Undo"));
		jmEdit.add(new JMenuItem("Redo"));
		jmEdit.addSeparator();
		jmEdit.add(new JMenuItem("Cut"));
		jmEdit.add(new JMenuItem("Copy"));
		jmEdit.add(new JMenuItem("Paste"));
		//about
		JMenu jmAbout = new JMenu("About");
		jmAbout.add(new JMenuItem(Talide._func_test));
		jmAbout.addSeparator();
		jmAbout.add(new JMenuItem("About Talide"));
		
		add(jmFile);
		add(jmEdit);
		add(jmAbout);
	}
}