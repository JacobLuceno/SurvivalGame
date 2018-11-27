package model;

public class Map {
    private final int GAME_BOARD_X;
    private final int GAME_BOARD_Y;

    private Noise noise;
    private TerrainTile [][] terrainMap;
    private float [][] terVal;
    //TODO implement a minimap which utilizes the TerrainTiles' reaveledToPlayer attribute

    public Map(int gameBoardX, int gameBoardY){
        this.GAME_BOARD_X = gameBoardX;
        this.GAME_BOARD_Y = gameBoardY;

        noise = new Noise(this.GAME_BOARD_X, this.GAME_BOARD_Y);
        terVal = noise.getNoiseArray();
        terrainMap = new TerrainTile[this.GAME_BOARD_Y][this.GAME_BOARD_X];
        do{ // This loop insures the game places the base, but currently heavily favors top few rows
            for (int y = 0; y < this.GAME_BOARD_Y; y++){
                for (int x = 0; x < this.GAME_BOARD_X; x++){
                    terrainMap[y][x] = new TerrainTile(terVal[y][x], new Vector2(y,x));
                }
            }
        } while (!TerrainTile.getBasePlaced());
    }


    //TODO Delete Test functions
    public void printMap(){
        for (TerrainTile[] y : terrainMap){
            for (TerrainTile x : y){
                switch (x.getTerrainType()) {
                    case WATER:
                        System.out.print("Water\t");
                        break;
                    case DESERT:
                        if (x.getHasStatObj()) {
                            switch (x.getStatObjType()) {
                                case ROCK:
                                    System.out.print("Rock\t");
                                    break;
                                case BUSH:
                                    System.out.print("Bush\t");
                                    break;
                            }
                        } else {
                            System.out.print("Desert\t");
                        }
                        break;
                    case GRASSLAND:
                        if (x.getHasStatObj()) {
                            switch (x.getStatObjType()) {
                                case ROCK:
                                    System.out.print("Rock\t");
                                    break;
                                case BUSH:
                                    System.out.print("Bush\t");
                                    break;
                                case BASE:
                                    System.out.print("Base\t");
                                    break;
                                case TREE:
                                    System.out.print("Tree\t");
                                    break;
                            }
                        } else {
                            System.out.print("Grass\t");
                        }
                        break;
                }
                }
                System.out.println();
            }
        }
    public void printTerrainTypes() {
        for (TerrainTile[] y : terrainMap) {
            for (TerrainTile x : y) {
                System.out.print(x.toString());
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        Map map = new Map(50,50);
        map.printMap();
    }
}





