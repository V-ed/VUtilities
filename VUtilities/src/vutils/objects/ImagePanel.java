package vutils.objects;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import vutils.tools.ToolFiles;

// TODO Javadoc

public class ImagePanel extends JPanel {
	
	public final static int FILL = 0;
	public final static int FIT = 1;
	public final static int FIXED = 2;
	
	public final static int WEST = 0;
	public final static int NORTH = 0;
	public final static int CENTER = 1;
	public final static int EAST = 2;
	public final static int SOUTH = 2;
	
	private Image image = null;
	
	private int displayType;
	private int posX = CENTER;
	private int posY = CENTER;
	
	public ImagePanel(Image image, int imageDisplay){
		
		super();
		
		if(imageDisplay < WEST || imageDisplay > EAST)
			throw new IllegalArgumentException(
					"The imageDisplay parameter cannot be outside of the range "
							+ WEST + " <= imageDisplay <= " + EAST + ".");
		
		this.image = image;
		
		this.displayType = imageDisplay;
		
		repaint();
		
	}
	
	public ImagePanel(Image image){
		
		this(image, FILL);
		
	}
	
	public ImagePanel(int imageDisplay){
		
		this(ToolFiles.getImageFromResources("../images/no_image_icon.png"),
				imageDisplay);
		
	}
	
	public ImagePanel(){
		
		this(FILL);
		
	}
	
	protected void drawImage(Graphics g){
		
		if(image != null){
			
			switch(displayType){
			case FIXED:
				
				setSize(getImageSizeX(), getImageSizeY());
				
				setLayout(null);
				
			case FILL:
				
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
				
				break;
			
			case FIT:
				
				int imageWidth = getImageSizeX();
				int imageHeight = getImageSizeY();
				
				int finalWidth = 0;
				int finalHeight = 0;
				
				double ratioWidth = (double)getWidth() / (double)imageWidth;
				double ratioHeight = (double)getHeight() / (double)imageHeight;
				
				if(ratioWidth < ratioHeight){
					
					finalWidth = (int)(ratioWidth * imageWidth);
					finalHeight = (int)(ratioWidth * imageHeight);
					
				}
				else{
					
					finalWidth = (int)(ratioHeight * imageWidth);
					finalHeight = (int)(ratioHeight * imageHeight);
					
				}
				
				int finalPosX = -1;
				int finalPosY = -1;
				
				switch(posX){
				case WEST:
					finalPosX = 0;
					break;
				case CENTER:
					finalPosX = (getWidth() - finalWidth) / 2;
					break;
				case EAST:
					finalPosX = getWidth() - finalWidth;
					break;
				}
				
				switch(posY){
				case NORTH:
					finalPosY = 0;
					break;
				case CENTER:
					finalPosY = (getHeight() - finalHeight) / 2;
					break;
				case SOUTH:
					finalPosY = getHeight() - finalHeight;
					break;
				}
				
				g.drawImage(image, finalPosX, finalPosY, finalWidth,
						finalHeight, this);
				
				break;
			}
			
		}
		
	}
	
	@Override
	protected void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		drawImage(g);
		
	}
	
	public void setImage(Image image){
		this.image = image;
	}
	
	public int getImageSizeX(){
		return image.getWidth(this);
	}
	
	public int getImageSizeY(){
		return image.getHeight(this);
	}
	
	public int getImagePosX(){
		return posX;
	}
	
	public int getImagePosY(){
		return posY;
	}
	
	public void setImagePosX(int posX){
		
		if(posX < WEST || posX > EAST)
			throw new IllegalArgumentException(
					"This parameter cannot be outside of the range " + WEST
							+ " <= posX <= " + EAST + ".");
		
		this.posX = posX;
		
	}
	
	public void setImagePosY(int posY){
		
		if(posY < WEST || posY > EAST)
			throw new IllegalArgumentException(
					"This parameter cannot be outside of the range " + WEST
							+ " <= posY <= " + EAST + ".");
		
		this.posY = posY;
		
	}
	
}
