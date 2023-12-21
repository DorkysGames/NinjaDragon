package com.mygdx.NinjaDragon.Tools;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.NinjaDragon.NinjaDragon;

import java.util.Locale;

/**
 * Created by Brian on 8/17/15.
 */
public class Hud implements Disposable {

    private Stage stage;
    private Stage loadingStage;

    private Label scoreLabel;
    private Label distanceLabel;
    private Label goalLabel;

    public Hud(boolean isInfiniteMode, BitmapFont hudFont, TextureAtlas atlas, Preferences prefs, boolean hyperModeFlag, int levelNumber){
        int score = 0;
        int distanceAchieved = 0;
        int goalDistance = 0;
        int deathValue;
        String deathValueString;

        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        loadingStage = new Stage(viewport);
        if (hyperModeFlag){
            deathValue = prefs.getInteger("level" + levelNumber + "attemptCountH", 0);
        } else {
            deathValue = prefs.getInteger("level" + levelNumber + "attemptCount", 0);
        }

        deathValueString = String.valueOf(deathValue);


        if (isInfiniteMode) {
            // make a score hud if infinite mode
            Table infiniteModeTable = new Table();
            infiniteModeTable.top();
            infiniteModeTable.setFillParent(true);
            scoreLabel = new Label(String.format(Locale.getDefault(),"Score: %01d", score), new Label.LabelStyle(hudFont, Color.WHITE));
            infiniteModeTable.add(scoreLabel);
            stage.addActor(infiniteModeTable);

        } else {
            // made a level hud if not infinite mode
            Table flagTable = new Table();
            flagTable.top().left();
            flagTable.setFillParent(true);
            flagTable.add().spaceRight(130).padTop(70);
            Image finish_flag = new Image(new TextureRegion(atlas.findRegion("flag"), 0, 0, 71, 50));
            flagTable.add(finish_flag).size(finish_flag.getWidth(), finish_flag.getHeight());
            stage.addActor(flagTable);

            Table levelDistanceTable = new Table();
            levelDistanceTable.top();
            levelDistanceTable.setFillParent(true);
            distanceLabel = new Label(String.format(Locale.getDefault(),"%01d", distanceAchieved), new Label.LabelStyle(hudFont, Color.WHITE));
            goalLabel = new Label(String.format(Locale.getDefault(),"/%01d", goalDistance), new Label.LabelStyle(hudFont, Color.WHITE));
            levelDistanceTable.add().spaceRight(125);
            levelDistanceTable.add(distanceLabel);
            levelDistanceTable.add(goalLabel);
            stage.addActor(levelDistanceTable);
        }
        // display the loading text if the game is loading
        Table loadingTextTable = new Table();
        loadingTextTable.center().padBottom(50);
        loadingTextTable.setFillParent(true);
        if (levelNumber == 26) {
            Label loadingLabel = new Label("You won't survive!\n     Attempt " + deathValueString + "...", new Label.LabelStyle(hudFont, Color.RED));
            loadingTextTable.add(loadingLabel);
        } else {
            Label loadingLabel = new Label("Attempt " + deathValueString + "...", new Label.LabelStyle(hudFont, Color.BLACK));
            loadingTextTable.add(loadingLabel);
        }
        loadingStage.addActor(loadingTextTable);

    }

    public void draw() {
        stage.draw();
    }

    public void loadingStageDraw() {
        loadingStage.draw();
    }

    public void updateScore(int score){
        scoreLabel.setText(String.format(Locale.getDefault(),"Score: %01d", score));
    }

    public void setDistanceAchieved(int distanceAchieved) {
        distanceLabel.setText(String.format(Locale.getDefault(),"%01d", distanceAchieved));
    }

    public void setGoalDistance(int goalDistance) {
        goalLabel.setText(String.format(Locale.getDefault(),"/%01d", goalDistance));
    }

    @Override
    public void dispose() {
        loadingStage.dispose();
        stage.dispose();
    }
}
