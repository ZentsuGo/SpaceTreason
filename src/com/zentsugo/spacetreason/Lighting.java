package com.zentsugo.spacetreason;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Lighting {
	private static World world;				
	private static TiledMap tilemap;
	private static ArrayList<Light> lights = new ArrayList<Light>();
	private static int MAX_LIGHTS = 8; //to be changed in options
	private static RayHandler rayHandler;
	
	
	public static void generateLights(World w, TiledMap t) {
		world = w;
		tilemap = t;
		
		rayHandler = new RayHandler(world);
		
		int numLights = 0;
		
		MapLayer lightLayer = t.getLayers().get("Light");
		
		for (MapObject object : lightLayer.getObjects()) {
			if (numLights < MAX_LIGHTS) {
				Light light = null;
				if (object instanceof RectangleMapObject) {
					Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
					light = new PointLight(rayHandler, 1000, Color.YELLOW, 12,
							(rectangle.x + rectangle.width) / 2, (rectangle.y + rectangle.height) / 2);
				} else if (object instanceof CircleMapObject) {
					Circle circle = ((CircleMapObject) object).getCircle();
					light = new PointLight(rayHandler, 1000, Color.YELLOW, 12, circle.x, circle.y);
				}
			}
		}
	}
	
	public static RayHandler getRayHandler() {
		return rayHandler;
	}
	
	public static void update() {
		rayHandler.updateAndRender();
	}
	
	public static void clearLights() {
		for (Light light : lights) {
			light.dispose();
		}
		lights.clear();
	}
	
	public static void dispose() {
		clearLights();
		rayHandler.dispose();
	}
}
