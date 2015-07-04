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
	// fields end


	// constructors
	public CircuitPanel(Circuit c) {
		_circuit = c;
		_img_cir    = Talide._img_cir;
		_img_slot   = Talide._img_slot;
		
		Dimension preferredSize = new Dimension(
			CIR_START_WIDTH  + c.getWidth()  * CIR_GRID_WIDTH  +  5,
			CIR_START_HEIGHT + c.getHeight() * CIR_GRID_HEIGHT - 41
		);
		
		setPreferredSize(preferredSize);
	}
	// constructors end
	
	
	// methods
	public void update(Graphics g) { paintComponent(g); }
	
	public void paintComponent(Graphics g) {
		// create buffers
		BufferedImage drawBuf = new BufferedImage(
			getWidth(),
			getHeight(),
			BufferedImage.TYPE_INT_ARGB
		);
		Graphics gBuf = drawBuf.getGraphics();
		
		// paint start button
		int __start_pos_y = _circuit.getStartRow() * CIR_GRID_HEIGHT;
		gBuf.drawImage(_img_cir[5], 0, __start_pos_y, null);
		
		// for each row, paint the wire, all Slot's, and finally the end
		int __row_n = _circuit.getHeight();
		int __col_n = _circuit.getWidth();
		int __x = CIR_WIRE_POS_X;
		int __y = CIR_WIRE_POS_Y;
		int __wire_length = __col_n * CIR_GRID_WIDTH;
		Circuit.Iterator it = _circuit.iterator();
		for (int i=0; i<__row_n; ++i) {
			__x = CIR_WIRE_POS_X;
			gBuf.drawImage(_img_cir[3],
				__x, __y, __x + __wire_length, __y + 5,
				0, 0, 1, 5,
				null
			);
			for (int j=0; j<__col_n; ++j) {
				paintSlot(gBuf, __x, __y, it);
				__x += CIR_GRID_WIDTH;
				it.increase();
			}
			gBuf.drawImage(_img_cir[4], __x + CIR_END_DX, __y + CIR_END_DY, null);
			__y += CIR_GRID_HEIGHT;
		}
		
		// flush buffer
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.drawImage(drawBuf, 0, 0, this);
	}
	
	private void paintSlot(Graphics g, int x, int y, Circuit.Iterator it) {
		String[] tokens = it.val().split(":");
		// TODO
		g.drawImage(_img_slot[it.isExecuting() ? 1 : 0], x, y - 15, null);
	}
	// methods end
}