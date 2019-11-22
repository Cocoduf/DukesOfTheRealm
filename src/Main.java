import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Pane playfieldLayer;
	private Scene scene;
	private AnimationTimer gameLoop;
	Group root;
	
	private ArrayList<Castle> castles = new ArrayList<>();
	private ArrayList<Ost> osts = new ArrayList<>();

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
					
				
				for (Castle castle : Main.this.castles) {
					castle.update();
					
					// Pour le moment, on veut que les chateau génèrent automatiquement un Ost contenant un Pikeman quand ils ont assez de florins
					if (castle.getTreasury() > SoldierType.PIKEMAN.getCost()) {
						castle.pay(SoldierType.PIKEMAN.getCost());
						Ost ost = new Ost(playfieldLayer, castle.x, castle.y);
						ost.addSoldier(SoldierType.PIKEMAN);
						
						/*TODO: sélectionner arbitrairement un autre chateau pour cible,
						 * calculer la direction à prendre pour aller à cet autre chateau,
						 * et attribuer cette direction à l'Ost comme ci-dessous.
						 * */
						ost.setDx(2);
						ost.setDy(2);
						
						Main.this.osts.add(ost);
					}
				}
				
				for (Ost ost : Main.this.osts) {
					ost.move();
				}
				
				StatusBar.getInstance().update();
				
			}
		
		};
		
		gameLoop.start();
		
	}
	
	private double[] randomLocationOnScreen(double offsetLeft, double offsetRight, double offsetTop, double offsetBottom) {
		double[] location = new double[2];
		location[0] = Math.floor(Math.random() * (Settings.SCENE_WIDTH+1 - offsetRight)) + offsetLeft; // x
		location[1] = Math.floor(Math.random() * (Settings.SCENE_HEIGHT+1 - offsetBottom)) + offsetTop+Settings.STATUS_BAR_HEIGHT; // y
		return location;
	}
	
	public void createStatusBar() {
		root.getChildren().addAll(StatusBar.getInstance().getUIStatusBar());
	}
	
	private void createCastles() {
		for (int i = 0; i < Settings.PLAYER_COUNT; i++) {
			Castle castle = new Castle(playfieldLayer, 0, 0, "PLAYER "+i);
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
	
	private void loadGame() {
		createStatusBar();
		createCastles();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
