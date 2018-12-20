package model;

public class Map {

    //private final game map parameters
    private final int GAME_BOARD_X;
    private final int GAME_BOARD_Y;

    //The terrainMap contains references to the game's TerrainTiles.  These hold information about each grid space.
    private TerrainTile[][] terrainMap;


    public Map(Game game) {
        //Set final variables
        this.GAME_BOARD_X = game.getWIDTH();
        this.GAME_BOARD_Y = game.getHEIGHT();
        //instantiate Perlin Noise generating object
        Noise noise = new Noise(this.GAME_BOARD_X, this.GAME_BOARD_Y);
        //get the Perlin Noise Array
        float[][] terVal = noise.getNoiseArray();

        //Initialize TerrainTiles
        setUpTerrainMap(terVal, game);
    }

    //Calls the constructor for each TerrainTile in the array, passing in a unique Perlin Noise value to determine
    //characteristics of tile.
    private void setUpTerrainMap(float[][] terVal, Game game){
        //Intializes the TerrainTile array to the size of the current game
        terrainMap = new TerrainTile[this.GAME_BOARD_Y][this.GAME_BOARD_X];

        for (int y = 0; y < this.GAME_BOARD_Y; y++) {
            for (int x = 0; x < this.GAME_BOARD_X; x++) {
                //Each TerrainTile takes a perlin value to determine terrainType, holds on to a reference to its
                //position in the grid, and sets a reference to the game to which it belongs
                terrainMap[y][x] = new TerrainTile(terVal[y][x], new Vector2(y, x), game);
            }
        }
    }




    //GETTERS AND SETTERS
    public TerrainTile[][] getTerrainMap() {
        return terrainMap;
    }
    public int getGAME_BOARD_X() {
        return GAME_BOARD_X;
    }
    public int getGAME_BOARD_Y() {
        return GAME_BOARD_Y;
    }
}