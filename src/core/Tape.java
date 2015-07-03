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


/*****************
   public class   
*****************/
public class Tape implements Cloneable {
	// fields
	private TapeNode _read;
	private MarginNode _mrgn_l;
	private MarginNode _mrgn_r;
	// fields end


	// constructors
	public Tape(String str) {
		this();
	}
	
	public Tape() {
		_mrgn_l = new MarginNode();
		_mrgn_r = new MarginNode();
		_mrgn_l._next = _mrgn_r;
		_read = _mrgn_r._prev = _mrgn_l;
	}
	
	public Tape(Tape t) {
		this();
		
		_mrgn_l._length = t._mrgn_l._length;
		_mrgn_r._length = t._mrgn_r._length;
		
		TapeNode node = _mrgn_l;
		for (TapeNode it=t._mrgn_l._next; it!=t._mrgn_r; it=it._next) {
			node = new TapeNode(it._value, node, _mrgn_r);
			node._prev._next = _mrgn_r._prev = node;
			if (t._read==it) _read = node;
		}
		
		if (_read==null) {
			if (t._read==t._mrgn_l) {
				_read = _mrgn_l;
			} else if (t._read==t._mrgn_r) {
				_read = _mrgn_r;
			} else {
				System.err.println("[ Err ] Tape::Tape(Tape)");
				System.err.println("[ Msg ] Variable(s) confilcted.");
			}
		}
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
		int _length = 1;
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
		public TapeConst val() { return _focus._value; }
		
		public void decrease() {
			if (_depth==0) {
				--_depth;
				if (_depth!=0) {
					_focus = _focus._prev;
				}
			} else {
				_focus = _focus._prev;
				if (_focus==_mrgn_l) {
					--_depth;
				}
			}
		}
		
		public void increase() {
			if (_depth==0) {
				++_depth;
				if (_depth!=0) {
					_focus = _focus._next;
				}
			} else {
				_focus = _focus._next;
				if (_focus==_mrgn_r) {
					++_depth;
				}
			}
		}
		// methods end
	}
	// nested classes end


	// methods
	public Tape clone() { return new Tape(this); }
	
	public void shiftLeft() {
		if (_read==_mrgn_l) {
			++_mrgn_l._length;
		} else if (_read==_mrgn_r && _mrgn_r._length>1) {
			--_mrgn_r._length;
		} else {
			_read = _read._prev;
		}
	}
	
	public void shiftRight() {
		if (_read==_mrgn_r) {
			++_mrgn_r._length;
		} else if (_read==_mrgn_l && _mrgn_l._length>1) {
			--_mrgn_l._length;
		} else {
			_read = _read._next;
		}
	}
	
	public void override(TapeConst v) {
		// if we override the current value to be X, we try to trim off X's
		// otherwise, we insert enough X's to some end of the tape.
		if (v==TapeConst.TC_X) {
			_read._value = v;
			if (_mrgn_l._next==_read) {
				_read = _read._next;
				while (_read!=_mrgn_r && _read._value==TapeConst.TC_X) {
					_read = _read._next;
				}
				_mrgn_l._next = _read;
				_read = _read._prev = _mrgn_l;
			} else if (_mrgn_r._prev==_read) {
				_read = _read._prev;
				while (_read!=_mrgn_l && _read._value==TapeConst.TC_X) {
					_read = _read._prev;
				}
				_mrgn_r._prev = _read;
				_read = _read._next = _mrgn_r;
			}
		} else {
			if (_read==_mrgn_l) {
				for (int i=_mrgn_l._length; i>0; --i) {
					_mrgn_l._next = new TapeNode(TapeConst.TC_X, _mrgn_l, _mrgn_l._next);
				}
				_read = _mrgn_l._next;
				_mrgn_l._length = 1;
			} else if (_read==_mrgn_r) {
				for (int i=_mrgn_r._length; i>0; --i) {
					_mrgn_r._prev = new TapeNode(TapeConst.TC_X, _mrgn_r._prev, _mrgn_r);
				}
				_read = _mrgn_r._prev;
				_mrgn_r._length = 1;
			}
			_read._value = v;
		}
	}
	
	public TapeConst read() { return _read._value; }
	
	public Iterator iterator() { return new Iterator(_read); }
	
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