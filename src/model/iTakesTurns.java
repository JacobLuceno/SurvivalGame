package model;

public interface iTakesTurns {

    //target is not necessarily used in all combat logic implementations but is essential for a subset of them
    boolean combatLogic(MobileObject target);


}
