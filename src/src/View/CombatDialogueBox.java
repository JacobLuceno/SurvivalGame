package View;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyValue;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


public class CombatDialogueBox extends Group {


    private static final int GAME_HEIGHT= 672;
    private static final int GAME_WIDTH = 672;
    private static final int MESSAGE_HEIGHT = 300;
    private static final int MESSAGE_WIDTH = 650;
    private static final Image DIALOGUE_BOX = new Image("View/Resources/dialogueBox.png");

    private StackPane mainPane;
    private Scene parentScene;

    private boolean messageFinished;
    private String message;
    private AnimationTimer animationTimer;

    private boolean interactKeyDown;


    public CombatDialogueBox(String message, Scene parentScene) {
        messageFinished = false;
        interactKeyDown = false;
        this.message = message;
        this.parentScene = parentScene;
        setUpListener();
        setUpMessageBox();
    }

    private void setUpListener(){
        parentScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE){
                interactKeyDown = true;
            }
        });
        parentScene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SPACE){
                interactKeyDown = false;
            }
        });
    }
    private void setUpMessageBox(){
        this.setLayoutX(0);
        this.setLayoutY(0);
        mainPane  = new StackPane();
        mainPane.setLayoutX(GAME_WIDTH);
        mainPane.setLayoutY(GAME_HEIGHT/2);
        //mainPane.setMaxSize(MESSAGE_WIDTH, MESSAGE_HEIGHT);
        //mainPane.setMinSize(MESSAGE_WIDTH, MESSAGE_HEIGHT);
        Label curMessage = new Label(message);
        ImageView dialogueBoxBackground = new ImageView(DIALOGUE_BOX);
        mainPane.getChildren().addAll(dialogueBoxBackground,curMessage);
        this.getChildren().add(mainPane);
    }

    public void displayMessage(){
        TranslateTransition tt = new TranslateTransition(new Duration(250), mainPane);
        tt.setToX(-1*MESSAGE_WIDTH);
        tt.play();
        tt.setOnFinished(e -> {
            playerResponseListener();
        });
    }

    private void hideMessage(){
        TranslateTransition tt = new TranslateTransition(new Duration(150), mainPane);
        tt.setToX(-3*MESSAGE_WIDTH);
        tt.play();
        tt.setOnFinished(e -> messageFinished = true);
    }

    private void playerResponseListener(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
               if (interactKeyDown){
                   hideMessage();
                   animationTimer.stop();
               }
            }
        };
        animationTimer.start();
    }


    public boolean isMessageFinished() {
        return messageFinished;
    }

}
