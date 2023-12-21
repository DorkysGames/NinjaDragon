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
 * Created by Brian on 9/16/2016.
 */
public class LevelSelectStateButtons {
    private Stage stage;
    private boolean levelSelectButtonPressed, backBtnPressed;
    private Sound buttonClickSound;
    private int levelNumber;
    private TextureAtlas levelIconsAtlas = new TextureAtlas();


    public LevelSelectStateButtons(AssetManager manager, Preferences prefs) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, NinjaDragon.batch);
        TextureAtlas menuIconsAtlas = manager.get("menuTextures.atlas");
        levelIconsAtlas = manager.get("levelSelectButtons.atlas");
        Gdx.input.setInputProcessor(stage);
        buttonClickSound = manager.get("Audio/Sounds/buttonClickSound.ogg", Sound.class);
        Image secretLevelImage = new Image(new TextureRegion(levelIconsAtlas.findRegion("secretLevelBtn"), 0, 0, 30, 55));

        Table backgroundTable = new Table();
        backgroundTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2, 0, 0);
        Image background = new Image(new TextureRegion(menuIconsAtlas.findRegion("mainMenuBackground"), 0, 0, 480, 800));
        backgroundTable.add(background).size(background.getWidth(), background.getHeight());
        stage.addActor(backgroundTable);

        // table for header text
        Image levelSelectHeaderText = new Image(new TextureRegion(levelIconsAtlas.findRegion("levelSelectHeaderText"), 0, 0, 428, 173));
        Table headerTable = new Table();
        headerTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT - 90, 0, 0);

        // table and image for back button
        Table backButtonTable = new Table();
        backButtonTable.left().bottom();
        backButtonTable.padBottom(25).padLeft(25);

        Table secretLevelTable = new Table();
        secretLevelTable.setBounds(NinjaDragon.WIDTH / 2 + 160, NinjaDragon.HEIGHT - 80, 0, 0);

        Image backButtonImage = new Image(new TextureRegion(menuIconsAtlas.findRegion("backButton"), 0, 0, 64, 50));
        backButtonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backBtnPressed = true;
            }
        });

        // table and images for level buttons
        Table table = new Table();
        table.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT - 450, 0, 0);

        if (prefs.getBoolean("hyperLevel1Cleared", false)) {
            hyperLevelCleared(table,1);
        } else if (prefs.getBoolean("level1Cleared", false)) {
            levelCleared(table, 1);
        } else {
            levelIcon(table, 1);
        }
        table.add().spaceRight(10);

        for (int i = 2; i <= 25; i++) {
            if (prefs.getBoolean("hyperLevel" + i + "Cleared", false)) {
                hyperLevelCleared(table,i);
            } else if (prefs.getBoolean("level" + i + "Cleared", false)) {
                levelCleared(table, i);
            } else if (prefs.getBoolean("level" + (i-1) + "Cleared", false)) {
                // checks for secret levels 21 and 25
                if (i == 21) {
                    if (prefs.getBoolean("Achievement4", false)) {
                        levelIcon(table, i);
                    } else {
                        levelLocked(table);
                    }
                } else if (i == 25) {
                    if (prefs.getBoolean("Achievement9", false)) {
                        levelIcon(table, i);
                    } else {
                        levelLocked(table);
                    }
                } else {
                    levelIcon(table, i);
                }
            } else {
                levelLocked(table);
            }
            if (i%5 == 0) {
                table.row().padTop(15);
            } else {
                table.add().spaceRight(10);
            }
        }

        // for level 26
        if (prefs.getBoolean("level25Cleared", false)) {
            secretLevelImage.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    levelSelectButtonPressed = true;
                    levelNumber = 26;
                }
            });

        }

        headerTable.add(levelSelectHeaderText).size(levelSelectHeaderText.getWidth(), levelSelectHeaderText.getHeight());

        backButtonTable.add(backButtonImage).size(backButtonImage.getWidth(), backButtonImage.getHeight());

        secretLevelTable.add(secretLevelImage).size(secretLevelImage.getWidth(), secretLevelImage.getHeight());

        stage.addActor(headerTable);
        stage.addActor(table);
        stage.addActor(backButtonTable);
        stage.addActor(secretLevelTable);
    }

    public void draw() {
        stage.draw();
    }

    private void levelIcon(Table table, final int level){
        Image levelBtn = new Image(new TextureRegion(levelIconsAtlas.findRegion("level" + level + "Btn"), 0, 0, 80, 96));
        levelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelSelectButtonPressed = true;
                levelNumber = level;
            }
        });
        table.add(levelBtn).size(levelBtn.getWidth(), levelBtn.getHeight());
    }

    private void levelCleared(Table table, final int level){
        Image levelCleared = new Image(new TextureRegion(levelIconsAtlas.findRegion("levelCleared"), 0, 0, 80, 96));
        levelCleared.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelSelectButtonPressed = true;
                levelNumber = level;
            }
        });
        table.add(levelCleared).size(levelCleared.getWidth(), levelCleared.getHeight());
    }

    private void hyperLevelCleared(Table table, final int level){
        Image hyperLevelCleared = new Image(new TextureRegion(levelIconsAtlas.findRegion("hyperModeCleared"), 0, 0, 80, 96));
        hyperLevelCleared.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelSelectButtonPressed = true;
                levelNumber = level;
            }
        });
        table.add(hyperLevelCleared).size(hyperLevelCleared.getWidth(), hyperLevelCleared.getHeight());
    }

    private void levelLocked(Table table) {
        Image levelLocked = new Image(new TextureRegion(levelIconsAtlas.findRegion("levelLocked"), 0, 0, 80, 96));
        table.add(levelLocked).size(levelLocked.getWidth(), levelLocked.getHeight());
    }

    public void playButtonClick(Preferences prefs) {
        if (prefs.getBoolean("SoundOn", true)) {
            buttonClickSound.play();
        }
    }

    public boolean isLevelSelectButtonPressed() {
        return levelSelectButtonPressed;
    }

    public boolean isBackBtnPressed() {
        return backBtnPressed;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void dispose() {
        stage.dispose();
    }
}
