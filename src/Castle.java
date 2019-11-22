import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Castle extends Sprite {

	public static final double CASTLE_WIDTH = 80;
	public static final double CASTLE_HEIGHT = 80;

	public static final SpriteDisplay display = new SpriteDisplay().addFill(Color.LIGHTGREY);
	
	private CastleGate gate;
	private String owner = "undefined";
	private double level = 1;

	public Castle(Pane layer, double x, double y, String owner) {
		super(layer, x, y, CASTLE_WIDTH, CASTLE_HEIGHT, display);
		
		this.owner = owner;
		
    	rectangleView.setOnMousePressed(new EventHandler<MouseEvent>() {
    	    public void handle(MouseEvent me) {
    	        System.out.println("Castle clicked");
    	        //Castle.this.removeFromLayer();
    	        StatusBar.getInstance().update(Castle.this);
    	    }
    	});
		
    	this.createGate(layer);
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public double getLevel() {
		return level;
	}

	public void setLevel(double level) {
		this.level = level;
	}
	
	private void createGate(Pane layer) {
		
		double gateX, gateY, gateWidth, gateHeight;
		
		double direction = Math.floor(Math.random() * 4);
		
		double thickness = 6;
		double offset = 10;
		
		if (direction == 0) { // north
			gateX = x+offset;
			gateY = y-thickness;
			gateWidth = width-2*offset;
			gateHeight = thickness;
		} else if (direction == 1) { // east
			gateX = x+width;
			gateY = y+offset;
			gateWidth = thickness;
			gateHeight = height-2*offset;
		} else if (direction == 2) { // south
			gateX = x+offset;
			gateY = y+height;
			gateWidth = width-2*offset;
			gateHeight = thickness;
		} else { // west
			gateX = x-thickness;
			gateY = y+offset;
			gateWidth = thickness;
			gateHeight = height-2*offset;
		}

		this.gate = new CastleGate(layer, gateX, gateY, gateWidth, gateHeight);
		this.addChildSprite(this.gate);
	}

}
