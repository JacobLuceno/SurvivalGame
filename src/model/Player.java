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
    private boolean displayRestPrompt;
    private boolean displayUpgradesPrompt;
    private int cyclesSinceRest;



    public Player(Vector2 pos, Game game, int maxHP){
        super(pos, game,1, maxHP);
        stepsToday = 0;
        currentWeight = 0;
        sticks = 0;
        tool = Tool.HAND;
        hidden = false;
        harvesting = false;
        interactingWithBase = false;
        resting = false;
        cyclesSinceRest = 1;
        setFacing(Game.Direction.DOWN);
        if (checkForBase()){
            displayUpgradesPrompt = true;
        }
        if (checkForRestable()){
            displayRestPrompt = true;
        }
    }


    public void upgradeTool(){
        tool = tool.upgrade();
    }
    public boolean spendSticks(int sticksSpent){
        if (getSticks() - sticksSpent >= 0){
            sticks -= sticksSpent;
            return true;
        }
        return false;
    }
    public void gatherSticks(int sticksHarvested) {sticks += sticksHarvested;}


    @Override
    public boolean flee(){
        System.out.println("Player attempts to flee");
        Random rand = new Random();
        int success = rand.nextInt(2);
        if (success == 1){
            System.out.println("Player has fled");
            setFled(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean attack(MobileObject target){
        System.out.println("Player attacks");
        Random rand = new Random();
        int successful = rand.nextInt(2);
        switch(successful){
            case 0:
                System.out.println("Player misses");
                return false;
            default:
                System.out.println("Player hits!");
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
                    default:
                        target.sufferHarm(4);
                }
                return true;
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
            if (t.getPosition().getv0() == a.getPosition().getv0() && t.getPosition().getv1() == a.getPosition().getv1()
                || t.getPosition().getv0() == a.getPriorLoc().getv0() && t.getPosition().getv1() == a.getPriorLoc().getv1()){
                displayUpgradesPrompt = false;
                displayRestPrompt = false;
                a.interact(this);
                break;
            }
        }

        if (game.getCurrentEncounter() == null) {
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
                move(getFacing());
                takeStep();
                hidden = false;

            }
            displayRestPrompt = false;
            displayUpgradesPrompt = false;
            if (checkForBase()){
                displayUpgradesPrompt = true;
            }
            if (checkForRestable()){
                displayRestPrompt = true;
            }

        }
    }
    public void Rest(){
        if (cyclesSinceRest >= 2) {
            int x = (int) getPosition().getv1();
            int y = (int) getPosition().getv0();
            TerrainTile[][] tMap = getGame().getMap().getTerrainMap();

            for (int i = y - 1; i < y + 2; i++) {
                for (int j = x - 1; j < x + 2; j++) {
                    try{
                        if (tMap[i][j].getHasStatObj()) {
                            if (tMap[i][j].getStatObj() instanceof iRestable) {
                                resting = true;
                                getGame().clearAllAnimals();
                                ((iRestable) tMap[i][j].getStatObj()).restAt(this);
                                break;
                            }
                        }
                    } catch(IndexOutOfBoundsException e){
                        //do nothing. just an illegal space to check
                    }
                }

            }
        }
    }

    public boolean checkForRestable(){
        int x = (int) getPosition().getv1();
        int y = (int) getPosition().getv0();
        TerrainTile[][] tMap = getGame().getMap().getTerrainMap();

        for (int i = y - 1; i < y + 2; i++) {
            for (int j = x - 1; j < x + 2; j++) {
                try {
                    if (tMap[i][j].getHasStatObj()) {
                        if (tMap[i][j].getStatObj() instanceof iRestable) {
                            return true;
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

    public void setDisplayRestPrompt(boolean displayRestPrompt) {
        this.displayRestPrompt = displayRestPrompt;
    }

    public void setDisplayUpgradesPrompt(boolean displayUpgradesPrompt) {
        this.displayUpgradesPrompt = displayUpgradesPrompt;
    }

}
