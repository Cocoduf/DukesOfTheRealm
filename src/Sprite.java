import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Sprite {
	
	private Pane layer;
	
	protected Rectangle rectangleView;

	protected double x;
	protected double y;
	
    protected double width;
    protected double height;
    
    // A Sprite can have a list of children Sprite. When the parent Sprite moves or is removed from the view, the same applies to its children.
    private ArrayList<Sprite> children = new ArrayList<Sprite>();
	
	public Sprite(Pane layer, double x, double y, double width, double height, SpriteDisplay display) {
		this.layer = layer;
    	this.x = x;
    	this.y = y;
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
        	this.layer.getChildren().remove(child.rectangleView);
        }
    }
    
    public void changeDisplay(SpriteDisplay display) {
    	this.rectangleView.setFill(display.getFillColor());
    	this.rectangleView.setStroke(display.getStrokeColor()); 
    	this.rectangleView.setStrokeWidth(display.getStrokeWidth());
    }
    
    public void moveTo(double x, double y) {
    	for (Sprite child : this.children) {
    		child.moveTo(x + (child.x - this.x), y + (child.y - this.y));
        }

    	this.x = x;
    	this.y = y;
    	this.rectangleView.setX(x);
    	this.rectangleView.setY(y);
    }
    
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
    	
    	//return !(this.x + this.width < s.x || s.x + s.width < this.x || this.y + this.height < s.y || s.y + s.height < this.y);
    	return !(right1 < left2 || right2 < left1 || bottom1 < top2 || bottom2 < top1);
    }
	
}
