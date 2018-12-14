package model;

abstract class GameObject {

    private Vector2 position;

    GameObject(){
        this.position = null;
    }

    GameObject(Vector2 position){
        this.position= position;
    }

    //Getter and setter
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public Vector2 getPosition() {
        return position;
    }
}
