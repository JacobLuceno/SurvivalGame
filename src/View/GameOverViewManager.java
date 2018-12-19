package View;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOverViewManager extends Group {

    private final static Image WIN_SCREEN = new Image("View/Resources/winScreen.png");
    private final static Image LOSE_SCREEN = new Image("View/Resources/loseScreen.png");

    public GameOverViewManager(boolean gameWon) {
            setUpGameOverScreen(gameWon);
    }

    private void setUpGameOverScreen(boolean gameWon){
        ImageView background;
        if (gameWon){
            background = new ImageView(WIN_SCREEN);
        }
        else {
            background = new ImageView(LOSE_SCREEN);
        }

        this.getChildren().add(background);
    }
}
