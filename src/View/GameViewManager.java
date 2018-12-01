package View;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import model.Game;
import model.Map;
import model.TerrainTile;
import model.Vector2;

public class GameViewManager {
    private static final int HEIGHT= 672;
    private static final int WIDTH = 672;
    private static final int GAMETILE_WIDTH = 32;

    private AnimationTimer animationTimer;

    private static final Image GRASSLAND_IMAGE = new Image("View/Resources/grass.png");
    private static final Image DESERT_IMAGE = new Image("View/Resources/desert.png");
    private static final Image WATER_IMAGE = new Image("View/Resources/water.png");
    private static final Image TREE_IMAGE = new Image("View/Resources/tree.png");
    private static final Image ROCK_IMAGE = new Image("View/Resources/rock.png");
    private static final Image BUSH_IMAGE = new Image("View/Resources/bush.png");
    private static final Image CACTUS_IMAGE = new Image("View/Resources/cactus.png");
    private static final Image PLAYER_IMAGE = new Image("View/Resources/player test sprite.png");

    private Game game;
    private SubScene subscene;
    private ParallelCamera camera;

    private ImageView playerImage;
    private Vector2 currentPlayerLoc;
    Pane playerRegion;

    public GameViewManager(Game game){
        this.game = game;
        playerImage = new ImageView(PLAYER_IMAGE);
        GridPane terrainGrid = setUpTerrainGridPane();
        GridPane statObjGrid = setUpStatObjGridPane();
        playerRegion = setUpPlayer();
        Group group = new Group();
        group.getChildren().addAll(terrainGrid, statObjGrid, playerRegion);
        setUpCamera();
        subscene = new SubScene(group, WIDTH, HEIGHT);
        subscene.setCamera(camera);

        gameLoop();

    }

    private void gameLoop(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePlayerLocation();
                updateCameraPosition();
            }
        };
        animationTimer.start();
    }

    private void setUpCamera(){
        camera = new ParallelCamera();
        camera.setNearClip(0.1);
        camera.setFarClip(100);
        camera.setTranslateZ(-50);
        updateCameraPosition();

    }
    private GridPane setUpTerrainGridPane(){
        Map map = game.getMap();
        GridPane gp = new GridPane();
        for (int y = 0; y < map.getGAME_BOARD_Y(); y++){
            for (int x = 0; x < map.getGAME_BOARD_X(); x++){
                switch (map.getTerrainMap()[y][x].getTerrainType()){
                    case GRASSLAND:
                        gp.add(new ImageView(GRASSLAND_IMAGE), x, y);
                        break;
                    case DESERT:
                        gp.add(new ImageView(DESERT_IMAGE), x, y);
                        break;
                    case WATER:
                        gp.add(new ImageView(WATER_IMAGE), x, y);
                        break;
                    default:
                        System.out.println("not detecting terrain type");
                }
            }
        }
        return gp;
    }
    private GridPane setUpStatObjGridPane(){
        Map map = game.getMap();
        GridPane gp = new GridPane();
        for (int y = 0; y < map.getGAME_BOARD_Y(); y++){
            for (int x = 0; x < map.getGAME_BOARD_X(); x++){
                if (map.getTerrainMap()[y][x].getHasStatObj()) {
                    switch (map.getTerrainMap()[y][x].getStatObjType()) {
                        case ROCK:
                            gp.add(new ImageView(ROCK_IMAGE), x, y);
                            break;
                        case TREE:
                            gp.add(new ImageView(TREE_IMAGE), x, y);
                            break;
                        case BUSH:
                            if (map.getTerrainMap()[y][x].getTerrainType() == TerrainTile.TerrainType.DESERT) {
                                gp.add(new ImageView(CACTUS_IMAGE), x, y);
                            } else {
                                gp.add(new ImageView(BUSH_IMAGE), x, y);
                            }
                            break;
                        default:
                            System.out.println("not detecting terrain type");
                    }
                }
            }
        }
        return gp;
    }
    private Pane setUpPlayer(){
       Pane region = new Pane();
       region.setMaxSize(game.getWIDTH(), game.getHEIGHT());
        playerImage.setTranslateX(game.getPlayer().getPosition().getv1()*GAMETILE_WIDTH);
        playerImage.setTranslateY(game.getPlayer().getPosition().getv0()*GAMETILE_WIDTH);
        playerImage.setTranslateZ(-10);
        region.getChildren().add(playerImage);
       return region;
    }

    private void updateCameraPosition(){
        camera.setTranslateX(playerImage.getTranslateX()-320);
        camera.setTranslateY(playerImage.getTranslateY()-320);
    }

    private void updatePlayerLocation(){
        if (currentPlayerLoc != game.getPlayer().getPosition()) {
            TranslateTransition tt = new TranslateTransition(new Duration(300),playerImage);
            tt.setToX(game.getPlayer().getPosition().getv1()*GAMETILE_WIDTH);
            tt.setToY(game.getPlayer().getPosition().getv0()*GAMETILE_WIDTH);
            currentPlayerLoc = game.getPlayer().getPosition();
            game.getPlayer().setBusy(true);
            tt.play();
            tt.setOnFinished(e -> {
                game.getPlayer().setBusy(false);
            });
        }
    }

    public SubScene getSubscene() {
        return subscene;
    }
}
