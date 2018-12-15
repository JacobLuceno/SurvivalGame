package model;

import java.util.Random;

public class Tree extends StationaryObject {
    private boolean hasSticks;
    private int numberSticks;



    public Tree(Vector2 pos, Game game) {
        super(pos, game, false);
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
    @Override
    public void interact(Player player){
        player.gatherSticks(harvestSticks());
        System.out.println(player.getSticks());
    }

    //Getters
    public boolean getHasSticks() {
        return hasSticks;
    }
    public int getNumberSticks() {
        return numberSticks;
    }
}


