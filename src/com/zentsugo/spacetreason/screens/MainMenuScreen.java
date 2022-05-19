package com.zentsugo.spacetreason.screens;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.zentsugo.spacetreason.Listener;
import com.zentsugo.spacetreason.SpaceTreason;
import com.zentsugo.spacetreason.Utils;

/**
 * Created by zents on 26/01/2018.
 */

public class MainMenuScreen implements Screen {

    private SpriteBatch batch;
    private SpaceTreason game;
    private static Texture currentTexture;
    private Timer timer;
    /*private Stage stage;
    private Table table;
    private Skin skin;
    private TextButton startBtn;*/

    public MainMenuScreen(SpaceTreason game) {
        this.game = game;
        batch = game.batch;
    }

    @Override
    public void show() {
    	/*stage = new Stage();
    	table = new Table();
    	skin = new Skin();
    	startBtn = new TextButton("Start", skin);
    	table.add(startBtn).row();
    	stage.addActor(table);*/
    	currentTexture = Listener.play_btn_texture;
    	Gdx.input.setInputProcessor(new InputAdapter () {
    		   @Override
    		   public boolean touchDown (int x, int y, int pointer, int button) {
   	    		if (x >= (Listener.SCREEN_WIDTH / 2) - (Listener.play_btn_texture.getWidth() / 2) &&
   	    				x <= (Listener.SCREEN_WIDTH / 2) + (Listener.play_btn_texture.getWidth() / 2) &&
   	    				y >= (Listener.SCREEN_HEIGHT / 2) - (Listener.play_btn_texture.getHeight() / 2) &&
   	    				y <= (Listener.SCREEN_HEIGHT / 2) + (Listener.play_btn_texture.getHeight() / 2)) {
   	    			currentTexture = Listener.play_btn2_texture;
   	    			timer = Utils.timer(new Runnable(){
   						public void run() {
   							game.setScreen(new PlayScreen(game));
   						}
   					}, 2);
   	    		}
    		       return true;
    		   }

    		   @Override
    		   public boolean touchUp (int x, int y, int pointer, int button) {
    			   if (currentTexture != Listener.play_btn_texture) currentTexture = Listener.play_btn_texture;
    			   return true;
    		   }
    		});
    }

    //int count = 0;
    //float time;
    
    public void update(float delta) {
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        
        batch.begin();
        	batch.draw(Listener.BG2_texture, 0, 0);
        	batch.draw(currentTexture, (Listener.SCREEN_WIDTH / 2) - (Listener.play_btn_texture.getWidth() / 2),
        			(Listener.SCREEN_HEIGHT / 2) - (Listener.play_btn_texture.getHeight() / 2));
        batch.end();
        
        //stage.act(delta);
        
        //loop each second
        /*time += delta;
        if (time >= 1) {
            System.out.println("Time : " + time + " // Delta : " + delta);
            System.out.println("Count : " + count);
            count++;
            time-=1;
        }*/
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
