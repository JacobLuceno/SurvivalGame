package model;


public abstract class Animal extends MobileObject implements iTakesTurns, Interactable{

    private int meatVal;
    private Vector2 targetPos;
    private Vector2 nextPos;
    private boolean canWalkGrass;
    private boolean canWalkDesert;
    private boolean canWalkWater;
    private boolean remove;

    public Animal(Vector2 pos, int speed, int maxHP, int meatVal, boolean[] canWalk){
        super(pos, speed, maxHP);
        this.meatVal = meatVal;
        this.canWalkGrass = canWalk[0];
        this.canWalkDesert = canWalk[1];
        this.canWalkWater = canWalk[2];
        remove = false;
    }

    protected void chooseNewTargetPosition(TerrainTile[][] tMap) {
        boolean pathChosen = false;
        int attempts = 0;
        do {
            targetPos = getPosition().getDisplacementVector(5, 5);
            if (canWalkWater && tMap[(int)targetPos.getv0()][(int)targetPos.getv0()].getTerrainType() == TerrainTile.TerrainType.WATER){
                pathChosen = true;
            } else if (canWalkGrass && tMap[(int)targetPos.getv0()][(int)targetPos.getv0()].getTerrainType() == TerrainTile.TerrainType.GRASSLAND) {
                if (tMap[(int) targetPos.getv0()][(int) targetPos.getv0()].getHasStatObj()) {
                    if (tMap[(int) targetPos.getv0()][(int) targetPos.getv0()].getStatObj().isPassable()){
                        pathChosen = true;
                    }
                }
            } else if (canWalkDesert && tMap[(int)targetPos.getv0()][(int)targetPos.getv0()].getTerrainType() == TerrainTile.TerrainType.DESERT) {
                if (tMap[(int) targetPos.getv0()][(int) targetPos.getv0()].getHasStatObj()) {
                    if (tMap[(int) targetPos.getv0()][(int) targetPos.getv0()].getStatObj().isPassable()){
                        pathChosen = true;
                    }
                }
            } else if (attempts > 100){
                targetPos = getPosition();
                break;
            }
            attempts++;
        } while (!pathChosen);
    }

    public abstract void combatLogic();

    @Override
    public abstract void interact(Player player);


    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

}
