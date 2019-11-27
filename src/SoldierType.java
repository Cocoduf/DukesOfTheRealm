import javafx.scene.paint.Color;

/**
 * The SoldierType enum is used to define and share the parameters that differentiate each Soldier type.
 *
 */
public enum SoldierType {

	PIKEMAN("Piquier", 100, 5, 2, 1, 1, 30, 30, new SpriteDisplay().setFill(Color.CRIMSON));
	
	private String name;
	private int cost;
	private int productionTime;
	private int speed;
	private int health;
	private int damage;
	private double width;
	private double height;
	private SpriteDisplay display;
	
	SoldierType(String name, int cost, int productionTime, int speed, int health, int damage, double width, double height, SpriteDisplay display) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.cost = cost;
		this.productionTime = productionTime;
		this.speed = speed;
		this.health = health;
		this.damage = damage;
		this.display = display;
	}
	
	public String getName() {
		return name;
	}

	public int getCost() {
		return cost;
	}
	
	public int getProductionTime() {
		return productionTime;
	}

	public int getSpeed() {
		return speed;
	}

	public int getHealth() {
		return health;
	}

	public int getDamage() {
		return damage;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public SpriteDisplay getDisplay() {
		return display;
	}
	
}
