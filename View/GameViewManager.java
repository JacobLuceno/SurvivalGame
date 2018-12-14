package View;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.Game;
import model.Map;
import model.TerrainTile;
import model.Vector2;

import java.util.ArrayList;


public class GameViewManager {
    private static final int HEIGHT= 672;
    private static final int WIDTH = 672;
    private static final int GAMETILE_WIDTH = 32;


    private static final Image GRASSLAND_IMAGE = new Image("View/Resources/grass.png");
    private static final Image DESERT_IMAGE = new Image("View/Resources/desert.png");
    private static final Image WATER_IMAGE = new Image("View/Resources/water.png");
    private static final Image TREE_IMAGE = new Image("View/Resources/pine tree.png");
    private static final Image TREE_STUMP_IMAGE = new Image("View/Resources/pine tree stump.png");
    private static final Image ROCK_IMAGE = new Image("View/Resources/rock.png");
    private static final Image BUSH_IMAGE = new Image("View/Resources/bush.png");
    private static final Image CACTUS_IMAGE = new Image("View/Resources/cactus.png");
    private static final Image PLAYER_IMAGE = new Image("View/Resources/player test sprite.png");
    private static final Image FIRE_BLOOM = new Image("View/Resources/fireBloom.png");
    private static final Image CAMPFIRE_IMAGE = new Image("View/Resources/campFire.png");
    private static final Image RABBIT_IMAGE = new Image("View/Resources/rabbit.png");
    private AnimationTimer animationTimer;

    private Game game;
    private SubScene subscene;
    private ParallelCamera camera;

    private ImageView playerImage;
    private ArrayList<AnimalImageView> animalImages;
    private Vector2 currentPlayerLoc;
    private Pane playerRegion;
    private GridPane terrainGrid;
    private GridPane statObjGrid;
    private StackPane nightShiftPane;
    private Pane animalPane;
    private Group group;
    private Circle nightClippingPlane;
    private ScaleTransition st;

    private boolean dayMode;
    private ImageView[][] treeSprites;

    private int curAnimalListSize;

    public GameViewManager(Game game){
        this.game = game;
        dayMode = game.isDayTime();
        curAnimalListSize = 0;
        treeSprites = new ImageView[game.getHEIGHT()][game.getWIDTH()];
        playerImage = new ImageView(PLAYER_IMAGE);
        animalImages = new ArrayList<>();
        terrainGrid = setUpTerrainGridPane();
        statObjGrid = setUpStatObjGridPane();
        animalPane = new Pane();
        animalPane.setMaxSize(game.getWIDTH(), game.getHEIGHT());
        playerRegion = setUpPlayer();
        group = new Group();
        group.getChildren().addAll(terrainGrid, statObjGrid, playerRegion, animalPane);
        setUpCamera();
        subscene = new SubScene(group, WIDTH, HEIGHT);
        subscene.setCamera(camera);

        gameLoop();

    }

    private void gameLoop(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!Game.isInCombat()) {
                    updateCameraPosition();
                    updatePlayerLocation();
                    updateDayCycle();
                    if (!dayMode) {
                        updateNightShiftLocation();
                    }
                    if (curAnimalListSize < game.getAnimals().size()) {
                        spawnNewAnimal();
                        curAnimalListSize++;
                    }
                    if (game.isAnimalTaggedForRemoval()) {
                        for (AnimalImageView animalImage : animalImages){
                            if (animalImage.getAnimal().isRemove()) {
                                game.getAnimals().remove(animalImage.getAnimal());
                                animalPane.getChildren().remove(animalImage);
                                curAnimalListSize--;
                                animalImages.remove(animalImage);
                                break;
                            }
                        }
                        System.out.println("Number of Animal Images is " + Integer.toString(curAnimalListSize));
                        game.setAnimalTaggedForRemoval(false);
                    }
                }
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
                            ImageView tree = new ImageView(TREE_IMAGE);
                            treeSprites[y][x] = tree;
                            gp.add(tree, x, y);
                            break;
                        case BUSH:
                            if (map.getTerrainMap()[y][x].getTerrainType() == TerrainTile.TerrainType.DESERT) {
                                gp.add(new ImageView(CACTUS_IMAGE), x, y);
                            } else {
                                gp.add(new ImageView(BUSH_IMAGE), x, y);
                            }
                            break;
                        case BASE:
                            gp.add(new ImageView(CAMPFIRE_IMAGE), x, y);
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
    //ONLY WORKS FOR RABBITS BRO
    private void spawnNewAnimal(){
        AnimalImageView animal = new AnimalImageView(RABBIT_IMAGE, game.getAnimals().get(game.getAnimals().size() - 1));
        animal.setTranslateX(game.getAnimals().get(game.getAnimals().size() - 1).getPosition().getv1()*GAMETILE_WIDTH);
        animal.setTranslateY(game.getAnimals().get(game.getAnimals().size() - 1).getPosition().getv0()*GAMETILE_WIDTH);
        animal.setTranslateZ(-10);
        animalPane.getChildren().add(animal);
        animalImages.add(animal);
    }

    private void updateCameraPosition(){
            camera.setTranslateX(playerImage.getTranslateX() - 320);
            camera.setTranslateY(playerImage.getTranslateY() - 320);
    }
    private void updatePlayerLocation(){
        if (currentPlayerLoc != game.getPlayer().getPosition()) {
            walkingAnimation();
        } else if (game.getPlayer().isHarvesting()){
            harvestingAnimation();
        }
    }
    private void updateDayCycle(){
        if (dayMode != game.isDayTime()){
            dayMode = game.isDayTime();
            if  (!dayMode){
                nightShiftPane = new StackPane();
                ImageView nightShiftImageView = new ImageView(FIRE_BLOOM);
                nightShiftPane.getChildren().add(nightShiftImageView);
                updateNightShiftLocation();
                nightShiftStartAnimation();
                subscene.setClip(nightClippingPlane);
                st.play();
                st.setOnFinished(e -> group.getChildren().add(nightShiftPane));

            } else{
                nightShiftEndAnimation();
                st.play();
                st.setOnFinished(e -> {subscene.setClip(null);
                });
                group.getChildren().remove(nightShiftPane);
            }
        }
    }

    private void updateNightShiftLocation(){
        nightShiftPane.setTranslateX(playerImage.getTranslateX() - 250);
        nightShiftPane.setTranslateY(playerImage.getTranslateY() - 245);
    }

    private void nightShiftEndAnimation(){
        st = new ScaleTransition(Duration.millis(1000), nightClippingPlane);
        st.setToX(10f);
        st.setToY(10f);
        st.setAutoReverse(false);
    }

    private void nightShiftStartAnimation(){
        nightClippingPlane = new Circle(playerImage.getLayoutX() + 336,playerImage.getLayoutY() + 336,1500);
        st = new ScaleTransition(Duration.millis(750), nightClippingPlane);
        st.setToX(1/10f);
        st.setToY(1/10f);
        st.setAutoReverse(false);
    }

    private void walkingAnimation(){
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
    private void harvestingAnimation(){
        TranslateTransition tt = new TranslateTransition(new Duration(150),playerImage);
        final ImageView treeToReplace;
        float x;
        float y;
        switch(game.getPlayer().getFacing()){
            case UP:
                x = game.getPlayer().getPosition().getv1();
                y = (game.getPlayer().getPosition().getv0()-1);
                tt.setToX(x*GAMETILE_WIDTH);
                tt.setToY(y*GAMETILE_WIDTH);
                treeToReplace = treeSprites[(int) game.getPlayer().getPosition().getv0()-1][(int) game.getPlayer().getPosition().getv1()];
                break;
            case DOWN:
                x = game.getPlayer().getPosition().getv1();
                y = (game.getPlayer().getPosition().getv0()+1);
                tt.setToX(x*GAMETILE_WIDTH);
                tt.setToY(y*GAMETILE_WIDTH);
                treeToReplace = treeSprites[(int) game.getPlayer().getPosition().getv0()+1][(int) game.getPlayer().getPosition().getv1()];
                break;
            case RIGHT:
                x = (game.getPlayer().getPosition().getv1()+1);
                y = game.getPlayer().getPosition().getv0();
                tt.setToX(x*GAMETILE_WIDTH);
                tt.setToY(y*GAMETILE_WIDTH);
                treeToReplace = treeSprites[(int) game.getPlayer().getPosition().getv0()][(int) game.getPlayer().getPosition().getv1()+1];
                break;
            case LEFT:
                x =(game.getPlayer().getPosition().getv1()-1);
                y = game.getPlayer().getPosition().getv0();
                tt.setToX(x*GAMETILE_WIDTH);
                tt.setToY(y*GAMETILE_WIDTH);
                treeToReplace = treeSprites[(int) game.getPlayer().getPosition().getv0()][(int) game.getPlayer().getPosition().getv1()-1];
                break;
            default:
                x=0;
                y=0;
                treeToReplace = null;
        }
        game.getPlayer().setBusy(true);
        game.getPlayer().setHarvesting(false);
        tt.play();
        tt.setOnFinished(e -> {
            if(treeToReplace != null) {
                statObjGrid.getChildren().remove(treeToReplace);
                statObjGrid.add(new ImageView(TREE_STUMP_IMAGE),(int)x,(int)y);
            }
            TranslateTransition tt2 = new TranslateTransition(new Duration(150),playerImage);
            tt2.setToX(game.getPlayer().getPosition().getv1()*GAMETILE_WIDTH);
            tt2.setToY(game.getPlayer().getPosition().getv0()*GAMETILE_WIDTH);
            tt2.play();
            tt2.setOnFinished(e2 ->{
                game.getPlayer().setBusy(false);
            });
        });
    }

    public SubScene getSubscene() {
        return subscene;
    }
}
