/******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2013/3/4
  VERSION : v1.0
******************************************************************************/


/************
   package   
************/
package pedeslib.movie;


/************
   import    
************/


/*****************
   public class   
*****************/
public abstract class MoviePeriod {
	// fields
	protected int _N;
	protected int _margin_N;
	// fields end


	// constructors
	public MoviePeriod() { this(20, 10); }
	public MoviePeriod(int framesCount, int marginFramesCount) {
		_N = framesCount;
		_margin_N = marginFramesCount;
	}
	// constructors end


	// methods
	public final int getFramesCount() { return _N; }
	public final int getMarginFramesCount() { return _margin_N; }

	public abstract void broadcast(int tick);
	public void ending() {}
	// methods end
}