package model;

public abstract class MobileObject extends GameObject {
    //Private final instance Variables which define the object's key characteristics
    //
    // The walking animations duration is divided by the objects speed, allowing it move more frequently with higher
    //speeds
    private final int SPEED;
    //The maximum damage an object can take before it "dies"
    private final int MAX_HIT_POINTS;

    //Object's state variables:

    //  Remaining HP
    private int currentHitPoints;
    //  Flag for combat/ whether the game is lost in the case of the player
    private boolean alive;
    //  An extremely important Flag for the UI which returns false only after the objects movement animation is
    //  completed, and is set true each time the object changes positions.
    private boolean busy;
    //  Another combat flag, useful in determining the outcome of a combatEncounter
    private boolean fled;
    //  Another combat flag, useful in determining who's turn it is to act in the combat initiative
    private boolean turnOver;


    //The objects most recent location
    private Vector2 priorLoc;


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

    //Abstract methods to be implemented by animals and player, necessary for combat
    abstract public boolean flee();
    abstract public boolean attack(MobileObject target);

    //This function removes HP from the object, and if the HP hits zero, marks the object as dead
    public void sufferHarm(int harm){
        this.setCurrentHitPoints(this.getCurrentHitPoints() - harm);
        if (this.getCurrentHitPoints() <= 0){
            currentHitPoints = 0;
            this.setAlive(false);
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
