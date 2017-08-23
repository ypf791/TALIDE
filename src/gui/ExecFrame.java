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

import talide.core.*;
import pedeslib.movie.*;


/*****************
   public class   
*****************/
public class ExecFrame extends JFrame
                    implements PeriodProvider {
	// fields
	private Tape    _tape;
	private Circuit _circuit;
	private TapePanel    _tapePanel;
	private CircuitPanel _circuitPanel;
	private Movie _tapeMovie;
	// fields end
	
	
	// constructors
	public ExecFrame(Tape tape, Circuit circuit) {
		//Instantiate compenents
		_tape    = tape.clone();
		_circuit = circuit.clone();
		_tapePanel    = new TapePanel(_tape);
		_circuitPanel = new CircuitPanel(_circuit);
		_tapeMovie = new Movie(_tapePanel, this);
		
		//Adjust non-customized components
		
		//Declare container of the frame
		Container cp = getContentPane();
		
		//Define Layout Manager
		cp.setLayout(new BorderLayout());
		
		//Add components
		cp.add(_circuitPanel, BorderLayout.CENTER);
		cp.add(_tapePanel, BorderLayout.NORTH);
		
		//Set the dimension of the frame
		pack();
		
		//Center the frame
		Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension thisSize = getSize();
		if (thisSize.height > scrnSize.height) thisSize.height = scrnSize.height;
		if (thisSize.width  > scrnSize.width ) thisSize.width  = scrnSize.width;
		setLocation(
			(scrnSize.width  - thisSize.width ) / 2,
			(scrnSize.height - thisSize.height) / 2
		);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	// constructors end
	
	
	// methods
	public MoviePeriod nextMovie() {
		return _tapePanel.getPeriod(_circuit.execOneSlot(_tape.read()));
	}
	// methods end
}