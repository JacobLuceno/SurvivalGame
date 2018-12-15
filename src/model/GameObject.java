package model;

abstract class GameObject {

    private Vector2 position;

    private Game game;

    GameObject(){
        this.position = null;
    }

    GameObject(Game game){
        this.game = game;
    }
    GameObject(Vector2 position, Game game){
        this.position= position;
        this.game = game;
    }

    //Getter and setter
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Game getGame() {
        return game;
    }
}
