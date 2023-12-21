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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.NinjaDragon.NinjaDragon;

public class ExitGameButtons {
    private Stage stage;
    private boolean yesPressed, noPressed;
    private Sound buttonClickSound;
    private FreeTypeFontGenerator generator;
    private BitmapFont menuHeaderFont;


    public ExitGameButtons(AssetManager manager, final Preferences prefs) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, NinjaDragon.batch);
        TextureAtlas mainMenuAtlas = manager.get("menuTextures.atlas");
        Gdx.input.setInputProcessor(stage);
        buttonClickSound = manager.get("Audio/Sounds/buttonClickSound.ogg", Sound.class);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 38;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Ninja_Font.ttf"));
        menuHeaderFont = generator.generateFont(parameter);

        // create the victory buttons background and buttons
        Table exitTable = new Table();
        exitTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2, 0, 0);

        Image exitMenuBackground = new Image(new TextureRegion(mainMenuAtlas.findRegion("menuBackground"), 0, 0, 350, 233));
        Table exitTextTable = new Table();
        exitTextTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 85, 0, 0);
        Label exitTextLabel = new Label("Exit Game?", new Label.LabelStyle(menuHeaderFont, Color.GOLD));
        exitTextTable.add(exitTextLabel);

        Table table = new Table();
        table.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 - 25, 0, 0);

        Image noButton = new Image(new TextureRegion(mainMenuAtlas.findRegion("noButton"), 0, 0, 110, 90));
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick(prefs);
                noPressed = true;
            }
        });

        Image yesButton = new Image(new TextureRegion(mainMenuAtlas.findRegion("yesButton"), 0, 0, 110, 90));
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                yesPressed = true;
            }
        });

        exitTable.add(exitMenuBackground).size(exitMenuBackground.getWidth(), exitMenuBackground.getHeight());

        table.add(noButton).size(noButton.getWidth(), noButton.getHeight());
        table.add().spaceRight(30);
        table.add(yesButton).size(yesButton.getWidth(), yesButton.getHeight());


        stage.addActor(exitTable);
        stage.addActor(exitTextTable);
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

    public boolean isNoPressed() {
        return noPressed;
    }

    public boolean isYesPressed() {
        return yesPressed;
    }

    public void setIsNoPressed(Boolean noPressed) {
        this.noPressed = noPressed;
    }

    public void setIsYesPressed(Boolean yesPressed) {
        this.yesPressed = yesPressed;
    }

    public void dispose() {
        generator.dispose();
        menuHeaderFont.dispose();
        stage.dispose();
    }
}