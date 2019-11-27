import java.util.ArrayList;

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

	public Ost(Pane layer, Castle source, Castle target) {
		super(layer, source.getCenterX(), source.getCenterY(), 0, 0, new SpriteDisplay());
		
		this.source = source;
		this.target = target;
		
		updateDirection(source.getCastleGate().getCenterX(), source.getCastleGate().getCenterY());
	}
	
	public void addSoldier(SoldierType soldierType) {
		Soldier soldier = new Soldier(layer, x-soldierType.getWidth()/2, y-soldierType.getHeight()/2, soldierType);
		soldier.toBack();
		addChildSprite(soldier);
	}
	
	public Castle getSource() {
		return source;
	}
	
	public boolean hasResolved() {
		return resolved;
	}
	
	public void update() {
		
		if (!wentThroughSourceGate && !this.overlap(source)) {
			//updateDirection(target.getCastleGate().getCenterX(), target.getCastleGate().getCenterY());
			updateDirection(target.getCenterX(), target.getCenterY());
			wentThroughSourceGate = true;
			toFront();
		} else if (this.overlap(target)) {
			target.receiveOst(this);
			resolved = true;
		}
		
		move();
	}

}
