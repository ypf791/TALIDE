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
import java.awt.image.*;

import talide.core.*;


/*****************
   public class   
*****************/
public class CircuitPanel extends JPanel {
	// fields
	protected Circuit _circuit;
	
	protected Image[] _img_cir;
	protected Image[] _img_slot;
	
	private final int CIR_WIRE_POS_X = 53;
	private final int CIR_WIRE_POS_Y = 22;
	private final int CIR_START_WIDTH  = 53;
	private final int CIR_START_HEIGHT = 47;
	private final int CIR_GRID_WIDTH  = 41;
	private final int CIR_GRID_HEIGHT = 44;
	private final int CIR_END_DX = -11;
	private final int CIR_END_DY = -2;
	
	private BufferedImage _circuitBuf;
	// fields end


	// constructors
	public CircuitPanel(Circuit c) {
		_circuit = c;
		_img_cir    = Talide._img_cir;
		_img_slot   = Talide._img_slot;
		
		int __x = CIR_START_WIDTH  + c.getWidth()  * CIR_GRID_WIDTH  +  5;
		int __y = CIR_START_HEIGHT + c.getHeight() * CIR_GRID_HEIGHT - 41;
		Dimension preferredSize = new Dimension(__x, __y);
		
		_circuitBuf = new BufferedImage(__x, __y, BufferedImage.TYPE_INT_ARGB);
		
		paintCircuit(_circuitBuf.getGraphics());
		
		setOpaque(false);
		setPreferredSize(preferredSize);
	}
	// constructors end
	
	
	// methods
	public void update(Graphics g) { paintComponent(g); }
	
	public void paintComponent(Graphics g) {
		g.drawImage(_circuitBuf, 0, 0, null);
	}
	
	private void paintCircuit(Graphics g) {
		// paint start button
		int __start_pos_y = _circuit.getStartRow() * CIR_GRID_HEIGHT;
		g.drawImage(_img_cir[5], 0, __start_pos_y, null);
		
		// for each row, paint the wire, all Slot's, and finally the end
		int __row_n = _circuit.getHeight();
		int __col_n = _circuit.getWidth();
		int __x = CIR_WIRE_POS_X;
		int __y = CIR_WIRE_POS_Y;
		int __wire_length = __col_n * CIR_GRID_WIDTH;
		Circuit.Iterator it = _circuit.iterator();
		for (int i=0; i<__row_n; ++i) {
			__x = CIR_WIRE_POS_X;
			g.drawImage(_img_cir[3],
				__x, __y, __x + __wire_length, __y + 5,
				0, 0, 1, 5,
				null
			);
			for (int j=0; j<__col_n; ++j) {
				paintSlot(g, __x, __y, it);
				__x += CIR_GRID_WIDTH;
				it.increase();
			}
			g.drawImage(_img_cir[4], __x + CIR_END_DX, __y + CIR_END_DY, null);
			__y += CIR_GRID_HEIGHT;
		}
	}
	
	private void paintSlot(Graphics g, int x, int y, Circuit.Iterator it) {
		String[] tokens = it.val().split(":");
		int imgIdx = 0;
		int offsetX = 0;
		int offsetY = -15;
		int flg_brg_loop = 0;
		int flg_brg_dir  = 0;
		
		// TODO
		switch (tokens[0]) {
			case "j":
				flg_brg_loop = Integer.parseInt(tokens[1]);
				offsetX = -6;
				offsetY = -22;
				imgIdx = 6 - 2 * flg_brg_loop;
				break;
			case "if":
				switch (tokens[2]) {
					case "+": flg_brg_dir = 1; break;
					case "-": flg_brg_dir = -1; break;
				}
				switch (tokens[1]) {
					case "1": imgIdx = 3; break;
					case "x": imgIdx = 7; break;
				}
				imgIdx -= flg_brg_dir;
				break;
			case "ovrd":
				switch (tokens[1]) {
					case "0": imgIdx = 16; break;
					case "1": imgIdx = 18; break;
					case "x": imgIdx = 20; break;
				}
				break;
			case "sh":
				switch (tokens[1]) {
					case "-": imgIdx = 22; break;
					case "+": imgIdx = 24; break;
				}
				break;
			case "emp": default: break;
		}
		
		imgIdx += it.isExecuting() ? 1 : 0;
		g.drawImage(_img_slot[imgIdx], x + offsetX, y + offsetY, null);
		if (flg_brg_loop!=0) {
			int loop_length = (-1 - flg_brg_loop) * CIR_GRID_WIDTH;
			int __x = x + offsetX - loop_length;
			int __y = y + offsetY;
			g.drawImage(_img_cir[1],
				__x, __y, __x + loop_length, __y + 4,
				0, 0, 41, 4,
				null
			);
			g.drawImage(_img_cir[2], __x - 22, __y, null);
		}
		if (flg_brg_dir!=0) {
			int __offsetX = 13;
			int __offsetY = -5 + 22 * flg_brg_dir;
			g.drawImage(_img_cir[0], x + __offsetX, y + __offsetY, null);
		}
	}
	// methods end
}