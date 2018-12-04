package View;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.Game;
import model.TerrainTile;
import model.Vector2;


public class MiniMapViewManager {

    private Game game;
    private static final int HEIGHT= 672;
    private static final int WIDTH = 224;
    private final Canvas MINIMAP_CANVAS;
    private final Canvas UI_ELEMENTS;
    private static final Image BACK_PANEL_IMAGE = new Image("View/Resources/minimap backpanel.png");
    private static final Image HEART_IMAGE = new Image("View/Resources/heart.png");
    private AnimationTimer gameLoop;
    private Label curHPLabel;

    private GraphicsContext gc;
    private SubScene subscene;

    private Vector2 currentPlayerLoc;
    private int currentPlayerHP;


    public MiniMapViewManager(Game game){
        this.game    = game;
        MINIMAP_CANVAS = new Canvas(game.getWIDTH()*2, game.getHEIGHT()*2);
        UI_ELEMENTS = new Canvas (224,672);
        gc = MINIMAP_CANVAS.getGraphicsContext2D();

        currentPlayerHP = game.getPlayer().getCurrentHitPoints();
        buildUI();
        currentPlayerLoc = game.getPlayer().getPosition();
        updateMiniMap();
        gameLoop();
    }

    private void gameLoop(){
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (currentPlayerLoc != game.getPlayer().getPosition()) {
                    updateMiniMap();
                    currentPlayerLoc = game.getPlayer().getPosition();
                }
                if (currentPlayerHP != game.getPlayer().getCurrentHitPoints()){
                    curHPLabel.setText("\t" + Integer.toString(currentPlayerHP));
                    currentPlayerHP = game.getPlayer().getCurrentHitPoints();
                }
            }
        };
        gameLoop.start();
    }

    private void updateMiniMap(){
        for (TerrainTile[] y : game.getMap().getTerrainMap()){
            for (TerrainTile x : y){
                if (x.isRevealedOnMiniMap()){
                    switch (x.getTerrainType()){
                        case GRASSLAND:
                            gc.setFill(Color.FORESTGREEN);
                            break;
                        case DESERT:
                            gc.setFill(Color.SANDYBROWN);
                            break;
                        case WATER:
                            gc.setFill(Color.SKYBLUE);
                            break;
                    }
                } else {
                    gc.setFill(Color.BLACK);
                }
                gc.fillRect(x.getPosition().getv1()*2, x.getPosition().getv0()*2, 2, 2);
                gc.setFill(Color.RED);
                gc.fillRect(game.getPlayer().getPosition().getv1()*2, game.getPlayer().getPosition().getv0()*2,3,3);
            }
        }
    }

    private void buildUI(){
        GraphicsContext gc = UI_ELEMENTS.getGraphicsContext2D();

        gc.setFill(Color.DARKGOLDENROD);
        gc.fillRect(0,0, 224,224);
        gc.fillRect(10,235, 204, 30);

        Group group = new Group();

        ImageView heart = new ImageView(HEART_IMAGE);
        heart.setX(15);
        heart.setY(232);

        Label HPLabel = new Label("\t\t\t/\t" + game.getPlayer().getMAX_HIT_POINTS() + "\tHP");
        HPLabel.setLayoutX(30);
        HPLabel.setLayoutY(235);
        curHPLabel = new Label("\t" + Integer.toString(currentPlayerHP));
        curHPLabel.setLayoutX(30);
        curHPLabel.setLayoutY(235);

        group.getChildren().addAll(new ImageView(BACK_PANEL_IMAGE),UI_ELEMENTS,MINIMAP_CANVAS, heart, HPLabel, curHPLabel);
        MINIMAP_CANVAS.setLayoutX(12);
        MINIMAP_CANVAS.setLayoutY(12);
        subscene = new SubScene(group, WIDTH, HEIGHT);



    }

    public SubScene getSubscene() {
        return subscene;
    }
}
