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
	
	private final int CIR_START_WIDTH;
	private final int CIR_START_HEIGHT;
	private final int BRG_END_WIDTH;
	private final int BRG_END_HEIGHT;
	private final int BRG_COL_WIDTH;
	private final int CIR_END_DX = -11;
	private final int CIR_END_DY = -2;
	private final int SLOT_CENTER_DX = 15;
	private final int SLOT_CENTER_DY = 1;
	private final int MIN_GRID_WIDTH  = 35;
	private final int MIN_GRID_HEIGHT = 44;
	private final int R_THRESHOLD = 16;
	
	private int _wire_pos_x;
	private int _wire_pos_y = 22; // actually immutable
	private int _grid_width;
	private int _grid_height;
	
	private StartButton _startButton;
	private Pressable _pressed = null;
	// fields end


	// constructors
	public CircuitPanel(Circuit c, Executable exe) {
		_circuit = c;
		_img_cir = Talide._img_cir;
		
		CIR_START_WIDTH  = _img_cir[6].getWidth(null);
		CIR_START_HEIGHT = _img_cir[6].getHeight(null);
		BRG_END_WIDTH  = _img_cir[2].getWidth(null);
		BRG_END_HEIGHT = _img_cir[2].getHeight(null);
		BRG_COL_WIDTH  = _img_cir[0].getWidth(null);
		
		int pref_dw = 6;
		
		_grid_width  = MIN_GRID_WIDTH + pref_dw;
		_grid_height = MIN_GRID_HEIGHT;
		_wire_pos_x  = 12 + _grid_width;
		
		int __x = c.getWidth()  * _grid_width  + 15 + CIR_START_WIDTH;
		int __y = c.getHeight() * _grid_height + 8;
		
		_startButton = new StartButton(exe);
		
		Dimension preferredSize = new Dimension(__x, __y);
		Dimension minimumSize = new Dimension(__x - c.getWidth() * pref_dw, __y);
		
		setOpaque(false);
		setPreferredSize(preferredSize);
		setMinimumSize(minimumSize);
		
		addMouseListener(new CollideAdapter());
	}
	// constructors end
	
	
	// nested classes
	private class StartButton implements Pressable {
		// fields
		private int _imgIdx;
		private int _rlsImgIdx;
		private Executable _ctrlPoint;
		// fields end
		
		
		// constructors
		public StartButton(Executable exe) {
			_imgIdx = _rlsImgIdx = 6;
			_ctrlPoint = exe;
		}
		// constructors end


		// methods
		public Image getImage() { return _img_cir[_imgIdx]; }
		
		public void press() { _imgIdx = 7; }
		
		public void release(boolean confirm) {
			if (confirm) {
				_rlsImgIdx = 14 - _rlsImgIdx;
				if (_rlsImgIdx==8) {
					_ctrlPoint.start();
				} else {
					_ctrlPoint.shutdown();
				}
			}
			_imgIdx = _rlsImgIdx;
		}
	}
	
	private class CollideAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			_pressed = getCollided(e);
			if (_pressed!=null) {
				_pressed.press();
				repaint();
			}
		}
		public void mouseReleased(MouseEvent e) {
			if (_pressed!=null) {
				_pressed.release(_pressed==getCollided(e));
				_pressed = null;
				repaint();
			}
		}
	}
	// nested classes end
	
	
	// methods
	public void update(Graphics g) { paintComponent(g); }
	
	public void paintComponent(Graphics g) {
		int __row_n = _circuit.getHeight();
		int __col_n = _circuit.getWidth();
		int __x = _wire_pos_x;
		int __y = _wire_pos_y;
		int __w = Math.max(getWidth(), 17 + (__col_n + 1) * MIN_GRID_WIDTH);
		int __h = getHeight();
		
		BufferedImage slotBuf;
		slotBuf = new BufferedImage(__w, __h, BufferedImage.TYPE_INT_ARGB);
		Graphics gSlot = slotBuf.getGraphics();
		
		// compute _grid_width, _grid_height & _wire_pos_x
		_grid_width = (__w - 17) / (__col_n + 1);
		if (__row_n<2) {
			_grid_height = __h; // set to be full height if just 1 row
		} else {
			_grid_height = (__h - CIR_START_HEIGHT) / (__row_n - 1);
		}
		_grid_width  = Math.max(MIN_GRID_WIDTH,  _grid_width);
		_grid_height = Math.max(MIN_GRID_HEIGHT, _grid_height);
		_wire_pos_x  = 12 + _grid_width;
		
		// paint start button
		int __start_pos_y = _circuit.getStartRow() * _grid_height;
		g.drawImage(_startButton.getImage(), 0, __start_pos_y, null);
		g.drawImage(_img_cir[4],
			CIR_START_WIDTH, __y + __start_pos_y, _wire_pos_x - CIR_START_WIDTH, 5,
			null
		);
		
		// for each row, paint the wire, its end, and finally all the Slot's
		int __wire_length = __col_n * _grid_width;
		Circuit.Iterator it = _circuit.iterator();
		for (int i=0; i<__row_n; ++i) {
			__x = _wire_pos_x;
			g.drawImage(_img_cir[4], __x, __y, __wire_length, 5, null);
			g.drawImage(_img_cir[5], __w - 16, __y + CIR_END_DY, null);
			for (int j=0; j<__col_n; ++j) {
				paintSlot(g, gSlot, __x + SLOT_CENTER_DX, __y + SLOT_CENTER_DY, it);
				__x += _grid_width;
				it.increase();
			}
			__y += _grid_height;
		}
		
		// flush _slotBuf
		g.drawImage(slotBuf, 0, 0, null);
	}
	
	private void paintSlot(Graphics g, Graphics gSlot,
	                       int x, int y, Circuit.Iterator it) {
		Slot s = it.val();
		for (Bridge brg : s.getBridges()) {
			if (brg._dir==1 || brg._dir==-1) {
				g.drawImage(_img_cir[0],
					x - (BRG_COL_WIDTH >> 1),
					y + R_THRESHOLD - (brg._dir<0 ? _grid_height : 0),
					BRG_COL_WIDTH,
					_grid_height - (R_THRESHOLD << 1),
					null
				);
			}
			if (brg._jump<-1) {
				int jump_length = brg._jump * _grid_width;
				int __x = x - (BRG_END_WIDTH >> 1);
				int __y = y - R_THRESHOLD - BRG_END_HEIGHT;
				g.drawImage(_img_cir[2], __x + jump_length, __y, null);
				g.drawImage(_img_cir[1],
					__x + jump_length + BRG_END_WIDTH, __y,
					     -jump_length - BRG_END_WIDTH,   4,
					null
				);
				g.drawImage(_img_cir[3], __x, __y, null);
			}
		}
		Image toDraw = s.getImage(it.isExecuting());
		int imgW = toDraw.getWidth(null);
		int imgH = toDraw.getHeight(null);
		gSlot.drawImage(toDraw, x - (imgW >> 1), y - (imgH >> 1), null);
	}
	
	private Pressable getCollided(MouseEvent e){
		Object rtn = null;
		int x = e.getX();
		int y = e.getY();
		
		// check _startButton
		if (Math.hypot(x-22, y-19)<=18) return _startButton;
		
		// check slots
		x += R_THRESHOLD - _wire_pos_x - SLOT_CENTER_DX;
		y += R_THRESHOLD - _wire_pos_y - SLOT_CENTER_DY;
		
		if (x>=0 && y>=0) {
			int r = y / _grid_height;
			int c = x / _grid_width;
			x = x % _grid_width  - R_THRESHOLD;
			y = y % _grid_height - R_THRESHOLD;
			if (Math.hypot(x, y)<=R_THRESHOLD) rtn = _circuit.getSlot(r, c);
		}
		return ((rtn instanceof Pressable) ? (Pressable)rtn : null);
	}
	// methods end
}