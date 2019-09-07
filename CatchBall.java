import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.applet.*;
import java.awt.event.*;
import java.util.Random;
import java.net.URL;
class CatchBall extends JFrame
{
 JLabel la=new JLabel(new ImageIcon(getClass().getResource("images/background.jpg")));
 AudioClip clip1,clip2;
 JLabel bar=new JLabel();
 int sw,sh,nb,sb,wb,eb,count1,count2,barx,bary,ballx=100,bally,totalball,barw=80,barh=30,x,y,remainingball,th,px,py;
 JLabel []ball;
 JLabel hit=new JLabel("Hit : "+ count1);
 JLabel miss=new JLabel("Miss : "+ count2);
 Random ran=new Random();
 boolean started=false,escape=false,pause=false; 
 JLabel activeball; 
 JLabel startpanel=new JLabel();
 BarListener barlistener=new BarListener();
 final JCheckBox cb;
 ThrowBall thread;
 public CatchBall()
 {
  	super("bouncing ball");
  	loadSound();
  	Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
  	sw=d.width;sh=d.height;sb=sh-38;eb=sw-18;
  	setSize(sw,sh);
  	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
  	barx=(sw-barw)/2;
  	bary=sb-110;
  	la.setLayout(null);
	cb=new JCheckBox("MUTE");
  	addStartPanel();
  	addScore();
  	addfire();	
  	addBar();
  	addBall();
	addPause();
  	addKeyListener(new MoveBarListener());
  	setVisible(true);
 }
 private void addStartPanel()
 {
   	px=(sw-700)/2;
   	py=(sh-550)/2;
	startpanel.setBorder(BorderFactory.createLineBorder(Color.green,5,true));
   	startpanel.setBounds(px,py,700,550);
   	startpanel.setBackground(new Color(100,250,98));
   	startpanel.setLayout(new BorderLayout());
   	startpanel.add(new JLabel(new ImageIcon(getClass().getResource("images/start3.jfif"))));
   	la.add(startpanel);
   	addPanelButton();	
   	addMute();
 }
 void addPanelButton()
 {
	JPanel p1=new JPanel();
	startpanel.add(p1,"North");
	JButton [] bt=new JButton[2];
	String [] str={"START GAME","EXIT"};
	for(int i=0;i<bt.length;i++)
	{
	  bt[i]=new JButton(str[i]);	
	  bt[i].setFocusable(false);
	  bt[i].setForeground(Color.red);
	  bt[i].setBackground(Color.yellow);
	  bt[i].setFont(new Font("arial",1,23));
	  p1.add(bt[i]);
	}
	bt[0].addActionListener(new ActionListener()
	{
	  public void actionPerformed(ActionEvent evt)
	  {
		startpanel.setVisible(false);
		escape=false;
		started=true;
		addMouseMotionListener(barlistener);
		hideCursor(true);
		thread=new ThrowBall();thread.start();
    	  }
	});
	bt[1].setForeground(Color.yellow);
	bt[1].setBackground(Color.red);
	bt[1].addActionListener(new ActionListener()
	{
	  public void actionPerformed(ActionEvent evt)
	  {
		System.exit(0);
    	  }
	});
 }
 private void addMute()
 {
	JPanel p2=new JPanel();
	startpanel.add(p2,"South");
	cb.setFocusable(false);
	cb.setFont(new Font("cooper",1,25));
	cb.setForeground(Color.blue);
        p2.add(cb);
	cb.setSelected(true);
        cb.addActionListener(new ActionListener()
	{
	  public void actionPerformed(ActionEvent evt)
	  {
		if(cb.isSelected())
		  clip2.stop();
		else
		  clip2.loop();	
	  }
	});
 }
 private void addPause()
 {
	JLabel lapause=new JLabel("<html><h2 style='color:white;font-size:18'>Press P to pause or unpause game</h2></html>");
	lapause.setBounds(px+10,py+10,400,40);
	la.add(lapause);

	JLabel laescape=new JLabel("<html><h2 style='color:white;font-size:18'>Press Esc to restart or exit</h2></html>");
	laescape.setBounds(px+420,py+10,400,40);
	la.add(laescape);
 } 
 private void addBar()
 {
  	bar.setBounds(barx,bary,barw,barh);
  	bar.setBorder(BorderFactory.createLineBorder(Color.yellow));
  	bar.setOpaque(true);
  	bar.setBackground(Color.red);
  	la.add(bar);
 }
 private void addScore()
 {
  	int sx=(sw-600)/2;	
  	hit.setBounds(sx,250,300,100);
  	miss.setBounds(sx+300,250,300,100);
  	hit.setForeground(Color.green);
  	miss.setForeground(Color.red);
  	hit.setFont(new Font("jokerman",1,50));
  	miss.setFont(new Font("jokerman",1,50));
  	this.add(la);
  	la.add(hit);la.add(miss);
 }
 private void addfire()
 {
    for(int c=0;c<=sw;c+=45) 
    {	
	JLabel fire=new JLabel(new ImageIcon(getClass().getResource("images/fire.gif")));
	fire.setBounds(c,sb-78,45,78);
	la.add(fire);
    }
 }
 private void loadSound()
 {
   try
   {
       clip1=Applet.newAudioClip(getClass().getResource("/sound/SOUND136.wav"));
       clip2=Applet.newAudioClip(getClass().getResource("/sound/intro.mid"));
   }catch(Exception ex){}
 } 
 private void addBall()
 {
  totalBall();
  ball=new JLabel[totalball];
  for(int i=0;i<ball.length;i++)
  {
	ball[i]=new JLabel(new ImageIcon(getClass().getResource("images/ball.png")));
	ball[i].setBounds(ballx+=30,bally,30,30);
	la.add(ball[i]);
  }	
 } 
 private void totalBall()
 {
	int x=eb-200;
	totalball=x/30;
	remainingball=totalball;
	th=totalball/2;
 } 
 class MoveBarListener extends KeyAdapter
 {
  @SuppressWarnings({"unchecked","deprecation"})
  public void keyPressed(KeyEvent evt) 
  {
	int kc=evt.getKeyCode();
	if(kc==KeyEvent.VK_P && started && !pause)
        {
	  thread.suspend();
	  removeMouseMotionListener(barlistener);
	  pause=true;
        }
	else if(started && pause)
        {
	  thread.resume();
	  addMouseMotionListener(barlistener);
	  pause=false;
        }  
	if(kc==KeyEvent.VK_ESCAPE && !escape)
	{
	  thread.suspend();
          int option=JOptionPane.showConfirmDialog(CatchBall.this,"Are you sure?");	
	  if(option==JOptionPane.YES_OPTION)
	  {
	   	resetGame();
	  }
	  else
	  {
	        thread.resume();	
	  }
	}
  }
 }
 void resetGame()
 {
	escape=true;
	startpanel.setVisible(true);
	la.remove(activeball);
	count1=count2=0;
	ballx=100;
	for(int i=0;i<ball.length;i++)
	{
	  if(ball[i]==null)
	  {
	    ball[i]=new JLabel(new ImageIcon(getClass().getResource("images/ball.png")));
	    la.add(ball[i]);
	  }
	  ball[i].setBounds(ballx+=30,0,30,30);
	}
  	clip2.stop();
	cb.setSelected(true);
	hit.setText("Hit : "+ count1);
	miss.setText("Miss : "+ count1);
	removeMouseMotionListener(barlistener);
	barx=(sw-barw)/2;
  	bary=sb-110;
	remainingball=totalball;
	started=false;
	hideCursor(false);
 }
 class BarListener extends MouseMotionAdapter
 {
   public void mouseMoved(MouseEvent evt)
   {
	barx=evt.getX();
	bar.setBounds(barx,bary,barw,barh);
   }
 }
 class ThrowBall extends Thread
 {
  public void run()
  {
    boolean dirright=false;
    if(remainingball==0)
    {
     JOptionPane.showMessageDialog(CatchBall.this,"Gave Over");	
     resetGame();
     return;
    }
    int balli=0;
    do
    {
      balli=new java.util.Random().nextInt(totalball);
    }while(ball[balli]==null);
    activeball=ball[balli];
    ball[balli]=null;
    x=activeball.getX();	
    y=activeball.getY();
    remainingball--;
    dirright=(balli<=th)?true:false;
    while(true)
    {
	if(escape)
        {
	  la.remove(activeball);
	  break;
	}
	else if(dirright)
        {
	  x+=2;
	  if(x>=eb-30)
	  {
		dirright=false;
          }
        }
	else if(!dirright)
        {
	  x-=2;
	  if(x<=wb)
	  {
		dirright=true;
          }
        }
	y+=3;
	activeball.setBounds(x,y,30,30);
	try{
	  Thread.sleep(3);
	}catch(Exception ex){}
	if(isCollision())
        {
	  clip1.play();
	  hit.setText("Hit : "+ (++count1));
	  activeball.setVisible(false);
	  thread=new ThrowBall();thread.start();
	  return;
        } 
	else if(y>=sb)	
	{
	 activeball.setVisible(false);
	 miss.setText("Miss : "+ (++count2));
	 thread=new ThrowBall();thread.start();
	 return;
	}
    }   	
  }   	
 } 
 boolean isCollision()
 {
   int c=x-barx;
   int d=bary-y;
   if((d>=25 && d<=29) && (c>=-31 && c<=barw))
    return true;
   return false;
 } 
 private void hideCursor(boolean st)
 {
   if(st)
   {
	 BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	 Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
    	 cursorImg, new Point(0, 0), "blank cursor");
	 this.getContentPane().setCursor(blankCursor);
   }
   else
     this.getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
 }
 public static void main(String args[])
 {
	JFrame.setDefaultLookAndFeelDecorated(true);
        new CatchBall();
 } 
} 
