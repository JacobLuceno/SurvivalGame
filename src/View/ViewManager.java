
package View;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Game;


public class ViewManager {
        private static final int HEIGHT= 872;
        private static final int WIDTH = 1120;

        private BorderPane bp;
        private Scene scene;
        private Stage stage;

        private GameViewManager gvm;
        private MiniMapViewManager mmvm;
        private InventoryViewManager ivm;
        private Game game;


        public ViewManager(Game game){
            this.game = game;
            gvm = new GameViewManager(game);
            mmvm = new MiniMapViewManager(game);
            ivm = new InventoryViewManager(game);
            bp = new BorderPane();
            bp.setCenter(gvm.getSubscene());
            bp.setLeft(mmvm.getSubscene());
            bp.setRight(ivm.getSubscene());
            bp.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
            bp.setAlignment(gvm.getSubscene(), Pos.CENTER);
            bp.setAlignment(mmvm.getSubscene(), Pos.CENTER_RIGHT);
            bp.setAlignment(ivm.getSubscene(), Pos.CENTER_LEFT);

            bp.setPrefSize(1120,872);
            scene = new Scene(bp, WIDTH, HEIGHT, false);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("S U R V I V E");
        }


        public Stage getStage(){
            return stage;
        }
        public Scene getScene() {
            return scene;
        }
}
