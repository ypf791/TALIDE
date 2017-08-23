/******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2015/7/11
  VERSION : v1.0
  ---
******************************************************************************/


/************
   package   
************/
package talide.core;


/************
   import    
************/


/*********************
   public interface   
*********************/
public interface Pressable {
	// methods
	void press();
	
	void release(boolean confirm); // confirm==true if rls_obj==prs_obj
	// methods end
}