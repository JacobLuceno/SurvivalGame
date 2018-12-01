package controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import model.Game;

public class ControlManager {

    private Scene scene;
    private Game game;

    private boolean rightKeyDown;
    private boolean leftKeyDown;
    private boolean upKeyDown;
    private boolean downKeyDown;
    private boolean interactKeyDown;

    private AnimationTimer animationTimer;

    public ControlManager(Scene scene, Game game){
        this.scene = scene;
        this.game = game;
        setUpListeners();
        gameLoop();
    }

    private void setUpListeners(){
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP){
                setUpKeyDown(true);
            }
            if (e.getCode() == KeyCode.DOWN){
                setDownKeyDown(true);
            }
            if (e.getCode() == KeyCode.RIGHT){
                setRightKeyDown(true);
            }
            if (e.getCode() == KeyCode.LEFT){
                setLeftKeyDown(true);
            }
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE){
                setInteractKeyDown(true);
            }
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.UP){
                setUpKeyDown(false);
            }
            if (e.getCode() == KeyCode.DOWN){
                setDownKeyDown(false);
            }
            if (e.getCode() == KeyCode.RIGHT){
                setRightKeyDown(false);
            }
            if (e.getCode() == KeyCode.LEFT){
                setLeftKeyDown(false);
            }
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE){
                setInteractKeyDown(false);
            }
        });

    }

    private void gameLoop(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        animationTimer.start();
    }

    private void update(){
        if(!game.getPlayer().isBusy()) {
            if (upKeyDown) {
                game.getPlayer().move(Game.Direction.UP, game.getWIDTH(), game.getHEIGHT());
            } else if (downKeyDown) {
                game.getPlayer().move(Game.Direction.DOWN, game.getWIDTH(), game.getHEIGHT());
            } else if (rightKeyDown) {
                game.getPlayer().move(Game.Direction.RIGHT, game.getWIDTH(), game.getHEIGHT());
            } else if (leftKeyDown) {
                game.getPlayer().move(Game.Direction.LEFT, game.getWIDTH(), game.getHEIGHT());
            }
        }
    }



    public void setDownKeyDown(boolean downKeyDown) {
        this.downKeyDown = downKeyDown;
    }

    public void setLeftKeyDown(boolean leftKeyDown) {
        this.leftKeyDown = leftKeyDown;
    }

    public void setRightKeyDown(boolean rightKeyDown) {
        this.rightKeyDown = rightKeyDown;
    }

    public void setUpKeyDown(boolean upKeyDown) {
        this.upKeyDown = upKeyDown;
    }

    public void setInteractKeyDown(boolean interactKeyDown) {
        this.interactKeyDown = interactKeyDown;
    }


}
