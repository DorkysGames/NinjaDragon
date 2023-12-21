package com.mygdx.NinjaDragon.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.mygdx.NinjaDragon.NinjaDragon;
import com.mygdx.NinjaDragon.Tools.AdHandler;
import com.mygdx.NinjaDragon.Tools.ExitGameButtons;
import com.mygdx.NinjaDragon.Tools.MenuStateButtons;

/**
 * Created by Brian on 9/14/2016.
 */
public class MainMenuState extends State {
    private MenuStateButtons menuStateButtons;
    private ExitGameButtons exitGameButtons;
    private boolean exitMenuRendered;
    private boolean renderExitMenu;
    private Music music;

    public MainMenuState(GameStateManager gsm, AssetManager manager, AdHandler handler, Preferences prefs) {
        super(gsm, manager, handler, prefs);
        cam.setToOrtho(false, NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2);
        menuStateButtons = new MenuStateButtons(manager, prefs);
        exitMenuRendered = false;
        renderExitMenu = false;
        checkIfMusicIsPlaying("Chinatown");
        checkIfMusicIsPlaying("Energetic_Sport_Symphony");
        checkIfMusicIsPlaying("Positive_Dubstep");
        checkIfMusicIsPlaying("Back_To_The_8_Bit");
        checkIfMusicIsPlaying("Chiptune_Vengeance");
        music = manager.get("Audio/Music/Asian_Ambient.mp3");
        if (!music.isPlaying()) {
            if (prefs.getBoolean("MusicOn", true)) {
                music.stop();
                music.setLooping(true);
                music.play();
            }
        }
    }

    @Override
    protected void handleInput(float dt) {
        // Go to level select screen
        if (menuStateButtons.isLevelSelectPressed()) {
            gsm.set(new LevelSelectState(gsm, manager, handler, prefs));
        } else
            // Go to infinite mode
        if(menuStateButtons.isInfiniteModePressed()) {
            gsm.set(new PlayState(gsm, manager,handler, prefs, 0));
        } else
            // Go to achievement screen
        if(menuStateButtons.isAchievementsPressed()) {
            gsm.set(new AchievementState(gsm, manager,handler, prefs));
        } else
        // go to options screen
        if(menuStateButtons.isOptionsPressed()) {
            gsm.set(new OptionsState(gsm, manager,handler, prefs));
        } else
            // open privacy policy
        if(menuStateButtons.isPolicyPressed()) {
            Gdx.net.openURI("https://www.freeprivacypolicy.com/privacy/view/d5e1a714772b9b614be95f5e65126c22");
            menuStateButtons.setPolicyPressed(false);
         }
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            renderExitMenu = true;
        }
        if (exitMenuRendered) {
            if (exitGameButtons.isYesPressed()) {
                exitGameButtons.setIsYesPressed(false);
                if (music.isPlaying()) {
                    music.stop();
                }
                this.dispose();
                Gdx.app.exit();
            }
            if (exitGameButtons.isNoPressed()) {
                exitGameButtons.setIsNoPressed(false);
                renderExitMenu = false;
                exitMenuRendered = false;
                exitGameButtons.dispose();
                exitGameButtons = null;
                menuStateButtons.dispose();
                menuStateButtons = new MenuStateButtons(manager, prefs);
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        menuStateButtons.draw();

        // creating the exit menu
        if (renderExitMenu) {
            if (!exitMenuRendered) {
                exitGameButtons = new ExitGameButtons(manager, prefs);
                exitMenuRendered = true;
                if (prefs.getBoolean("SoundOn", true)) {
                    manager.get("Audio/Sounds/AncientScrollAppear.ogg", Sound.class).play();
                }
            }
            exitGameButtons.draw();
        }
    }

    @Override
    public void dispose() {
        menuStateButtons.dispose();
        if (exitGameButtons != null) {
            exitGameButtons.dispose();
        }
    }

    private void checkIfMusicIsPlaying(String songName) {
        music = manager.get("Audio/Music/" + songName + ".mp3");
        if (music.isPlaying()){
            music.stop();
        }
    }
}
