import javafx.scene.paint.Color;

/**
 * The SpriteDisplay class is used to define and share the parameters that determine how a Sprite looks.
 * Once defined, a SpriteDisplay is not meant to be altered.
 * A Sprite can however have multiple SpriteDisplay, and switch between them when needed using the method Sprite.changeDisplay(SpriteDisplay display).
 *
 */
public final class SpriteDisplay {

	private Color fillColor = null;
	private Color strokeColor = null;
	private double strokeWidth = 0;
	
	public SpriteDisplay() {}
	
	public SpriteDisplay setFill(Color fill) {
		this.fillColor = fill;
		return this;
	}
	
	public SpriteDisplay setStroke(Color stroke, double width) {
		this.strokeColor = stroke;
		this.strokeWidth = width;
		return this;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public double getStrokeWidth() {
		return strokeWidth;
	}
	
}
