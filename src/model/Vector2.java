package model;

import java.util.Random;

public class Vector2{

    private float v0; // y component
    private float v1; // x component

    public Vector2(boolean random){
        if (random) {
            randomizeVector();
        } else {
            this.v0 = 0;
            this.v1 = 0;
        }
    }
    public Vector2(float v0, float v1){
        this.v0 = v0;
        this.v1 = v1;
    }
    private void randomizeVector(){
        Random rand = new Random();
        int v = rand.nextInt(3);
        switch(v){
            case 0:
                this.v0 = 1;
                this.v1 = 1;
                break;
            case 1:
                this.v0 = -1;
                this.v1= 1;
                break;
            case 2:
                this.v0 = 1;
                this.v1 = -1;
                break;
            case 3:
                this.v0 = -1;
                this.v1 = -1;
                break;
        }
    }

    //Getters And Setters
    public void setV0(float v0) {
        this.v0 = v0;
    }
    public void setV1(float v1) {
        this.v1 = v1;
    }
    public float getv1(){
        return this.v1;
    }
    public float getv0(){
        return this.v0;
    }
}
