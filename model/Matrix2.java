package model;
import java.util.Arrays;


public class Matrix2 {
    private int[][] map;
    private int[][] matrix;

    public Matrix2(int[][] map) {
        this.map = map;
        // Select which algorithm to use, 1 or 2.
        //matrix = adjMat();
        //matrix = adjMat2();
    }

    public int[][] adjMat() {
        int row = map.length;
        int col = map[0].length;
        int i;
        int j;
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
                    if ((i + 1) % col != 1 && (i < j)) //Last element in a row is NOT connected to first element in next row.
                        matrix[y][x] = 1;
                    else if ((i - 1) % col != 0 && (i > j)) //First element in a row is NOT connected to last element in previous row.
                        matrix[y][x] = 1;
                }

                //Above and below nodes.
                else if ((Math.abs(i - j)) % col == 0) {
                    if (Math.abs(i-j)/col == 1) //Prevents from connecting node to another node that is two or more rows below/above.
                        matrix[y][x] = 1;
                }
            }
        }
        return matrix;
    }

    public int[][] adjMat2 () {
       int col = map[0].length;
       int n = map.length * col;
       matrix = new int[n][n];

       for (int r = 0; r < map.length; r++) {
           for (int c = 0; c < col; c++) {
               int i = r * col + c;
               if (c > 0) {
                   matrix[i-1][i] = 1;
                   matrix[i][i - 1] = 1;
               }

               if (r > 0) {
                   matrix[i-col][i]= 1;
                   matrix[i][i-col] = 1;
               }
           }
       }
       return matrix;
    }

    public int[][] getMatrix() {
        return matrix;
    }


    public static void main(String[] args) {
        Matrix2 mat = new Matrix2(new int[3][3]);
        //Prints out matrix.
        System.out.println(Arrays.deepToString(mat.getMatrix()).replace("], ", "]\n"));
    }


}
