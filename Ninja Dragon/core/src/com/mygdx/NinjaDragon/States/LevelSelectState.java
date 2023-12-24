package com.mygdx.NinjaDragon.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.mygdx.NinjaDragon.Tools.AdHandler;
import com.mygdx.NinjaDragon.Tools.LevelSelectStateButtons;

/**
 * Created by Brian on 9/14/2016.
 */
class LevelSelectState extends State {
    private LevelSelectStateButtons levelSelectStateButtons;

    LevelSelectState(GameStateManager gsm, AssetManager manager, AdHandler handler, Preferences prefs) {
    super(gsm, manager, handler, prefs);
    levelSelectStateButtons = new LevelSelectStateButtons(manager, prefs);
        // if music is not playing, play it
        Music music = manager.get("Audio/Music/Asian_Ambient.mp3");
        if (!music.isPlaying()){
            if (prefs.getBoolean("MusicOn", true)) {
                music.stop();
                music.setLooping(true);
                music.play();
            }
        }
    }

    @Override
    protected void handleInput(float dt) {
    if (levelSelectStateButtons.isBackBtnPressed() || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
        levelSelectStateButtons.playButtonClick(prefs);
        gsm.set(new MainMenuState(gsm, manager, handler, prefs));
    }
        // go to level selected
    if (levelSelectStateButtons.isLevelSelectButtonPressed()) {
        levelSelectStateButtons.playButtonClick(prefs);
        gsm.set(new PlayState(gsm, manager, handler, prefs, levelSelectStateButtons.getLevelNumber()));
    }
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        levelSelectStateButtons.draw();
    }

    @Override
    public void dispose() {
        levelSelectStateButtons.dispose();
    }


}
