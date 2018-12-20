
//TODO HANDLE KNOWN ISSUES
//  Buttons in general need to be prettied up
//  Static occurrences in Game and TerrainTile, which may be better off passed as parameters
//  music player crashes on startup fairly often


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
        Game game = new Game();  //TODO Continue cleaning from here
        ViewManager vm = new ViewManager(game);
        ControlManager cm = new ControlManager(vm.getScene(), game);
        primaryStage = vm.getStage();
        MusicManager mm = new MusicManager();
        primaryStage.show();
    }
}
