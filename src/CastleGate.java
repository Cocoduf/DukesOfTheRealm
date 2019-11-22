import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CastleGate extends Sprite {

	public static final SpriteDisplay display = new SpriteDisplay().setFill(Color.CHOCOLATE);

	public CastleGate(Pane layer, double x, double y, double width, double height) {
		super(layer, x, y, width, height, display);
		
	}

}
