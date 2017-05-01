package objects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

// TODO Javadoc

public class ImagePanel extends JPanel {
	
	final static int FILL = 0;
	final static int FIT = 1;
	
	final static int LEFT = 0;
	final static int CENTER = 1;
	final static int RIGHT = 2;
	
	private BufferedImage image = null;
	
	private Image imageNoImage;
	
	private int displayType;
	
	public ImagePanel(Image imageNoImage, int imageDisplay){
		
		super();
		
		this.displayType = imageDisplay;
		
	}
	
	public ImagePanel(Image imageNoImage){
		
		this(imageNoImage, FILL);
		
		this.imageNoImage = imageNoImage;
		
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public void setImage(BufferedImage image){
		this.image = image;
	}
	
	protected void drawImage(Graphics g){
		
		switch(displayType){
		case FILL:
			
			if(image == null){
				g.drawImage(imageNoImage, 0, 0, getWidth(), getHeight(), this);
			}
			else{
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
			
			break;
		
		case FIT:
			
			// TODO Program a method to scale the image appropriatly.
			
			break;
		}
		
	}
	
	@Override
	protected void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		drawImage(g);
		
	}
	
}
