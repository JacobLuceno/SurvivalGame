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



public class Herbivore extends Animal {
    //Herbivore type used in instantiation and giving proper meat to player on a kill
    public enum HerbType{RABBIT, FISH, DEER}

    private HerbType type;

    public Herbivore(Vector2 pos, Game game, int speed, int maxHP, int damage, boolean[] canWalk, Image combatImage, Image gameImage, HerbType type) {
        super(pos, game, speed, maxHP, damage, canWalk, combatImage, gameImage);
        this.type = type;
        setFled(false);
    }
    //Herbivores exclusively flee during combat
    @Override
    public boolean combatLogic(MobileObject target) {
        return flee();
    }

    //Getter
    public HerbType getType() {
        return type;
    }
}
