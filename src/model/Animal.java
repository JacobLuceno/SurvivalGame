package model;


import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public abstract class Animal extends MobileObject implements iTakesTurns, Interactable{

    private int meatVal;
    private int damage;
    protected Vector2 targetPos;
    protected ArrayList<Vector2> currentPath;
    protected Vector2 nextPos;
    private int pathIndex;
    private boolean canWalkGrass;
    private boolean canWalkDesert;
    private boolean canWalkWater;
    private boolean pathCompleted;
    private boolean remove;
    private AStarPathFinding pathfinder;

    private final Image combatImage;


    private final Image gameImage;

    public Animal(Vector2 pos, Game game, int speed, int maxHP, int meatVal, int damage, boolean[] canWalk, Image combatImage, Image gameImage){
        super(pos, game, speed, maxHP);
        this.meatVal = meatVal;
        this.canWalkGrass = canWalk[0];
        this.canWalkDesert = canWalk[1];
        this.canWalkWater = canWalk[2];
        this.combatImage = combatImage;
        this.gameImage = gameImage;
        this.damage = damage;
        pathCompleted = true;
        remove = false;
        pathfinder = new AStarPathFinding(getGame(), new boolean[]{canWalkGrass, canWalkDesert, canWalkWater});
    }

    protected void chooseNewPosition() {
        Game game = getGame();
        TerrainTile[][] tMap = game.getMap().getTerrainMap();
        boolean pathChosen = false;
        do {
            targetPos = getPosition().getDisplacementVector(5, 5);
            if (targetPos.getv0() < game.getHEIGHT() && targetPos.getv1() < game.getWIDTH() && targetPos.getv0() >= 0 && targetPos.getv1() >= 0) {
                if (canWalkWater && tMap[(int) targetPos.getv0()][(int) targetPos.getv1()].getTerrainType() == TerrainTile.TerrainType.WATER) {
                    pathChosen = true;
                } else if (canWalkGrass && tMap[(int) targetPos.getv0()][(int) targetPos.getv1()].getTerrainType() == TerrainTile.TerrainType.GRASSLAND) {
                    if (tMap[(int) targetPos.getv0()][(int) targetPos.getv1()].getHasStatObj()) {
                        if (tMap[(int) targetPos.getv0()][(int) targetPos.getv1()].getStatObj().isPassable()) {
                            pathChosen = true;
                        }
                    } else {
                        pathChosen = true;
                    }
                } else if (canWalkDesert && tMap[(int) targetPos.getv0()][(int) targetPos.getv1()].getTerrainType() == TerrainTile.TerrainType.DESERT) {
                    if (tMap[(int) targetPos.getv0()][(int) targetPos.getv1()].getHasStatObj()) {
                        if (tMap[(int) targetPos.getv0()][(int) targetPos.getv1()].getStatObj().isPassable()) {
                            pathChosen = true;
                        }
                    }else {
                        pathChosen = true;
                    }
                }
            }
        } while (!pathChosen);
    }

    public void wander(){
        if (getPosition().getv1() == getGame().getPlayer().getPosition().getv1() && getPosition().getv0() == getGame().getPlayer().getPosition().getv0()){
            this.interact(getGame().getPlayer());
        }
        if (pathCompleted){
            chooseNewPosition();
            currentPath = pathfinder.findPath(getPosition(), targetPos);
            pathCompleted = false;
            pathIndex = 0;
        }
        if (getPosition().getv0() != currentPath.get(currentPath.size() - 1).getv0() || getPosition().getv1() != currentPath.get(currentPath.size() - 1).getv1()){
            pathIndex++;
            setPriorLoc(getPosition());
            setPosition(currentPath.get(pathIndex));
            setBusy(true);
        } else{
            currentPath.clear();
            pathCompleted = true;
        }
    }

    @Override
    public abstract boolean combatLogic(MobileObject target);

    @Override
    public void interact(Player player) {
        if (Game.getCurrentEncounter() == null) {
            Game.setCurrentEncounter(new CombatEncounter(player, this));
            Game.setInCombat(true);
        }
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }
    public Image getCombatImage() {
        return combatImage;
    }
    public Image getGameImage() {
        return gameImage;
    }

    @Override
    public boolean flee() {
        Random rand = new Random();
        int fleeing = rand.nextInt(4);
        if (fleeing == 1){
            setFled(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean attack(MobileObject target) {
        Random rand = new Random();
        int successful = rand.nextInt(2);
        switch(successful){
            case 0:
                return false;
            default:
                target.sufferHarm(damage);
                return true;
            }
        }



    public boolean isCanWalkGrass() {
        return canWalkGrass;
    }

    public boolean isCanWalkDesert() {
        return canWalkDesert;
    }

    public boolean isCanWalkWater() {
        return canWalkWater;
    }
}
