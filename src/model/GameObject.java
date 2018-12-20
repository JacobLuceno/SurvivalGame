package model;

abstract class GameObject {
    //all objects in the game hold on to these two important references:
    // Their position in the game world, and the game to which they belong.
    private Vector2 position;
    private Game game;

    //Multiple Constructor options due to the frequency and variation that may be required in creating new GameObjects
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

    //Getters and setters
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
