import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	
	private Pane playfieldLayer;
	private Scene scene;
	private AnimationTimer gameLoop;
	Group root;
	
	private boolean paused = false;
	private Text gameStateText = new Text();
	
	private ArrayList<Castle> castles = new ArrayList<>();
	private ArrayList<Ost> osts = new ArrayList<>();
	
	private Castle selectedCastle = null; // Castle displayed in StatusBar
	private Castle targetedCastle = null; // Castle targeted by selectedCastle when the player is planning an attack

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new Group();
		scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT + Settings.STATUS_BAR_HEIGHT);
		
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		playfieldLayer = new Pane();
		root.getChildren().add(playfieldLayer);
		
		loadGame();
		
		gameLoop = new AnimationTimer() {

			@Override
			public void handle(long now) {
				
				if (!Main.this.isPaused()) {
				
					Main.this.castles.forEach(castle -> castle.update());
					////
					for (Castle castle : Main.this.castles) {
						if (!castle.isPlayerOwned()) {
							if (castle.buySoldier(SoldierType.PIKEMAN)) {
							
								// random basic attack
								if (Math.random() > 0.8) {
									Castle target = castles.get(new Random().nextInt(castles.size()));
									if (target != castle) {
										Ost ost = new Ost(playfieldLayer, castle, target);
										ost.addSoldier(SoldierType.PIKEMAN);
										castle.removeSoldier(SoldierType.PIKEMAN);
										Main.this.osts.add(ost);
									}
								}
								
							}
						}
					}
					////
					Main.this.osts.forEach(ost -> ost.update());
					Main.this.osts.removeIf(ost -> ost.hasResolved());
				
				}
				
				/* below: not affected by PAUSE (mainly related to UI) */
				
				for (Castle castle : Main.this.castles) {
					
					// update selected castle
					if (castle.isJustClicked() == 1) {
						selectedCastle = castle;
						targetedCastle = null;
						StatusBar.getInstance().setSelectedCastle(selectedCastle);
					} else if (castle.isJustClicked() == 2 && selectedCastle.isPlayerOwned() && !castle.isPlayerOwned()) {
						targetedCastle = castle;
						Main.this.togglePause();
						Main.this.popupPlanAttack(primaryStage);
						
					}
					castle.resetClick();
				}
				
				StatusBar.getInstance().update();
				
			}
		
		};
		
		gameLoop.start();
		
	}
	
	public static double[] randomLocationOnScreen(double offsetLeft, double offsetRight, double offsetTop, double offsetBottom) {
		double[] location = new double[2];
		location[0] = Math.floor(Math.random() * (Settings.SCENE_WIDTH+1 - offsetRight)) + offsetLeft; // x
		location[1] = Math.floor(Math.random() * (Settings.SCENE_HEIGHT+1 - offsetBottom)) + offsetTop+Settings.STATUS_BAR_HEIGHT; // y
		return location;
	}
	
	public static double[] getCosineDirection(double x1, double y1, double x2, double y2) {
    	double q = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
		double dx = (x2-x1) / q;
		double dy = (y2-y1) / q;
		return new double[] {dx, dy};
    }
	
	private void createStatusBar() {
		root.getChildren().add(StatusBar.getInstance().getUIStatusBar());
	}
	
	private void createCastles() {
		for (int i = 0; i < Settings.PLAYER_COUNT; i++) {
			Castle castle = new Castle(playfieldLayer, 0, 0, i==0?Settings.PLAYER_NAME:"ENEMY "+i);
			double[] location;
			boolean free;
			do {
				free = true;
				location = randomLocationOnScreen(Castle.CASTLE_WIDTH, Castle.CASTLE_WIDTH*2, Castle.CASTLE_HEIGHT, Castle.CASTLE_HEIGHT*2);
				castle.moveTo(location[0], location[1]);
				for (Castle c : castles) {
					if (castle.overlap(c)) {
						free = false;
					}
				}
				if (free) {
					castles.add(castle);
				}
			} while (!free);
		}
	}
	
	private void changeGameStateText(String text) {
		gameStateText.setText(text);
		double textwidth = gameStateText.getLayoutBounds().getWidth();
		double textheight = gameStateText.getLayoutBounds().getHeight();
		gameStateText.setX((Settings.SCENE_WIDTH-textwidth)/2);
		gameStateText.setY(Settings.SCENE_HEIGHT+Settings.STATUS_BAR_HEIGHT-textheight);
	}
	
	private void togglePause() {
		this.paused = !this.paused;
		changeGameStateText(paused ? "Jeu en pause" : "");
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	public void popupPlanAttack(Stage primaryStage) {
		final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox();
        
        Label soldierTotal = new Label("0");
        
        HashMap<SoldierType, Integer> ostData = new HashMap<>();

    	GridPane ostGrid = new GridPane();
    	ostGrid.setHgap(10);
    	int i = 0;
    	for (SoldierType type : SoldierType.values()) {
    		ostData.put(type, 0);
    		Label soldierLabel = new Label(type.getName());
    		ostGrid.add(soldierLabel, 0, i);
    		Label soldierAmount = new Label("0");
    		ostGrid.add(soldierAmount, 2, i);
    		Label soldierAmountSeparator = new Label("/");
    		ostGrid.add(soldierAmountSeparator, 3, i);
    		Label soldierMax = new Label(Integer.toString(selectedCastle.getSoldierAmount(type)));
    		ostGrid.add(soldierMax, 4, i);
    		Button soldierDecrement = new Button("-");
    		ostGrid.add(soldierDecrement, 1, i);
    		soldierDecrement.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					int amount = ostData.get(type);
					if (amount > 0) {
			    		ostData.put(type, amount - 1);
						soldierAmount.setText(Integer.toString(amount - 1));
						soldierTotal.setText(Integer.toString(Integer.parseInt(soldierTotal.getText()) - 1));
					}
				}
			});
    		Button soldierIncrement = new Button("+");
    		ostGrid.add(soldierIncrement, 5, i);
    		soldierIncrement.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					int amount = ostData.get(type);
					int max = new Integer(soldierMax.getText());
					if (amount < max && Integer.parseInt(soldierTotal.getText()) < 3) {
			    		ostData.put(type, amount + 1);
						soldierAmount.setText(Integer.toString(amount + 1));
						soldierTotal.setText(Integer.toString(Integer.parseInt(soldierTotal.getText()) + 1));
					}
				}
			});
    		i++;
    	}
    	
    	Button confirmAttack = new Button("Attaquer !");
    	confirmAttack.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Ost ost = new Ost(playfieldLayer, selectedCastle, targetedCastle);
				
				for (SoldierType type : ostData.keySet()) {
					for (int i = 0; i < ostData.get(type); i++) {
						ost.addSoldier(type);
						selectedCastle.removeSoldier(type);
					}
				}
				
				Main.this.osts.add(ost);
				dialog.close();
				togglePause();
			}
    		
    	});
        
        dialogVbox.getChildren().addAll(new Text("Planification d'attaque."), ostGrid,
        		new HBox(new Label("Soldats selectionnés : "), soldierTotal, new Label("/3")), confirmAttack);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        
        dialog.setOnCloseRequest((WindowEvent e) -> {
        	togglePause();
        });
        
        dialog.show();
        
	}
	
	private void loadGame() {
		gameStateText.setFont(Font.font("Arial", 60));
		root.getChildren().add(gameStateText);
		changeGameStateText("");
		
		scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.SPACE) {
				togglePause();
			}
		});
		
		createStatusBar();
		createCastles();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
