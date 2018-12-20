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
    private final int maxWeight;

    private  ArrayList<Food> contents;

    public Inventory(int maxWeight){
        this.maxWeight = maxWeight;
        numberOfBerries = 0;
        numberOfRabbits = 0;
        numberOfDeer = 0;
        numberOfFish = 0;
        numberOfWolves = 0;
        numberOfLions = 0;
        numberOfCrocodiles = 0;
        currentWeight = 0;
        contents = new ArrayList<> ();

    }
    public int getNumberOfBerries() {
        return numberOfBerries;
    }

    public int getNumberOfRabbits() {
        return numberOfRabbits;
    }

    public int getNumberOfDeer() {
        return numberOfDeer;
    }

    public int getNumberOfFish() {
        return numberOfFish;
    }

    public int getNumberOfWolves() {
        return numberOfWolves;
    }

    public int getNumberOfLions() {
        return numberOfLions;
    }

    public int getNumberOfCrocodiles() {
        return numberOfCrocodiles;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public ArrayList<Food> getContents() {
        return contents;
    }

    public void insertFood(Food.FoodType foodType){
        Food food = new Food(foodType);
        contents.add(food);
        currentWeight += food.getWeight();
        System.out.println("Food Inserted.");
        if (currentWeight > maxWeight){
            traceBack(buildTable());
            System.out.println("Bag optimized");
        }
        else{
            updateValue(food, 1);
        }

    }

    public Food removeFood(Food.FoodType foodType){
        for(Food food : contents){
            if(food.getFoodType() == foodType){
                contents.remove(food);
                updateValue(food, -1);
                currentWeight -= food.getWeight();
                return food;
            }
        }
        System.out.println("That food is not in your inventory");
        return null;
    }

    private  void updateValue(Food food, int i){
        switch(food.getFoodType()){
            case Rabbit:
                numberOfRabbits+=i;
                break;
            case Deer:
                numberOfDeer+=i;
                break;
            case Fish:
                numberOfFish+=i;
                break;
            case Wolf:
                numberOfWolves+=i;
                break;
            case Lion:
                numberOfLions+=i;
                break;
            case Crocodile:
                numberOfCrocodiles+=i;
                break;
            default:
                numberOfBerries+=i;
        }
    }

    private void updateAllFoodValues(){
        numberOfBerries=0;
        numberOfCrocodiles=0;
        numberOfDeer=0;
        numberOfFish=0;
        numberOfLions=0;
        numberOfRabbits=0;
        numberOfWolves=0;

        for (Food food : contents){
            updateValue(food, 1);
        }
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private int[][] buildTable() {
        int n = contents.size(); //number of items in inventory
        int B[][] = new int[n + 1][maxWeight + 1]; // knapsack table

        for (int i = 0; i <= n; i++) { // looping through items in inventory
            for (int k = 0; k <= maxWeight; k++) { //looping through 0 - max weight
                if (i == 0 || k == 0) {
                    B[i][k] = 0;     //Initialize the base cases
                } else {
                    if (contents.get(i-1).getWeight() <= k) { // if the current item can be in the solution
                        B[i][k] = max(contents.get(i-1).getHpValue() + B[i - 1][k - contents.get(i-1).getWeight()], B[i-1 ][k]);
                    } else {
                        B[i][k] = B[i - 1][k];
                    }
                }
            }
        }

        return B;
    }

    private void traceBack (int[][] table) {
        // boolean added = false;
        int i = contents.size();
        int k = maxWeight;
        while(i > 0 && maxWeight > 0){
            if(table[i][k] != table[i-1][k]){
                k = k - contents.get(i-1).getWeight();
                i = i-1;
            }
            else{
                currentWeight -= contents.get(i-1).getWeight();
                contents.remove(i-1);
                i= i-1;
            }
        }
        updateAllFoodValues();
    }
}