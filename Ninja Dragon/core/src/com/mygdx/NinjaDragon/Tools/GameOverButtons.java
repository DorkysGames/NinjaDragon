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

/**
 * Created by Brian on 9/27/2016.
 */

public class GameOverButtons {
    private Stage stage, highScoreStage;
    private boolean backToLevelSelectPressed, backToMainMenuPressed, restartLevelPressed, videoButtonPressed;
    private Sound buttonClickSound;
    private Label highScoreLabel;


    public GameOverButtons(AssetManager manager, boolean isInfiniteMode, BitmapFont highScoreFont, BitmapFont menuHeaderFont, final Preferences prefs, AdHandler handler, boolean videoAlreadyDisplayed, TextureAtlas atlas) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, NinjaDragon.batch);
        highScoreStage = new Stage(viewport, NinjaDragon.batch);
        buttonClickSound = manager.get("Audio/Sounds/buttonClickSound.ogg", Sound.class);

        // create the game over background and buttons
        Table gameOverTable = new Table();
        gameOverTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 100, 0, 0);

        Image gameOverBackground = new Image(new TextureRegion(atlas.findRegion("menuBackground"), 0, 0, 350, 233));

        Table table = new Table();
        table.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 100, 0, 0);
        if (!isInfiniteMode) {
            Image backToLevelSelectBtn = new Image(new TextureRegion(atlas.findRegion("BackToLevelSelect"), 0, 0, 110, 90));
            backToLevelSelectBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    backToLevelSelectPressed = true;
                }
            });
            table.add(backToLevelSelectBtn).size(backToLevelSelectBtn.getWidth(), backToLevelSelectBtn.getHeight());
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

        Image restartLevelBtn = new Image(new TextureRegion(atlas.findRegion("RestartLevelButton"), 0, 0, 110, 90));
        restartLevelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick(prefs);
                restartLevelPressed = true;
            }
        });

        gameOverTable.add(gameOverBackground).size(gameOverBackground.getWidth(), gameOverBackground.getHeight());

        table.add().spaceRight(25);
        table.add(restartLevelBtn).size(restartLevelBtn.getWidth(), restartLevelBtn.getHeight());

        stage.addActor(gameOverTable);
        stage.addActor(table);

        // if it is infinite mode, set the high score label
        // if the high score is a new high score, set the star
        if (!isInfiniteMode) {
            Table gameOverText = new Table();
            gameOverText.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 185, 0, 0);
            Label gameOverLabel = new Label("Game Over", new Label.LabelStyle(menuHeaderFont, Color.GOLD));
            gameOverText.add(gameOverLabel);
            stage.addActor(gameOverText);
        } else {
            Table infiniteModeScore = new Table();
            infiniteModeScore.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 185, 0, 0);
            highScoreLabel = new Label("High Score: " + prefs.getInteger("highScore", 0), new Label.LabelStyle(highScoreFont, Color.GOLD));
            infiniteModeScore.add(highScoreLabel);

            Table newHighScoreTable = new Table();
            newHighScoreTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 185, 0, 0);
            Image newHighScorePicture = new Image(new TextureRegion(atlas.findRegion("newHighScoreImage"), 0, 0, 48, 48));
            newHighScoreTable.add(newHighScorePicture).size(newHighScorePicture.getWidth(), newHighScorePicture.getHeight()).padRight(240);
            highScoreStage.addActor(newHighScoreTable);

            stage.addActor(infiniteModeScore);
        }

        // set the video button based on whether the video is ready to be played
        if (handler.isVideoAdReady() && !videoAlreadyDisplayed) {
            Table videoButtonTable = new Table();
            videoButtonTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 35, 0, 0);

            Image videoButton = new Image(new TextureRegion(atlas.findRegion("videoButton"), 0, 0, 250, 50));
            videoButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    videoButtonPressed = true;
                }
            });
        videoButtonTable.add(videoButton).size(videoButton.getWidth(), videoButton.getHeight());
            stage.addActor(videoButtonTable);
        } else {
            Table disabeledVideoButtonTable = new Table();
            disabeledVideoButtonTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 35, 0, 0);
            Image disabeledVideoButton = new Image(new TextureRegion(atlas.findRegion("disabeledVideoButton"), 0, 0, 250, 50));
            disabeledVideoButtonTable.add(disabeledVideoButton).size(disabeledVideoButton.getWidth(), disabeledVideoButton.getHeight());
            stage.addActor(disabeledVideoButtonTable);
        }
    }

    public void draw() {
        stage.draw();
    }

    public void drawHighScoreImage() {
        highScoreStage.draw();
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

    public boolean isRestartLevelPressed() {
        return restartLevelPressed;
    }

    public boolean isVideoButtonPressed() {
        return videoButtonPressed ;
    }

    public void setIsVideoButtonPressed(boolean videoButtonPressed) {
        this.videoButtonPressed = videoButtonPressed;
    }

    public void setInputListener (){
        Gdx.input.setInputProcessor(stage);
    }

    public void updateHighScore(Preferences prefs) {
        highScoreLabel.setText("High Score: " + prefs.getInteger("highScore", 0));
    }

    public void dispose() {
        stage.dispose();
        highScoreStage.dispose();
    }
}