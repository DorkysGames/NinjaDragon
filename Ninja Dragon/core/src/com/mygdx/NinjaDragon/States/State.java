package com.mygdx.NinjaDragon.States;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.NinjaDragon.Tools.AdHandler;

/**
 * Created by Brian on 9/14/2016.
 */
abstract class State {
    OrthographicCamera cam;
    // manages state of game, play state, pause state, etc.
    GameStateManager gsm;
    protected AssetManager manager;
    protected AdHandler handler;
    protected Preferences prefs;

    State(GameStateManager gsm, AssetManager manager, AdHandler handler, Preferences prefs) {
        this.gsm = gsm;
        this.manager = manager;
        this.handler = handler;
        this.prefs = prefs;
        cam = new OrthographicCamera();

    }

    protected abstract void handleInput(float dt);
    // delta time is the difference between one frame rendered and the next frame rendered
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}