
package View;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Game;


public class ViewManager {
        private static final int HEIGHT= 600;
        private static final int WIDTH = 800;

        private BorderPane bp;
        private Scene scene;
        private Stage stage;

        private GameViewManager gvm;
        private Game game;


        public ViewManager(Game game){
            this.game = game;
            gvm = new GameViewManager(game);
            bp = new BorderPane();
            StackPane sp = new StackPane();
            sp.getChildren().addAll(gvm.getSubscene());
            bp.setCenter(sp);
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
