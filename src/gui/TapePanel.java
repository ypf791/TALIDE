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
import java.util.Random;

import talide.core.*;
import pedeslib.movie.*;


/*****************
   public class   
*****************/
public class TapePanel extends JPanel {
	// fields
	private Tape _tape;
	private double _shift;
	private boolean[] _strips;	// null if it is not showing strips
	private TapeConst _strips_val;
	
	private final Image[] _img;
	
	private final int TP_HEIGHT;
	private final int TP_END_WIDTH;
	private final int TP_END_HEIGHT;
	private final int TP_FRAME_WIDTH;
	private final int TP_FRAME_HEIGHT;
	private final int TP_UNIT_WIDTH;
	private final int TP_UNIT_HEIGHT;
	// fields end


	// constructors
	public TapePanel(Tape t) {
		_tape = t;
		_shift = 0.0d;
		_strips = null;
		_img = Talide._img_tape;
		
		MediaTracker mt = new MediaTracker(this);
		int __length = _img.length;
		for (int i=0; i<__length; ++i) {
			mt.addImage(_img[i], i);
			try {
				mt.waitForID(i);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		TP_END_WIDTH    = _img[3].getWidth(null);
		TP_END_HEIGHT   = _img[3].getHeight(null);
		TP_FRAME_WIDTH  = _img[5].getWidth(null);
		TP_FRAME_HEIGHT = _img[5].getHeight(null);
		TP_UNIT_WIDTH   = _img[0].getWidth(null);
		TP_UNIT_HEIGHT  = _img[0].getHeight(null);
		TP_HEIGHT = Math.max(TP_END_HEIGHT, TP_FRAME_HEIGHT);
		
		Dimension _preferredSize = new Dimension(
			2 * TP_END_WIDTH + 3 * TP_UNIT_WIDTH,
			TP_HEIGHT
		);
		
		setPreferredSize(_preferredSize);
		setMinimumSize(_preferredSize);
	}
	// constructors end
	
	
	// nested classes
	class ShiftLeftPeriod extends MoviePeriod {
		public void broadcast(int tick) { _shift += 0.05d; }
		
		public void ending() {
			_shift = 0.0d;
			_tape.shiftLeft();
		}
	}
	
	class ShiftRightPeriod extends MoviePeriod {
		public void broadcast(int tick) { _shift -= 0.05d; }
		
		public void ending() {
			_shift = 0.0d;
			_tape.shiftRight();
		}
	}
	
	class OverridePeriod extends MoviePeriod {
		private TapeConst _toOverride;
		private int _order[];
		
		public OverridePeriod(TapeConst tc) {
			super();
			
			_toOverride = tc;
			_order = new int[getFramesCount()];
			
			Random rand = new Random();
			for (int i=getFramesCount()-1; i>=0; --i) _order[i] = i;
			for (int i=getFramesCount()-1; i>0; --i) {
				int swp = rand.nextInt(i+1);
				if (i==swp) continue;
				_order[i]   = _order[swp] ^ _order[i];
				_order[swp] = _order[swp] ^ _order[i];
				_order[i]   = _order[swp] ^ _order[i];
			}
		}
		
		public void broadcast(int tick) {
			if (tick==0) {
				// initialize
				_strips_val = _toOverride;
				_strips = new boolean[getFramesCount()];
			}
			_strips[_order[tick]] = true;
		}
		
		public void ending() {
			_strips = null;
			_tape.override(_toOverride);
		}
	}
	// nested classes end
	
	
	// methods
	public void update(Graphics g) { paintComponent(g); }
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		/*
		  Tape should be painted in the order of
		    TAPE, STRIPS, ENDS, SHADOW and FRAME
		  We create a BufferedImage for double-buffering.
		  For painting TAPE, we
		    1. decide horizontal range limitation (__l_lim, __r_lim)
		    2. declare position flags for painting units (__x, __y)
		    3. paint left-hand side
		    4. paint right-hand side
		    P.S.
		      For each hand side, we initialize __x, get iterator for _tape, 
		      paint full units, and paint the incomplete unit in final.
		*/
		
		// create buffers
		BufferedImage drawBuf = new BufferedImage(
			getWidth(),
			getHeight(),
			BufferedImage.TYPE_INT_ARGB
		);
		Graphics gBuf = drawBuf.getGraphics();
		
		// paint TAPE
		int __l_lim = 6;
		int __r_lim = getWidth() - 6;
		int __x = (getWidth() - TP_UNIT_WIDTH) / 2 + (int)(_shift * TP_UNIT_WIDTH);
		int __y = (TP_HEIGHT - TP_UNIT_HEIGHT) / 2;
		// paint the left-hand side
		Tape.Iterator it = _tape.iterator();
		for (; __x>=__l_lim; __x-=TP_UNIT_WIDTH) {
			gBuf.drawImage(_img[it.val().toInt()], __x, __y, null);
			it.decrease();
		}
		gBuf.drawImage(_img[it.val().toInt()],
			__l_lim,       __y, TP_UNIT_WIDTH + __x, TP_UNIT_HEIGHT + __y,
			__l_lim - __x,   0, TP_UNIT_WIDTH,       TP_UNIT_HEIGHT,
			null
		);
		// paint the right-hand side
		it = _tape.iterator();
		__x = (getWidth() - TP_UNIT_WIDTH) / 2 + (int)(_shift * TP_UNIT_WIDTH);
		it.increase();
		__x += TP_UNIT_WIDTH;
		for (; __x+TP_UNIT_WIDTH<=__r_lim; __x+=TP_UNIT_WIDTH) {
			gBuf.drawImage(_img[it.val().toInt()], __x, __y, null);
			it.increase();
		}
		gBuf.drawImage(_img[it.val().toInt()],
			__x, __y, __r_lim,       TP_UNIT_HEIGHT + __y,
			  0,   0, __r_lim - __x, TP_UNIT_HEIGHT,
			null
		);
		
		// paint STRIPS
		if (_strips!=null) {
			__x = (getWidth() - TP_UNIT_WIDTH) / 2 + (int)(_shift * TP_UNIT_WIDTH);
			int n = _strips.length;
			int x_s[] = new int[n+1];
			Image toPaint = _img[_strips_val.toInt()];
			x_s[0] = 0;
			for (int i=1; i<=n; ++i) {
				x_s[i] = TP_UNIT_WIDTH * i / n;
				if (!_strips[i-1]) continue;
				gBuf.drawImage(toPaint,
					__x + x_s[i-1], __y, __x + x_s[i], __y + TP_UNIT_HEIGHT,
					      x_s[i-1],   0,       x_s[i],       TP_UNIT_HEIGHT,
					null
				);
			}
		}
		
		// paint ENDS
		int __end_align_y = (TP_HEIGHT - TP_END_HEIGHT) / 2;
		gBuf.drawImage(_img[3],                         0, __end_align_y, null);
		gBuf.drawImage(_img[4], getWidth() - TP_END_WIDTH, __end_align_y, null);
		
		// paint SHADOW
		// in this part, we cannot help but using values depending on the details
		//   of images used
		int __inset_l = TP_END_WIDTH - 4;
		int __inset_r = getWidth() - __inset_l - TP_UNIT_WIDTH;
		gBuf.drawImage(_img[6], __inset_l, __y, null);
		gBuf.drawImage(_img[7], __inset_r, __y, null);
		gBuf.drawImage(_img[8],
			TP_END_WIDTH, (TP_HEIGHT + TP_UNIT_HEIGHT) / 2,
			getWidth() - 2 * TP_END_WIDTH, _img[8].getHeight(null),
			null
		);
		
		// paint FRAME
		gBuf.drawImage(_img[5],
			(getWidth() - TP_FRAME_WIDTH) / 2, (TP_HEIGHT - TP_FRAME_HEIGHT) / 2,
			null
		);
		
		// flush buffer
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.drawImage(drawBuf, 0, 0, this);
	}
	
	public MoviePeriod getPeriod(ExecResult er) {
		switch (er) {
			case SH_L:   return new ShiftLeftPeriod();
			case SH_R:   return new ShiftRightPeriod();
			case OVRD_0: return new OverridePeriod(TapeConst.TC_0);
			case OVRD_1: return new OverridePeriod(TapeConst.TC_1);
			case OVRD_X: return new OverridePeriod(TapeConst.TC_X);
			case NIL: default:
				return new MoviePeriod(){
					public void broadcast(int tick) {}
				};
		}
	}
	// methods end
}