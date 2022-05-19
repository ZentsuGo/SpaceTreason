package com.zentsugo.spacetreason;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.zentsugo.spacetreason.entities.Player;

public class Utils {
	
	//thanks Madmenyo
	public static ShapeRenderer srender = new ShapeRenderer();
	
	public static void drawCone(float gx, float gy, float x1, float x, float y, float z, Color color, Matrix4 projectionMatrix) {
		srender.setProjectionMatrix(projectionMatrix);
		srender.begin(ShapeRenderer.ShapeType.Filled);
		srender.setColor(color);
		srender.triangle(gx+5f, gy+5f, gx+5f, gy+5f, gx+5f, gy+4f);
		srender.end();
	}
	
	public static void rumble(float power, float time) {
		Rumble.rumble(power, time);
	}
	
	public static void rumbleCheck(OrthographicCamera camera, Player player, float delta) {
		if (Rumble.getRumbleTimeLeft() > 0){
		    Rumble.tick(delta);
		    camera.translate(Rumble.getPos());
		 } else {
			 camera.position.lerp(new Vector3(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0), .2f);
		}
	}
	
	public static Timer timer(final Runnable runnable, int seconds) {
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				Gdx.app.postRunnable(runnable);
			}
		}, seconds * 1000);
		return t;
	}
	
    public static void drawLine(Vector2 start, Vector2 end, Color color, int lineWidth, Matrix4 projectionMatrix) {
    	Gdx.gl20.glLineWidth(lineWidth);
    	Gdx.gl20.glEnable(GL20.GL_BLEND);
    	Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        srender.setProjectionMatrix(projectionMatrix);
        srender.begin(ShapeRenderer.ShapeType.Line);
	        srender.setColor(color);
	        srender.line(start, end);
        srender.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);
        Gdx.gl20.glLineWidth(1);
    }

	public static boolean isOutOfScreen(float x, float y) {
		if (x >= 500 || x <= -50) return true;
		if (y >= 700 || y <= -50) return true;
		return false;
	}
	
	public static float faceTo(Vector2 position, Vector2 target) {
		return (float) Math.toRadians(target.sub(position).angle());
	}
	
	public static Vector2 moveTo(Vector2 position, float angle) {
		float moveX = (float) Math.cos(angle);
		float moveY = (float) Math.sin(angle);
		return new Vector2(moveX, moveY);
	}
	
	public static float randomX() {
		Random r = new Random();
		return (float) r.nextInt(349)+1;
	}
	
	public static float randomY() {
		Random r = new Random();
		return (float) r.nextInt(599)+1;
	}
}
