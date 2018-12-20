package model;

import java.util.Random;

public class Bush extends  StationaryObject{

    //Variables for the number of fruit on the bush, and a boolean flag for whether its non-zero
    private int numberOfBerries;
    private boolean hasBerries;

    //Bushes by default have 1/3 chance of containing a berry
    public Bush( Vector2 position, Game game){
        super(position, game, true);
        Random rand = new Random();
        numberOfBerries = rand.nextInt(3) - 1;
        if (numberOfBerries > 0) {
            hasBerries = true;
        }
    }
    //when a player calls interact on the bush, if it has a berries, it will be added to player's inventory.  regardless
    //the value 0 is then assigned for number of berries.
    @Override
    public void interact(Player player){
        if (numberOfBerries > 0) {
            player.getInventory().insertFood(Food.FoodType.Berries);
        }
        hasBerries = false;
        numberOfBerries = 0;
    }

    //Getter
    public boolean getHasBerries(){
        return hasBerries;
    }


}