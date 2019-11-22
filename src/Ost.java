import java.util.ArrayList;

import javafx.scene.layout.Pane;

/**
 * An Ost is a group of Soldier.
 *
 */
public class Ost extends Sprite {

	public Ost(Pane layer, double x, double y) {
		super(layer, x, y, 0, 0, new SpriteDisplay());
	}
	
	public void addSoldier(SoldierType soldierType) {
		Soldier soldier = new Soldier(layer, x, y, soldierType);
		addChildSprite(soldier);
	}

}
