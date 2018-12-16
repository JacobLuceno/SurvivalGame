
//TODO HANDLE KNOWN ISSUES
//  Player can be pushed outside bounds of game when fleeing if base is near edge
//  Buttons in general need to be prettied up
//  Static occurrences in Game and TerrainTile, which may be better off passed as parameters
//  sometimes game crashes when new animal is spawned. no exceptions or errors thrown.  Not sure how to tackle this one
//  music player crashes on startup fairly often


import View.MusicManager;
import View.ViewManager;
import controller.ControlManager;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Game;

import javax.naming.ldap.Control;

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
