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


/***********************
   public enumeration   
***********************/
public enum TapeConst {
	TC_0(0), TC_1(1), TC_X(2);
	
	private int _value;
	
	private TapeConst(int v) { _value = v; }
	
	public int toInt() { return _value; }
}