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

public class VictoryButtons {
    private Stage stage;
    private boolean backToLevelSelectPressed, restartLevelPressed, nextLevelPressed;
    private Sound buttonClickSound;


    public VictoryButtons(AssetManager manager, BitmapFont menuHeaderFont, final Preferences prefs, int levelNumber, TextureAtlas atlas) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, NinjaDragon.batch);
        Gdx.input.setInputProcessor(stage);
        buttonClickSound = manager.get("Audio/Sounds/buttonClickSound.ogg", Sound.class);

        // create the victory buttons background and buttons
        Table levelCompleteTable = new Table();
        levelCompleteTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 100, 0, 0);
        Image levelCompleteBackground = new Image(new TextureRegion(atlas.findRegion("menuBackground"), 0, 0, 350, 233));

        Table levelCompleteText = new Table();
        levelCompleteText.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 185, 0, 0);
        Label levelCompleteLabel = new Label("Victory!", new Label.LabelStyle(menuHeaderFont, Color.GOLD));
        levelCompleteText.add(levelCompleteLabel);

        Table table = new Table();
        table.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 80, 0, 0);

        Image backToLevelSelectButton = new Image(new TextureRegion(atlas.findRegion("BackToLevelSelect"), 0, 0, 110, 90));
        backToLevelSelectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick(prefs);
                backToLevelSelectPressed = true;
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

        Image nextLevelButton = new Image(new TextureRegion(atlas.findRegion("NextLevelButton"), 0, 0, 110, 90));
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick(prefs);
                nextLevelPressed = true;
            }
        });

        levelCompleteTable.add(levelCompleteBackground).size(levelCompleteBackground.getWidth(), levelCompleteBackground.getHeight());

        table.add(backToLevelSelectButton).size(backToLevelSelectButton.getWidth(), backToLevelSelectButton.getHeight());
        table.add(restartLevelButton).size(restartLevelButton.getWidth(), restartLevelButton.getHeight());
        if (levelNumber != 20 && levelNumber < 24 ) {
            table.add(nextLevelButton).size(nextLevelButton.getWidth(), nextLevelButton.getHeight());
        } else {
            if (levelNumber == 20 && prefs.getBoolean("Achievement4", false)) {
                table.add(nextLevelButton).size(nextLevelButton.getWidth(), nextLevelButton.getHeight());
            } else {
                if (levelNumber == 24 && prefs.getBoolean("Achievement9", false)) {
                    table.add(nextLevelButton).size(nextLevelButton.getWidth(), nextLevelButton.getHeight());
                }
            }
        }


        stage.addActor(levelCompleteTable);
        stage.addActor(levelCompleteText);
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

    public boolean isRestartLevelPressed() {
        return restartLevelPressed;
    }

    public boolean isNextLevelPressed() {
        return nextLevelPressed;
    }

    public void dispose() {
        stage.dispose();
    }
}