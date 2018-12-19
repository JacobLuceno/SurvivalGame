package model;

import javafx.scene.image.Image;


public class Herbivore extends Animal {

    public enum HerbType{RABBIT, FISH, DEER}

    private HerbType type;

    public Herbivore(Vector2 pos, Game game, int speed, int maxHP, int meatVal, int damage, boolean[] canWalk, Image combatImage, Image gameImage, HerbType type) {
        super(pos, game, speed, maxHP, meatVal, damage, canWalk, combatImage, gameImage);
        this.type = type;
        setFled(false);
    }

    @Override
    public boolean combatLogic(MobileObject target) {
        if (flee()){
            return true;
        }
        return false;
    }

    @Override
    public void interact(Player player) {
        if (Game.getCurrentEncounter() == null) {
            Game.setCurrentEncounter(new CombatEncounter(player, this));
            Game.setInCombat(true);
        }
    }


    public HerbType getType() {
        return type;
    }



}
