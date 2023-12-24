package com.mygdx.NinjaDragon.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.mygdx.NinjaDragon.Tools.AchievementStateButtons;
import com.mygdx.NinjaDragon.Tools.AdHandler;

/**
 * Created by Brian on 9/14/2016.
 */
class AchievementState extends State {
    private AchievementStateButtons achievementStateButtons;

    AchievementState(GameStateManager gsm, AssetManager manager, AdHandler handler, Preferences prefs) {
        super(gsm, manager, handler, prefs);
        achievementStateButtons = new AchievementStateButtons(manager, prefs);
    }

    @Override
    protected void handleInput(float dt) {
        if (achievementStateButtons.isBackBtnPressed() || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            achievementStateButtons.playButtonClick(prefs);
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
        achievementStateButtons.draw();
    }

    @Override
    public void dispose() {
        achievementStateButtons.dispose();
    }


}
