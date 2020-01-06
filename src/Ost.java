import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.layout.Pane;

/**
 * An Ost is a group of Soldier.
 *
 */
public class Ost extends Sprite {
	
	private boolean resolved = false;
	private Castle source;
	private Castle target;
	private boolean wentThroughSourceGate = false;
	private HashMap<SoldierType, Integer> soldiers = new HashMap<>();

	public Ost(Pane layer, Castle source, Castle target) {
		super(layer, source.getCenterX(), source.getCenterY(), 0, 0, new SpriteDisplay());
		
		this.source = source;
		this.target = target;
		
		updateDirection(source.getCastleGate().getCenterX(), source.getCastleGate().getCenterY());
	}
	
	public void addSoldier(SoldierType type) {
		Soldier soldier = new Soldier(layer,
				x-type.getWidth()*1.5 + Math.floor(Math.random()*type.getWidth()*2),
				y-type.getHeight()*1.5 + Math.floor(Math.random()*type.getHeight()*2),
				type);
		soldier.toBack();
		addChildSprite(soldier);
		soldiers.put(type, getSoldierAmount(type) + 1);
	}
	
	public void removeSoldier(SoldierType type) {
		if (getSoldierAmount(type) > 0) {
			soldiers.put(type, getSoldierAmount(type) - 1);
		}
	}
	
	public int getSoldierAmount(SoldierType type) {
		return soldiers.containsKey(type) ? soldiers.get(type) : 0;
	}
	
	public int getSoldiersTotal() {
		int total = 0;
		for (SoldierType type : soldiers.keySet()) {
			total += soldiers.get(type);
		}
		return total;
	}
	
	public HashMap<SoldierType, Integer> getSoldiers() {
		return soldiers;
	}
	
	public Castle getSource() {
		return source;
	}
	
	/**
	 * Return true if the ost has reached its destination and finished the attack
	 * @return
	 */
	public boolean hasResolved() {
		return resolved;
	}
	
	/**
	 * Update the ost, moving it.
	 */
	public void update() {
		
		if (!wentThroughSourceGate && !this.overlap(source)) {
			updateDirection(target.getCenterX(), target.getCenterY());
			wentThroughSourceGate = true;
			toFront();
		} else if (this.overlap(target)) {
			target.receiveOst(this);
			resolved = true;
			removeFromLayer();
		}
		
		move();
	}

}
