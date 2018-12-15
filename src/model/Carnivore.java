package model;

import javafx.scene.image.Image;

public class Carnivore extends Animal {

    public enum CarnType{WOLF, LION, CROC}

    private final int VIEW_DISTANCE = 7;
    private CarnType type;

    public Carnivore(Vector2 pos, Game game, int speed, int maxHP, int meatVal, int damage, boolean[] canWalk, Image combatImage, Image gameImage, CarnType type) {
        super(pos, game, speed, maxHP, meatVal, damage, canWalk, combatImage, gameImage);
        this.type = type;
        setFled(false);
    }

    @Override
    public boolean combatLogic(MobileObject target) {
        if (attack(target)){
            return true;
        }
        return false;
    }

    @Override
    public void chooseNewPosition(){
        Game game = getGame();
        Player player = game.getPlayer();
        if (scanForPlayer(player)){
            targetPos = player.getPosition();
        }
        else {
            super.chooseNewPosition();
        }
    }

    private boolean scanForPlayer(Player player){
        if (getPosition().distance(player.getPosition()) <= VIEW_DISTANCE && !player.isHidden()){
            return true;
        }
        else {
            return false;
        }
    }






}
