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
	private int _row_N;
	private int _col_N;
	private int _start_row;
	private Slot[][] _slot;
	protected int _row_now;
	protected int _col_now;
	protected NextToExec _bufferedNTE;
	// fields end


	// constructors
	public Circuit(String str) {
		this();
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
	}
	
	public Circuit() {
		_start_row = 0;
		_row_now = 0;
		_col_now = -1;
		_bufferedNTE = Slot._defaultNTE;
	}
	// constructors end


	// methods
	public Circuit clone() {
		return this;
	}
	
	public ExecResult execOneSlot(TapeConst tc) {
		_row_now += _bufferedNTE._diff_row;
		_col_now += _bufferedNTE._diff_col;
		_bufferedNTE = _slot[_row_now][_col_now].exec(tc);
		return _bufferedNTE._result;
	}
	// methods end
}