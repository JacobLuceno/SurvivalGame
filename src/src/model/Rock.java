package model;

public class Rock extends StationaryObject {

    public Rock(Vector2 position, Game game){
        super(position, game, false);
    }

    //Interacting with a rock hides the player, moves them into the rock's square, and iterates the player's steps
    @Override
    public void interact(Player player){
        player.setHidden(true);
        player.setPosition(getPosition());
        player.takeStep();
    }
}