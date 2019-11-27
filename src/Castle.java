import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Castle extends Sprite {

	public static final double CASTLE_WIDTH = 80;
	public static final double CASTLE_HEIGHT = 80;

	public static final SpriteDisplay neutral_display = new SpriteDisplay().setFill(Color.LIGHTGRAY);
	public static final SpriteDisplay player_display = new SpriteDisplay().setFill(Color.GOLD);
	
	private CastleGate gate;
	
	private String owner = "undefined";
	private int level = 1;
	private int treasury = 0;
	private HashMap<SoldierType, Integer> army = new HashMap<>();

	public Castle(Pane layer, double x, double y, String owner) {
		super(layer, x, y, CASTLE_WIDTH, CASTLE_HEIGHT, owner==Settings.PLAYER_NAME?player_display:neutral_display);
		
		this.owner = owner;
		
    	rectangleView.setOnMousePressed(new EventHandler<MouseEvent>() {
    	    public void handle(MouseEvent me) {
    	        StatusBar.getInstance().setSelectedCastle(Castle.this);
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getTreasury() {
		return this.treasury;
	}
	
	public void setTreasury(int treasury) {
		this.treasury = treasury;
	}
	
	public void pay(int amount) {
		this.treasury -= amount;
	}
	
	// Return true if the transaction was successful
	public boolean buySoldier(SoldierType type) {
		if (treasury >= type.getCost()) {
			pay(type.getCost());
			army.put(type, (army.containsKey(type) ? army.get(type) : 0) + 1); // increment by 1 or put 1 if null
			return true;
		} else {
			return false;
		}
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
	
	public void receiveOst(Ost ost) {
		if (ost.getSource().owner == owner) {
			//friendly encounter
		} else {
			//resolve attack
		}
	}
	
	public void update() {
		this.treasury += this.level*1;
	}

}
