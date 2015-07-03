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
import pedeslib.movie.*;


/*****************
   public class   
*****************/
public class TapePanel extends JPanel {
	// fields
	private Tape _tape;
	private double _shift;
	private boolean[] _strips;	// null if it is not showing strips
	private Dimension _preferredSize;
	
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
		
		TP_END_WIDTH  = _img[3].getWidth(null);
		TP_END_HEIGHT = _img[3].getHeight(null);
		TP_FRAME_WIDTH  = _img[5].getWidth(null);
		TP_FRAME_HEIGHT = _img[5].getHeight(null);
		TP_UNIT_WIDTH  = _img[0].getWidth(null);
		TP_UNIT_HEIGHT = _img[0].getHeight(null);
		TP_HEIGHT = Math.max(TP_END_HEIGHT, TP_FRAME_HEIGHT);
		
		_preferredSize = new Dimension(
			2 * TP_END_WIDTH + 4 * TP_UNIT_WIDTH,
			TP_HEIGHT
		);
		
		setPreferredSize(_preferredSize);
		setMinimumSize(_preferredSize);
	}
	// constructors end
	
	
	// methods
	public void update(Graphics g) {
		paintComponent(g);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// create buffers
		BufferedImage drawBuf = new BufferedImage(
			getWidth(),
			getHeight(),
			BufferedImage.TYPE_INT_ARGB
		);
		Graphics gBuf = drawBuf.getGraphics();
		
		// paint tape
		Tape.Iterator it = _tape.iterator();
		int __x = (getWidth() - TP_UNIT_WIDTH) / 2 + (int)(_shift * TP_UNIT_WIDTH);
		int __y = (TP_HEIGHT - TP_UNIT_HEIGHT) / 2;
		int __l_lim = 6;
		int __r_lim = getWidth() - 6;
		for (; __x>=__l_lim; __x-=TP_UNIT_WIDTH) {
			gBuf.drawImage(_img[it.val().toInt()], __x, __y, null);
			it.decrease();
		}
		gBuf.drawImage(_img[it.val().toInt()],
			__l_lim,       __y, TP_UNIT_WIDTH + __x, TP_UNIT_HEIGHT + __y,
			__l_lim - __x, 0,   TP_UNIT_WIDTH,       TP_UNIT_HEIGHT,
			null
		);
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
			0,   0,   __r_lim - __x, TP_UNIT_HEIGHT,
			null
		);
		
		// paint ends
		int __end_align_y = (TP_HEIGHT - TP_END_HEIGHT) / 2;
		gBuf.drawImage(_img[3], 0, __end_align_y, null);
		gBuf.drawImage(_img[4], getWidth() - TP_END_WIDTH, __end_align_y, null);
		
		// paint shadow
		gBuf.drawImage(_img[6],
			TP_END_WIDTH - 4, __y,
			null
		);
		gBuf.drawImage(_img[7],
			getWidth() - TP_END_WIDTH - TP_UNIT_WIDTH + 4, __y,
			null
		);
		gBuf.drawImage(_img[8],
			TP_END_WIDTH, (TP_HEIGHT + TP_UNIT_HEIGHT) / 2,
			getWidth() - 2 * TP_END_WIDTH, 4,
			null
		);
		
		// paint frame
		gBuf.drawImage(_img[5],
			(getWidth() - TP_FRAME_WIDTH) / 2, (TP_HEIGHT - TP_FRAME_HEIGHT) / 2,
			null
		);
		
		// flush buffer
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.drawImage(drawBuf, 0, 0, this);
	}
	// methods end
}