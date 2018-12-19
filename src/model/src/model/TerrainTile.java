package model;

import java.util.Random;

public class TerrainTile extends  GameObject {
    public enum StatObjType{ROCK, BUSH, TREE, BASE, NONE }
    public enum TerrainType {WATER, GRASSLAND, DESERT}

    static private boolean basePlaced;
    static private Vector2 baseLocation;

    private TerrainType terrainType;
    private StatObjType statObjType;
    private boolean hasStatObj;
    private  StationaryObject statObj;
    boolean revealedOnMiniMap;

    public TerrainTile(float perlinValue, Vector2 pos, Game game){
        super(pos, game);
        revealedOnMiniMap= false;
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
    public static boolean getBasePlaced() { return basePlaced;}
    public void setHasStatObjNull() {
        this.hasStatObj = false;
        this.statObj = null;
    }
    public String toString(){
        if (terrainType == TerrainType.DESERT) return "Desert\t";
        else if (terrainType == TerrainType.WATER) return "Water\t";
        else {return "Grass\t";}
    }
    public static void setBasePlaced(boolean basePlaced) {
        TerrainTile.basePlaced = basePlaced;
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
    public static Vector2 getBaseLocation() {
        return baseLocation;
    }
    public static void setBaseLocation(Vector2 baseLocation) {
        TerrainTile.baseLocation = baseLocation;
    }


}

