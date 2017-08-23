/*******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2015/6/20
  VERSION : v1.0
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
public class Circuit implements Cloneable {
	// fields
	private final int _row_N;
	private final int _col_N;
	private final int _start_row;
	
	private int _row_now;
	private int _col_now;
	
	private Slot[][] _slot;
	
	protected NextToExec _bufferedNTE;
	// fields end


	// constructors
	public Circuit(String str) {
		/****************************
		  Test circuit:
		  0[if0+,ifx+,ovrd0,sh-,j4]
		  1[emp,ovrd1,sh+,ifx-,j2]
		****************************/
		_row_N = 2;
		_col_N = 5;
		_slot = new Slot[][] {
			new Slot[] {
				Slot.parseSlot("if:0:+"),
				Slot.parseSlot("if:x:+"),
				Slot.parseSlot("ovrd:0"),
				Slot.parseSlot("sh:-"),
				Slot.parseSlot("j:-4")
			},
			new Slot[] {
				Slot.parseSlot("emp"),
				Slot.parseSlot("ovrd:1"),
				Slot.parseSlot("sh:+"),
				Slot.parseSlot("if:x:-"),
				Slot.parseSlot("j:-2")
			}
		};
		reset();
		_start_row = 0;
	}
	
	public Circuit() {
		_start_row = _row_N = _col_N = 0;
		reset();
	}
	// constructors end


	// nested classes
	public class Iterator {
		// fields
		private int _idx;
		private final int _mod;
		private final int _max;
		private Slot _value;
		// fields end
		
		
		// constructors
		public Iterator() {
			_idx = 0;
			_mod = _col_N;
			_max = _col_N * _row_N;
			_value = _slot[0][0];
		}
		// constructors end
		
		
		// methods
		public void decrease() {
			if (--_idx<0) {
				_value = null;
			} else {
				_value = _slot[_idx/_mod][_idx%_mod];
			}
		}
		
		public void increase() {
			if (++_idx>=_max) {
				_value = null;
			} else {
				_value = _slot[_idx/_mod][_idx%_mod];
			}
		}
		
		public Slot val() { return _value; }
		
		public boolean isExecuting() {
			int r = _idx / _mod;
			int c = _idx % _mod;
			return (r==_row_now && c==_col_now);
		}
		// methods end
	}
	// nested classes end
	
	
	// methods
	public Circuit clone() {
		return this;
	}
	
	public void reset() {
		_row_now = 0;
		_col_now = -1;
		_bufferedNTE = Slot._defaultNTE;
	}
	
	public ExecResult execOneSlot(TapeConst tc) {
		_row_now += _bufferedNTE._diff_row;
		_col_now += _bufferedNTE._diff_col;
		_bufferedNTE = _slot[_row_now][_col_now].exec(tc);
		return _bufferedNTE._result;
	}
	
	public Iterator iterator() { return new Iterator(); }
	
	public int getWidth() { return _col_N; }
	
	public int getHeight() { return _row_N; }
	
	public int getStartRow() { return _start_row; }
	
	public Slot getSlot(int r, int c) {
		try {
			return _slot[r][c];
		} catch (ArrayIndexOutOfBoundsException ex) {
			return null;
		}
	}
	// methods end
}
