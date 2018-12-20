package View;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Carnivore;
import model.CombatEncounter;
import model.Game;

import java.util.ArrayList;

public class CombatViewManager {

    private static final int HEIGHT= 672;
    private static final int WIDTH = 672;

    private final Image COMBAT_PANE_IMAGE = new Image("View/Resources/combatPane.png");
    private AnimationTimer animationTimer;
    private SubScene subscene;
    private Canvas combatCanvas;
    private Group group;
    private GraphicsContext gc;
    private CombatEncounter combatEncounter;
    private Game game;
    private ArrayList<Button> buttons;
    private boolean messageDisplayed;
    private CombatDialogueBox cdb;
    private Scene parentScene;

    private int curEnemyHP;
    private Label enemyLabel;

    public CombatViewManager(Game game, CombatEncounter combatEncounter, Scene parentScene){
        buttons = new ArrayList<>();
        combatEncounter.setGame(game);
        messageDisplayed = false;
        combatCanvas = new Canvas(WIDTH,HEIGHT);
        gc = combatCanvas.getGraphicsContext2D();
        this.combatEncounter = combatEncounter;
        this.game = game;
        this.parentScene = parentScene;
        setUpCombatUI();
        cdb = null;
        subscene = new SubScene(group,WIDTH,HEIGHT);
        gameLoop();
    }

    private void setUpCombatUI() {
        ImageView combatUIPane = new ImageView(COMBAT_PANE_IMAGE);
        ImageView combatPortrait = new ImageView(combatEncounter.getAnimal().getCombatImage());
        combatPortrait.setLayoutX(136);
        combatPortrait.setLayoutY(3f / 4f * HEIGHT - 450);

        Button fightButton = new Button("FIGHT");
        Button flightButton = new Button("FLIGHT");
        fightButton.setOnAction(e -> {
            if (combatEncounter.isPlayersTurn()) {
                if (combatEncounter.getPlayer().attack(combatEncounter.getAnimal())) {
                    combatEncounter.setMessage("You attack the animal!\nYou strike it savagely!");
                } else {
                    combatEncounter.setMessage("You attack the animal!\nIt leaps out of the way!");
                }
                combatEncounter.setMessageToDisplay(true);
                combatEncounter.setPlayersTurn(false);
                combatEncounter.setAnimalsTurn(true);
            }
        });
        flightButton.setOnAction(e -> {
            if (combatEncounter.isPlayersTurn()) {
                if (combatEncounter.getAnimal() instanceof Carnivore) {
                    if (combatEncounter.getPlayer().flee()) {
                        combatEncounter.setMessage("You attempt to flee!");
                    } else {
                        combatEncounter.setMessage("You attempt to flee!\nThe animal blocks your path!");
                    }
                    combatEncounter.setMessageToDisplay(true);
                } else {
                    combatEncounter.getPlayer().setFled(true);
                }
                    combatEncounter.setPlayersTurn(false);
                    combatEncounter.setAnimalsTurn(true);

            }
        });

        fightButton.setFocusTraversable(false);
        flightButton.setFocusTraversable(false);
        buttons.add(fightButton);
        buttons.add(flightButton);
        fightButton.setLayoutX(100);
        fightButton.setLayoutY(3f/4f*HEIGHT);
        flightButton.setLayoutX(WIDTH - 100);
        flightButton.setLayoutY(3f/4f*HEIGHT);

        enemyLabel = new Label("Enemy HP:\t" + combatEncounter.getAnimal().getCurrentHitPoints() + " / "
                                + combatEncounter.getAnimal().getMAX_HIT_POINTS());
        enemyLabel.setLayoutX(300);
        enemyLabel.setLayoutY(30);
        curEnemyHP = combatEncounter.getAnimal().getCurrentHitPoints();

        group = new Group(combatUIPane,combatCanvas, fightButton, flightButton, combatPortrait, enemyLabel);

    }
    private void gameLoop(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (combatEncounter.isEncounterOver() && !combatEncounter.isMessageToDisplay()){
                    game.setInCombat(false);
                    game.setCurrentEncounter(null);
                    animationTimer.stop();
                }
                if (!combatEncounter.isMessageToDisplay() && !combatEncounter.isEncounterOver()) {
                    combatEncounter.combatTurn();
                }
                else {
                    if (!messageDisplayed) {
                        messageDisplayed = true;
                        cdb = new CombatDialogueBox(combatEncounter.getMessage(), parentScene);
                        group.getChildren().add(cdb);
                        cdb.displayMessage();
                    }
                    else {
                        if (cdb != null){
                            if (cdb.isMessageFinished()){
                                combatEncounter.setMessageToDisplay(false);
                                messageDisplayed = false;
                                cdb = null;
                            }
                        }
                    }
                }
                updateEnemyLabel();
            }
        };
        animationTimer.start();
    }

    private void updateEnemyLabel(){
        if (curEnemyHP != combatEncounter.getAnimal().getCurrentHitPoints()){
            curEnemyHP = combatEncounter.getAnimal().getCurrentHitPoints();
            enemyLabel.setText("Enemy HP:\t" + combatEncounter.getAnimal().getCurrentHitPoints() + " / "
                    + combatEncounter.getAnimal().getMAX_HIT_POINTS());
        }
    }


    public SubScene getCombatCombatSubscene() {
        return subscene;
    }
}
