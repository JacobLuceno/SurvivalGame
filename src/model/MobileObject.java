package model;

public abstract class MobileObject extends GameObject {
    private final int SPEED;
    private final int MAX_HIT_POINTS;
    private int currentHitPoints;
    private boolean alive;
    private boolean busy;
    private boolean fled;
    private Vector2 priorLoc;
    private Game.Direction facing;


    private boolean turnOver;

    public MobileObject(Vector2 pos, Game game, int speed, int maxHitPoints){
        super(pos, game);
        priorLoc = new Vector2(getPosition().getv0(), getPosition().getv1());
        this.SPEED = speed;
        this.MAX_HIT_POINTS = maxHitPoints;
        this.currentHitPoints = maxHitPoints;
        alive = true;
        busy = false;
        turnOver = false;
    }

    abstract public boolean flee();
    abstract public boolean attack(MobileObject target);
    public void sufferHarm(int harm){
        this.setCurrentHitPoints(this.getCurrentHitPoints() - harm);
        if (this.getCurrentHitPoints() <= 0){
            this.setAlive(false);
        }
    }
    public void move(Game.Direction direction){
        switch(direction){
            case RIGHT:
                if (this.getPosition().getv1() < Game.getWIDTH()- 1) {
                    this.setPosition(new Vector2(this.getPosition().getv0(), this.getPosition().getv1() + 1));
                }
                break;
            case UP:
                if (this.getPosition().getv0() > 0) {
                    this.setPosition(new Vector2(this.getPosition().getv0() - 1, this.getPosition().getv1()));
                }
                break;
            case DOWN:
                if (this.getPosition().getv0() < Game.getHEIGHT() - 1) {
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

    //GETTERS AND SETTERS

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
    public Game.Direction getFacing() {
        return facing;
    }
    public void setFacing(Game.Direction facing) {
        this.facing = facing;
    }
    public boolean isFled() {
        return fled;
    }
    public void setFled(boolean fled){
        this.fled = fled;
    }
    public Vector2 getPriorLoc() {
        return priorLoc;
    }
    public void setPriorLoc(Vector2 priorLoc) {
        this.priorLoc = priorLoc;
    }
    public boolean isTurnOver() {
        return turnOver;
    }
    public void setTurnOver(boolean turnOver) {
        this.turnOver = turnOver;
    }
}
