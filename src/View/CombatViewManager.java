package View;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

    public CombatViewManager(Game game, CombatEncounter combatEncounter){
        buttons = new ArrayList<>();
        combatCanvas = new Canvas(WIDTH,HEIGHT);
        gc = combatCanvas.getGraphicsContext2D();
        this.combatEncounter = combatEncounter;
        this.game = game;
        setUpCombatUI();

        subscene = new SubScene(group,WIDTH,HEIGHT);
        System.out.println("CVM spawned");
        gameLoop();
    }

    private void setUpCombatUI(){
        ImageView combatUIPane = new ImageView(COMBAT_PANE_IMAGE);
        Button fightButton = new Button("FIGHT");
        Button flightButton = new Button("FLIGHT");
        fightButton.setOnAction(e->{
            if (combatEncounter.isPlayersTurn()) {
                System.out.println("Player attacks");
                combatEncounter.getAnimal().sufferHarm(combatEncounter.getPlayer().attack());
                combatEncounter.setPlayersTurn(false);
                combatEncounter.setAnimalsTurn(true);
            }
        });
        flightButton.setOnAction(e ->{
            if (combatEncounter.isPlayersTurn()) {
                System.out.println("Player attempts to flee");
                combatEncounter.getPlayer().flee();
                combatEncounter.setPlayersTurn(false);
                combatEncounter.setAnimalsTurn(true);
            }
        });

        buttons.add(fightButton);
        buttons.add(flightButton);
        fightButton.setLayoutX(100);
        fightButton.setLayoutY(3f/4f*HEIGHT);
        flightButton.setLayoutX(WIDTH - 100);
        flightButton.setLayoutY(3f/4f*HEIGHT);
        group = new Group(combatUIPane,combatCanvas, fightButton, flightButton);

    }
    private void gameLoop(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (combatEncounter.isEncounterOver()){
                    animationTimer.stop();
                }
                combatEncounter.combatTurn();
            }
        };
        animationTimer.start();
    }


    public SubScene getCombatCombatSubscene() {
        return subscene;
    }
}
