import javafx.scene.layout.Pane;

public class Soldier extends Sprite {
	
	private int cost;
	private int productionTime;
	private int speed;
	private int health;
	private int damage;

	public Soldier(Pane layer, double x, double y, SoldierType soldierType) {
		super(layer, x, y, soldierType.getWidth(), soldierType.getHeight(), soldierType.getDisplay());
		
		this.cost = soldierType.getCost();
		this.productionTime = soldierType.getProductionTime();
		this.speed = soldierType.getSpeed();
		this.health = soldierType.getHealth();
		this.damage = soldierType.getDamage();
	}

}
