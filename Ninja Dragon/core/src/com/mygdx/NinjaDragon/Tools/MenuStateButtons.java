package com.mygdx.NinjaDragon.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.NinjaDragon.NinjaDragon;

/**
 * Created by Brian on 9/14/2016.
 */
    public class MenuStateButtons {
    private Stage stage;
        private boolean levelSelectPressed, infiniteModePressed, achievementsPressed, optionsPressed, policyPressed;
        private Sound buttonClickSound;


    public MenuStateButtons(AssetManager manager, final Preferences prefs) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
            stage = new Stage(viewport, NinjaDragon.batch);
            TextureAtlas mainMenuAtlas = manager.get("menuTextures.atlas");
            Gdx.input.setInputProcessor(stage);
            buttonClickSound = manager.get("Audio/Sounds/buttonClickSound.ogg", Sound.class);

            Table backgroundTable = new Table();
            backgroundTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2, 0, 0);
            Image background = new Image(new TextureRegion(mainMenuAtlas.findRegion("mainMenuBackground"), 0, 0, 480, 800));
            backgroundTable.add(background).size(background.getWidth(), background.getHeight());
            stage.addActor(backgroundTable);

            Table titleTable = new Table();
            titleTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT - 135, 0, 0);
            Image ninjaDragonTitle = new Image(new TextureRegion(mainMenuAtlas.findRegion("NinjaDragonTitle"), 0, 0, 360, 251));
            titleTable.add(ninjaDragonTitle).size(ninjaDragonTitle.getWidth(), ninjaDragonTitle.getHeight());
            stage.addActor(titleTable);

        // add the buttons for the main menu
            Table table = new Table();
            table.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 - 50, 0, 0);
            Image levelSelectBtn = new Image(new TextureRegion(mainMenuAtlas.findRegion("levelSelectBtn"), 0, 0, 200, 50));
            levelSelectBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    levelSelectPressed = true;
                }
            });

        Image infiniteModeBtn = new Image(new TextureRegion(mainMenuAtlas.findRegion("infiniteModeBtn"), 0, 0, 200, 50));
            infiniteModeBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    infiniteModePressed = true;
                }
            });

        Image achievementsBtn = new Image(new TextureRegion(mainMenuAtlas.findRegion("achievementsBtn"), 0, 0, 200, 50));
            achievementsBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    achievementsPressed = true;
                }
            });

        Image optionsBtn = new Image(new TextureRegion(mainMenuAtlas.findRegion("optionsBtn"), 0, 0, 200, 50));
        optionsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick(prefs);
                optionsPressed = true;
            }
        });

        Image policyBtn = new Image(new TextureRegion(mainMenuAtlas.findRegion("policyBtn"), 0, 0, 200, 50));
        policyBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick(prefs);
                policyPressed = true;
            }
        });

            table.add(levelSelectBtn).size(levelSelectBtn.getWidth(), levelSelectBtn.getHeight());
            table.row().padTop(25);
            table.add(infiniteModeBtn).size(infiniteModeBtn.getWidth(), infiniteModeBtn.getHeight());
            table.row().padTop(25);
            table.add(achievementsBtn).size(achievementsBtn.getWidth(), achievementsBtn.getHeight());
            table.row().padTop(25);
            table.add(optionsBtn).size(optionsBtn.getWidth(), optionsBtn.getHeight());
            table.row().padTop(25);
            table.add(policyBtn).size(policyBtn.getWidth(), policyBtn.getHeight());

            stage.addActor(table);
        }

        public void draw() {
            stage.draw();
        }

        private void playButtonClick(Preferences prefs) {
            if (prefs.getBoolean("SoundOn", true)) {
                buttonClickSound.play();
            }
        }

        public boolean isLevelSelectPressed() {
            return levelSelectPressed;
        }

        public boolean isInfiniteModePressed() {
            return infiniteModePressed;
        }

        public boolean isAchievementsPressed() {
            return achievementsPressed;
        }

    public boolean isOptionsPressed() {
        return optionsPressed;
    }

    public boolean isPolicyPressed() {
        return policyPressed;
    }

    public void setPolicyPressed(boolean policyPressed) {
        this.policyPressed = policyPressed;
    }

    public void dispose() {
        stage.dispose();
    }
    }

