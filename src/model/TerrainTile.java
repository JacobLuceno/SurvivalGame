package model;

import java.util.Random;

public class TerrainTile extends  GameObject {
    //defines possible values for statObj
    public enum StatObjType{ROCK, BUSH, TREE, BASE, NONE }
    //defines possible terrainTypes
    public enum TerrainType {WATER, GRASSLAND, DESERT}
    // Holds a reference to the type of terrain of this tile
    private TerrainType terrainType;
    // holds a reference to the type of stationaryObject the tile contains
    private StatObjType statObjType;
    //a flag signifying whether the associated tile has a stationaryObject
    private boolean hasStatObj;
    //holds a reference to the tile's stationaryObject
    private StationaryObject statObj;
    //a boolean value signifying whether the given tile is currently revealed on the miniMap
    private boolean revealedOnMiniMap;

    public TerrainTile(float perlinValue, Vector2 pos, Game game){
        super(pos, game);
        revealedOnMiniMap= false;
        //Depending on the Perlin value, the terrain tile is given a specific terrainType.  That terrainType defines
        //which setUp function is subsequently called.
        if (perlinValue <= -0.25){
            terrainType = TerrainType.WATER;
            hasStatObj = false;
            statObjType = StatObjType.NONE;
        }
        else if (perlinValue < 0.25){
            terrainType = TerrainType.GRASSLAND;
            grasslandSetup();
        }
        else {
            terrainType = TerrainType.DESERT;
            desertSetup();
        }
    }

    //The following two functions use random numbers to probabilistically define the landscape, populating it with
    // trees, rocks, and bushes/cacti
    private void grasslandSetup(){
        Random rand = new Random();
        int statObjValue =rand.nextInt(20);
        hasStatObj = true;

        switch(statObjValue){
            case 0:
            case 1:
                statObjType = StatObjType.BUSH;
                statObj = new Bush(super.getPosition(), getGame());
                break;
            case 3:
            case 4:
                statObjType = StatObjType.TREE;
                statObj = new Tree(super.getPosition(), getGame());
                break;
            case 5:
                statObjType = StatObjType.ROCK;
                statObj = new Rock(super.getPosition(), getGame());
                break;
            default:
                statObjType = StatObjType.NONE;
                hasStatObj = false;
        }
    }
    private void desertSetup(){
        Random rand = new Random();
        int statObjInt =rand.nextInt(30);
        hasStatObj = false;
        switch (statObjInt){
            case 0:
                hasStatObj = true;
                statObjType = StatObjType.BUSH;
                statObj = new Bush(super.getPosition(), getGame());
                break;
            case 1:
            case 2:
                hasStatObj = true;
                statObjType = StatObjType.ROCK;
                statObj = new Rock(super.getPosition(), getGame());
                break;
        }
    }

    //removes reference to stationaryObject
    public void setHasStatObjNull() {
        this.hasStatObj = false;
        this.statObj = null;
    }
    //used for debugging purposes when designing perlin noise based map
    public String toString(){
        if (terrainType == TerrainType.DESERT) return "Desert\t";
        else if (terrainType == TerrainType.WATER) return "Water\t";
        else {return "Grass\t";}
    }

    //GETTERS AND SETTERS

    public StationaryObject getStatObj() {
        return statObj;
    }
    public StatObjType getStatObjType() {
        return statObjType;
    }
    public TerrainType getTerrainType() {
        return terrainType;
    }
    public boolean getHasStatObj() {
        return hasStatObj;
    }
    public void setHasStatObj(boolean hasStatObj) {
        this.hasStatObj = hasStatObj;
    }
    public void setStatObjType(StatObjType statObjType) {
        this.statObjType = statObjType;
    }
    public void setStatObj(StationaryObject statObj) {
        this.statObj = statObj;
    }
    public void setRevealedOnMiniMap(boolean revealedOnMiniMap) {
        this.revealedOnMiniMap = revealedOnMiniMap;
    }
    public boolean isRevealedOnMiniMap() {
        return revealedOnMiniMap;
    }


}

