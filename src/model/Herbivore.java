package model;

import javafx.scene.image.Image;

import java.util.Random;

public class Herbivore extends Animal {

    public enum HerbType{RABBIT, FISH, DEER}

    private HerbType type;

    public Herbivore(Vector2 pos, int speed, int maxHP, int meatVal, boolean[] canWalk, Image combatImage, Image gameImage, HerbType type) {
        super(pos, speed, maxHP, meatVal, canWalk, combatImage, gameImage);
        this.type = type;
        setFled(false);
    }

    @Override
    public void combatLogic() {
        System.out.println("Animal took turn");
        Random rand = new Random();
        int fleeing = rand.nextInt(4);
        if (fleeing == 1){
            setFled(true);
            System.out.println("Animal has fled");
        }
    }

    @Override
    public void interact(Player player) {
        if (Game.getCurrentEncounter() == null) {
            Game.setCurrentEncounter(new CombatEncounter(player, this));
            Game.setInCombat(true);
        }
    }


}
