package model;

import java.util.Random;

public class Player extends MobileObject{
    //Enum for tool type. contains a function to update to the next tool.  used by the UI and the player class
    public enum Tool {HAND, STAKE, ROCK, SPEAR;
        public Tool upgrade(){
            try {
                return values()[ordinal() + 1];
            }
            catch(Exception e) {
                return values()[ordinal()];
            }
        }
    }

    //Final Variables that define the player's capabilities
    private final int MAX_WEIGHT = 10;

    //Variables which describe the player's state:
    private boolean hidden;
    private boolean harvesting;
    private boolean resting;
    private boolean canRest;
    private Game.Direction facing;
    private int stepsToday;
    private int cyclesSinceRest;
    private iRestable curRestObj;

    //Items the player is currently equipped with
    private int sticks;
    private Inventory inventory;
    private Tool tool;

    //Tags for the UI to tell it when to present certain prompts to the user regarding whether they can rest or
    //upgrade their base/tools
    private boolean displayRestPrompt;
    private boolean displayUpgradesPrompt;


    public Player(Vector2 pos, Game game, int maxHP){
        super(pos, game,1, maxHP);
        stepsToday = 0;
        sticks = 0;
        tool = Tool.HAND;
        canRest = false;
        hidden = false;
        harvesting = false;
        resting = false;
        cyclesSinceRest = 1;
        inventory = new Inventory(MAX_WEIGHT);
        setFacing(Game.Direction.DOWN);
        displayUpgradesPrompt = false;
        displayRestPrompt = false;
    }

    //Package Private Methods called by the Model
    //upgradeTool - called by the base when the player is present at it and decides to upgrade their tool.  It is called
    //              from the Base class to represent the abstraction that the player must be present at Base to upgrade
    //              their tools.
    //spendSticks - called when the player incurs a cost.  If the player as enough sticks to cover the cost, they are
    //              subtracted from the player's sticks, and the function returns true.  If that player cannot afford
    //              the upgrade, the function simply returns false instead.
    //gatherSticks - gather sticks is called by Tree class when the player interacts with it.  It adds the sticks
    //              harvested from the tree to the player's total.
    //heal - Takes an integer parameters and increases currentHP by that amount. If that amount is greater than the
    //       player's max health, the currentHP is set to MaxHP instead.
    //takeStep - simply iterates stepsToday.  Used for interactable objects which let the player move into their grid
    //           space
    void upgradeTool(){
        tool = tool.upgrade();
    }
    boolean spendSticks(int sticksSpent){
        if (getSticks() - sticksSpent >= 0){
            sticks -= sticksSpent;
            return true;
        }
        return false;
    }
    void gatherSticks(int sticksHarvested) {sticks += sticksHarvested;}
    void heal(int HP){
        int currentHP = this.getCurrentHitPoints() + HP;
        this.setCurrentHitPoints( (currentHP > this.getMAX_HIT_POINTS()) ? this.getMAX_HIT_POINTS() : currentHP);
    }
    void takeStep(){
        stepsToday++;
    }

    //Scans the inventory for a specific type of food, and if it is found in inventory it is removed, and the player
    //heals by the food's given HP value
    public void eat(Food.FoodType type){
        Food eaten = inventory.removeFood(type);
        if (eaten != null){
            heal(eaten.getHpValue());
        }
    }

    //Combat Methods:
    //Flee - when the player attempts to flee from a carnivore they run the risk of failure.  This function flips a coin
    //       to determine whether the player is successful or not, sets "isFled" accordingly, and returns the player's
    //       success state in the form of a boolean
    //Attack - takes a target as a parameter, and flips a coin.  If the player is successful they deal damage to the
    //         target depending on their weapon.
    @Override
    public boolean flee(){
        Random rand = new Random();
        boolean success = rand.nextBoolean();
        if (success){
            setFled(true);
            return true;
        }
        return false;
    }
    @Override
    public boolean attack(MobileObject target){
        System.out.println("Player attacks");
        Random rand = new Random();
        boolean successful = rand.nextBoolean();
        if (successful){
                switch(tool){
                    case HAND:
                        target.sufferHarm(1);
                        break;
                    case STAKE:
                        target.sufferHarm(2);
                        break;
                    case ROCK:
                        target.sufferHarm(3);
                        break;
                    case SPEAR:
                        target.sufferHarm(4);
                        break;
                    default:
                        System.out.println("Using ill-defined weapon.");
                }
                return true;
        } else {
            return false;
        }
    }

    //This function analyzes a proposed grid space for animals present, and interactable objects.  If there are animals,
    //combat is triggered.  If there are stationaryObjects, their interact methods are called.  If there is empty space,
    //the player moves into the square.
    public void attemptMove(TerrainTile t){
        //null check to see if invalid TerrainTile has been passed.
        if (t == null) {
            return;
        }

        Game game = getGame();

        //Checks to see if there is an animal in the space the player is attempting to move into.  If so, interact is
        //called on that animal.
        for (Animal a : game.getAnimals()){
            if (t.getPosition().getv0() == a.getPosition().getv0() && t.getPosition().getv1() == a.getPosition().getv1()
                || t.getPosition().getv0() == a.getPriorLoc().getv0() && t.getPosition().getv1() == a.getPriorLoc().getv1()){
                displayUpgradesPrompt = false;
                displayRestPrompt = false;
                //In the case of all animals, interact will set the game.currentCombatEncounter
                a.interact(this);
                break;
            }
        }
        //Equivalent statement to "if animal not encountered" or if animal interacts outside of combat (does not occur)
        if (game.getCurrentEncounter() == null) {
            //set prior location to current location before changing current location
            setPriorLoc(new Vector2(getPosition().getv0(), getPosition().getv1()));
            //since all stationaryObjects implement interactable, interact can and should be called.
            if (t.getHasStatObj()) {
                    t.getStatObj().interact(this);
                    //detects trees and bushes, and sets them null to represent player harvesting them
                    if (t.getStatObjType() == TerrainTile.StatObjType.TREE
                        || t.getStatObjType() == TerrainTile.StatObjType.BUSH) {
                        harvesting = true;
                        t.setHasStatObjNull();
                    }
            //If no stationaryObject is present on TerrainTile t, the player moves into that tile, their steps increase,
            // and they are considered unhidden (had their been a rock in the space the player would automatically) be
            // set hidden when calling interact on the rock.  Setting hidden false here guarantees that when a player
            // leaves a grid square in which they are hidden they are revealed by default.
            } else {
                setPosition(t.getPosition());
                takeStep();
                hidden = false;
            }
            //checkForBase checks to see if the player is adjacent to their base
            displayUpgradesPrompt = checkForBase();
            //If the player is near a restable object, and they haven't rested in at least 1 full cycle they can rest.
            //also sets "canRest"
            displayRestPrompt = checkForRestable();
        }
    }

    //If the player currently meets the criteria for resting, this function sets the player so they can not currently
    //rest again, turns off the rest/upgrade prompt, clears the animals from the board, sets the resting flag true, and finally
    //calls .restAt for iRestable object in range.
    public void AttemptRest(){
        if (canRest) {
            canRest = false;
            displayRestPrompt = false;
            displayUpgradesPrompt = false;
            resting = true;
            getGame().clearAllAnimals();
            curRestObj.restAt(this);
        }
    }

    //Adjacency and Criteria checking functions:
    //checkForRestable - this function scans through the adjacent squares, and if the encounters a restable object
    //                  sets that object as the current rest target.  If the player is sufficiently "tired" IE if the
    //                  player hasn't rested in a full cycle, it sets canRest true, and returns true.
    //cehckForBase - iterates through adjacent squares and returns true if the player is next to the base.
    private boolean checkForRestable(){
        int x = (int) getPosition().getv1();
        int y = (int) getPosition().getv0();
        TerrainTile[][] tMap = getGame().getMap().getTerrainMap();

        for (int i = y - 1; i < y + 2; i++) {
            for (int j = x - 1; j < x + 2; j++) {
                try {
                    if (tMap[i][j].getHasStatObj()) {
                        StationaryObject statObj = tMap[i][j].getStatObj();
                        if (statObj instanceof iRestable) {
                            curRestObj = (iRestable) statObj;
                            if (cyclesSinceRest >= 2) {
                                canRest = true;
                                return true;
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e){
                    // do nothing. just referencing a grid space out of bounds
                }
            }
        }
        return false;
    }
    public boolean checkForBase(){
        int x = (int) getPosition().getv1();
        int y = (int) getPosition().getv0();
        TerrainTile[][] tMap = getGame().getMap().getTerrainMap();

        for (int i = y - 1; i < y + 2; i++) {
            for (int j = x - 1; j < x + 2; j++) {
                try {
                    if (tMap[i][j].getHasStatObj()) {
                        if (tMap[i][j].getStatObj() instanceof Base) {
                            return true;
                        }
                    }
                } catch (IndexOutOfBoundsException e){
                    //do nothing. just a space off the map
                }
            }
        }
        return false;
    }


    //Getters and setters

    public int getStepsToday() {
        return stepsToday;
    }
    public void setStepsToday(int stepsToday) {
        this.stepsToday = stepsToday;
    }
    public int getSticks() {
        return sticks;
    }
    public Tool getTool() {
        return tool;
    }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    public Boolean isHidden() {
        return hidden;
    }
    public void setHarvesting(boolean harvesting) {
        this.harvesting = harvesting;
    }
    public boolean isHarvesting() {
        return harvesting;
    }
    public boolean isResting() {
        return resting;
    }
    public void setResting(boolean resting) {
        this.resting = resting;
    }
    public int getCyclesSinceRest() {
        return cyclesSinceRest;
    }
    public void setCyclesSinceRest(int cyclesSinceRest) {
        this.cyclesSinceRest = cyclesSinceRest;
    }
    public boolean isDisplayRestPrompt() {
        return displayRestPrompt;
    }
    public boolean isDisplayUpgradesPrompt() {
        return displayUpgradesPrompt;
    }
    public Inventory getInventory() {
        return inventory;
    }
    public Game.Direction getFacing() {
        return facing;
    }
    public void setFacing(Game.Direction facing) {
        this.facing = facing;
    }
    public int getMAX_WEIGHT() {
        return MAX_WEIGHT;
    }



}
