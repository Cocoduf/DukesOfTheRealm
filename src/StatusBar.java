import java.util.Queue;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Singleton class used to manage the status bar displayed at the top of the screen. Call StatusBar.getInstance() to access its methods.
 *
 */
public class StatusBar {
	private Castle selectedCastle = null;
	
	private VBox UIStatusBar = new VBox(); // includes UITopBox and UIProductionLine
	private HBox UITopBox = new HBox(); // includes UILabelsBox, UIValuesBox and UISoldiersGrid
	private VBox UILabelsBox = new VBox();
	private VBox UIValuesBox = new VBox();
	private GridPane UISoldiersGrid = new GridPane();
	private HBox UIProductionLine = new HBox();
	
	private Text UILabelCastleOwner = new Text();
	private Text UIValueCastleOwner = new Text();
	private Text UILabelCastleLevel = new Text();
	private Text UIValueCastleLevel = new Text();
	private Text UILabelCastleTreasury = new Text();
	private Text UIValueCastleTreasury = new Text();
	private Text UILabelCastleUpgrade = new Text();
	private Button UIButtonCastleUpgrade = new Button();
	
	private Text UILabelProductionLine = new Text();
	private ProgressBar UIProgressBarProductionLine = new ProgressBar();
	private Text UISoldiersTypeProductionLine = new Text();
	
	private StatusBar() {
		
		// castle informations
		UILabelCastleOwner.setText("Propriétaire : ");
		UILabelCastleLevel.setText("Niveau : ");
		UILabelCastleTreasury.setText("Florins : ");
		UILabelCastleUpgrade.setText("Améliorer : ");
		UILabelCastleUpgrade.setVisible(false);
		UIButtonCastleUpgrade.setVisible(false);
		UIButtonCastleUpgrade.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				StatusBar.this.upgradeCastleHandler();
			}
		});
		UILabelsBox.setAlignment(Pos.TOP_RIGHT);
		UILabelsBox.getChildren().addAll(UILabelCastleOwner, UILabelCastleLevel, UILabelCastleTreasury, UILabelCastleUpgrade);
		UIValuesBox.getChildren().addAll(UIValueCastleOwner, UIValueCastleLevel, UIValueCastleTreasury, UIButtonCastleUpgrade);
		
		// soldiers management
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
		UISoldiersGrid.getStyleClass().add("soldiersGrid");
		UISoldiersGrid.setHgap(10);
		
		UILabelProductionLine.setText("Production : ");
		UILabelProductionLine.setVisible(false);
		UIProgressBarProductionLine.setProgress(0);
		UIProgressBarProductionLine.setVisible(false);
		UISoldiersTypeProductionLine.setVisible(false);
		UIProductionLine.getChildren().addAll(UILabelProductionLine, UIProgressBarProductionLine, UISoldiersTypeProductionLine);

		// assemble everything
		UITopBox.getChildren().addAll(UILabelsBox, UIValuesBox, UISoldiersGrid);
		UIStatusBar.getChildren().addAll(UITopBox, UIProductionLine);
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
	
	public void setSelectedCastle(Castle castle) {
		this.selectedCastle = castle;
	}
	
	public void buySoldierHandler(SoldierType type) {
		selectedCastle.buySoldier(type);
	}
	
	public void upgradeCastleHandler() {
		selectedCastle.buyUpgrade();
	}
	
	/**
	 * Update the visibility and values of the labels, etc.
	 */
	public void update() {
		if (selectedCastle != null) {
			UIValueCastleOwner.setText(selectedCastle.getOwner());
			UIValueCastleLevel.setText(Integer.toString(selectedCastle.getLevel()));
			UIValueCastleTreasury.setText(Integer.toString(selectedCastle.getTreasury()));
			UIButtonCastleUpgrade.setText(Integer.toString(selectedCastle.getUpgradeCost())+"F");
			UIButtonCastleUpgrade.setVisible(selectedCastle.isPlayerOwned());
			UILabelCastleUpgrade.setVisible(selectedCastle.isPlayerOwned());
			UILabelProductionLine.setVisible(selectedCastle.isPlayerOwned());
			UIProgressBarProductionLine.setVisible(selectedCastle.isPlayerOwned());
			UISoldiersTypeProductionLine.setVisible(selectedCastle.isPlayerOwned());
			
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
				
				i++;
			}
			
			// display a progress bar showing the current production for the next soldier
			Queue<SoldierType> productionLine = selectedCastle.getProductionLine();
			SoldierType nextSoldier = productionLine.peek();
			if (nextSoldier != null) {
				UIProgressBarProductionLine.setProgress((double)selectedCastle.getSoldierProduction() / (double)nextSoldier.getProductionTime());
			} else {
				UIProgressBarProductionLine.setProgress(0);
			}
			
			// display the type of each soldier in the production line
			String soldiersType = " ";
			for (SoldierType st : productionLine) {
				soldiersType += st.getName() + " ";
			}
			UISoldiersTypeProductionLine.setText(soldiersType);
			
		}
	}
	
	/**
	 * Access a node in a gridPane at the given index.
	 * @param gridPane
	 * @param col
	 * @param row
	 * @return Node or null if none
	 */
	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return node;
	        }
	    }
	    return null;
	}
	
}