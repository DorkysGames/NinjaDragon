package com.mygdx.NinjaDragon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.NinjaDragon.States.MainMenuState;
import com.mygdx.NinjaDragon.Tools.AdHandler;
import com.mygdx.NinjaDragon.States.GameStateManager;

public class NinjaDragon extends Game {
    public static final String TITLE = "Ninja Dragon";
    public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final float PPM = 100;

	// collision bits
	public static final short DRAGON_BIT = 2;
	public static final short GROUND_BIT = 4;
	public static final short SPIKE_BIT = 8;
	public static final short H_SHURIKEN_BIT = 16;
	public static final short V_SHURIKEN_BIT = 32;
	public static final short FINISHLINE_BIT = 64;

    // components needed to run the game
	private GameStateManager gsm;
	public static SpriteBatch batch;
	public AssetManager manager;
	private AdHandler handler;

	public NinjaDragon(AdHandler handler){
		this.handler = handler;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        // add the assets to the asset manager
		manager = new AssetManager();
		manager.load("Audio/Music/Asian_Ambient.mp3", Music.class);
		manager.load("Audio/Music/Chinatown.mp3", Music.class);
		manager.load("Audio/Music/Energetic_Sport_Symphony.mp3", Music.class);
		manager.load("Audio/Music/Positive_Dubstep.mp3", Music.class);
		manager.load("Audio/Music/Back_To_The_8_Bit.mp3", Music.class);
		manager.load("Audio/Music/Chiptune_Vengeance.mp3", Music.class);
		manager.load("Audio/Sounds/buttonClickSound.ogg", Sound.class);
		manager.load("Audio/Sounds/InvincibilityPowerUp.ogg", Sound.class);
		manager.load("Audio/Sounds/ShieldPowerUp.ogg", Sound.class);
		manager.load("Audio/Sounds/PowerDown.ogg", Sound.class);
		manager.load("Audio/Sounds/Warning.ogg", Sound.class);
		manager.load("Audio/Sounds/AncientScrollAppear.ogg", Sound.class);
		manager.load("Audio/Sounds/GongSound.ogg", Sound.class);
		manager.load("Audio/Sounds/HitSound.ogg", Sound.class);
		manager.load("menuTextures.atlas", TextureAtlas.class);
		manager.load("optionsTextures.atlas", TextureAtlas.class);
		manager.load("levelSelectButtons.atlas", TextureAtlas.class);
		manager.load("achievementTextures.atlas", TextureAtlas.class);
		manager.load("LevelComponents/PlayStateTextures.atlas", TextureAtlas.class);
		manager.load("LevelComponents/Invincibility.atlas", TextureAtlas.class);
		manager.finishLoading();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(0,0,0,0);
		gsm.push(new MainMenuState(gsm, manager, handler, prefs));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
	}
}
