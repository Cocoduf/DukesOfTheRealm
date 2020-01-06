import javafx.scene.layout.Pane;

/**
 * This class represents the actual Soldier entity that moves on the board.
 *
 */
public class Soldier extends Sprite {

	public Soldier(Pane layer, double x, double y, SoldierType soldierType) {
		super(layer, x, y, soldierType.getWidth(), soldierType.getHeight(), soldierType.getDisplay());
		
		setSpeed(soldierType.getSpeed());
	}

}
