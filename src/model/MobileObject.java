package model;

public abstract class MobileObject extends GameObject {
    private final int SPEED;
    private final int MAX_HIT_POINTS;
    private int currentHitPoints;
    private boolean alive;
    private boolean busy;

    public MobileObject(Vector2 pos, int speed, int maxHitPoints){
        super(pos);
        this.SPEED = speed;
        this.MAX_HIT_POINTS = maxHitPoints;
        this.currentHitPoints = maxHitPoints;
        alive = true;
        busy = false;
    }

    public void sufferHarm(int harm){
        this.setCurrentHitPoints(this.getCurrentHitPoints() - harm);
        if (this.getCurrentHitPoints() <= 0){
            this.setAlive(false);
        }
    }
    public void move(Game.Direction direction, int maxX, int maxY){
        switch(direction){
            case RIGHT:
                if (this.getPosition().getv1() < maxX - 1) {
                    this.setPosition(new Vector2(this.getPosition().getv0(), this.getPosition().getv1() + 1));
                }
                break;
            case UP:
                if (this.getPosition().getv0() > 0) {
                    this.setPosition(new Vector2(this.getPosition().getv0() - 1, this.getPosition().getv1()));
                }
                break;
            case DOWN:
                if (this.getPosition().getv0() < maxY - 1) {
                    this.setPosition(new Vector2(this.getPosition().getv0() + 1, this.getPosition().getv1()));
                }
                break;
            case LEFT:
                if (this.getPosition().getv1() > 0) {
                    this.setPosition(new Vector2(this.getPosition().getv0(), this.getPosition().getv1() - 1));
                }
                break;
        }
    }

    //Getters and setters

    public int getMAX_HIT_POINTS() {
        return MAX_HIT_POINTS;
    }
    public int getCurrentHitPoints() {
        return currentHitPoints;
    }
    public void setCurrentHitPoints(int currentHitPoints) {
        this.currentHitPoints = currentHitPoints;
    }
    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public int getSpeed() {
        return SPEED;
    }
    public boolean isBusy() {
        return busy;
    }
    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
