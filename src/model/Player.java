package model;

import java.util.Random;

public class Player extends MobileObject {
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

    public Player(Vector2 pos, int maxHP){
        super(pos, 1, maxHP);
        stepsToday = 0;
        currentWeight = 0;
        sticks = 0;
        tool = Tool.HAND;
    }

    public void upgradeTool(){
        tool = tool.upgrade();
    }
    public void spendSticks(int sticksSpent){
        sticks -= sticksSpent;
    }
    public void gatherSticks(int sticksHarvested) {sticks += sticksHarvested;}
    public int attack(){
        Random rand = new Random();
        int successful = rand.nextInt(1);
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
}
