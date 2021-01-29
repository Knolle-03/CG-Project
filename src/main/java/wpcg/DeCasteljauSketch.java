package wpcg;


import processing.core.PApplet;


public class DeCasteljauSketch extends PApplet {


    public void settings(){
        size(1000, 600);
    }

    public void setup() {



    }

    public void draw(){
        background(0);

    }


    public static void main(String[] args){
        String[] processingArgs = {"wpcg.MySketch"};
        DeCasteljauSketch deCasteljauSketch = new DeCasteljauSketch();
        PApplet.runSketch(processingArgs, deCasteljauSketch);
    }

}
