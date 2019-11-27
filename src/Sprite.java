import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Sprite {
	
	protected Pane layer;
	
	protected Rectangle rectangleView;
	protected SpriteDisplay currentDisplay;

	protected double x;
	protected double y;
	
	protected double dx;
	protected double dy;
	protected double speed;
	
    protected double width;
    protected double height;
    
    // A Sprite can have a list of children Sprite. When the parent Sprite moves or is removed from the view, the same applies to its children.
    private ArrayList<Sprite> children = new ArrayList<Sprite>();
	
	public Sprite(Pane layer, double x, double y, double width, double height, SpriteDisplay display) {
		this.layer = layer;
    	this.x = x;
    	this.y = y;
    	this.dx = 0;
    	this.dy = 0;
    	this.speed = 1;
    	this.width = width;
    	this.height = height;
    	
    	this.rectangleView = new Rectangle(x, y, width, height);
    	this.changeDisplay(display);
    	
    	this.addToLayer();
	}

    public void addToLayer() {
        this.layer.getChildren().add(this.rectangleView);
    }
    
    public void addChildSprite(Sprite child) {
    	this.children.add(child);
    }

    public void removeFromLayer() {
        this.layer.getChildren().remove(this.rectangleView);
        
        for (Sprite child : this.children) {
        	child.removeFromLayer();
        }
    }
    
    public void changeDisplay(SpriteDisplay display) {
    	this.rectangleView.setFill(display.getFillColor());
    	this.rectangleView.setStroke(display.getStrokeColor()); 
    	this.rectangleView.setStrokeWidth(display.getStrokeWidth());
    	this.currentDisplay = display;
    }
    
    // Remove any priority the Sprite would have to be displayed on top of an other Sprite
    public void toBack() {
    	for (Sprite child : this.children) {
        	child.toBack();
        }
    	this.rectangleView.toBack();
    }

    public void toFront() {
    	for (Sprite child : this.children) {
        	child.toFront();
        }
    	this.rectangleView.toFront();
    }
    
    public double getX() {
    	return this.x;
    }
    
    public double getY() {
    	return this.y;
    }
    
    private double getWidth() {
		return this.width;
	}
    
    private double getHeight() {
		return this.height;
	}
    
    public double getCenterX() {
    	return getX()+getWidth()/2;
    }
    
    public double getCenterY() {
    	return getY()+getHeight()/2;
    }

	// Movement on the X axis
    public void setDx(double dx) {
    	this.dx = dx;
    }
    
    // Movement on the Y axis
    public void setDy(double dy) {
    	this.dy = dy;
    }
    
    public void setSpeed(double speed) {
    	this.speed = speed;
    }
    
	public void updateDirection(double destX, double destY) {
		double[] direction = Main.getCosineDirection(this.getX(), this.getY(), destX, destY);
		this.setDx(direction[0]);
		this.setDy(direction[1]);
	}
    
    /**
     * Sets the coordinates of the Sprite to (x, y) and relatively updates the coordinates of its children.
     * @param x
     * @param y
     */
    public void moveTo(double x, double y) {
    	for (Sprite child : this.children) {
    		child.moveTo(x + (child.x - this.x), y + (child.y - this.y));
        }

    	this.x = x;
    	this.y = y;
    	this.rectangleView.setX(x);
    	this.rectangleView.setY(y);
    }
    
    public void move() {
    	this.moveTo(this.x+(this.dx*this.speed), this.y+(this.dy*this.speed));
    }
    
    /**
     * Checks if two Sprite are colliding. The does not take into account their children.
     * @param s
     * @return true if collision.
     */
    public boolean collision(Sprite s) {
    	return !(this.x + this.width < s.x || s.x + s.width < this.x || this.y + this.height < s.y || s.y + s.height < this.y);
    }
    
    /**
     * Checks if two Sprite are overlapping. This takes into account their children by calculating the boundaries of the rectangular region that contains each Sprite and its children.
     * @param s
     * @return true if overlap.
     */
    public boolean overlap(Sprite s) {
    	double left1=this.x, right1=this.x + this.width, top1=this.y, bottom1=this.y + this.height;
    	for (Sprite child : this.children) {
    		if (child.x < left1) left1 = child.x;
    		if (child.x+child.width > right1) right1 = child.x+child.width;
    		if (child.y < top1) top1 = child.y;
    		if (child.y+child.height > bottom1) bottom1 = child.y+child.height;
        }

    	double left2=s.x, right2=s.x + s.width, top2=s.y, bottom2=s.y + s.height;
    	for (Sprite child : s.children) {
    		if (child.x < left2) left2 = child.x;
    		if (child.x+child.width > right2) right2 = child.x+child.width;
    		if (child.y < top2) top2 = child.y;
    		if (child.y+child.height > bottom2) bottom2 = child.y+child.height;
        }
    	
    	return !(right1 < left2 || right2 < left1 || bottom1 < top2 || bottom2 < top1);
    }
	
}
