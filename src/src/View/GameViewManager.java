package View;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.*;

import java.util.ArrayList;


public class GameViewManager {
    private static final int HEIGHT= 672;
    private static final int WIDTH = 672;
    private static final int GAMETILE_WIDTH = 32;

    private static final String UPGRADE_TOOL_PROMPT_TEXT = "PRESS 'T' TO UPGRADE TOOL: STICKS x ";
    private static final String UPGRADE_BASE_PROMPT_TEXT = "PRESS 'B' TO UPGRADE BASE: STICKS x ";
    private static final String REST_PROMPT_TEXT = "PRESS 'R' TO REST";

    private static final Image GRASSLAND_IMAGE = new Image("View/Resources/grass.png");
    private static final Image DESERT_IMAGE = new Image("View/Resources/desert.png");
    private static final Image WATER_IMAGE = new Image("View/Resources/water.png");
    private static final Image TREE_IMAGE = new Image("View/Resources/pine tree.png");
    private static final Image TREE_STUMP_IMAGE = new Image("View/Resources/pine tree stump.png");
    private static final Image ROCK_IMAGE = new Image("View/Resources/rock.png");
    private static final Image BUSH_WITH_BERRIES_IMAGE = new Image("View/Resources/bush_with_berry.png");
    private static final Image BUSH_WITHOUT_BERRIES = new Image("View/Resources/bush_no_berry.png");
    private static final Image BUSH_HARVESTED_IMAGE = new Image("View/Resources/bush_harvested.png");
    private static final Image CACTUS_IMAGE = new Image("View/Resources/cactus.png");
    private static final Image CACTUS_HARVESTED_IMAGE = new Image("View/Resources/cactus_harvested.png");
    private static final Image PLAYER_IMAGE = new Image("View/Resources/player.png");
    private static final Image FIRE_BLOOM = new Image("View/Resources/fireBloom.png");
    private static final Image CAMPFIRE_IMAGE = new Image("View/Resources/campFire.png");
    private static final Image [] UPGRADED_BASE_IMAGES = new Image[] {new Image("View/Resources/shack.png"),
                                                                        new Image("View/Resources/house.png"),
                                                                        new Image ("View/Resources/fort.png")};
    private AnimationTimer animationTimer;

    private Game game;
    private SubScene subscene;
    private ParallelCamera camera;

    private ImageView playerImage;
    private ArrayList<AnimalImageView> animalImages;
    private Vector2 currentPlayerLoc;
    private Pane playerRegion;
    private GridPane terrainGrid;
    private Pane statObjPane;
    private Pane rockPane;
    private StackPane nightShiftPane;
    private Pane animalPane;
    private Group group;
    private Circle nightClippingPlane;
    private Circle restingClippingPlane;
    private ScaleTransition st;

    private boolean dayMode;
    private ImageView[][] removableSprites;

    private Label restPrompt;
    private Label upgradePrompt;
    private boolean restPromptDisplayed;
    private boolean upgradePromptDisplayed;

    private int curAnimalListSize;

    public GameViewManager(Game game){
        this.game = game;
        currentPlayerLoc = game.getPlayer().getPosition();
        dayMode = game.isDayTime();
        curAnimalListSize = 0;
        removableSprites = new ImageView[game.getHEIGHT()][game.getWIDTH()];
        playerImage = new ImageView(PLAYER_IMAGE);
        animalImages = new ArrayList<>();
        rockPane = new Pane();
        terrainGrid = setUpTerrainGridPane();
        statObjPane = setUpStatObjGridPane();
        animalPane = new Pane();
        animalPane.setMaxSize(game.getWIDTH(), game.getHEIGHT());
        playerRegion = setUpPlayer();
        group = new Group();
        group.getChildren().addAll(terrainGrid, statObjPane, playerRegion, rockPane, animalPane);
        setUpCamera();
        subscene = new SubScene(group, WIDTH, HEIGHT);
        restPrompt = new Label();
        upgradePrompt = new Label();
        restPromptDisplayed = false;
        upgradePromptDisplayed = false;
        subscene.setCamera(camera);

        gameLoop();

    }

    private void gameLoop(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!game.isInCombat()) {
                    updateCameraPosition();
                    updatePlayerLocation();
                    replaceBaseImage();
                    displayPrompts();
                    updateAnimalsLocations();
                    updateDayCycle();
                    if (game.getPlayer().isResting()){
                        restingAnimation();
                    }
                    if (!dayMode) {
                        updateNightShiftLocation();
                    }
                    if (curAnimalListSize < game.getAnimals().size()) {
                        spawnNewAnimal();
                        curAnimalListSize++;
                    }
                }
                if (game.isAnimalTaggedForRemoval()) {
                    ArrayList<AnimalImageView> removeList = new ArrayList<>();
                    for (AnimalImageView animalImage : animalImages){
                        if (animalImage.getAnimal().isRemove()) {
                            removeList.add(animalImage);
                            game.getAnimals().remove(animalImage.getAnimal());
                            animalPane.getChildren().remove(animalImage);
                            curAnimalListSize--;
                        }
                    }
                    for (AnimalImageView a : removeList){
                        animalImages.remove(a);
                    }
                    game.setAnimalTaggedForRemoval(false);
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
                        ImageView iv = new ImageView(GRASSLAND_IMAGE);
                        gp.add(iv, x, y);
                        break;
                    case DESERT:
                        iv = new ImageView(DESERT_IMAGE);
                        gp.add(iv, x, y);
                        break;
                    case WATER:
                        iv = new ImageView(WATER_IMAGE);
                        gp.add(iv, x, y);
                        break;
                    default:
                        System.out.println("not detecting terrain type");
                }
            }
        }
        return gp;
    }

    private Pane setUpStatObjGridPane(){
        Map map = game.getMap();
        Pane gp = new Pane();
        for (int y = 0; y < map.getGAME_BOARD_Y(); y++){
            for (int x = 0; x < map.getGAME_BOARD_X(); x++){
                if (map.getTerrainMap()[y][x].getHasStatObj()) {
                    StationaryObject statObj = map.getTerrainMap()[y][x].getStatObj();
                    switch (map.getTerrainMap()[y][x].getStatObjType()) {
                        case ROCK:
                            ImageView obj = new ImageView(ROCK_IMAGE);
                            obj.setCache(true);
                            obj.setCacheHint(CacheHint.SPEED);
                            obj.setTranslateX(statObj.getPosition().getv1()*GAMETILE_WIDTH);
                            obj.setTranslateY(statObj.getPosition().getv0()*GAMETILE_WIDTH);
                            rockPane.getChildren().add(obj);
                            break;
                        case TREE:
                            ImageView tree = new ImageView(TREE_IMAGE);
                            tree.setCache(true);
                            tree.setCacheHint(CacheHint.SPEED);
                            removableSprites[y][x] = tree;
                            tree.setTranslateX(statObj.getPosition().getv1()*GAMETILE_WIDTH);
                            tree.setTranslateY(statObj.getPosition().getv0()*GAMETILE_WIDTH);
                            gp.getChildren().add(tree);
                            break;
                        case BUSH:
                            if (map.getTerrainMap()[y][x].getTerrainType() == TerrainTile.TerrainType.DESERT) {
                                obj = new ImageView(CACTUS_IMAGE);
                                obj.setCache(true);
                                obj.setCacheHint(CacheHint.SPEED);
                                removableSprites[y][x] = obj;
                                obj.setTranslateX(statObj.getPosition().getv1()*GAMETILE_WIDTH);
                                obj.setTranslateY(statObj.getPosition().getv0()*GAMETILE_WIDTH);
                                gp.getChildren().add(obj);
                            } else {
                                if (statObj instanceof Bush) {
                                    if (((Bush) statObj).getHasBerries())
                                        obj = new ImageView(BUSH_WITH_BERRIES_IMAGE);
                                    else{
                                        obj = new ImageView(BUSH_WITHOUT_BERRIES);
                                    }
                                    obj.setCache(true);
                                    obj.setCacheHint(CacheHint.SPEED);
                                    removableSprites[y][x] = obj;
                                    obj.setTranslateX(statObj.getPosition().getv1() * GAMETILE_WIDTH);
                                    obj.setTranslateY(statObj.getPosition().getv0() * GAMETILE_WIDTH);
                                    gp.getChildren().add(obj);
                                }
                            }
                            break;
                        case BASE:
                            obj = new ImageView(CAMPFIRE_IMAGE);
                            obj.setCache(true);
                            obj.setCacheHint(CacheHint.SPEED);
                            removableSprites[y][x] = obj;
                            obj.setTranslateX(statObj.getPosition().getv1()*GAMETILE_WIDTH);
                            obj.setTranslateY(statObj.getPosition().getv0()*GAMETILE_WIDTH);
                            gp.getChildren().add(obj);
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
       playerImage.setCache(true);
       playerImage.setCacheHint(CacheHint.SPEED);
       region.getChildren().add(playerImage);
       return region;
    }

    private void spawnNewAnimal(){
        AnimalImageView animal = new AnimalImageView(game.getAnimals().get(game.getAnimals().size() - 1).getGameImage(), game.getAnimals().get(game.getAnimals().size() - 1));
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
        if (currentPlayerLoc.getv0() != game.getPlayer().getPosition().getv0() || currentPlayerLoc.getv1() != game.getPlayer().getPosition().getv1()) {
            walkingAnimation();
        } else if (game.getPlayer().isHarvesting()){
            harvestingAnimation();
        }
    }
    private void updateAnimalsLocations() {
        for (AnimalImageView aiv : animalImages) {
                animalWalkingAnimation(aiv);
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

    private void displayPrompts(){
        Player player = game.getPlayer();
        if (player.isDisplayUpgradesPrompt()){
            if (!upgradePromptDisplayed) {
                upgradePromptDisplayed = true;
                upgradePrompt.setTextFill(Color.WHITE);
                group.getChildren().add(upgradePrompt);
            }
            upgradePrompt.setTranslateX(playerImage.getTranslateX()-80);
            upgradePrompt.setTranslateY(playerImage.getTranslateY()+30);
            upgradePrompt.setText("");
            if (game.getBase().getBaseStatus().ordinal() < 3){
                upgradePrompt.setText(UPGRADE_BASE_PROMPT_TEXT + Integer.toString(game.getBase().getCurBaseCost()) + '\n');
            }
            if (player.getTool().ordinal() < 3){
                upgradePrompt.setText(upgradePrompt.getText() + UPGRADE_TOOL_PROMPT_TEXT + Integer.toString(game.getBase().getCurToolCost()));
            }
        }else {
            upgradePromptDisplayed = false;
            group.getChildren().remove(upgradePrompt);
        }
        if (player.isDisplayRestPrompt()){
            if (!restPromptDisplayed) {
                restPromptDisplayed = true;
                restPrompt.setTextFill(Color.WHITE);
                group.getChildren().add(restPrompt);
            }
            restPrompt.setTranslateX(playerImage.getTranslateX()-25);
            restPrompt.setTranslateY(playerImage.getTranslateY()-15);
            restPrompt.setText(REST_PROMPT_TEXT);
        } else{
            restPromptDisplayed = false;
            group.getChildren().remove(restPrompt);
        }

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

    private void animalWalkingAnimation(AnimalImageView aiv){
            if (!aiv.isAnimationTriggered()){
                aiv.setAnimationTriggered(true);
                TranslateTransition tt = new TranslateTransition(new Duration(600f/aiv.getAnimal().getSpeed()),aiv);
                tt.setToX(aiv.getAnimal().getPosition().getv1()*GAMETILE_WIDTH);
                tt.setToY(aiv.getAnimal().getPosition().getv0()*GAMETILE_WIDTH);
                tt.play();
                tt.setOnFinished(e -> {
                    aiv.getAnimal().setBusy(false);
                    aiv.setAnimationTriggered(false);
                });
            }
    }

    private void restingAnimation(){
        boolean nextComesNight;
        game.getPlayer().setResting(false);
        game.getPlayer().setBusy(true);
        if (dayMode){
            restingClippingPlane = new Circle(playerImage.getLayoutX() + 336,playerImage.getLayoutY() + 336,1500);
            nextComesNight= true;
        } else {
            restingClippingPlane = new Circle(playerImage.getLayoutX() + 336,playerImage.getLayoutY() + 336,150);
            nextComesNight = false;
        }
        st = new ScaleTransition(Duration.millis(750), restingClippingPlane);
        st.setToX(0.00015);
        st.setToY(0.00015);
        st.setAutoReverse(false);
        subscene.setClip(restingClippingPlane);

        if (game.getCurrentEncounter() != null){
            st.setOnFinished(e -> {
                game.getPlayer().setBusy(false);
                game.setInCombat(true);
                subscene.setClip(nightClippingPlane);
            });
            st.play();

        } else {
            st.setOnFinished(e-> {
                ScaleTransition st2 = new ScaleTransition(Duration.millis(750), restingClippingPlane);
                st2.setAutoReverse(false);
                System.out.println("Shrinking animation finished");
                if (!nextComesNight) {
                    st2.setToX(10f);
                    st2.setToY(10f);
                    group.getChildren().remove(nightShiftPane);
                } else {
                    st2.setToX(1/10f);
                    st2.setToY(1/10f);
                    nightShiftPane = new StackPane();
                    ImageView nightShiftImageView = new ImageView(FIRE_BLOOM);
                    nightShiftPane.getChildren().add(nightShiftImageView);
                    updateNightShiftLocation();
                    group.getChildren().add(nightShiftPane);
                }
                game.getPlayer().setStepsToday(game.getSTEPS_PER_CYCLE() + 1);
                game.checkDayCycle();
                dayMode = game.isDayTime();

                st2.setOnFinished(e2 -> {
                    if  (!dayMode){
                        nightClippingPlane = new Circle(playerImage.getLayoutX() + 336,playerImage.getLayoutY() + 336,150);
                        subscene.setClip(nightClippingPlane);
                    } else{
                        subscene.setClip(null);
                    }
                    game.getPlayer().setBusy(false);});
                st2.play();
            });
            st.play();
        }
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
                treeToReplace = removableSprites[(int) game.getPlayer().getPosition().getv0()-1][(int) game.getPlayer().getPosition().getv1()];
                break;
            case DOWN:
                x = game.getPlayer().getPosition().getv1();
                y = (game.getPlayer().getPosition().getv0()+1);
                tt.setToX(x*GAMETILE_WIDTH);
                tt.setToY(y*GAMETILE_WIDTH);
                treeToReplace = removableSprites[(int) game.getPlayer().getPosition().getv0()+1][(int) game.getPlayer().getPosition().getv1()];
                break;
            case RIGHT:
                x = (game.getPlayer().getPosition().getv1()+1);
                y = game.getPlayer().getPosition().getv0();
                tt.setToX(x*GAMETILE_WIDTH);
                tt.setToY(y*GAMETILE_WIDTH);
                treeToReplace = removableSprites[(int) game.getPlayer().getPosition().getv0()][(int) game.getPlayer().getPosition().getv1()+1];
                break;
            case LEFT:
                x =(game.getPlayer().getPosition().getv1()-1);
                y = game.getPlayer().getPosition().getv0();
                tt.setToX(x*GAMETILE_WIDTH);
                tt.setToY(y*GAMETILE_WIDTH);
                treeToReplace = removableSprites[(int) game.getPlayer().getPosition().getv0()][(int) game.getPlayer().getPosition().getv1()-1];
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
                statObjPane.getChildren().remove(treeToReplace);
                ImageView replacement = null;
                if (game.getMap().getTerrainMap()[(int)y][(int)x].getStatObjType()== TerrainTile.StatObjType.TREE) {
                    replacement = new ImageView(TREE_STUMP_IMAGE);
                } else if (game.getMap().getTerrainMap()[(int)y][(int)x].getStatObjType()== TerrainTile.StatObjType.BUSH
                        && game.getMap().getTerrainMap()[(int)y][(int)x].getTerrainType()== TerrainTile.TerrainType.GRASSLAND) {
                    replacement = new ImageView(BUSH_HARVESTED_IMAGE);
                } else if (game.getMap().getTerrainMap()[(int)y][(int)x].getStatObjType()== TerrainTile.StatObjType.BUSH
                        && game.getMap().getTerrainMap()[(int)y][(int)x].getTerrainType()== TerrainTile.TerrainType.DESERT){
                    replacement = new ImageView(CACTUS_HARVESTED_IMAGE);
                }
                if (replacement != null) {
                    replacement.setTranslateX(x * GAMETILE_WIDTH);
                    replacement.setTranslateY(y * GAMETILE_WIDTH);
                    statObjPane.getChildren().add(replacement);
                }
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

    public void replaceBaseImage(){
        if (game.getBase().isBeenUpgraded()) {
            game.getBase().setBeenUpgraded(false);
            Vector2 basePos = game.getBase().getPosition();
            int x = (int)basePos.getv1();
            int y = (int)basePos.getv0();
            statObjPane.getChildren().remove(removableSprites[y][x]);
            ImageView replacement = new ImageView (UPGRADED_BASE_IMAGES[game.getBase().getBaseStatus().ordinal()-1]);
            removableSprites[y][x] = replacement;
            replacement.setTranslateX(x * GAMETILE_WIDTH);
            replacement.setTranslateY(y * GAMETILE_WIDTH);
            statObjPane.getChildren().add(replacement);
        }
    }

    public SubScene getSubscene() {
        return subscene;
    }
}
