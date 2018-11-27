package model;

import java.util.Random;

public class TerrainTile extends  GameObject {
    public enum StatObjType{ROCK, BUSH, TREE, BASE, NONE }
    public enum TerrainType {WATER, GRASSLAND, DESERT}

    static private boolean basePlaced;

    private TerrainType terrainType;
    private StatObjType statObjType;
    private boolean hasStatObj;
    private  StationaryObject statObj;
    // TODO Private boolean revealedOnMiniMap;

    public TerrainTile(float perlinValue, Vector2 pos){
        super(pos);
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
                break;
            case 3:
            case 4:
                statObjType = StatObjType.TREE;
                statObj = new Tree(super.getPosition());
                break;
            case 5:
                statObjType = StatObjType.ROCK;
                statObj = new Rock(super.getPosition());
                break;
            case 9:
                if (!basePlaced){
                    statObjType = StatObjType.BASE;
                }else {
                    statObjType = StatObjType.NONE;
                    hasStatObj = false;
                }
                basePlaced = true;
                break;
            default:
                statObjType = StatObjType.NONE;
                hasStatObj = false;
        }
    }
    private void desertSetup(){
        Random rand = new Random();
        int statObj =rand.nextInt(15);
        hasStatObj = false;
        switch (statObj){
            case 0:
                hasStatObj = true;
                statObjType = StatObjType.BUSH;
                break;
            case 1:
            case 2:
                hasStatObj = true;
                statObjType = StatObjType.ROCK;
                break;
        }
    }

    //GETTERS

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

    public String toString(){
        if (terrainType == TerrainType.DESERT) return "Desert\t";
        else if (terrainType == TerrainType.WATER) return "Water\t";
        else {return "Grass\t";}
    }
}

