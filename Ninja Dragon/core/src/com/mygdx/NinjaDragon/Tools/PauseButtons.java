package com.mygdx.NinjaDragon.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.NinjaDragon.NinjaDragon;

public class PauseButtons {
    private Stage stage;
    private boolean backToLevelSelectPressed,backToMainMenuPressed,resumeGamePressed, restartLevelPressed;
    private Sound buttonClickSound;


    public PauseButtons(AssetManager manager, boolean isInfniteMode, BitmapFont menuHeaderFont, final Preferences prefs, TextureAtlas atlas) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, NinjaDragon.batch);
        Gdx.input.setInputProcessor(stage);
        buttonClickSound = manager.get("Audio/Sounds/buttonClickSound.ogg", Sound.class);

        // create the victory buttons background and buttons
        Table pauseTable = new Table();
        pauseTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 100, 0, 0);

        Image pauseBackground = new Image(new TextureRegion(atlas.findRegion("menuBackground"), 0, 0, 350, 233));

        Table pauseText = new Table();
        pauseText.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 185, 0, 0);
        Label pauseLabel = new Label("Paused", new Label.LabelStyle(menuHeaderFont, Color.GOLD));
        pauseText.add(pauseLabel);

        Table table = new Table();
        table.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 80, 0, 0);

        if (!isInfniteMode) {
            Image backToLevelSelectButton = new Image(new TextureRegion(atlas.findRegion("BackToLevelSelect"), 0, 0, 110, 90));
            backToLevelSelectButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    backToLevelSelectPressed = true;
                }
            });
            table.add(backToLevelSelectButton).size(backToLevelSelectButton.getWidth(), backToLevelSelectButton.getHeight());
        } else {
            Image backToMainMenuBtn = new Image(new TextureRegion(atlas.findRegion("BackToMainMenu"), 0, 0, 110, 90));
            backToMainMenuBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    backToMainMenuPressed = true;

                }
            });
            table.add(backToMainMenuBtn).size(backToMainMenuBtn.getWidth(), backToMainMenuBtn.getHeight());
        }
        Image resumeGameButton = new Image(new TextureRegion(atlas.findRegion("ResumeGameButton"), 0, 0, 110, 90));
        resumeGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick(prefs);
                resumeGamePressed = true;
            }
        });
        Image restartLevelButton = new Image(new TextureRegion(atlas.findRegion("RestartLevelButton"), 0, 0, 110, 90));
        restartLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick(prefs);
                restartLevelPressed = true;
            }
        });


        pauseTable.add(pauseBackground).size(pauseBackground.getWidth(), pauseBackground.getHeight());

        table.add(resumeGameButton).size(resumeGameButton.getWidth(), resumeGameButton.getHeight());
        table.add(restartLevelButton).size(restartLevelButton.getWidth(), restartLevelButton.getHeight());


        stage.addActor(pauseTable);
        stage.addActor(pauseText);
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

    public boolean isBackToLevelSelectPressed() {
        return backToLevelSelectPressed;
    }

    public boolean isBackToMainMenuPressed() {
        return backToMainMenuPressed;
    }

    public boolean isResumeGamePressed() {
        return resumeGamePressed;
    }

    public boolean isRestartLevelPressed() {
        return restartLevelPressed;
    }

    public void dispose() {
        stage.dispose();
    }
}