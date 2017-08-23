/******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2013/3/4
  VERSION : v1.0
  ---
    [class Movie] is actually the class designed to schedule actions and
execute them automatically. These actions may adjust some parameters that
control the look of some component shown on the screen, so one should provide
the target that would be updated by the object of [class Movie].

    To use this class, you can construct objects compatible to [class
MoviePeriod], overriding [void broadcast(int tick)] to change the status
of the component. The function [void ending()] is provided for some special
needs, for instance, some re-initialization.

    One can use [void addPeriod(MoviePeriod mp)] to add a period into the movie
object. Notice that if one feeds it with [null], nothing will be appended.

    To add periods dynamically, one way provided is to use a class implements
[interface PeriodProvider]. The interface has no method but [MoviePeriod
nextMovie()], called when any period is end. This function should return an
object compatible to [class MoviePeriod], which will be appended to the current
movie object. Returning null may cause the movie to terminate.

Function list:

class Movie:
  * (constructor) Movie(Component comp,
                        int timePerFrame,
                        PeriodProvider provider)

        comp         : [ target updated ]
        timePerFrame : [ specifying the time between frames ]
                       [ default: 10 ]
        provider     : [ object that appends movie periods to this movie ]
                       [ default: null ]

  * void addPeriod(MoviePeriod mp)
        Append mp to the movie

  * void start()
        Start playing the movie

  * void shut()
        Terminate the current movie period

  * void terminate(boolean doEnding)
        Terminate the whole movie

  * void pause()
        Pause the movie. Able to continue by calling proceed().

  * void proceed()
        Continue the paused movie.

  * boolean isPlaying()
        Return if the movie is playing.
******************************************************************************/


/************
   package   
************/
package pedeslib.movie;


/************
   import    
************/
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;


/*****************
   public class   
*****************/
public class Movie {
	// fields
	private int         _dt;
	private boolean     _playing;
	private SeriesTimer _now;
	private SeriesTimer _last;
	private Component   _comp;

	PeriodProvider      _periodProvider;
	// fields end


	// nested classes
	private class SeriesTimer extends Timer {
		SeriesTimer _next;
		MoviePeriod _period;
		
		public SeriesTimer(MoviePeriod mp) {
			super(_dt, new Period(mp));
			_period = mp;
		}

		public final void begin() { super.start(); }
		public final void end() {
			super.stop();
			if (_periodProvider != null) addPeriod(_periodProvider.nextMovie());
			if (_next != null) {
				_next.start();
			} else {
				_playing = false;
				_comp.repaint();
			}
		}
	}
	
	private class Period implements ActionListener {
		MoviePeriod _period;

		protected int _timer;
		protected int _margin_timer;

		public Period(MoviePeriod mp) {
			_period = mp;
			_timer = _margin_timer = 0;
		}

		public final void actionPerformed(ActionEvent e) {
			if (_timer < _period.getFramesCount()) {
				_period.broadcast(_timer++);
			} else if (_margin_timer < _period.getMarginFramesCount()) {
				++_margin_timer;
			} else {
				_period.ending();
				_now.end();
				_now = _now._next;
				_timer = _margin_timer = 0;
			}
			_comp.revalidate();
			_comp.repaint();
		}
	}
	// nested classes end


	// constructors
	public Movie(Component comp) {
		this(comp, 10, null);
	}

	public Movie(Component comp, PeriodProvider provider) {
		this(comp, 10, provider);
	}

	public Movie(Component comp, int timePerFrame) {
		this(comp, timePerFrame, null);
	}

	public Movie(Component comp, int timePerFrame, PeriodProvider provider) {
		_playing = false;
		_periodProvider = provider;
		_comp = comp;
		_dt = timePerFrame;
	}
	// constructors end


	// static methods
	// static methods end


	// methods
	public void addPeriod(MoviePeriod mp) {
		if (mp == null) return;
		if (_now == null) {
			_now = _last = new SeriesTimer(mp);
		} else {
			_last = _last._next = new SeriesTimer(mp);
		}
	}

	public void start() {
		if (_playing == true) return;
		if (_now == null) {
			if (_periodProvider == null) return;
			addPeriod(_periodProvider.nextMovie());
		}
		if (_now == null) return;
		_playing = true;
		_now.begin();
	}

	public void shut() { if (_playing == true) _now.end(); }

	public void terminate(boolean doEnding) {
		if (_playing == false) return;
		_now.stop();
		if (doEnding) _now._period.ending();
		_comp.revalidate();
		_comp.repaint();
		_playing = false;
		_now = null;
	}

	public void pause() { _now.stop(); }

	public void proceed() { _now.start(); }

	public boolean isPlaying() { return _playing; }
	// methods end
}