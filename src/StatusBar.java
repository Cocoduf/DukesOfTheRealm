import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * Singleton class
 *
 */
public class StatusBar {
	private VBox UIStatusBar;
	private Text UICastleOwner = new Text();
	private Text UICastleLevel = new Text();
	
	private StatusBar() {
		UIStatusBar = new VBox();
		UICastleOwner.setText("");
		UICastleLevel.setText("");
		UIStatusBar.getStyleClass().add("statusBar");
		UIStatusBar.getChildren().addAll(UICastleOwner, UICastleLevel);
		UIStatusBar.getStyleClass().add("statusBar");
		UIStatusBar.relocate(0, 0);
		UIStatusBar.setPrefSize(Settings.SCENE_WIDTH, Settings.STATUS_BAR_HEIGHT);
	}
	
	private static StatusBar INSTANCE = new StatusBar();
	
	public static StatusBar getInstance() {
		return INSTANCE;
	}
	
	public VBox getUIStatusBar() {
		return this.UIStatusBar;
	}
	
	public void update(Castle castle) {
		this.UICastleOwner.setText("Propriétaire : "+castle.getOwner());
		this.UICastleLevel.setText("Niveau          : "+Double.toString(castle.getLevel()));
	}
	
}