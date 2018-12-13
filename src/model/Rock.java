package model;

public class Rock extends StationaryObject {

    public Rock(Vector2 position){
        super(position, false);
    }

    @Override
    public void interact(Player player){
        player.setHidden(true);
        player.setPosition(super.getPosition());
        player.takeStep();
    }
}