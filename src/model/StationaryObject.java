package model;

abstract class StationaryObject extends GameObject implements Interactable{
    private boolean passable;

    StationaryObject(){
        super();
        this.passable= true;
    }
    StationaryObject(Vector2 pos){
        super(pos);
        this.passable = true;
    }
    StationaryObject(boolean passable){
        super();
        this.passable = passable;
    }
    StationaryObject(Vector2 pos, boolean passable){
        super(pos);
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
