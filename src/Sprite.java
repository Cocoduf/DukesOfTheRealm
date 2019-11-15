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
    
    private Color color;
	
	public Sprite(Pane layer, double x, double y, double width, double height, Color color) {
		this.layer = layer;
    	this.x = x;
    	this.y = y;
    	this.width = width;
    	this.height = height;
    	this.color = color;
    	
    	rectangleView = new Rectangle(x, y, width, height);
    	rectangleView.setFill(Color.WHITE);
    	rectangleView.setStroke(color); 
    	rectangleView.setStrokeWidth(4);
    	
    	addToLayer();
	}

    public void addToLayer() {
        this.layer.getChildren().add(this.rectangleView);
    }

    public void removeFromLayer() {
        this.layer.getChildren().remove(this.rectangleView);
    }
	
}
