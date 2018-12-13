package model;

import java.util.Random;

public class Player extends MobileObject{
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

    private final int MAX_WEIGHT = 10;
    private int currentWeight;
    private int sticks;
    private int stepsToday;
    private Tool tool;
    private boolean hidden;
    private boolean harvesting;
    private boolean resting;
    private boolean interactingWithBase;



    public Player(Vector2 pos, int maxHP){
        super(pos, 1, maxHP);
        stepsToday = 0;
        currentWeight = 0;
        sticks = 0;
        tool = Tool.HAND;
        hidden = false;
        harvesting = false;
        interactingWithBase = false;
        resting = false;
        setFacing(Game.Direction.DOWN);
    }


    public void upgradeTool(){
        tool = tool.upgrade();
    }
    public void spendSticks(int sticksSpent){
        sticks -= sticksSpent;
    }
    public void gatherSticks(int sticksHarvested) {sticks += sticksHarvested;}

    public void flee(){
        Random rand = new Random();
        int success = rand.nextInt(2);
        if (success == 1){
            setFled(true);
        }
    }

    public int attack(){
        Random rand = new Random();
        int successful = rand.nextInt(2);
        switch(successful){
            case 0:
                return 0;
            default:
                switch(tool){
                    case HAND:
                        return 1;
                    case STAKE:
                        return 2;
                    case ROCK:
                        return 3;
                    default:
                        return 4;
                }
        }
    }

    public void heal(int HP){
        int currentHP = this.getCurrentHitPoints() + HP;
        this.setCurrentHitPoints( (currentHP > this.getMAX_HIT_POINTS()) ? this.getMAX_HIT_POINTS() : currentHP);
    }

    public void takeStep(){
        stepsToday++;
    }
    public void attemptMove(TerrainTile t, Game game){
        if (t == null) {
            return;
        }
        for (Animal a : game.getAnimals()){
            if (t.getPosition().getv0() == a.getPosition().getv0() && t.getPosition().getv1() == a.getPosition().getv1()){
                a.interact(this);
                game.setAnimalTaggedForRemoval(true);
                break;
            }
        }
        if (Game.getCurrentEncounter() == null) {
            setPriorLoc(new Vector2(getPosition().getv0(), getPosition().getv1()));
            if (t.getHasStatObj()) {
                if (t.getStatObj() instanceof Interactable) {
                    t.getStatObj().interact(this);
                    if (t.getStatObjType() == TerrainTile.StatObjType.TREE) {
                        harvesting = true;
                        t.setHasStatObjNull();
                    }
                }
            } else {
                move(super.getFacing());
                takeStep();
                hidden = false;
            }
        }
    }


    //Getters and setters

    public int getStepsToday() {
        return stepsToday;
    }
    public void setStepsToday(int stepsToday) {
        this.stepsToday = stepsToday;
    }
    public int getCurrentWeight() {
        return currentWeight;
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
    public boolean isInteractingWithBase() {
        return interactingWithBase;
    }

    public void setInteractingWithBase(boolean interactingWithBase) {
        this.interactingWithBase = interactingWithBase;
    }
    public boolean isResting() {
        return resting;
    }

    public void setResting(boolean resting) {
        this.resting = resting;
    }




}
