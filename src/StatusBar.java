import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Singleton class used to manage the status bar displayed at the top of the screen. Call StatusBar.getInstance() to access its methods.
 *
 */
public class StatusBar {
	private Castle selectedCastle = null;
	private HBox UIStatusBar = new HBox();
	private VBox UILabelsBox = new VBox();
	private VBox UIValuesBox = new VBox();
	private Text UILabelCastleOwner = new Text();
	private Text UIValueCastleOwner = new Text();
	private Text UILabelCastleLevel = new Text();
	private Text UIValueCastleLevel = new Text();
	private Text UILabelCastleTreasury = new Text();
	private Text UIValueCastleTreasury = new Text();
	
	private StatusBar() {
		
		//TODO: créer une VBox qui contient un Text qui dit "Piquier" et un bouton qui dit "Produire (X florins)" avec X etant le coût (SoldierType.PIKEMAN.getCost())
		
		UILabelCastleOwner.setText("Propriétaire : ");
		UILabelCastleLevel.setText("Niveau : ");
		UILabelCastleTreasury.setText("Florins : ");
		UILabelsBox.setAlignment(Pos.TOP_RIGHT);
		UILabelsBox.getChildren().addAll(UILabelCastleOwner, UILabelCastleLevel, UILabelCastleTreasury);
		UIValuesBox.getChildren().addAll(UIValueCastleOwner, UIValueCastleLevel, UIValueCastleTreasury);
		UIStatusBar.getChildren().addAll(UILabelsBox, UIValuesBox);
		UIStatusBar.getStyleClass().add("statusBar");
		UIStatusBar.relocate(0, 0);
		UIStatusBar.setPrefSize(Settings.SCENE_WIDTH, Settings.STATUS_BAR_HEIGHT);
	}
	
	private static StatusBar INSTANCE = new StatusBar();
	
	public static StatusBar getInstance() {
		return INSTANCE;
	}
	
	public HBox getUIStatusBar() {
		return this.UIStatusBar;
	}
	
	public void setSelectedCastle(Castle castle) {
		this.selectedCastle = castle;
	}
	
	public void update() {
		if (selectedCastle != null) {
			this.UIValueCastleOwner.setText(selectedCastle.getOwner());
			this.UIValueCastleLevel.setText(Integer.toString(selectedCastle.getLevel()));
			this.UIValueCastleTreasury.setText(Integer.toString(selectedCastle.getTreasury()));
		}
	}
	
}