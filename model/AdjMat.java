package model;
import java.util.Arrays;

public class AdjMat {
    private Map map;
    private int[][] matrix;

    public AdjMat(Map map) {
        this.map = map;

        //Select which algorithm to use: 1 or 2.
        //matrix = setUpAdjMat1();
        matrix = setUpAdjMat2();
        /** TO DO: IMPLEMENT (A* Algorithm) */
        checkNeighbor();
    }

    public int[][] setUpAdjMat1() {
        int col = map.getGAME_BOARD_X();
        int matSize = col * map.getGAME_BOARD_Y();
        matrix = new int[matSize][matSize];

        for (int r = 0; r < map.getGAME_BOARD_Y(); r++) {
            for (int c = 0; c < col; c++) {
                int i = r * col + c; // Index that represents each element in adj mat. 1,2,3...
                if (c > 0) {
                    matrix[i-1][i] = 1;
                    matrix[i][i-1] = 1;
                }
                if (r > 0) {
                    matrix[i-col][i] = 1;
                    matrix[i][i-col] = 1;
                }
            }
        }
        return matrix;
    }

    public int[][] setUpAdjMat2() {
        int row = map.getGAME_BOARD_Y();
        int col = map.getGAME_BOARD_X();
        int i, j;
        matrix = new int[row*col][row*col];

        for (int y = 0; y < row*col; y++) {
            for (int x = 0; x < row*col; x++) {
                j = y + 1;
                i = x + 1;
                //If nodes are equal.
                if (i == j)
                    matrix[y][x] = 1;
                //If nodes are "next" to each other, i.e, their diff == 1.
                else if (Math.abs(i - j) == 1) {
                    if ((i + 1) % col != 1 && (i < j))  //Last element in a row is NOT connected to first element in next row.
                        matrix[y][x] = 1;
                    else if ((i - 1) % col != 0 && (i > j)) //First element in a row is NOT connected to last element in previous row.
                        matrix[y][x] = 1;
                }
                //Above and below nodes.
                else if ((Math.abs(i - j)) % col == 0) {
                    if (Math.abs(i-j)/col == 1)  //Prevents from connecting node to another node that is two or more rows below/above.
                        matrix[y][x] = 1;
                }
            }
        }
        return matrix;
    }

    public void checkNeighbor() {
        //Implement
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public static void main(String[] args) {
        //AdjMat aj = new AdjMat(new Map(10,10));
        //Prints out matrix.
        int[][] grid = new int[3][3];
        System.out.println(Arrays.deepToString(grid).replace("], ", "]\n"));

    }

}
