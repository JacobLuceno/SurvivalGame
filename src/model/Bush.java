package model;

public class Bush extends  StationaryObject{

    int numberOfBerries;
    boolean hasBerries;

    public Bush(){
        super(true);
        numberOfBerries = 1;
        hasBerries = true;

    }

    public Bush( Vector2 position){
        super(position, true);
        numberOfBerries = 1;
        hasBerries = true;
    }

    @Override
    public void interact(Player player){
        numberOfBerries = 0;
    }

    public int getNumberOfBerries(){
        return numberOfBerries;
    }

    public boolean getHasBerries(){
        return hasBerries;
    }


    public int gatherBerries(){
        hasBerries = false;
        int returnVal = numberOfBerries;
        numberOfBerries = 0;
        return returnVal;
    }
}