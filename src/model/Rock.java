package model;

public class Rock extends StationaryObject {

    public Rock(Vector2 position, Game game){
        super(position, game, false);
    }

    @Override
    public void interact(Player player){
        player.setHidden(true);
        player.setPosition(getPosition());
        player.takeStep();
    }
}