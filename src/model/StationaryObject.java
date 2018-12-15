package model;

abstract class StationaryObject extends GameObject implements Interactable{
    private boolean passable;

    StationaryObject(){
        super();
        this.passable= true;
    }
    StationaryObject(Vector2 pos, Game game){
        super(pos, game);
        this.passable = true;
    }
    StationaryObject(Game game, boolean passable){
        super(game);
        this.passable = passable;
    }
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
