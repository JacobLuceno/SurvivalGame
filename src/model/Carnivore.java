package model;

import javafx.scene.image.Image;


//NOTE:  The original UML we suggested specified individual classes for each herbivore/carnivore subtype.  However,
// since all animals of a specific type (carnivore or herbivore) implement the same behaviors and logic, it didn't
//feel prudent to create new classes for each animal subtype.  There is no polymorphism between rabbits and deer, or
// lions and crocodiles, in the limited scope of their behaviors.  They wander around and they flee from/ attck the
// player in combat.  As such, the difference between a "deer" and a "rabbit" in the scope of this game is the same
// difference between a "red" car and a "blue" car in the classical example of object oriented programming.  As such,
// we decided that herbivore/carnivore would be non abstract classes, with the type of animal signified by an enum
// belonging to the herbivore/carnivore classes, and the only differences between them would be the superficial values
// such as hit point maximum, speed, and damage.  Please see the carnivore/herbvore factory classes for specifics
// in their construction.



public class Carnivore extends Animal {
    //Carnivore type used in instantiation and giving proper meat to player on a kill
    public enum CarnType{WOLF, LION, CROC}
    //The distance the carnivore can lock onto a player form
    private final int VIEW_DISTANCE = 7;
    private CarnType type;

    public Carnivore(Vector2 pos, Game game, int speed, int maxHP, int damage, boolean[] canWalk, Image combatImage, Image gameImage, CarnType type) {
        super(pos, game, speed, maxHP, damage, canWalk, combatImage, gameImage);
        this.type = type;
        setFled(false);
    }
    //Carnivores exclusively attack in combat
    @Override
    public boolean combatLogic(MobileObject target) {
        return attack(target);
    }
    //This function overrides the default implementation in the Animal class.  It first checks if the player is
    //in range, not hidden behind a rock, and not on terrain it can't reach.  If the player meets these criteria the
    //player's location becomes the carnivore's target.  If the these criteria are not met the animal defaults to the
    //super implementation of the function
    @Override
    protected void chooseNewPosition(){
        Game game = getGame();
        Player player = game.getPlayer();
        TerrainTile.TerrainType terrain = game.getMap().getTerrainMap()[(int)player.getPosition().getv0()][(int)player.getPosition().getv1()].getTerrainType();
        if (scanForPlayer(player, terrain)){
            setTargetPos(player.getPosition());
        }
        else {
            super.chooseNewPosition();
        }
    }
    //Subroutine to determine if player is currently a valid target
    private boolean scanForPlayer(Player player, TerrainTile.TerrainType terrain){
        return (getPosition().distance(player.getPosition()) <= VIEW_DISTANCE && !player.isHidden() && canWalk(terrain));
    }
    //A subroutine called in the scanning for a player, to determine if the terrain type is valid
    private boolean canWalk(TerrainTile.TerrainType terrain){
        switch (terrain){
            case WATER:
                return isCanWalkWater();
            case GRASSLAND:
                return isCanWalkGrass();
            case DESERT:
                return isCanWalkDesert();
            default:
                return false;
        }
    }

    //Getter

    public CarnType getType() {
        return type;
    }


}
