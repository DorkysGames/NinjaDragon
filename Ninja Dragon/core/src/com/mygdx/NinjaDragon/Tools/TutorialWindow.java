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

public class TutorialWindow {
    private Stage stage;
    private boolean gotItPressed, doNotShowAgainPressed, doNotShowAgainCheckedPressed;
    private Sound buttonClickSound;


    public TutorialWindow(AssetManager manager, final Preferences prefs, TextureAtlas atlas, boolean displayChecked) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, NinjaDragon.batch);
        Gdx.input.setInputProcessor(stage);
        buttonClickSound = manager.get("Audio/Sounds/buttonClickSound.ogg", Sound.class);

        // create the tutorial background and buttons
        Table tutorialTable = new Table();
        tutorialTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 100, 0, 0);
        Image tutorialBackground = new Image(new TextureRegion(atlas.findRegion("TutorialBackground"), 0, 0, 364, 448));

        Table table = new Table();
        table.setBounds(NinjaDragon.WIDTH / 2 + 125, NinjaDragon.HEIGHT / 2 - 40, 0, 0);

        Table gotItTable = new Table();
        gotItTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 - 78, 0, 0);

        Image doNotShowAgainChecked = new Image(new TextureRegion(atlas.findRegion("DoNotShowAgainChecked"), 0, 0, 77, 70));
        Image doNotShowAgain = new Image(new TextureRegion(atlas.findRegion("DoNotShowAgain"), 0, 0, 77, 70));

        if (displayChecked) {
            doNotShowAgainChecked.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    doNotShowAgainCheckedPressed = true;
                }
            });
        } else {
            doNotShowAgain.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    doNotShowAgainPressed = true;
                }
            });
        }

        Image gotItButton = new Image(new TextureRegion(atlas.findRegion("GotItButton"), 0, 0, 139, 76));
        gotItButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonClick(prefs);
                gotItPressed = true;
            }
        });

        tutorialTable.add(tutorialBackground).size(tutorialBackground.getWidth(), tutorialBackground.getHeight());

        if (displayChecked) {
            table.add(doNotShowAgainChecked).size(doNotShowAgainChecked.getWidth(), doNotShowAgainChecked.getHeight());
        } else {
            table.add(doNotShowAgain).size(doNotShowAgain.getWidth(), doNotShowAgain.getHeight());
        }

        gotItTable.add(gotItButton).size(gotItButton.getWidth(), gotItButton.getHeight());

        stage.addActor(tutorialTable);
        stage.addActor(table);
        stage.addActor(gotItTable);
    }

    public void draw() {
        stage.draw();
    }

    private void playButtonClick(Preferences prefs) {
        if (prefs.getBoolean("SoundOn", true)) {
            buttonClickSound.play();
        }
    }

    public boolean isGotItPressed() {
        return gotItPressed;
    }

    public boolean isDoNotShowAgainPressed() {
        return doNotShowAgainPressed;
    }

    public boolean isDoNotShowAgainCheckedPressed() {
        return doNotShowAgainCheckedPressed;
    }

    public void dispose() {
        stage.dispose();
    }
}