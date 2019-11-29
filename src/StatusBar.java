import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
	private Text UILabelCastleUpgrade = new Text();
	private Button UIValueCastleUpgrade = new Button();
	
	private StatusBar() {
		UILabelCastleOwner.setText("Propriétaire : ");
		UILabelCastleLevel.setText("Niveau : ");
		UILabelCastleTreasury.setText("Florins : ");
		UILabelCastleUpgrade.setText("Améliorer : ");
		UILabelCastleUpgrade.setVisible(false);
		UIValueCastleUpgrade.setVisible(false);
		UIValueCastleUpgrade.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				StatusBar.this.upgradeCaslteHandler();
			}
		});
		
		UILabelsBox.setAlignment(Pos.TOP_RIGHT);
		UILabelsBox.getChildren().addAll(UILabelCastleOwner, UILabelCastleLevel, UILabelCastleTreasury, UILabelCastleUpgrade);
		UIValuesBox.getChildren().addAll(UIValueCastleOwner, UIValueCastleLevel, UIValueCastleTreasury, UIValueCastleUpgrade);
		
		//TODO: ajouter un titre à la grille ("Armée", "Troupes" ou "Soldats").
		int i = 0;
		for (SoldierType type : SoldierType.values()) {
			Label soldierLabel = new Label();
			UISoldiersGrid.add(soldierLabel, i, 0);
			GridPane.setHalignment(soldierLabel, HPos.CENTER);
			soldierLabel.setVisible(false);
			
			Label soldierAmount = new Label();
			UISoldiersGrid.add(soldierAmount, i, 1);
			GridPane.setHalignment(soldierAmount, HPos.CENTER);
			soldierAmount.setVisible(false);
			
			Button soldierBuy = new Button();
			UISoldiersGrid.add(soldierBuy, i, 2);
			GridPane.setHalignment(soldierBuy, HPos.CENTER);
			soldierBuy.setVisible(false);
			soldierBuy.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					StatusBar.this.buySoldierHandler(type);
				}
			});
			
			i++;
		}
		
		//TODO: afficher la productionLine du chateau courant

		UISoldiersGrid.getStyleClass().add("soldiersGrid");
		UISoldiersGrid.setHgap(10);
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
	
	public void buySoldierHandler(SoldierType type) {
		selectedCastle.buySoldier(type);
	}
	
	public void upgradeCaslteHandler() {
		selectedCastle.buyUpgrade();
	}
	
	public void update() {
		if (selectedCastle != null) {
			this.UIValueCastleOwner.setText(selectedCastle.getOwner());
			this.UIValueCastleLevel.setText(Integer.toString(selectedCastle.getLevel()));
			this.UIValueCastleTreasury.setText(Integer.toString(selectedCastle.getTreasury()));
			this.UIValueCastleUpgrade.setText(Integer.toString(selectedCastle.getUpgradeCost())+"F");
			this.UIValueCastleUpgrade.setVisible(selectedCastle.isPlayerOwned());
			this.UILabelCastleUpgrade.setVisible(selectedCastle.isPlayerOwned());
			
			int i = 0;
			for (SoldierType type : SoldierType.values()) {
				Label soldierLabel = ((Label)getNodeFromGridPane(UISoldiersGrid, i, 0));
				soldierLabel.setText(type.getName());
				soldierLabel.setVisible(true);
				Label soldierAmount = ((Label)getNodeFromGridPane(UISoldiersGrid, i, 1));
				soldierAmount.setText(Integer.toString(selectedCastle.getSoldierAmount(type)));
				soldierAmount.setVisible(true);
				Button soldierBuy = ((Button)getNodeFromGridPane(UISoldiersGrid, i, 2));
				soldierBuy.setText("Produire ("+type.getCost()+"F)");
				soldierBuy.setVisible(selectedCastle.isPlayerOwned());
			}
		}
	}
	
	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return node;
	        }
	    }
	    return null;
	}
	
}