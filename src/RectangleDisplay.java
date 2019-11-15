import javafx.scene.paint.Color;

public final class RectangleDisplay {

	private double width;
	private double height;
	private Color fillColor;
	private Color strokeColor;
	private double strokeWidth;
	
	public RectangleDisplay(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	public RectangleDisplay addFill(Color fill) {
		this.fillColor = fill;
		return this;
	}
	
	public RectangleDisplay addStroke(Color stroke, double width) {
		this.strokeColor = stroke;
		this.strokeWidth = width;
		return this;
	}
	
}
