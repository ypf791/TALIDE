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
                    implements PeriodProvider, Executable {
	// fields
	private Tape    _tape;
	private Circuit _circuit;
	private TapePanel    _tapePanel;
	private CircuitPanel _circuitPanel;
	private Movie _tapeMovie;
	// fields end
	
	
	// constructors
	public ExecFrame(Tape tape, Circuit circuit) {
		// Instantiate compenents
		_tape    = tape.clone();
		_circuit = circuit.clone();
		_tapePanel    = new TapePanel(_tape);
		_circuitPanel = new CircuitPanel(_circuit, this);
		_tapeMovie = new Movie(this, 8, this);
		
		// Declare container of the frame
		Container cp = getContentPane();
		
		// Define Layout Manager
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		cp.setLayout(layout);
		// set constraints
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(_tapePanel, c);
		cp.add(_tapePanel);
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		layout.setConstraints(_circuitPanel, c);
		cp.add(_circuitPanel);
		
		// Set the dimension of the frame
		pack();
		
		// Center the frame
		Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension thisSize = getSize();
		if (thisSize.height > scrnSize.height) thisSize.height = scrnSize.height;
		if (thisSize.width  > scrnSize.width ) thisSize.width  = scrnSize.width;
		setLocation(
			(scrnSize.width  - thisSize.width ) / 2,
			(scrnSize.height - thisSize.height) / 2
		);
		
		cp.setBackground(Color.WHITE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	// constructors end
	
	
	// methods
	public MoviePeriod nextMovie() {
		return _tapePanel.getPeriod(_circuit.execOneSlot(_tape.read()));
	}
	
	public void start() {
		_tapeMovie.start();
	}
	
	public void shutdown() {
		_tapeMovie.terminate(true);
		_tape.reset();
		_circuit.reset();
	}
	// methods end
}