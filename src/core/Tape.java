/*******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2015/6/20
  VERSION : v1.0
  ---
	Tape flow:
    NEXT flow: accessed with _next
              _mrgn_l => Node => Node => ... => Node => _mrgn_r => (self)
    (self) <=         <=      <=      <=     <=      <=
                                           PREV flow: accessed with _prev

  Empty Tape:
    _mrgn_l <==> _mrgn_r
        ^- _read -^
    Either case is OK.

  MarginNode:
    _length represent where the _read pointer is. If _read does not point to a
    MarginNode, _length is meaningless.
    _length is at least 1.
*******************************************************************************/


/************
   package   
************/
package talide.core;


/***********
   import   
***********/
import java.util.Vector;


/*****************
   public class   
*****************/
public class Tape implements Cloneable {
	// fields
	private TapeNode _read;
	private MarginNode _mrgn_l;
	private MarginNode _mrgn_r;
	
	private TapeConst[] _rawData;
	private int _init_idx;
	
	private Vector<Iterator> _trackers;
	// fields end


	// constructors
	public Tape(String str) { this(new TapeConst[] { TapeConst.TC_0 }, 0); }
	
	public Tape() {
		_mrgn_l = new MarginNode();
		_mrgn_r = new MarginNode();
		_mrgn_l._next = _mrgn_r;
		_read = _mrgn_r._prev = _mrgn_l;
		_trackers = new Vector<Iterator>();
	}
	
	public Tape(TapeConst[] raw, int init) {
		this();
		
		_rawData = raw.clone();
		_init_idx = init;
		
		reset();
	}
	
	public Tape(Tape t) {
		this();
		
		_rawData = t._rawData.clone();
		_mrgn_l._length = t._mrgn_l._length;
		_mrgn_r._length = t._mrgn_r._length;
		
		TapeNode node = _read = _mrgn_l;
		for (TapeNode it=t._mrgn_l._next; it!=t._mrgn_r; it=it._next) {
			node = new TapeNode(it._value, node, _mrgn_r);
			node._prev._next = _mrgn_r._prev = node;
			if (t._read==it) _read = node;
		}
		
		if (t._read==t._mrgn_r) _read = _mrgn_r;
	}
	// constructors end


	// nested classes
	private class TapeNode {
		// fields
		TapeConst _value;
		TapeNode _prev;
		TapeNode _next;
		// fields end


		// constructors
		TapeNode(TapeConst v) {
			_value = v;
			_next = _prev = this;
		}
		
		TapeNode(TapeConst v, TapeNode p, TapeNode n) {
			_value = v;
			_next = n;
			_prev = p;
		}
		// constructors end
	}

	private class MarginNode extends TapeNode {
		// fields
		int _length = 0;
		// fields end


		// constructors
		MarginNode() { super(TapeConst.TC_X); }
		// constructors end
	}
	
	public class Iterator {
		// fields
		private TapeNode _focus;
		private int _depth;
		// fields end
		
		
		// constructors
		public Iterator(TapeNode node) {
			_focus = node;
			_depth = 0;
			if (node==_mrgn_l) {
				_depth -= _mrgn_l._length;
			} else if (node==_mrgn_r) {
				_depth += _mrgn_r._length;
			}
		}
		// constructors end
		
		
		// methods
		public TapeConst val() {
            if (_focus == null) {
                return TapeConst.TC_X;
            } else {
                return _focus._value;
            }
        }
		
		public void decrease() {
            if (_focus == null) return;
			if (_depth != 0) --_depth;
			if (_depth == 0) _focus = _focus._prev;
			if (_focus == _mrgn_l) --_depth;
		}
		
		public void increase() {
            if (_focus == null) return;
			if (_depth != 0) ++_depth;
			if (_depth == 0) _focus = _focus._next;
			if (_focus == _mrgn_r) ++_depth;
		}
        
        public void disable() {
            _focus = null;
        }
		// methods end
	}
	// nested classes end


	// methods
	public Tape clone() { return this; }
	
	public void reset() {
		if (_rawData==null) return;
		if (_rawData.length<1) return;
		if (_rawData.length<=_init_idx || _init_idx<0) _init_idx = 0;
		
		_mrgn_l._length = 0;
		_mrgn_r._length = 0;
		
		int __length = _rawData.length;
		TapeNode node = _mrgn_l;
		for (int i=0; i<__length; ++i) {
			node = new TapeNode(_rawData[i], node, _mrgn_r);
			node._prev._next = _mrgn_r._prev = node;
			if (i==_init_idx) _read = node;
		}
		trim();
		
		for (Iterator it : _trackers) it.disable();
		_trackers.clear();
	}
	
	private void trim() {
		boolean meet;
		TapeNode node;
		
		// left
		meet = (_read==_mrgn_l);
		node = _mrgn_l._next;
		while (node!=_mrgn_r && node._value==TapeConst.TC_X) {
			meet = meet || (node==_read);
			if (meet) ++_mrgn_l._length;
			node = node._next;
		}
		_mrgn_l._next = node;
		node._prev = _mrgn_l;
		if (meet) _read = _mrgn_l;
		
		// right
		meet = (_read==_mrgn_r);
		node = _mrgn_r._prev;
		while (node!=_mrgn_l && node._value==TapeConst.TC_X) {
			meet = meet || (node==_read);
			if (meet) ++_mrgn_r._length;
			node = node._prev;
		}
		_mrgn_r._prev = node;
		node._next = _mrgn_r;
		if (meet) _read = _mrgn_r;
	}
	
	public void shiftLeft() {
		if (_read==_mrgn_r) --_mrgn_r._length;
		if (_mrgn_l._length==0) _read = _read._prev;
		if (_read==_mrgn_l) ++_mrgn_l._length;
	}
	
	public void shiftRight() {
		if (_read==_mrgn_l) --_mrgn_l._length;
		if (_mrgn_l._length==0) _read = _read._next;
		if (_read==_mrgn_r) ++_mrgn_r._length;
	}
	
	public void override(TapeConst v) {
		// if we override the current value to be X, we try to trim off X's
		// otherwise, we insert enough X's to some end of the tape.
		if (v==TapeConst.TC_X) {
			_read._value = v;
			trim();
		} else {
			if (_read==_mrgn_l) {
				for (int i=_mrgn_l._length; i>0; --i) {
					TapeNode node = new TapeNode(TapeConst.TC_X, _mrgn_l, _mrgn_l._next);
					node._prev._next = node._next._prev = node;
				}
				_read = _mrgn_l._next;
				_mrgn_l._length = 0;
			} else if (_read==_mrgn_r) {
				for (int i=_mrgn_r._length; i>0; --i) {
					TapeNode node = new TapeNode(TapeConst.TC_X, _mrgn_r._prev, _mrgn_r);
					node._prev._next = node._next._prev = node;
				}
				_read = _mrgn_r._prev;
				_mrgn_r._length = 0;
			}
			_read._value = v;
		}
	}
	
	public TapeConst read() { return _read._value; }
	
	public Iterator iterator() {
		_trackers.add(new Iterator(_read));
		return _trackers.lastElement();
	}
	
	public void accept(ExecResult er) {
		switch (er) {
			case SH_L:   shiftLeft();  break;
			case SH_R:   shiftRight(); break;
			case OVRD_0: override(TapeConst.TC_0); break;
			case OVRD_1: override(TapeConst.TC_1); break;
			case OVRD_X: override(TapeConst.TC_X);
			case NIL: default: break;
		}
	}
	// methods end
}