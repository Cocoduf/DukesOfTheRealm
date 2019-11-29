import javafx.scene.layout.Pane;

/**
 * This class represents the actual Soldier entity that moves on the board.
 *
 */
public class Soldier extends Sprite {
	
	private int health;
	private int damage;

	public Soldier(Pane layer, double x, double y, SoldierType soldierType) {
		super(layer, x, y, soldierType.getWidth(), soldierType.getHeight(), soldierType.getDisplay());
		
		setSpeed(soldierType.getSpeed());
		this.health = soldierType.getHealth();
		this.damage = soldierType.getDamage();
	}

}
