/*******************************************************************************
  AUTHOR  : pedeschen
  DATE    : 2015/6/7
  VERSION : v1.0
  ---
  TALIDE:
  Turing Assembly Language IDE
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

import talide.function.*;
import talide.core.*;


/***************
   main class   
***************/
public class Talide extends JFrame {
	// static fields
	public static Talide _instance;
	
	public static Image _img_slot[];
	public static Image _img_slot_p[];
	public static Image _img_tape[];
	public static Image _img_env[];
	
	public static Func _func_new;
	public static Func _func_open;
	public static Func _func_test;
	// static fields end


	// fields
	public ProjectPane  _prjPane;
	public DevelopPane  _dvpPane;
	public JToolBar     _toolBar;
	public JMenuBar     _menuBar;
	// fields end


	// constructors
	public Talide() {
		super("Talide");
		
		//Instantiate compenents
		_prjPane = new ProjectPane();
		_dvpPane = new DevelopPane();
		_toolBar = new MyToolBar();
		_menuBar = new MyMenuBar();
		
		//Adjust non-customized components
		_toolBar.setFloatable(false);
		
		//Declare container of the frame
		Container cp = getContentPane();
		
		//Define Layout Manager
		cp.setLayout(new BorderLayout());
		
		//Add components
		JSplitPane sp = new JSplitPane();
		sp.setLeftComponent(_prjPane);
		sp.setRightComponent(_dvpPane);
		cp.add(sp, BorderLayout.CENTER);
		cp.add(_toolBar, BorderLayout.NORTH);
		setJMenuBar(_menuBar);
		
		//Set the dimension of the frame
		pack();
		
		//Center the frame
		Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension thisSize = getSize();
		if (thisSize.height > scrnSize.height) thisSize.height = scrnSize.height;
		if (thisSize.width  > scrnSize.width ) thisSize.width  = scrnSize.width;
		setLocation(
			(scrnSize.width  - thisSize.width ) / 2,
			(scrnSize.height - thisSize.height) / 2
		);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	// constructors end


	// static methods
	private static void loadResources() {
		ClassLoader cl = Talide.class.getClassLoader();
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		//Loading images
		String fname_slot[] = new String[] {
			"emp/emp",
			"if/if_1_down",   "if/if_1_up",     "if/if_x_down",   "if/if_x_up",
			"loop/loop_2",    "loop/loop_3",    "loop/loop_4",
			"ovrd/ovrd_0",    "ovrd/ovrd_1",    "ovrd/ovrd_x",
			"sft/sft_l",      "sft/sft_r"
		};
		String fname_slot_p[] = new String[] {
			"if_p/if_0_down", "if_p/if_1_down", "if_p/if_x_down",
			"if_p/if_0_up",   "if_p/if_1_up",   "if_p/if_x_up",
			"loop_p/loop_2",  "loop_p/loop_3",  "loop_p/loop_4",
			"ovrd_p/ovrd_0",  "ovrd_p/ovrd_1",
			"sft_p/sft_l",    "sft_p/sft_r"
		};

		_img_env = new Image[] {
			tk.createImage(cl.getResource("img/env/brg_col.gif")),
			tk.createImage(cl.getResource("img/env/brg_loop.gif")),
			tk.createImage(cl.getResource("img/env/brg_loop_end.gif")),
			tk.createImage(cl.getResource("img/env/row_end.png")),
			tk.createImage(cl.getResource("img/env/start.png")),
			tk.createImage(cl.getResource("img/env/start_press.png")),
			tk.createImage(cl.getResource("img/env/start_run.png"))
		};
		_img_tape = new Image[] {
			tk.createImage(cl.getResource("img/tape/0.png")),
			tk.createImage(cl.getResource("img/tape/1.png")),
			tk.createImage(cl.getResource("img/tape/x.png")),
			tk.createImage(cl.getResource("img/tape/end_l.png")),
			tk.createImage(cl.getResource("img/tape/end_r.png")),
			tk.createImage(cl.getResource("img/tape/frame.png")),
			tk.createImage(cl.getResource("img/tape/shade_l.png")),
			tk.createImage(cl.getResource("img/tape/shade_r.png")),
			tk.createImage(cl.getResource("img/tape/shade_b.png"))
		};
		_img_slot = new Image[26];
		for (int i=0; i<13; ++i) {
			String pre = "img/slot/" + fname_slot[i];
			_img_slot[2*i]   = tk.createImage(cl.getResource(pre + ".png"));
			_img_slot[2*i+1] = tk.createImage(cl.getResource(pre + "_run.png"));
		}
		_img_slot_p = new Image[52];
		for (int i=0; i<13; ++i) {
			String pre = "img/slot/" + fname_slot_p[i];
			_img_slot_p[4*i]   = tk.createImage(cl.getResource(pre + ".png"));
			_img_slot_p[4*i+1] = tk.createImage(cl.getResource(pre + "_run.png"));
			_img_slot_p[4*i+2] = tk.createImage(cl.getResource(pre + "_set.png"));
			_img_slot_p[4*i+3] = tk.createImage(cl.getResource(pre + "_press.png"));
		}
		//End loading images
	}
	
	private static void instantiateFuncs() {
		_func_new  = new FuncNew();
		_func_open = new FuncOpen();
		_func_test = new FuncTest();
	}
	
	public static JFrame getInstance() {
		return _instance;
	}
	// static methods end


	// methods
	public void showExecFrame(Tape tape, Circuit circuit) {
		new ExecFrame(tape, circuit);
	}
	// methods end


	// main method
	public static void main(String args[]) {
		UIManager.put("swing.nimbus", Boolean.FALSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		Toolkit.getDefaultToolkit().setDynamicLayout(true);

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		loadResources();
		instantiateFuncs();
		_instance = new Talide();
		Func.setTalideInstance(_instance);
	}
	// main method end
}