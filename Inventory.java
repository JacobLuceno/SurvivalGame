package model;


import java.util.ArrayList;

public class Inventory {
    private int numberOfBerries;
    private int numberOfRabbits;
    private int numberOfDeer;
    private int numberOfFish;
    private int numberOfWolves;
    private int numberOfLions;
    private int numberOfCrocodiles;
    private int currentWeight;
    private final int maxWeight= 20;

    private static ArrayList<Food> contents;

    public Inventory(){
        numberOfBerries = 0;
        numberOfRabbits = 0;
        numberOfDeer = 0;
        numberOfFish = 0;
        numberOfWolves = 0;
        numberOfLions = 0;
        numberOfCrocodiles = 0;

    }


    public void insertFood(Food.FoodType foodType){
        Food food = new Food(foodType);
        contents.add(food);
        if (currentWeight+food.getWeight() > maxWeight){
            traceBack(buildTable());

        }
        else{
            updateValue(food, 1);
        }

    }

    public void removeFood(Food.FoodType foodType){
        Food food = new Food(foodType);
        for(int i = 0; i <=contents.size(); i++){
            if(contents.get(i) == food){
                contents.remove(i);
                updateValue(food, -1);
                return;
            }
            else{
                System.out.println("That food is not in your inventory");
            }
        }
    }

    public  void updateValue(Food food, int i){
        switch(food.getFoodType()){
            case Rabbit:
                numberOfRabbits=+i;
                break;
            case Deer:
                numberOfDeer=+i;
                break;
            case Fish:
                numberOfFish=+i;
                break;
            case Wolf:
                numberOfWolves=+i;
                break;
            case Lion:
                numberOfLions=+i;
                break;
            case Crocodile:
                numberOfCrocodiles=+i;
                break;
            default:
                numberOfBerries=+i;
        }
    }

    public int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public int[][] buildTable() {
        int n = contents.size(); //number of items in inventory
        int B[][] = new int[n + 1][maxWeight + 1]; // knapsack table

        for (int i = 0; i <= n; i++) { // looping through items in inventory
            for (int k = 0; k <= maxWeight; k++) { //looping through 0 - max weight
                if (i == 0 || k == 0) {
                    B[i][k] = 0;     //Initialize the base cases
                } else {
                    if (contents.get(i).getWeight() <= k) { // if the current item can be in the solution
                        B[i][k] = max(contents.get(i).getHpValue() + B[i - 1][k - contents.get(i).getWeight()], B[i - 1][k]);
                    } else {
                        B[i][k] = B[i - 1][k];
                    }
                }
            }
        }

     return B;
    }

    public void traceBack (int[][] table) {
       // boolean added = false;
        int i = contents.size();
        int k = maxWeight;
        while(i > 0 && maxWeight >0){
            if(table[i][k] != table[i-1][k]){
                i = i-1;
                k = k- contents.get(i).getWeight();
            }
            else{
                i= i-1;
            }
        }

    }

    public static void main(String[] args){
        Food foodArray[] = new Food[20];
        int i;
        for (i =0; i <= foodArray.length/2; i++){
            foodArray[i] = new Food(Food.FoodType.Lion);
        }
        for(i=i; i < foodArray.length; i++){
            foodArray[i] = new Food(Food.FoodType.Crocodile);
        }
        Inventory inv = new Inventory();
        for (int j = 0; j<foodArray.length; j++){
            inv.insertFood(foodArray[j].getFoodType());
        }
        inv.removeFood(Food.FoodType.Deer);
        inv.removeFood(Food.FoodType.Lion);

    }

}
