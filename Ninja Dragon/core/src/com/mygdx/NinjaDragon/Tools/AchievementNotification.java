package com.mygdx.NinjaDragon.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.NinjaDragon.NinjaDragon;

/**
 * Created by Brian on 12/20/2016.
 */

public class AchievementNotification{
    private Stage stage;
    private FreeTypeFontGenerator generator;
    private BitmapFont notificationFont;

    public AchievementNotification(int achievementNumber, TextureAtlas atlas) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, NinjaDragon.batch);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Quarto_Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 22;
        notificationFont = generator.generateFont(parameter);

        Table backgroundTable = new Table();
        backgroundTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 296, 0, 0);
        Image notificationBackground = new Image(new TextureRegion(atlas.findRegion("notificationBackground"), 0, 0, 400, 75));
        backgroundTable.add(notificationBackground).size(notificationBackground.getWidth(), notificationBackground.getHeight());
        stage.addActor(backgroundTable);

        Table achievementTable = new Table();
        achievementTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 296, 0, 0);
        String achievementLabelText = determineAchievement(achievementNumber);
        Label achievementNotification = new Label(achievementLabelText, new Label.LabelStyle(notificationFont, Color.FIREBRICK));
        achievementTable.add(achievementNotification);

        stage.addActor(achievementTable);


    }
    public void draw() {
        stage.draw();
    }

    private String determineAchievement(int achievementNumber){
        String achievementText = "";
        switch(achievementNumber){
            case 1:
                achievementText = "Achievement Complete!\nComplete Level 1";
                break;
            case 2:
                achievementText = "Achievement Complete!\nComplete Level 10";
                break;
            case 3:
                achievementText = "Achievement Complete!\nComplete Level 20";
                break;
            case 4:
                achievementText = "Achievement Complete!\nComplete Level 20 without powerups";
                break;
            case 5:
                achievementText = "Achievement Complete!\nComplete Secret Level 25";
                break;
            case 6:
                achievementText = "Achievement Complete!\nScore 50 In Infinite Mode";
                break;
            case 7:
                achievementText = "Achievement Complete!\nScore 100 In Infinite Mode";
                break;
            case 8:
                achievementText = "Achievement Complete!\nScore 250 In Infinite Mode";
                break;
            case 9:
                achievementText = "Achievement Complete!\nScore 500 In Infinite Mode";
                break;
            case 10:
                achievementText = "Achievement Complete!\nUse 10 Shields";
                break;
            case 11:
                achievementText = "Achievement Complete!\nUse 25 Shields";
                break;
            case 12:
                achievementText = "Achievement Complete!\nUse 50 Shields";
                break;
            case 13:
                achievementText = "Achievement Complete!\nUse 10 Invincibility";
                break;
            case 14:
                achievementText = "Achievement Complete!\nUse 25 Invincibility";
                break;
            case 15:
                achievementText = "Achievement Complete!\nUse 50 Invincibility";
                break;
            case 16:
                achievementText = "Achievement Complete!\nDie 100 Times";
                break;
            case 17:
                achievementText = "Achievement Complete!\nDie 250 Times";
                break;
            case 18:
                achievementText = "Achievement Complete!\nDie 500 Times";
                break;
            case 100:
                achievementText = "Achievement Complete!\nMultiple achievements completed";
                break;

        }
        return achievementText;
    }

    public void dispose() {
        generator.dispose();
        notificationFont.dispose();
        stage.dispose();
    }
}
