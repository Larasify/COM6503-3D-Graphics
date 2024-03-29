import gmaths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
/* I declare that this code is my own work */
/* Author Eylul Lara Cikis elcikis1@sheffield.ac.uk lara.cikis@gmail.com */
  
public class Hatch extends JFrame implements ActionListener {
  
  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;
  private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
  private GLCanvas canvas;
  private M04_GLEventListener glEventListener;
  private final FPSAnimator animator; 
  private Camera camera;

  public static void main(String[] args) {
    Hatch b1 = new Hatch("acp22elc Lara Cikis 3D Graphics Assignment");
    b1.getContentPane().setPreferredSize(dimension);
    b1.pack();
    b1.setVisible(true);
  }

  public Hatch(String textForTitleBar) {
    super(textForTitleBar);
    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
    canvas = new GLCanvas(glcapabilities);
    camera = new Camera(Camera.DEFAULT_POSITION, Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
    glEventListener = new M04_GLEventListener(camera);
    canvas.addGLEventListener(glEventListener);
    canvas.addMouseMotionListener(new MyMouseInput(camera));
    canvas.addKeyListener(new MyKeyboardInput(camera));
    getContentPane().add(canvas, BorderLayout.CENTER);
    
    JMenuBar menuBar=new JMenuBar();
    this.setJMenuBar(menuBar);
      JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
    menuBar.add(fileMenu);
    
    JPanel p = new JPanel(new GridLayout(2, 1));
      JButton b = new JButton("lamp1 position1");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("lamp1 position2");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("lamp1 position3");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("lamp2 position1");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("lamp2 position2");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("lamp2 position3");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("toggle spotlight 1");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("toggle spotlight 2");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("toggle world light 1");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("toggle world light 2");
      b.addActionListener(this);
      p.add(b);
    this.add(p, BorderLayout.SOUTH);
    
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        animator.stop();
        remove(canvas);
        dispose();
        System.exit(0);
      }
    });
    animator = new FPSAnimator(canvas, 60);
    animator.start();
  }
  
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equalsIgnoreCase("lamp1 position1")) {
      glEventListener.lamp1position1();
    }
    else if (e.getActionCommand().equalsIgnoreCase("lamp1 position2")) {
      glEventListener.lamp1position2();
    }
    else if (e.getActionCommand().equalsIgnoreCase("lamp1 position3")) {
      glEventListener.lamp1position3();
    }
    else if (e.getActionCommand().equalsIgnoreCase("lamp2 position1")) {
      glEventListener.lamp2position1();
    }
    else if (e.getActionCommand().equalsIgnoreCase("lamp2 position2")) {
      glEventListener.lamp2position2();
    }
    if (e.getActionCommand().equalsIgnoreCase("lamp2 position3")) {
      glEventListener.lamp2position3();
    }
    else if (e.getActionCommand().equalsIgnoreCase("toggle spotlight 1")) {
      glEventListener.togglespotlight1();
    }
    else if (e.getActionCommand().equalsIgnoreCase("toggle spotlight 2")) {
      glEventListener.togglespotlight2();
    }
    else if (e.getActionCommand().equalsIgnoreCase("toggle world light 1")) {
      glEventListener.toggleworldlight1();
    }
    else if (e.getActionCommand().equalsIgnoreCase("toggle world light 2")) {
      glEventListener.toggleworldlight2();
    }
    else if(e.getActionCommand().equalsIgnoreCase("quit"))
      System.exit(0);
  }
  
}
 
class MyKeyboardInput extends KeyAdapter  {
  private Camera camera;
  
  public MyKeyboardInput(Camera camera) {
    this.camera = camera;
  }
  
  public void keyPressed(KeyEvent e) {
    Camera.Movement m = Camera.Movement.NO_MOVEMENT;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
      case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
      case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
      case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
      case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
      case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
    }
    camera.keyboardInput(m);
  }
}

class MyMouseInput extends MouseMotionAdapter {
  private Point lastpoint;
  private Camera camera;
  
  public MyMouseInput(Camera camera) {
    this.camera = camera;
  }
  
    /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */    
  public void mouseDragged(MouseEvent e) {
    Point ms = e.getPoint();
    float sensitivity = 0.001f;
    float dx=(float) (ms.x-lastpoint.x)*sensitivity;
    float dy=(float) (ms.y-lastpoint.y)*sensitivity;
    //System.out.println("dy,dy: "+dx+","+dy);
    if (e.getModifiers()==MouseEvent.BUTTON1_MASK)
      camera.updateYawPitch(dx, -dy);
    lastpoint = ms;
  }

  /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */  
  public void mouseMoved(MouseEvent e) {   
    lastpoint = e.getPoint(); 
  }
}