package controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import model.Animal;
import model.Game;
import model.Player;
import model.TerrainTile;

import java.util.ArrayList;


public class ControlManager {

    private Scene scene;
    private Game game;
    private final int VIEW_DISTANCE = 10;

    private boolean rightKeyDown;
    private boolean leftKeyDown;
    private boolean upKeyDown;
    private boolean downKeyDown;

    private AnimationTimer animationTimer;

    private boolean gameWasInCombat;
    private boolean resetListeners;

    public ControlManager(Scene scene, Game game){
        this.scene = scene;
        this.game = game;
        gameWasInCombat = false;
        resetListeners = false;
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
            if (e.getCode() == KeyCode.R){
                game.getPlayer().Rest();
            }
            if (e.getCode() == KeyCode.B){
                if (game.getPlayer().checkForBase()){
                    game.getBase().buyNewBaseUpgrade(game.getPlayer());
                }
            }
            if (e.getCode() == KeyCode.T){
                if (game.getPlayer().checkForBase()){
                    game.getBase().buyNewToolUpgrade(game.getPlayer());
                }
            }
        });
    }

    private void gameLoop(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (resetListeners){
                    game.mapRevealAfterFlee();
                    setUpListeners();
                    upKeyDown = false;
                    downKeyDown = false;
                    rightKeyDown = false;
                    leftKeyDown = false;
                    resetListeners = false;
                }
                update();
            }
        };
        animationTimer.start();
    }

    private void update(){
        if(!game.getPlayer().isBusy() && !Game.isInCombat()) {
            if (gameWasInCombat){
                resetListeners = true;
                gameWasInCombat = false;
            }
            if (upKeyDown) {
                game.getPlayer().setFacing(Game.Direction.UP);
                game.getPlayer().attemptMove(targetPos(), game);
                revealMiniMap(game.getPlayer().getFacing());
                game.checkForEncounter();
            } else if (downKeyDown) {
                game.getPlayer().setFacing(Game.Direction.DOWN);
                game.getPlayer().attemptMove(targetPos(), game);
                revealMiniMap(game.getPlayer().getFacing());
                game.checkForEncounter();
            } else if (rightKeyDown) {
                game.getPlayer().setFacing(Game.Direction.RIGHT);
                game.getPlayer().attemptMove(targetPos(), game);
                revealMiniMap(game.getPlayer().getFacing());
                game.checkForEncounter();
            } else if (leftKeyDown) {
                game.getPlayer().setFacing(Game.Direction.LEFT);
                game.getPlayer().attemptMove(targetPos(), game);
                revealMiniMap(game.getPlayer().getFacing());
                game.checkForEncounter();
            }
            game.despawnDistantAnimals();
            game.checkDayCycle();

            if (!game.getPlayer().isAlive()){
                game.setGameOver(true);
                game.setGameWon(false);
            } else{
                if (game.getDay() == game.getDaysToSurvive()){
                    game.setGameWon(true);
                    game.setGameOver(true);
                }
            }
        }
        if (game.isInCombat()){
            gameWasInCombat = true;
        } else{
            ArrayList<Animal> animals = game.getAnimals();
            for (Animal animal : animals){
                if (!animal.isBusy()){
                    animal.wander();
                }
            }
        }
    }

    private TerrainTile targetPos(){
        Player player = game.getPlayer();
        try {
            switch (player.getFacing()) {
                case UP:
                    return game.getMap().getTerrainMap()[(int) player.getPosition().getv0() - 1][(int) player.getPosition().getv1()];
                case DOWN:
                    return game.getMap().getTerrainMap()[(int) player.getPosition().getv0() + 1][(int) player.getPosition().getv1()];
                case LEFT:
                    return game.getMap().getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1() - 1];
                case RIGHT:
                    return game.getMap().getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1() + 1];
            }
            return game.getMap().getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()];
        }
        catch(IndexOutOfBoundsException e) {
            return null;
        }
    }

    private void revealMiniMap(Game.Direction facing){
        switch(facing){
            case UP:
                for (int x = (int) game.getPlayer().getPosition().getv1() - 10; x <= game.getPlayer().getPosition().getv1() + 10; x++){
                    try {
                        game.getMap().getTerrainMap()[(int) game.getPlayer().getPosition().getv0() - 10][x].setRevealedOnMiniMap(true);
                    }
                    catch (IndexOutOfBoundsException e){
                        //do nothing, it just means theres no map to reveal there
                    }
                }
                break;
            case DOWN:
                for (int x = (int) game.getPlayer().getPosition().getv1() - 10; x <= game.getPlayer().getPosition().getv1() + 10; x++){
                    try {
                        game.getMap().getTerrainMap()[(int) game.getPlayer().getPosition().getv0() + 10][x].setRevealedOnMiniMap(true);
                    }
                    catch (IndexOutOfBoundsException e){
                        //do nothing, it just means theres no map to reveal there
                    }
                }
                break;
            case LEFT:
                for (int y = (int) game.getPlayer().getPosition().getv0() - 10; y <= game.getPlayer().getPosition().getv0() + 10; y++){
                    try {
                        game.getMap().getTerrainMap()[y][(int) game.getPlayer().getPosition().getv1() - 10].setRevealedOnMiniMap(true);
                    }
                    catch (IndexOutOfBoundsException e){
                        //do nothing, it just means theres no map to reveal there
                    }
                }
                break;
            case RIGHT:
                for (int y = (int) game.getPlayer().getPosition().getv0() - 10; y <= game.getPlayer().getPosition().getv0() + 10; y++){
                    try {
                        game.getMap().getTerrainMap()[y][(int) game.getPlayer().getPosition().getv1() + 10].setRevealedOnMiniMap(true);
                    }
                    catch (IndexOutOfBoundsException e){
                        //do nothing, it just means theres no map to reveal there
                    }
                }
                break;
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



}
