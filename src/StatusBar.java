import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
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
	private GridPane UISoldiersGrid = new GridPane();
	private Text UILabelCastleOwner = new Text();
	private Text UIValueCastleOwner = new Text();
	private Text UILabelCastleLevel = new Text();
	private Text UIValueCastleLevel = new Text();
	private Text UILabelCastleTreasury = new Text();
	private Text UIValueCastleTreasury = new Text();
	
	private StatusBar() {
		UILabelCastleOwner.setText("Propriétaire : ");
		UILabelCastleLevel.setText("Niveau : ");
		UILabelCastleTreasury.setText("Florins : ");
		UILabelsBox.setAlignment(Pos.TOP_RIGHT);
		UILabelsBox.getChildren().addAll(UILabelCastleOwner, UILabelCastleLevel, UILabelCastleTreasury);
		UIValuesBox.getChildren().addAll(UIValueCastleOwner, UIValueCastleLevel, UIValueCastleTreasury);
		
		//TODO: mettre dans la GridPane UISoldiersGrid:
		//une ligne Label pour chaque type de soldat,
		//une ligne Nombre pour le nombre de soldat dans le chateau,
		//et une ligne de boutons qui disent "Produire (X florins)" avec X etant le coût du soldat.
		//Mettre en forme (espacer les cellules entre elles, peut-être une police plus petite) et ajouter un titre à la grille ("Armée", "Troupes" ou "Soldats").
		UISoldiersGrid.add(new Text("text00"), 0, 0);
		UISoldiersGrid.add(new Text("text01"), 0, 1);
		UISoldiersGrid.add(new Text("text10"), 1, 0);
		UISoldiersGrid.add(new Text("text11"), 1, 1);

		UISoldiersGrid.getStyleClass().add("soldiersGrid");
		UIStatusBar.getChildren().addAll(UILabelsBox, UIValuesBox, UISoldiersGrid);
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