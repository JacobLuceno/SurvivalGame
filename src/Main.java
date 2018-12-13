
//TODO HANDLE KNOWN ISSUES
//  Animals can spawn outside of bounds of game and cannot be interacted with
//  Player can be pushed outside bounds of game when fleeing if base is near edge
//  Combat system needs dialogue boxes and images of animals being fought
//  Buttons in general need to be prettied up
//  Jose's implementation of A* may be changing terrtainTiles in nonsucustainable way
//  Static occurrences in Game and TerrainTile, which may be better off passed as parameters

import View.MusicManager;
import View.ViewManager;
import controller.ControlManager;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Game;

public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        ViewManager vm = new ViewManager(game);
        ControlManager cm = new ControlManager(vm.getScene(), game);
        primaryStage = vm.getStage();
        MusicManager mm = new MusicManager();
        primaryStage.show();
    }
}
