
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
