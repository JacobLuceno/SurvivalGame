package model;

import javafx.scene.image.Image;


public class Herbivore extends Animal {

    public enum HerbType{RABBIT, FISH, DEER}

    private HerbType type;

    public Herbivore(Vector2 pos, Game game, int speed, int maxHP, int damage, boolean[] canWalk, Image combatImage, Image gameImage, HerbType type) {
        super(pos, game, speed, maxHP, damage, canWalk, combatImage, gameImage);
        this.type = type;
        setFled(false);
    }

    @Override
    public boolean combatLogic(MobileObject target) {
        return flee();
    }

    @Override
    public void interact(Player player) {
        Game game = getGame();
        if (game.getCurrentEncounter() == null) {
            game.setCurrentEncounter(new CombatEncounter(player, this));
            game.setInCombat(true);
        }
    }


    public HerbType getType() {
        return type;
    }



}
