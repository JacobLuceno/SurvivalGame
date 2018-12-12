package model;

public class Map {
    private final int GAME_BOARD_X;
    private final int GAME_BOARD_Y;

    private Noise noise;
    private TerrainTile[][] terrainMap;
    private float[][] terVal;

    public Map(int gameBoardX, int gameBoardY) {
        this.GAME_BOARD_X = gameBoardX;
        this.GAME_BOARD_Y = gameBoardY;

        noise = new Noise(this.GAME_BOARD_X, this.GAME_BOARD_Y);
        terVal = noise.getNoiseArray();
        terrainMap = new TerrainTile[this.GAME_BOARD_Y][this.GAME_BOARD_X];
        for (int y = 0; y < this.GAME_BOARD_Y; y++) {
            for (int x = 0; x < this.GAME_BOARD_X; x++) {
                terrainMap[y][x] = new TerrainTile(terVal[y][x], new Vector2(y, x));
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





