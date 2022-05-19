package com.zentsugo.spacetreason;

import java.util.Random;
import com.badlogic.gdx.math.Vector3;

//thanks carelesslabs
public class Rumble {
    private static float time = 0;
    private static float currentTime = 0;
    private static float power = 0;
    private static float currentPower = 0;
    private static Random random;
    private static Vector3 pos = new Vector3();
 
    public static void rumble(float apower, float atime) {
        random = new Random();
        power = apower;
        time = atime;
        currentTime = 0;
    }
 
    public static Vector3 tick(float delta) {
        if (currentTime <= time) {
            currentPower = power * ((time - currentTime) / time);
 
            pos.x = (random.nextFloat() - 0.5f) * 2 * currentPower;
            pos.y = (random.nextFloat() - 0.5f) * 2 * currentPower;
 
            currentTime += delta;
        } else {
            time = 0;
        }
        return pos;
    }
 
    public static float getRumbleTimeLeft() {
        return time;
    }
 
    public static Vector3 getPos() {
        return pos;
    }
}