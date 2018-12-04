package model;

import java.util.Random;

public class Tree extends StationaryObject {
    private boolean hasSticks;
    private int numberSticks;

    public Tree(){
        super(false);
        Random rand = new Random();
        this.numberSticks = rand.nextInt(3) + 1;
        if (this.numberSticks !=0){
            hasSticks = true;
        }
    }
    public Tree(Vector2 pos) {
        super(pos, false);
        Random rand = new Random();
        this.numberSticks = rand.nextInt(3) + 1;
        if (this.numberSticks != 0) {
            hasSticks = true;
        }
    }

    public int harvestSticks(){
            hasSticks = false;
            int returnVal = numberSticks;
            numberSticks = 0;
            return returnVal;
    }

    //Getters
    public boolean getHasSticks() {
        return hasSticks;
    }
    public int getNumberSticks() {
        return numberSticks;
    }
}


