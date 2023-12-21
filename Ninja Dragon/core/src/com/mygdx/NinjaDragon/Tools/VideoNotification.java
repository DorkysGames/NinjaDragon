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

public class VideoNotification{
    private Stage invincibilityStage;
    private Stage shieldStage;
    private FreeTypeFontGenerator generator;
    private BitmapFont notificationFont;

    public VideoNotification(TextureAtlas atlas) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        invincibilityStage = new Stage(viewport, NinjaDragon.batch);
        shieldStage = new Stage(viewport, NinjaDragon.batch);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Quarto_Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 22;
        notificationFont = generator.generateFont(parameter);

        Table invincibilityBackgroundTable = new Table();
        invincibilityBackgroundTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 296, 0, 0);

        Image invincibilityNotificationBackground = new Image(new TextureRegion(atlas.findRegion("notificationBackground"), 0, 0, 400, 75));
        invincibilityBackgroundTable.add(invincibilityNotificationBackground).size(invincibilityNotificationBackground.getWidth(), invincibilityNotificationBackground.getHeight());
        invincibilityStage.addActor(invincibilityBackgroundTable);

        Table shieldBackgroundTable = new Table();
        shieldBackgroundTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 296, 0, 0);
        Image shieldNotificationBackground = new Image(new TextureRegion(atlas.findRegion("notificationBackground"), 0, 0, 400, 75));
        shieldBackgroundTable.add(shieldNotificationBackground).size(shieldNotificationBackground.getWidth(), shieldNotificationBackground.getHeight());
        shieldStage.addActor(shieldBackgroundTable);

        Table invincibilityNotificationTable = new Table();
        invincibilityNotificationTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 296, 0, 0);
        Label invincibilityNotification = new Label("Reward Earned:\nYou received an invincibility powerup!", new Label.LabelStyle(notificationFont, Color.FIREBRICK));
        invincibilityNotificationTable.add(invincibilityNotification);

        Table shieldNotificationTable = new Table();
        shieldNotificationTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 296, 0, 0);
        Label shieldNotification = new Label("Reward Earned:\nYou received a shield powerup!", new Label.LabelStyle(notificationFont, Color.FIREBRICK));
        shieldNotificationTable.add(shieldNotification);

        invincibilityStage.addActor(invincibilityNotificationTable);
        shieldStage.addActor(shieldNotificationTable);

    }
    public void drawInvincibilityStage() {
        invincibilityStage.draw();
    }

    public void drawShieldStage() {
        shieldStage.draw();
    }

    public void dispose() {
        generator.dispose();
        notificationFont.dispose();
        invincibilityStage.dispose();
        shieldStage.dispose();
    }
}