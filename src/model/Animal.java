package model;


import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public abstract class Animal extends MobileObject implements iTakesTurns, Interactable{

    //These booleans define the type of terrain a given animal can walk through
    private boolean canWalkGrass;
    private boolean canWalkDesert;
    private boolean canWalkWater;
    //This number is used to define how much damage an animal does in combat. For herbivores it is set to 0, but
    //herbivores should have access to it in case in the future there is an herbivore which sometimes attacks in combat
    //rather than fleeing
    private int damage;

    //The following 5 members are used in the animal's pathfinding
    private Vector2 targetPos;
    private ArrayList<Vector2> currentPath;
    private int pathIndex;
    private boolean pathCompleted;
    private AStarPathFinding pathfinder;
    //these are the images the images the UI displays in various contexts
    private final Image combatImage;
    private final Image gameImage;
    //this is a boolean flag signifying the animal is ready to be removed from the game
    private boolean remove;


    public Animal(Vector2 pos, Game game, int speed, int maxHP, int damage, boolean[] canWalk, Image combatImage, Image gameImage){
        super(pos, game, speed, maxHP);
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


    //PATHFINDING AND MOVEMENT FUNCTIONS:
    //This function chooses a new position for the animal to walk to.  It randomly chooses a space within 5x and 5y positions
    //of its current position, checks that its a valid target (a terrain it can walk on, and a stationary object that's
    //passable.).  If the loop runs more than 5 times the function sets the target destination to the animals current
    //destination.  This is to insure that if the animal is unlucky and choosing a high number of invalid spots to
    //attempt to move to, the loop will break quickly so that the game isn't slowed down while the animal chooses a new
    //targetPosition.  It will another chance to choose a new path on the next gameLoop cycle.  This function is
    //overridden in the carnivore class to allow it specifically target the player if they are in range.
    protected void chooseNewPosition() {
        Game game = getGame();
        TerrainTile[][] tMap = game.getMap().getTerrainMap();
        boolean pathChosen = false;
        int attempts = 0;
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
            if (attempts > 4){
                targetPos = getPosition();
                break;
            }
            attempts++;
        } while (!pathChosen);
    }
    //This function checks if the animal shares a square with the player.   If they do, it calls interact to initiate
    //combat.  If not, it checks to see if the animal has completed its current walking path.  If it has, it sets a new
    //target position, resets the pathindex, and sets path completed to false.  The animal uses the A* pathfinding
    //algorithm to determine its path, and the pathfinder object returns an arrayList of Vector2 position coordinates.
    //if the animals current position is not its target position, the pathindex iterates, and the animal sets its
    //position to the next coordinate pair in its path.  Then it sets the busy flag so the UI knows to animate its
    //movement before it can move to the next coordinate.  Once the animal reaches its targetposition, it clears its
    //path and sets its pathcompleted flag to true, signifying on the next gameLoop pass that it's ready to choose a
    //new target position and path.
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

    //This is the function called whenever a player interacts with an animal.  It sets combat to true and passes the
    //current combatEncounter to the game object
    @Override
    public void interact(Player player) {
        Game game = getGame();
        if (game.getCurrentEncounter() == null) {
            game.setCurrentEncounter(new CombatEncounter(player, this));
            game.setInCombat(true);
        }
    }

    //COMBAT FUNCTIONS:
    //This function is overridden by carnivores and herbivores to determine their actions in combat
    @Override
    public abstract boolean combatLogic(MobileObject target);
    //The default implementation of fleeing.  The animal has a 1/4 chance to successfully flee each time the function is
    //called
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
    //Default implementation of attack.  Animal has 50/50 chance of successfully attacking its target, dealing its
    //damage value to the target's hit points on a success.  The function returns a boolean value to signify if the
    //attack was successful.
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

    //GETTERS AND SETTERS

    public boolean isCanWalkGrass() {
        return canWalkGrass;
    }
    public boolean isCanWalkDesert() {
        return canWalkDesert;
    }
    public boolean isCanWalkWater() {
        return canWalkWater;
    }
    public void setTargetPos(Vector2 targetPos) {
        this.targetPos = targetPos;
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
}
