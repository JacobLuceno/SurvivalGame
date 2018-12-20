package model;

import javafx.scene.image.Image;

public class Carnivore extends Animal {

    public enum CarnType{WOLF, LION, CROC}

    private final int VIEW_DISTANCE = 7;
    private CarnType type;

    public Carnivore(Vector2 pos, Game game, int speed, int maxHP, int damage, boolean[] canWalk, Image combatImage, Image gameImage, CarnType type) {
        super(pos, game, speed, maxHP, damage, canWalk, combatImage, gameImage);
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
    protected void chooseNewPosition(){
        Game game = getGame();
        Player player = game.getPlayer();
        TerrainTile.TerrainType terrain = game.getMap().getTerrainMap()[(int)player.getPosition().getv0()][(int)player.getPosition().getv1()].getTerrainType();
        if (scanForPlayer(player, terrain)){
            setTargetPos(player.getPosition());
        }
        else {
            super.chooseNewPosition();
        }
    }

    private boolean scanForPlayer(Player player, TerrainTile.TerrainType terrain){
        if (getPosition().distance(player.getPosition()) <= VIEW_DISTANCE && !player.isHidden() && canWalk(terrain)){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean canWalk(TerrainTile.TerrainType terrain){
        switch (terrain){
            case WATER:
                return isCanWalkWater();
            case GRASSLAND:
                return isCanWalkGrass();
            case DESERT:
                return isCanWalkDesert();
            default:
                return false;
        }
    }



    public CarnType getType() {
        return type;
    }



}
