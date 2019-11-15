import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Castle extends Sprite {

	public static final double CASTLE_WIDTH = 80;
	public static final double CASTLE_HEIGHT = 80;
	public static final Color CASTLE_COLOR = Color.CORNFLOWERBLUE;
	
	private CastleGate gate;

	public Castle(Pane layer, double x, double y) {
		super(layer, x, y, CASTLE_WIDTH, CASTLE_HEIGHT, CASTLE_COLOR);
		
    	rectangleView.setOnMousePressed(new EventHandler<MouseEvent>() {
    	    public void handle(MouseEvent me) {
    	        System.out.println("Castle clicked");
    	    }
    	});
		
    	createGate(layer);
	}
	
	private void createGate(Pane layer) {
		
		double gateX, gateY, gateWidth, gateHeight;
		
		double a = Math.floor(Math.random() * 4);
		
		if (a == 0) { // north
			gateX = x+10;
			gateY = y;
			gateWidth = width-20;
			gateHeight = 4;
		} else if (a == 1) { // east
			gateX = x+width;
			gateY = y+10;
			gateWidth = 4;
			gateHeight = height-20;
		} else if (a == 2) { // south
			gateX = x+10;
			gateY = y+height;
			gateWidth = width-20;
			gateHeight = 4;
		} else { // west
			gateX = x;
			gateY = y+10;
			gateWidth = 4;
			gateHeight = height-20;
		}

		gate = new CastleGate(layer, gateX, gateY, gateWidth, gateHeight);
	}

}
