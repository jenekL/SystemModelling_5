package com.company;

import java.util.Random;

public class ValuesGenerator {
    private Random r = new Random();
    private final int MX_EXP = 5;
    private final int MX_UNIFORM = 20;
    private final double SKO_UNIFORM = 5;

    public ValuesGenerator() {
    }

    public int discoverFate(int max){
        return r.nextInt(max);
    }

    public double getKsi(){
        return r.nextDouble();
    }

    public double getUniformY(){ //obsluj
        return (MX_UNIFORM - SKO_UNIFORM) + ((MX_UNIFORM + SKO_UNIFORM) - (MX_UNIFORM - SKO_UNIFORM)) * getKsi();
    }

    public double getExpY(){ //postup
        return (-1) * MX_EXP * Math.log(getKsi());
    }


}
