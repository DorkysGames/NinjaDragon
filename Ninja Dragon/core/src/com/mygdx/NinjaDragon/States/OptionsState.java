package com.mygdx.NinjaDragon.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.mygdx.NinjaDragon.Tools.AdHandler;
import com.mygdx.NinjaDragon.Tools.OptionsStateButtons;

/**
 * Created by Brian on 1/9/2017.
 */

class OptionsState extends State {
    private OptionsStateButtons optionsStateButtons;
    private Music music;

    OptionsState(GameStateManager gsm, AssetManager manager, AdHandler handler, Preferences prefs) {
        super(gsm, manager, handler, prefs);
        optionsStateButtons = new OptionsStateButtons(manager, prefs);
        music = manager.get("Audio/Music/Asian_Ambient.mp3");
    }

    @Override
    protected void handleInput(float dt) {
        // turn the music off
        if (optionsStateButtons.isMusicOffPressed()) {
            handleOptions("MusicOn", false);
            optionsStateButtons.setMusicOffPressed(false);
            if (music.isPlaying()){
                music.pause();
            }
        } else
            // turn the music on
            if(optionsStateButtons.isMusicOnPressed()) {
                handleOptions("MusicOn", true);
                optionsStateButtons.setMusicOnPressed(false);
                music.stop();
                music.play();
            } else
            // turn the sound off
        if(optionsStateButtons.isSoundOffPressed()) {
            handleOptions("SoundOn", false);
            optionsStateButtons.setSoundOffPressed(false);
        } else
        // turn the sound on
        if(optionsStateButtons.isSoundOnPressed()) {
            handleOptions("SoundOn", true);
            optionsStateButtons.setSoundOnPressed(false);
        } else
        if(optionsStateButtons.isHyperModeOffPressed()) {
            handleOptions("hyperMode", false);
            optionsStateButtons.setHyperModeOffPressed(false);
        } else
        if(optionsStateButtons.isHyperModeOnPressed()) {
            handleOptions("hyperMode", true);
            optionsStateButtons.setHyperModeOnPressed(false);
        } else
        // select outer buttons
        if(optionsStateButtons.isOuterButtonsPressed()) {
            handleOptions("InnerButtons", false);
            optionsStateButtons.setOuterButtonsPressed(false);
        } else
        // select inner buttons
        if(optionsStateButtons.isInnerButtonsPressed()) {
            handleOptions("InnerButtons", true);
            optionsStateButtons.setInnerButtonsPressed(false);
        } else
            // select no touch screen
            if(optionsStateButtons.isTouchScreenCheckedPressed()) {
        handleOptions("TouchScreenOn", false);
        optionsStateButtons.setTouchScreenCheckedPressed(false);
        } else
            // select touch screen
            if(optionsStateButtons.isTouchScreenPressed()) {
        handleOptions("TouchScreenOn", true);
        optionsStateButtons.setTouchScreenPressed(false);
        } else
        // select left buttons
        if(optionsStateButtons.isLeftButtonsPressed()) {
            handleOptions("LeftButtons", true);
            optionsStateButtons.setLeftButtonsPressed(false);
        } else
        // select right buttons
        if(optionsStateButtons.isRightButtonsPressed()) {
            handleOptions("LeftButtons", false);
            optionsStateButtons.setRightButtonsPressed(false);
        } else
        // go back to the main menu
        if (optionsStateButtons.isBackBtnPressed() || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            optionsStateButtons.playButtonClick(prefs);
            gsm.set(new MainMenuState(gsm, manager, handler, prefs));
        }
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
    sb.setProjectionMatrix(cam.combined);
    optionsStateButtons.draw();
    }

    private void handleOptions(String prefKey, boolean prefValue) {
        prefs.putBoolean(prefKey, prefValue);
        prefs.flush();
        optionsStateButtons.dispose();
        optionsStateButtons = new OptionsStateButtons(manager,prefs);
    }

    @Override
    public void dispose() {
        optionsStateButtons.dispose();
    }
}
