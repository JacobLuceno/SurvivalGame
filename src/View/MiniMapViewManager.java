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
import model.Base;
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
    private static final Image DAY_IMAGE = new Image("View/Resources/day_icon.png");
    private static final Image NIGHT_IMAGE = new Image("View/Resources/night_icon.png");
    private static final Image CALENDAR_IMAGE = new Image("View/Resources/calendar.png");
    private static final Image [] BASE_IMAGES = new Image[]{
            new Image("View/Resources/campFire.png", 64, 64, true, false),
            new Image("View/Resources/shack.png", 64, 64, true, false),
            new Image("View/Resources/house.png", 64, 64, true, false),
            new Image ("View/Resources/fort.png", 64, 64, true, false)};


    private AnimationTimer gameLoop;
    private Label curHPLabel;
    private Label curStepsLabel;
    private Label curBaseLabel;
    private Label curDayLabel;

    private GraphicsContext gc;
    private ImageView dayNightIcon;
    private ImageView baseIcon;
    private SubScene subscene;

    private Vector2 currentPlayerLoc;
    private int currentPlayerHP;
    private int currentDay;
    private Base.BaseStatus curBase;


    private boolean dayTime;


    public MiniMapViewManager(Game game){
        this.game = game;
        dayTime = game.isDayTime();
        MINIMAP_CANVAS = new Canvas(game.getWIDTH()*2, game.getHEIGHT()*2);
        UI_ELEMENTS = new Canvas (224,672);
        gc = MINIMAP_CANVAS.getGraphicsContext2D();
        curBase = game.getBase().getBaseStatus();
        currentDay = 1;
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
                    curHPLabel.setText("\t" + Integer.toString(game.getPlayer().getCurrentHitPoints()));
                    currentPlayerHP = game.getPlayer().getCurrentHitPoints();
                }
                if (currentDay != game.getDay()){
                    currentDay = game.getDay();
                    curDayLabel.setText("\t\tDay: " + currentDay);
                }
                if (dayTime != game.isDayTime()){
                    dayTime = game.isDayTime();
                    if (dayTime){
                        dayNightIcon.setImage(DAY_IMAGE);
                    } else {
                        dayNightIcon.setImage(NIGHT_IMAGE);
                    }
                }
                if (curBase != game.getBase().getBaseStatus()){
                    curBase = game.getBase().getBaseStatus();
                    curBaseLabel.setText("Current Base:\t\t" + baseStatusToString());
                    baseIcon.setImage(BASE_IMAGES[curBase.ordinal()]);
                }
                //TODO WRAP THIS IN SOME SORT OF BOOLEAN SO ITS NOT CALLED 60X A SECOND
                curStepsLabel.setText("\t" + Integer.toString(game.getSTEPS_PER_CYCLE() - game.getPlayer().getStepsToday()));
            }
        };
        gameLoop.start();
    }

    private void updateMiniMap(){
        Vector2 baseLoc = null;
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
                    if (x.getStatObjType() == TerrainTile.StatObjType.BASE){
                        baseLoc = x.getPosition();
                    }
                } else {
                    gc.setFill(Color.BLACK);
                }
                gc.fillRect(x.getPosition().getv1()*2, x.getPosition().getv0()*2, 2, 2);
                gc.setFill(Color.RED);
                gc.fillRect(game.getPlayer().getPosition().getv1()*2, game.getPlayer().getPosition().getv0()*2,3,3);
                if (baseLoc != null) {
                    gc.setFill(Color.DARKBLUE);
                    gc.fillRect(baseLoc.getv1() * 2, baseLoc.getv0() * 2, 3, 3);
                }
            }
        }
    }

    private void buildUI(){
        GraphicsContext gc = UI_ELEMENTS.getGraphicsContext2D();

        gc.setFill(Color.DARKGOLDENROD);
        gc.fillRect(0,0, 224,224);
        gc.fillRect(10,235, 204, 30);
        gc.fillRect(10, 295, 204, 30);
        gc.fillRect(10,365, 204, 30);
        gc.fillRect(10,435, 204, 100);

        Group group = new Group();

        ImageView calendarImageView = new ImageView(CALENDAR_IMAGE);
        calendarImageView.setX(15);
        calendarImageView.setY(232);

        curDayLabel = new Label("\t\tDay: " + currentDay);
        curDayLabel.setLayoutX(30);
        curDayLabel.setLayoutY(240);

        Label dayNightLabel = new Label("\t\tSteps Remaining");
        dayNightLabel.setLayoutX(30);
        dayNightLabel.setLayoutY(300);
        curStepsLabel = new Label("\t" + Integer.toString(game.getSTEPS_PER_CYCLE() - game.getPlayer().getStepsToday()));
        curStepsLabel.setLayoutX(30);
        curStepsLabel.setLayoutY(300);

        dayNightIcon = new ImageView(DAY_IMAGE);
        dayNightIcon.setLayoutX(15);
        dayNightIcon.setLayoutY(294);

        ImageView heart = new ImageView(HEART_IMAGE);
        heart.setX(15);
        heart.setY(362);

        Label HPLabel = new Label("\t\t\t/\t" + game.getPlayer().getMAX_HIT_POINTS() + "\tHP");
        HPLabel.setLayoutX(30);
        HPLabel.setLayoutY(370);
        curHPLabel = new Label("\t" + Integer.toString(currentPlayerHP));
        curHPLabel.setLayoutX(30);
        curHPLabel.setLayoutY(370);



        curBaseLabel = new Label("Current Base:\t\t" + baseStatusToString());
        curBaseLabel.setLayoutX(30);
        curBaseLabel.setLayoutY(440);

        baseIcon = new ImageView(BASE_IMAGES[0]);
        baseIcon.setLayoutX(120);
        baseIcon.setLayoutY(460);

        MINIMAP_CANVAS.setLayoutX(12);
        MINIMAP_CANVAS.setLayoutY(12);

        group.getChildren().addAll(new ImageView(BACK_PANEL_IMAGE),UI_ELEMENTS,MINIMAP_CANVAS, heart, HPLabel, curHPLabel, dayNightLabel, curStepsLabel, dayNightIcon, curBaseLabel, baseIcon, curDayLabel, calendarImageView);
        subscene = new SubScene(group, WIDTH, HEIGHT);



    }

    private String baseStatusToString(){
        String base = "";
        switch (curBase){
            case CAMP:
                base = "Camp";
                break;
            case SHACK:
                base = "Shack";
                break;
            case HOUSE:
                base = "House";
                break;
            case FORT:
                base = "Fort";
                break;
        }
        return base;
    }


    public SubScene getSubscene() {
        return subscene;
    }
}
