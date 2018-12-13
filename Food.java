package model;

public class Food {
    public enum FoodType{Berries, Rabbit, Deer, Fish, Wolf, Lion, Crocodile}
    private FoodType foodType;
    private int hpValue;
    private int weight;


    public Food(FoodType foodType){
        this.foodType = foodType;
        setValues();
    }

    public void setValues(){
        switch(getFoodType()){
            case Rabbit:
                hpValue = 3;
                weight = 2;
                break;
            case Deer:
                hpValue = 5;
                weight = 3;
                break;
            case Fish:
                hpValue = 2;
                weight = 2;
                break;
            case Wolf:
                hpValue = 2;
                weight = 1;
                break;
            case Lion:
                hpValue = 5;
                weight = 2;
                break;
            case Crocodile:
                hpValue = 4;
                weight = 2;
                break;
            default:
                hpValue = 1;
                weight = 1;
        }
    }

    public FoodType getFoodType(){
        return foodType;
    }

    public int getHpValue() {
        return hpValue;
    }

    public int getWeight() {
        return weight;
    }

    public String toString(){
        return "Food type: "+ getFoodType() + ", HP value: " + getHpValue() + ", Weight: " + getWeight();
    }


}
