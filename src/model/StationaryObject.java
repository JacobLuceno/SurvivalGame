package model;

abstract public class StationaryObject extends GameObject implements Interactable{
    //Determines whether animals may walk through/over object
    private boolean passable;

    //Stationary Objects are a single step away from GameObject in the class hierarchy. They do not contain any
    //methods which allow them to move, and contain only small bit of new information.  Whether or not they are
    //passable by animal classes, and that they are guaranteed to implement Interactable
    StationaryObject(Vector2 pos, Game game, boolean passable){
        super(pos, game);
        this.passable = passable;
    }

    //Getters and Setters
    public boolean isPassable() {
        return passable;
    }
    public void setPassable(boolean passable) {
        this.passable = passable;
    }
}
