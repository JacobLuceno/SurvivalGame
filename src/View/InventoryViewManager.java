package View;

import javafx.animation.AnimationTimer;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.Food;
import model.Game;
import model.Inventory;
import model.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class InventoryViewManager {
    private Game game;
    private static final int HEIGHT= 672;
    private static final int WIDTH = 224;
    private static final int BUTTON_SIZE = 64;
    private final Canvas INVENTORY_CANVAS;
    private static final Image BACK_PANEL_IMAGE = new Image("View/Resources/minimap backpanel.png");
    private static final Image RABBIT_ICON = new Image("/View/Resources/rabbit_icon.png", BUTTON_SIZE, BUTTON_SIZE, true, false);
    private static final Image FISH_ICON = new Image("/View/Resources/fish_icon.png", BUTTON_SIZE, BUTTON_SIZE, true, false);
    private static final Image DEER_ICON = new Image("/View/Resources/deer_icon.png", BUTTON_SIZE, BUTTON_SIZE, true, false);
    private static final Image LION_ICON = new Image("/View/Resources/lion_icon.png", BUTTON_SIZE, BUTTON_SIZE, true, false);
    private static final Image CROC_ICON = new Image("/View/Resources/croc_icon.png", BUTTON_SIZE, BUTTON_SIZE, true, false);
    private static final Image WOLF_ICON = new Image("/View/Resources/wolf_icon.png", BUTTON_SIZE, BUTTON_SIZE, true, false);
    private static final Image BERRY_ICON = new Image("/View/Resources/berry_icon.png", BUTTON_SIZE, BUTTON_SIZE, true, false);
    private static final Image CLICKED_ICON = new Image("/View/Resources/clicked_icon.png", BUTTON_SIZE, BUTTON_SIZE, true, false);
    private static final Image [] Tool_IMAGES = new Image[]{
            new Image("View/Resources/hand.png", 64, 64, true, false),
            new Image("View/Resources/stake.png", 64, 64, true, false),
            new Image("View/Resources/dagger.png", 64, 64, true, false),
            new Image ("View/Resources/spear.png", 64, 64, true, false)};
    private static final Image STICK_ICON = new Image("/View/Resources/stick.png", 32, 32, true, false);

    private SubScene subscene;
    private Group buttonGroup;
    private Group mainGroup;

    private int numBerry;
    private int numRab;
    private int numDeer;
    private int numFish;
    private int numWolf;
    private int numLion;
    private int numCroc;

    private Label numBerryLab;
    private Label numRabLab;
    private Label numDeerLab;
    private Label numFishLab;
    private Label numWolfLab;
    private Label numLionLab;
    private Label numCrocLab;

    private Player.Tool curTool;
    private ImageView curToolIcon;
    private Label curToolLabel;
    private int curSticks;
    private Label curSticksLabel;
    private int curWt;
    private Label curWtLab;

    private class ImageButton extends Parent {
        private ImageView iv;
        ImageButton(Image released, Food.FoodType foodtype) {
            this.iv = new ImageView(released);
            this.getChildren().add(this.iv);

            this.iv.setOnMousePressed(e -> iv.setImage(CLICKED_ICON));

            this.iv.setOnMouseReleased(e -> {
                iv.setImage(released);
                game.getPlayer().eat(foodtype);
            });

        }
    }

    public InventoryViewManager(Game game){
        this.game = game;
        mainGroup = new Group();
        INVENTORY_CANVAS = new Canvas(WIDTH, HEIGHT);
        mainGroup.getChildren().addAll(new ImageView(BACK_PANEL_IMAGE), INVENTORY_CANVAS);
        buttonGroup = new Group();
        numBerry = 0;
        numRab = 0;
        numDeer = 0;
        numFish = 0;
        numWolf = 0;
        numLion = 0;
        numCroc = 0;
        curSticks = 0;
        curTool = Player.Tool.HAND;
        setUpUI();
        setUpButtons();
        mainGroup.getChildren().addAll(buttonGroup, curToolLabel, curToolIcon);
        subscene = new SubScene(mainGroup, WIDTH, HEIGHT);

        gameLoop();
    }

    private void setUpUI(){
        GraphicsContext gc = INVENTORY_CANVAS.getGraphicsContext2D();

        gc.setFill(Color.DARKGOLDENROD);
        gc.fillRect(18,192, 192,440);
        gc.fillRect(18,10, 190, 100);
        gc.fillRect(18, 135, 190, 30);

        curToolLabel = new Label("CURRENT TOOL\n\n" + toolToString());
        curToolLabel.setLayoutX(38);
        curToolLabel.setLayoutY(20);
        try{
            curToolLabel.setFont(Font.loadFont(new FileInputStream("src/View/Resources/ARCADECLASSIC.TTF"), 15));
        } catch (FileNotFoundException e) {
            System.out.print("Font not found");
            curToolLabel.setFont(Font.getDefault());
        }

        curToolIcon = new ImageView(Tool_IMAGES[0]);
        curToolIcon.setLayoutX(128);
        curToolIcon.setLayoutY(40);

        ImageView stickImageView = new ImageView(STICK_ICON);
        stickImageView.setLayoutY(135);
        stickImageView.setLayoutX(18);

        curSticksLabel = new Label("\t\t\tSticks\tx\t" + curSticks);
        curSticksLabel.setLayoutX(28);
        curSticksLabel.setLayoutY(142);
        try{
            curSticksLabel.setFont(Font.loadFont(new FileInputStream("src/View/Resources/ARCADECLASSIC.TTF"), 15));
        } catch (FileNotFoundException e) {
            System.out.print("Font not found");
            curSticksLabel.setFont(Font.getDefault());
        }

        curWtLab = new Label("Current \nWeight\n\n" + curWt + " of " + game.getPlayer().getMAX_WEIGHT());
        curWtLab.setLayoutX(36);
        curWtLab.setLayoutY(212);
        try{
            curWtLab.setFont(Font.loadFont(new FileInputStream("src/View/Resources/ARCADECLASSIC.TTF"), 15));
        } catch (FileNotFoundException e) {
            System.out.print("Font not found");
            curWtLab.setFont(Font.getDefault());
        }

        mainGroup.getChildren().addAll(stickImageView, curSticksLabel,curWtLab);
    }

    private void setUpButtons() {
        ArrayList<ImageButton> buttons = new ArrayList<>();

        ImageButton berryButton = new ImageButton(BERRY_ICON, Food.FoodType.Berries);
        berryButton.setLayoutX(128);
        berryButton.setLayoutY(220);
        buttons.add(berryButton);

        ImageButton rabbitButton = new ImageButton(RABBIT_ICON, Food.FoodType.Rabbit);
        rabbitButton.setLayoutY(330);
        rabbitButton.setLayoutX(28);
        buttons.add(rabbitButton);

        ImageButton deerButton = new ImageButton(DEER_ICON, Food.FoodType.Deer);
        deerButton.setLayoutY(440);
        deerButton.setLayoutX(28);
        buttons.add(deerButton);

        ImageButton fishButton = new ImageButton(FISH_ICON, Food.FoodType.Fish);
        fishButton.setLayoutY(550);
        fishButton.setLayoutX(28);
        buttons.add(fishButton);

        ImageButton wolfButton = new ImageButton(WOLF_ICON, Food.FoodType.Wolf);
        wolfButton.setLayoutX(128);
        wolfButton.setLayoutY(330);
        buttons.add(wolfButton);

        ImageButton lionButton = new ImageButton(LION_ICON, Food.FoodType.Lion);
        lionButton.setLayoutX(128);
        lionButton.setLayoutY(440);
        buttons.add(lionButton);

        ImageButton crocodileButton = new ImageButton(CROC_ICON, Food.FoodType.Crocodile);
        crocodileButton.setLayoutX(128);
        crocodileButton.setLayoutY(550);
        buttons.add(crocodileButton);

        ArrayList<Label> buttonLabels = new ArrayList<>();

        Label berry = new Label("BERRY");
        berry.setLayoutX(128);
        berry.setLayoutY(195);
        buttonLabels.add(berry);

        Label rab = new Label("RABBIT");
        rab.setLayoutX(28);
        rab.setLayoutY(305);
        buttonLabels.add(rab);

        Label der = new Label("DEER");
        der.setLayoutX(28);
        der.setLayoutY(415);
        buttonLabels.add(der);

        Label fis = new Label("FISH");
        fis.setLayoutX(28);
        fis.setLayoutY(525);
        buttonLabels.add(fis);

        Label wolf = new Label("WOLF");
        wolf.setLayoutX(128);
        wolf.setLayoutY(305);
        buttonLabels.add(wolf);

        Label lion = new Label("LION");
        lion.setLayoutX(128);
        lion.setLayoutY(415);
        buttonLabels.add(lion);

        Label croc = new Label("CROCODILE");
        croc.setLayoutX(108);
        croc.setLayoutY(525);
        buttonLabels.add(croc);

        ArrayList<Label> tallies = new ArrayList<>();

        numBerryLab = new Label("x" + numBerry);
        numBerryLab.setLayoutX(183);
        numBerryLab.setLayoutY(285);
        tallies.add(numBerryLab);

        numRabLab = new Label("x" + numRab);
        numRabLab.setLayoutX(83);
        numRabLab.setLayoutY(395);
        tallies.add(numRabLab);

        numDeerLab = new Label("x" + numDeer);
        numDeerLab.setLayoutX(83);
        numDeerLab.setLayoutY(505);
        tallies.add(numDeerLab);

        numFishLab = new Label("x" + numFish);
        numFishLab.setLayoutX(83);
        numFishLab.setLayoutY(615);
        tallies.add(numFishLab);

        numWolfLab = new Label("x" + numWolf);
        numWolfLab.setLayoutX(183);
        numWolfLab.setLayoutY(395);
        tallies.add(numWolfLab);

        numLionLab = new Label("x" + numLion);
        numLionLab.setLayoutX(183);
        numLionLab.setLayoutY(505);
        tallies.add(numLionLab);

        numCrocLab = new Label("x" + numCroc);
        numCrocLab.setLayoutX(183);
        numCrocLab.setLayoutY(615);
        tallies.add(numCrocLab);


        for (Label l : buttonLabels) {
            try {
                l.setFont(Font.loadFont(new FileInputStream("src/View/Resources/ARCADECLASSIC.TTF"), 20));
            } catch (FileNotFoundException e) {
                System.out.print("Font not found");
                l.setFont(Font.getDefault());
            }
            buttonGroup.getChildren().add(l);
        }

        for (Label l : tallies) {
            try {
                l.setFont(Font.loadFont(new FileInputStream("src/View/Resources/ARCADECLASSIC.TTF"), 15));
            } catch (FileNotFoundException e) {
                System.out.print("Font not found");
                l.setFont(Font.getDefault());
            }
            buttonGroup.getChildren().add(l);
        }

        for (ImageButton ib : buttons){
            buttonGroup.getChildren().add(ib);
        }
    }

    public void updateQuantities () {
        Inventory inv =  game.getPlayer().getInventory();
        if (numBerry != inv.getNumberOfBerries()){
            numBerry = inv.getNumberOfBerries();
            numBerryLab.setText("x" + numBerry);
        }
        if (numRab != inv.getNumberOfRabbits()){
            numRab = inv.getNumberOfRabbits();
            numRabLab.setText("x" + numRab);
        }
        if (numDeer != inv.getNumberOfDeer()){
            numDeer = inv.getNumberOfDeer();
            numDeerLab.setText("x" + numDeer);
        }
        if (numFish != inv.getNumberOfFish()){
            numFish = inv.getNumberOfFish();
            numFishLab.setText("x" + numFish);
        }
        if (numWolf != inv.getNumberOfWolves()){
            numWolf = inv.getNumberOfWolves();
            numWolfLab.setText("x" + numWolf);
        }
        if (numLion != inv.getNumberOfLions()){
            numLion = inv.getNumberOfLions();
            numLionLab.setText("x" + numLion);
        }
        if (numCroc != inv.getNumberOfCrocodiles()){
            numCroc = inv.getNumberOfCrocodiles();
            numCrocLab.setText("x" + numCroc);
        }
    }

    private void updateTool(){
        if (curTool != game.getPlayer().getTool()){
            curTool = game.getPlayer().getTool();
            curToolLabel.setText("CURRENT TOOL\n\n" + toolToString());
            curToolIcon.setImage(Tool_IMAGES[curTool.ordinal()]);
        }
    }

    private void updateStick(){
        if (curSticks != game.getPlayer().getSticks()){
            curSticks = game.getPlayer().getSticks();
            curSticksLabel.setText("\t\t\tSticks\tx\t" + curSticks);
        }
    }

    private void updateWeight(){
        if (curWt != game.getPlayer().getInventory().getCurrentWeight()){
            curWt = game.getPlayer().getInventory().getCurrentWeight();
            curWtLab.setText("Current \nWeight\n\n" + curWt + " of " + game.getPlayer().getMAX_WEIGHT());
        }
    }

    public void gameLoop() {
        AnimationTimer t = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateQuantities();
                updateTool();
                updateStick();
                updateWeight();
            }
        };
        t.start();
    }

    /*
    public ArrayList<ImageButton> getButtons() {
        return buttonsList;
    }*/
    private String toolToString(){
        switch(curTool){
            case HAND:
                return "Bare Hands";
            case STAKE:
                return "Wooden\n Stake";
            case ROCK:
                return "Stone Tip\n Dagger";
            case SPEAR:
                return "Ornate\n Spear";
            default:
                return "Invalid Weapon";
        }
    }



    public SubScene getSubscene() {
        return subscene;
    }
}