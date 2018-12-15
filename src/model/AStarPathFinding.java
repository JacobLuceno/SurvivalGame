package model;

import java.util.ArrayList;
import java.util.Collections;


public class AStarPathFinding {

    private Tile[][] grid;

    public AStarPathFinding (Game game, boolean[] canWalk) {
        setUpGrid(game, canWalk);
    }

    public void setUpGrid(Game game, boolean[] canWalk) {
        int rows = game.getMap().getTerrainMap().length;
        int cols = game.getMap().getTerrainMap()[0].length;

        grid = new Tile[rows][cols];

        //System.out.println("Setting up Tile grid and neighbors...");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Vector2 pos = new Vector2( (float) i, (float) j);
                boolean isObs = isObstacle(game, i, j, canWalk);
                grid[i][j] = new Tile(pos, isObs);
            }
        }

        for (Tile[] row : grid) {
            for (Tile t: row) {
                t.addNeighbors();
            }
        }

        //System.out.println("Grid and neighbors set successfully!");
    }

    public boolean isObstacle(Game game, int i, int j, boolean[] canWalk) {
        boolean canWalkGrass = canWalk[0];
        boolean canWalkDesert = canWalk[1];
        boolean canWalkWater = canWalk[2];
        boolean hasObj = game.getMap().getTerrainMap()[i][j].getHasStatObj();
        StationaryObject statObj = game.getMap().getTerrainMap()[i][j].getStatObj();
        TerrainTile.TerrainType terrain = game.getMap().getTerrainMap()[i][j].getTerrainType();

        switch(terrain) {
            case GRASSLAND:
                if (canWalkGrass) {
                    if (hasObj) {
                        if (statObj.isPassable()) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            case DESERT:
                if (canWalkDesert){
                    if (hasObj) {
                        if (statObj.isPassable()) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            case WATER:
                if (canWalkWater){
                    if (hasObj) {
                        if (statObj.isPassable()) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            default:
                return true;
        }
    }

    // Used to calculate hCost.
    public static int heuristic(Tile a, Tile b) {
        int i1 = (int) a.getPosition().getv0();
        int j1 = (int) a.getPosition().getv1();
        int i2 = (int) b.getPosition().getv0();
        int j2 = (int) b.getPosition().getv1();

        return  Math.max(Math.abs(i1 - i2), Math.abs(j1 - j2));
    }

    public ArrayList<Vector2> findPath(Vector2 start, Vector2 target) {
        ArrayList<Vector2> path = new ArrayList<>();
        ArrayList<Tile> openSet = new ArrayList<>();
        ArrayList<Tile> closedSet = new ArrayList<>();

        for (Tile[] y : grid){
            for (Tile x : y){
                x.fCost = 0.0;
                x.gCost = 0;
                x.hCost = 0.0;
                x.previous = null;
            }
        }

        openSet.add(grid[(int) start.getv0()][(int) start.getv1()]);
        Tile targetTile = grid[(int) target.getv0()][(int)target.getv1()];


        /** Algorithm starts here */
        while (openSet.size() > 0)
        {
            int lowestIndex = 0;
            for (int i = 0; i < openSet.size(); i++) {
                if (openSet.get(i).fCost < openSet.get(lowestIndex).fCost) {
                    lowestIndex = i;
                }
            }
            Tile current = openSet.get(lowestIndex);

            //Checks if target found
            if (current == targetTile) {

                // This block reconstructs the path.
                Tile temp = current;
                path.add(current.getPosition());
                while (temp.getPrevious() != null) {
                    path.add(temp.getPrevious().getPosition());
                    temp = temp.getPrevious();
                }

                Collections.reverse(path);
                break;
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Tile neighbor : current.getNeighbors())
            {
                if (!closedSet.contains(neighbor) && !neighbor.isWall) {   //Checks if it's passable.
                    int tempG = current.gCost + 1;

                    /*DEBUG PRINT STATEMENTS
                     System.out.println("closed set does not contain this neighbor");
                     System.out.print("Tile x: " + neighbor.getPosition().getv0()
                     + ", y: " + neighbor.getPosition().getv1());
                     */

                    if (openSet.contains(neighbor)) {
                        if (tempG < neighbor.gCost)
                            neighbor.gCost = tempG;
                    }
                    else {
                        neighbor.gCost = tempG;
                        openSet.add(neighbor);
                    }

                    neighbor.hCost = heuristic(neighbor, targetTile);
                    neighbor.fCost = neighbor.hCost + neighbor.gCost;
                    neighbor.setPrevious(current);
                }
            }
        }
        if (openSet.size() <= 0) {
            System.out.println("It's possible that there's no solution. Needs more testing to guarantee.");
            System.out.println("Returning start vector...");
            path.add(start);
        }
        return path;
    }

    public Tile[][] getGrid() {
        return grid;
    }

    //Private class
    private class Tile {
        private Vector2 position;
        private int gCost;
        private double hCost;
        private double fCost;
        private ArrayList<Tile> neighbors;
        private Tile previous = null;
        private boolean isWall = false;

        public Tile() {
            position = new Vector2(0f,0f);
            gCost = 0;
            hCost = 0.0;
            fCost = 0.0;
            neighbors = new ArrayList<>();
        }

        public Tile(Vector2 pos, boolean obs) {
            position = pos;
            gCost = 0;
            hCost = 0.0;
            fCost = 0.0;
            neighbors = new ArrayList<>();
            isWall = obs;
        }

        public void addNeighbors () {
            int i = (int) this.getPosition().getv0();
            int j = (int) this.getPosition().getv1();

            if (i < grid.length - 1)
                this.neighbors.add(grid[i + 1][j]);
            if (i > 0)
                this.neighbors.add(grid[i - 1][j]);
            if (j < grid[0].length - 1)
                this.neighbors.add(grid[i][j + 1]);
            if (j > 0)
                this.neighbors.add(grid[i][j - 1]);
        }


        public ArrayList<Tile> getNeighbors() {
            return neighbors;
        }

        public Tile getPrevious() {
            return previous;
        }

        public Vector2 getPosition() {
            return position;
        }

        public void setPrevious(Tile t) {
            previous = t;
        }
    }

}
