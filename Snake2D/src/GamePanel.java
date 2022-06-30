import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener ,KeyListener{
	
	//snake images 
	private ImageIcon snakeTitle= new ImageIcon("snaketitle.jpg");
	private ImageIcon leftMouth= new ImageIcon("leftmouth.png");
	private ImageIcon rightMouth= new ImageIcon("rightmouth.png");
	private ImageIcon upMouth= new ImageIcon("upmouth.png");
	private ImageIcon downMouth= new ImageIcon("downmouth.png");
	private ImageIcon snakeImage= new ImageIcon("snakeimage.png");
	private ImageIcon foodImage= new ImageIcon("enemy.png");
	
	//making score variable to count the score
	private int score =0;
	//giving positions to the food of the snake
	private int[] posX= {75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,
			575,600,625,650,675,700,725,750,775};
	
	private int[] posY= {100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500};
	
	//using random class to call random position for food
	private Random random=new Random();
	private int foodX,foodY;
	
	private int[] snakeXLength = new int[750];
	private int[] snakeYLength = new int[750];
	private int lengthOfSnake = 3;
	
	//snake directions
	//initially snake's head will be in right side so right is true
	// and others are false
	private boolean left = false;
	private boolean right = true;
	private boolean up = false;
	private boolean down = false;
	
	//initially moves are zero
	private int moves=0;
	//game over variable
	private boolean gameOver=false;
	
	//adding timer
	private Timer timer;
	private int delay=100;
	
	//Constructor
	GamePanel(){
		
		//without this keylistener methods will not work
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(true);
		
		//this timer helps in moving the snake
		timer =new Timer(delay,this);
		timer.start();
		snakefood();
	}
	
	//method to call snake food random on the gamepanel
	private void snakefood() {
		// TODO Auto-generated method stub
		foodX=posX[random.nextInt(29)];
		foodY=posY[random.nextInt(17)];
		
		//we don't want to spawn food on snake's body
		for(int i=lengthOfSnake-1;i>=0;i--) {
			if(snakeXLength[i]==foodX && snakeYLength[i]==foodY) {
				snakefood();
			}
		}
		
	}
	public void paint(Graphics g) {
		super.paint(g);
		
		g.setColor(Color.white);
		g.drawRect(20, 10, 851, 55);
		g.drawRect(20, 74, 851, 576);
		
		snakeTitle.paintIcon(this, g, 21, 11);
		
		g.setColor(Color.black);
		g.fillRect(21, 75, 850, 575);
		//initial position of snake in gamepanel
		if(moves==0) {
			snakeXLength[0]=100;
			snakeXLength[1]=75;
			snakeXLength[2]=50;
			
			snakeYLength[0]=100;
			snakeYLength[1]=100;
			snakeYLength[2]=100;
			
			
		}
		//these if conditions below only print the head of the snake
		//if snake is facing left direction
		if(left) {
			leftMouth.paintIcon(this, g, snakeXLength[0], snakeYLength[0]);
		}
		//if snake is facing right direction
		if(right) {
			rightMouth.paintIcon(this, g, snakeXLength[0], snakeYLength[0]);
		}
		//if snake is facing upward direction
		if(up) {
			upMouth.paintIcon(this, g, snakeXLength[0], snakeYLength[0]);
		}
		//if snake is facing downward direction
		if(down) {
			downMouth.paintIcon(this, g, snakeXLength[0], snakeYLength[0]);
		}
		
		//Adding snake body in gamepanel
		for(int i=1;i<lengthOfSnake;i++) {
			snakeImage.paintIcon(this, g, snakeXLength[i], snakeYLength[i]);
		}
		//printing food image in panel
		foodImage.paintIcon(this, g, foodX, foodY);
		
		//if gameOver we will print game over 
		if(gameOver) {
			g.setColor(Color.white);
			g.setFont(new Font("Arial",Font.BOLD,50));
			g.drawString("Game Over", 300, 300);
			
			g.setFont(new Font("Arial",Font.PLAIN,20));
			g.drawString("Press SPACE to Restart", 320, 350);
		}
		
		//displaying score and length of snake in top right corner
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.PLAIN,14));
		g.drawString("Score: "+score, 750, 30);
		g.drawString("Lenght: "+lengthOfSnake,750, 50);
		
		g.dispose();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for(int i=lengthOfSnake-1;i>0;i--) {
			snakeXLength[i]=snakeXLength[i-1];
			snakeYLength[i]=snakeYLength[i-1];
		}
		//here we are reducing the frame in x and y plane
		//for example if snake goes into right direction that means its distance is increasing
		//from x axis.
		//if snake goes into upward direction that means its distance from y plane is decreasing
		if(left) {
			snakeXLength[0]=snakeXLength[0]-25;
		}
		if(right)
			snakeXLength[0]=snakeXLength[0]+25;
		if(up)
			snakeYLength[0]=snakeYLength[0]-25;
		if(down)
			snakeYLength[0]=snakeYLength[0]+25;
		
		//if the snake head will touch the length of 851 of the gamepanel
		// then its position will become 25 in gamepanel and vice versa
		//in short its what doing is if snake goes into right and then it will 
		//come from left direction
		//this is for condition only left to right
		if(snakeXLength[0]>851)
			snakeXLength[0]=21;
		if(snakeXLength[0]<21)
			snakeXLength[0]=851;
		
		//for y axis if snake goes into downward and upward direction
		if(snakeYLength[0]>576)
			snakeYLength[0]=74;
		if(snakeYLength[0]<74)
			snakeYLength[0]=576;
		
		//calling method of eating snake food
		eatingFood();
		
		//game over by colliding snake head with its body
		gameOver();
		
		repaint();
	}
	
	
	private void gameOver() {
		// TODO Auto-generated method stub
		for(int i=lengthOfSnake-1;i>0;i--) {
			//note here i > 0 zero not >= 0 because 
			//if i will be >= zero 
			//then its heads position will be equal to its head position
			//and then game will not start it will say game over
			//if position of head equals with position of any body length of snake
			if(snakeXLength[i]==snakeXLength[0] && snakeYLength[i]==snakeYLength[0]) {
				timer.stop();
				gameOver=true;
				
			}
				
		}
	}

	private void eatingFood() {
		// TODO Auto-generated method stub
		if(snakeXLength[0]==foodX && snakeYLength[0]==foodY) {
			snakefood();
			lengthOfSnake++;
			score++;
		}
		
	}

	@Override
	//this is used for Key binding
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//if left key is pressed then left will be true and others will be false
		
		//left key only worked if the snake current direction is not right
		if(e.getKeyCode()==KeyEvent.VK_LEFT && (!right)) {
			left=true;
			right=false;
			up=false;
			down=false;
			//snake only start moving when we press any directional key
			moves++;
		}
		//if right key is pressed (right = true, others=false)
		//right only work if snake current direction is not left
		if(e.getKeyCode()==KeyEvent.VK_RIGHT && (!left)) {
			left=false;
			right=true;
			up=false;
			down=false;
			moves++;
		}
		//if up key is pressed (up=true,others=false)
		//up only work if the current direction of snake is not down
		if(e.getKeyCode()==KeyEvent.VK_UP && (!down)) {
			left=false;
			right=false;
			up=true;
			down=false;
			moves++;
		}
		//if down key is pressed (down=true,others=false)
		//down is only work is the current direction of snake is not up
		if(e.getKeyCode()==KeyEvent.VK_DOWN && (!up)) {
			left=false;
			right=false;
			up=false;
			down=true;
			moves++;
		}
		//Press space to restart
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			restart();
		}
		
	}
	private void restart() {
		// TODO Auto-generated method stub
		gameOver=false;
		moves=0;
		score=0;
		lengthOfSnake=3;
		right=true;
		left=false;
		up=false;
		down=false;
		snakefood();
		timer.start();
		repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
