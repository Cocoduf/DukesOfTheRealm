import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Castle extends Sprite {

	public static final double CASTLE_WIDTH = 80;
	public static final double CASTLE_HEIGHT = 80;

	public static final SpriteDisplay neutral_display = new SpriteDisplay().setFill(Color.LIGHTGRAY);
	public static final SpriteDisplay player_display = new SpriteDisplay().setFill(Color.GOLD);
	
	private CastleGate gate;
	private int justClicked = 0; // 0 = no, 1 = left click, 2 = right click
	
	private String owner = "undefined";
	private int level = 1;
	private int treasury = 0;
	private int soldierProduction = 0;
	private HashMap<SoldierType, Integer> army = new HashMap<>();
	private Queue<SoldierType> productionLine = new LinkedList<SoldierType>();

	public Castle(Pane layer, double x, double y, String owner) {
		super(layer, x, y, CASTLE_WIDTH, CASTLE_HEIGHT, owner==Settings.PLAYER_NAME?player_display:neutral_display);
		
		this.owner = owner;
		
    	rectangleView.setOnMousePressed(new EventHandler<MouseEvent>() {
    	    public void handle(MouseEvent me) {
    	    	if (me.getButton() == MouseButton.PRIMARY) {
    	    		justClicked = 1;
    	    	}
    	    	else if (me.getButton() == MouseButton.SECONDARY) {
    	    		justClicked = 2;
    	    	}
    	    }
    	});
		
    	this.createGate(layer);
	}
	
	public int isJustClicked() {
		return justClicked;
	}
	
	public void resetClick() {
		justClicked = 0;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
		if (owner==Settings.PLAYER_NAME) {
			changeDisplay(player_display);
		} else {
			changeDisplay(neutral_display);
		}
	}
	
	public boolean isPlayerOwned() {
		return owner == Settings.PLAYER_NAME;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getUpgradeCost() {
		return 1000 * level;
	}
	
	// Return true if the transaction was successful
	public boolean buyUpgrade() {
		if (treasury >= getUpgradeCost()) {
			pay(getUpgradeCost());
			level++;
			return true;
		} else {
			return false;
		}
	}
	
	public int getTreasury() {
		return this.treasury;
	}
	
	public void setTreasury(int treasury) {
		this.treasury = treasury;
	}
	
	public int getIncome() {
		return level*1;
	}
	
	public void pay(int amount) {
		treasury -= amount;
	}
	
	public int getSoldierAmount(SoldierType type) {
		return army.containsKey(type) ? army.get(type) : 0;
	}
	
	public Queue<SoldierType> getProductionLine() {
		return productionLine;
	}
	
	public int getSoldierProduction() {
		return soldierProduction;
	}
	
	// Return true if the transaction was successful
	public boolean buySoldier(SoldierType type) {
		if (treasury >= type.getCost() && productionLine.size() < Settings.MAX_PRODUCTION_LINE) {
			pay(type.getCost());
			productionLine.add(type);
			return true;
		} else {
			return false;
		}
	}
	
	public void addSoldier(SoldierType type, int amount) {
		army.put(type, getSoldierAmount(type) + amount);
	}
	
	public void removeSoldier(SoldierType type) {
		if (getSoldierAmount(type) > 0) {
			army.put(type, getSoldierAmount(type) - 1);
		}
	}
	
	private boolean hasNoArmy() {
		boolean no = true;
		for (SoldierType type : army.keySet()) {
			if (getSoldierAmount(type) > 0) {
				no = false;
			}
		}
		return no;
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
	
	public CastleGate getCastleGate() {
		return gate;
	}
	
	/**
	 * The castle receives a group of soldiers
	 * @param ost
	 */
	public void receiveOst(Ost ost) {
		HashMap<SoldierType, Integer> ostSoldiers = ost.getSoldiers();
		if (ost.getSource().owner == owner) { // friendly ost
			for (SoldierType type : ostSoldiers.keySet()) {
				addSoldier(type, ostSoldiers.get(type));
			}
		} else { // enemy ost
			for (SoldierType type : ostSoldiers.keySet()) {
				for (int i = 0; i < ostSoldiers.get(type); i++) {
					int damage = type.getDamage();
					while (damage > 0 && !hasNoArmy()) {
						damage = this.applyDamageToRandomSoldier(damage);
					}
				}
			}
			if (hasNoArmy()) { // no army left ? the attacker takes control of the castle
				setOwner(ost.getSource().owner);
			}
		}
	}
	
	/**
	 * Apply damage to a "random" soldier in the castle, killing it if it exceeds its health, and returning the leftover damage.
	 * @param damage
	 * @return int
	 */
	private int applyDamageToRandomSoldier(int damage) {
		for (SoldierType type : army.keySet()) {
			if (getSoldierAmount(type) > 0) {
				int health = type.getHealth();
				if (damage >= health) {
					removeSoldier(type);
				}
				damage -= health;
				break;
			}
		}
		return damage;
	}
	
	/**
	 * Update the castle, generating income and advancing the production
	 */
	public void update() {
		this.treasury += getIncome();
		
		// advance the production of the next soldier and create it if it's ready
		if (!productionLine.isEmpty()) {
			soldierProduction += level;
			if (soldierProduction >= productionLine.peek().getProductionTime()) {
				SoldierType type = productionLine.poll();
				addSoldier(type, 1);
				soldierProduction = 0;
			}
		}
	}

}
