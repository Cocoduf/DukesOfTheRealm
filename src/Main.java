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
					if (castle.getTreasury() > SoldierType.PIKEMAN.getCost() && castle.getOwner() != Settings.PLAYER_NAME) {
						castle.pay(SoldierType.PIKEMAN.getCost());
						Ost ost = new Ost(playfieldLayer, castle.x, castle.y);
						ost.addSoldier(SoldierType.PIKEMAN);
						
						Castle target = Main.this.castles.get(0);
						
						double[] direction = Main.getCosineDirection(castle.getX(), castle.getY(), target.getX(), target.getY());
						
						ost.setDx(direction[0]);
						ost.setDy(direction[1]);
						
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
	
	public void createStatusBar() {
		root.getChildren().addAll(StatusBar.getInstance().getUIStatusBar());
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
	
	private void loadGame() {
		createStatusBar();
		createCastles();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
