package model;

public class Noise {

    private final int GAME_MAP_X;
    private final int GAME_MAP_Y;

    private Vector2 [][] grid;
    private Vector2 [][] indexes;
    private float [][] noiseArray;

    public Noise(int GAME_MAP_X, int GAME_MAP_Y) {
        this.GAME_MAP_X = GAME_MAP_X;
        this.GAME_MAP_Y = GAME_MAP_Y;
        grid = new Vector2[(int) Math.ceil(this.GAME_MAP_Y / 5)][(int) Math.ceil(this.GAME_MAP_X / 5)];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = new Vector2(true);
            }
        }
        indexes = new Vector2[this.GAME_MAP_Y][this.GAME_MAP_X];
        for (int y = 0; y < this.GAME_MAP_Y; y++) {
            for (int x = 0; x < this.GAME_MAP_X; x++) {
                indexes[y][x] = new Vector2(y / 20f, x / 20f);
            }
        }
        noiseArray = new float[this.GAME_MAP_Y][this.GAME_MAP_X];
        for (int y = 0; y < GAME_MAP_Y; y++) {
            for (int x = 0; x < GAME_MAP_X; x++) {
                noiseArray[y][x] = perlinFunction(indexes[y][x].getv0(), indexes[y][x].getv1());
            }
        }
    }

    private float dotProduct(float gridx, float gridy, float x, float y){
        Vector2 dist = computeDistanceVector(gridx, gridy, x, y);
        float distY = dist.getv0();
        float distX = dist.getv1();
        return distX*grid[(int)gridy][(int)gridx].getv1() + distY*grid[(int)gridy][(int)gridx].getv0();
    }
    private Vector2 computeDistanceVector(float gridx, float gridy, float x, float y){
        return new Vector2(y - gridy, x - gridx);
    }
    private float linearInterpolation(float n0, float n1, float weight){
        return n0 + weight*(n1 - n0);
    }
    private float fadeFunction(float preFade){
        return (float)(6*Math.pow(preFade,5) - 15*Math.pow(preFade,4) + 10*Math.pow(preFade,3));
    }
    public float perlinFunction(float x, float y){
        int x0 = (int) x;
        int x1 = x0 + 1;
        int y0 = (int) y;
        int y1 = y0 + 1;

        float wx = x - x0;
        float wy = y - y0;

        float dot1, dot2, dot3, dot4, lin1, lin2, preFade;
        dot1 = dotProduct(x0,y0,x,y);
        dot2 = dotProduct(x1,y0,x,y);
        lin1 = linearInterpolation(dot1,dot2,wx);
        dot3 = dotProduct(x0,y1,x,y);
        dot4 = dotProduct(x1,y1,x,y);
        lin2 = linearInterpolation(dot3,dot4,wx);
        preFade = linearInterpolation(lin1,lin2,wy);
        return fadeFunction(preFade);

    }
    public void printNoise(){

        for (int y = 0; y < GAME_MAP_Y; y++) {
            for (int x = 0; x < GAME_MAP_X; x++) {
                if (perlinFunction(indexes[y][x].getv0(),indexes[y][x].getv1()) < -0.25f)
                    System.out.print("Black\t");
                else if (perlinFunction(indexes[y][x].getv0(),indexes[y][x].getv1()) < 0.25f)
                    System.out.print("Grey\t");
                else
                    System.out.print("White\t");
            }
            System.out.println();
        }

    }

    //GETTERS
    public float[][] getNoiseArray(){ return noiseArray;}
}
