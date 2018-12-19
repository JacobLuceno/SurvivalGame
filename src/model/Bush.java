package model;

import java.util.Random;

public class Bush extends  StationaryObject{

    int numberOfBerries;
    boolean hasBerries;


    public Bush( Vector2 position, Game game){
        super(position, game, true);
        Random rand = new Random();
        numberOfBerries = rand.nextInt(3) - 1;
        if (numberOfBerries > 0) {
            hasBerries = true;
        }
    }

    @Override
    public void interact(Player player){
        if (numberOfBerries > 0) {
            player.getInventory().insertFood(Food.FoodType.Berries);
        }
        numberOfBerries = 0;
    }

    public int getNumberOfBerries(){
        return numberOfBerries;
    }

    public boolean getHasBerries(){
        return hasBerries;
    }


}