package View;

import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.Game;

import java.util.ArrayList;

public class InventoryViewManager {
    private Game game;
    private static final int HEIGHT= 672;
    private static final int WIDTH = 224;
    private final Canvas INVENTORY_CANVAS;
    private static final Image BACK_PANEL_IMAGE = new Image("View/Resources/minimap backpanel.png");
    private GraphicsContext gc;
    private SubScene subscene;
    private ArrayList<Button> buttonsList;


    public InventoryViewManager(Game game){
        this.game = game;
        buttonsList = new ArrayList<>();
        INVENTORY_CANVAS = new Canvas(game.getWIDTH(), game.getHEIGHT());
        GridPane buttons = setUpButtons();
        StackPane sp = new StackPane();
        sp.getChildren().addAll(new ImageView(BACK_PANEL_IMAGE),buttons);
        buttons.setAlignment(Pos.CENTER);
        subscene = new SubScene(sp, WIDTH, HEIGHT);
}

    private GridPane setUpButtons(){
        GridPane gridpane = new GridPane();
        for (int y = 0; y < 2; y++){
            for (int x = 0; x < 2; x++){
                Button curButton = new Button("EAT");
                curButton.setFocusTraversable(false);
                buttonsList.add(curButton);
                gridpane.add(curButton, x, y);
            }
        }
        return gridpane;
    }


    public ArrayList<Button> getButtons() {
        return buttonsList;
    }
    public SubScene getSubscene() {
        return subscene;
    }
}
