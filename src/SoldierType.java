import javafx.scene.paint.Color;

/**
 * The SoldierType enum is used to define and share the parameters that differentiate each Soldier type.
 *
 */
public enum SoldierType {

	// To add a new type of Soldier, simply copy one of the following line and change the values
	PIKEMAN("Piquier", 100, 150, 2, 1, 1, 10, 10, new SpriteDisplay().setFill(Color.CRIMSON)),
	KNIGHT("Chevalier", 500, 600, 6, 3, 5, 10, 10, new SpriteDisplay().setFill(Color.DARKORANGE).setStroke(Color.DARKRED, 1)),
	ONAGER("Onagre", 1000, 3000, 1, 5, 10, 20, 20, new SpriteDisplay().setFill(Color.CORNFLOWERBLUE).setStroke(Color.DARKBLUE, 1));
	
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
