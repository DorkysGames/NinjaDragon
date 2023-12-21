package com.mygdx.NinjaDragon.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.NinjaDragon.NinjaDragon;

/**
 * Created by Brian on 1/9/2017.
 */
public class OptionsStateButtons {
    private Stage stage;
    private boolean musicOnPressed, musicOffPressed, soundOnPressed,soundOffPressed,hyperModeOnPressed, hyperModeOffPressed;
    private boolean innerButtonsPressed, outerButtonsPressed, backBtnPressed, touchScreenPressed, touchScreenCheckedPressed, leftButtonsPressed, rightButtonsPressed;
    private Sound buttonClickSound;
    private FreeTypeFontGenerator generator;
    private BitmapFont optionsFont;


    public OptionsStateButtons(AssetManager manager, final Preferences prefs) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, NinjaDragon.batch);

        TextureAtlas optionsIconsAtlas = manager.get("optionsTextures.atlas");
        TextureAtlas menuIconsAtlas = manager.get("menuTextures.atlas");
        Gdx.input.setInputProcessor(stage);
        buttonClickSound = manager.get("Audio/Sounds/buttonClickSound.ogg", Sound.class);

        Table backgroundTable = new Table();
        backgroundTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2, 0, 0);
        Image background = new Image(new TextureRegion(menuIconsAtlas.findRegion("mainMenuBackground"), 0, 0, 480, 800));
        backgroundTable.add(background).size(background.getWidth(), background.getHeight());
        stage.addActor(backgroundTable);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Quarto_Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        optionsFont = generator.generateFont(parameter);

        // table and image for back button
        Table backButtonTable = new Table();
        backButtonTable.left().bottom();
        backButtonTable.padBottom(25).padLeft(25);

        Image  backButtonImage = new Image(new TextureRegion(menuIconsAtlas.findRegion("backButton"), 0, 0, 64, 50));
        backButtonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backBtnPressed = true;
            }
        });

        // table for options frame
        Image optionsFrame = new Image(new TextureRegion(optionsIconsAtlas.findRegion("optionsFrame"), 0, 0, 495, 720));
        Table optionsFrameTable = new Table();
        optionsFrameTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 25, 0, 0);

        // table for header text
        Image optionsHeaderText = new Image(new TextureRegion(optionsIconsAtlas.findRegion("optionsHeaderText"), 0, 0, 266, 100));
        Table headerTable = new Table();
        headerTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT - 175, 0, 0);

        Table optionsMusicTextTable = new Table();
        optionsMusicTextTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT - 260, 0, 0);
        Image musicText = new Image(new TextureRegion(optionsIconsAtlas.findRegion("musicText"), 0, 0, 93, 40));
        Image soundText = new Image(new TextureRegion(optionsIconsAtlas.findRegion("soundText"), 0, 0, 93, 40));

        Table optionsHyperTextTable = new Table();
        optionsHyperTextTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT - 390, 0, 0);
        Image hyperModeText = new Image(new TextureRegion(optionsIconsAtlas.findRegion("hyperModeText"), 0, 0, 137, 40));

        Table optionsButtonLayoutTextTable = new Table();
        optionsButtonLayoutTextTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT - 550, 0, 0);

        Table onMusicButtonsTable = new Table();
        onMusicButtonsTable.setBounds(NinjaDragon.WIDTH / 2 - 100, NinjaDragon.HEIGHT - 315, 0, 0);

        Table onSoundButtonsTable = new Table();
        onSoundButtonsTable.setBounds(NinjaDragon.WIDTH / 2 + 100, NinjaDragon.HEIGHT - 315, 0, 0);

        Table hyperModeButtonsTable = new Table();
        hyperModeButtonsTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT - 440, 0, 0);

        Table layoutButtonsTable = new Table();
        layoutButtonsTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT - 625, 0, 0);


        // music is on, can turn off
        if (prefs.getBoolean("MusicOn", true)) {
            Image musicOnButton = new Image(new TextureRegion(optionsIconsAtlas.findRegion("musicOn"), 0, 0, 60, 60));
            musicOnButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    musicOffPressed = true;
                }
            });

            onMusicButtonsTable.add(musicOnButton).size(musicOnButton.getWidth(), musicOnButton.getHeight());
            // otherwise music is off, can turn on
        } else {
            Image musicOffButton = new Image(new TextureRegion(optionsIconsAtlas.findRegion("musicOff"), 0, 0, 60, 60));
            musicOffButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    musicOnPressed = true;
                }
            });
            onMusicButtonsTable.add(musicOffButton).size(musicOffButton.getWidth(), musicOffButton.getHeight());
        }

        // sound is on, can turn off
        if (prefs.getBoolean("SoundOn", true)) {
            Image soundOnButton = new Image(new TextureRegion(optionsIconsAtlas.findRegion("soundOn"), 0, 0, 60, 60));
            soundOnButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    soundOffPressed = true;
                }
            });
            onSoundButtonsTable.add(soundOnButton).size(soundOnButton.getWidth(), soundOnButton.getHeight());
            // otherwise sound is off, can turn on
        } else {
            Image soundOffButton = new Image(new TextureRegion(optionsIconsAtlas.findRegion("soundOff"), 0, 0, 60, 60));
            soundOffButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    soundOnPressed = true;
                }
            });
            onSoundButtonsTable.add(soundOffButton).size(soundOffButton.getWidth(), soundOffButton.getHeight());
        }

        // if hyper mode is unlocked
        if (prefs.getBoolean("hyperModeUnlocked", false)) {
            // hyper mode is on, turn it off
            if (prefs.getBoolean("hyperMode", false)) {
                Image hyperModeOnButton = new Image(new TextureRegion(optionsIconsAtlas.findRegion("onButton"), 0, 0, 60, 60));

                Image hyperModeOffButton = new Image(new TextureRegion(optionsIconsAtlas.findRegion("greyOffButton"), 0, 0, 60, 60));
                hyperModeOffButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        playButtonClick(prefs);
                        hyperModeOffPressed = true;
                    }
                });

                hyperModeButtonsTable.add(hyperModeOnButton).size(hyperModeOnButton.getWidth(), hyperModeOnButton.getHeight());
                hyperModeButtonsTable.add().spaceRight(50);
                hyperModeButtonsTable.add(hyperModeOffButton).size(hyperModeOffButton.getWidth(), hyperModeOffButton.getHeight());
                // otherwise hyper mode is off, turn it on
            } else {
                Image hyperModeOnButton = new Image(new TextureRegion(optionsIconsAtlas.findRegion("greyOnButton"), 0, 0, 60, 60));
                hyperModeOnButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        playButtonClick(prefs);
                        hyperModeOnPressed = true;
                    }
                });

                Image hyperModeOffButton = new Image(new TextureRegion(optionsIconsAtlas.findRegion("offButton"), 0, 0, 60, 60));

                hyperModeButtonsTable.add(hyperModeOnButton).size(hyperModeOnButton.getWidth(), hyperModeOnButton.getHeight());
                hyperModeButtonsTable.add().spaceRight(50);
                hyperModeButtonsTable.add(hyperModeOffButton).size(hyperModeOffButton.getWidth(), hyperModeOffButton.getHeight());
            }
            // otherwise grey it out
        } else {
            Image hyperModeOnButton = new Image(new TextureRegion(optionsIconsAtlas.findRegion("greyOnButton"), 0, 0, 60, 60));

            Image hyperModeOffButton = new Image(new TextureRegion(optionsIconsAtlas.findRegion("greyOffButton"), 0, 0, 60, 60));

            hyperModeButtonsTable.add(hyperModeOnButton).size(hyperModeOnButton.getWidth(), hyperModeOnButton.getHeight());
            hyperModeButtonsTable.add().spaceRight(50);
            hyperModeButtonsTable.add(hyperModeOffButton).size(hyperModeOffButton.getWidth(), hyperModeOffButton.getHeight());
        }

        // sound is on, can turn off
        if (prefs.getBoolean("TouchScreenOn", true)) {
            Image touchScreenButtonChecked = new Image(new TextureRegion(optionsIconsAtlas.findRegion("touchScreenButtonChecked"), 0, 0, 244, 65));
            touchScreenButtonChecked.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    touchScreenCheckedPressed = true;
                }
            });
            optionsButtonLayoutTextTable.add(touchScreenButtonChecked).size(touchScreenButtonChecked.getWidth(), touchScreenButtonChecked.getHeight());
            // otherwise sound is off, can turn on
        } else {
            Image touchScreenButtonUnchecked = new Image(new TextureRegion(optionsIconsAtlas.findRegion("touchScreenButton"), 0, 0, 244, 65));
            touchScreenButtonUnchecked.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playButtonClick(prefs);
                    touchScreenPressed = true;
                }
            });
            optionsButtonLayoutTextTable.add(touchScreenButtonUnchecked).size(touchScreenButtonUnchecked.getWidth(), touchScreenButtonUnchecked.getHeight());
        }

        // if the touch screen is not on
        if (!prefs.getBoolean("TouchScreenOn", true)) {
            if (prefs.getBoolean("InnerButtons", true)) {
                Image innerButtonsChecked = new Image(new TextureRegion(optionsIconsAtlas.findRegion("innerChecked"), 0, 0, 170, 85));

                Image outerButtonsUnchecked = new Image(new TextureRegion(optionsIconsAtlas.findRegion("outerUnchecked"), 0, 0, 170, 85));
                outerButtonsUnchecked.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        playButtonClick(prefs);
                        outerButtonsPressed = true;
                    }
                });

                layoutButtonsTable.add(innerButtonsChecked).size(innerButtonsChecked.getWidth(), innerButtonsChecked.getHeight());
                layoutButtonsTable.add().spaceRight(25);
                layoutButtonsTable.add(outerButtonsUnchecked).size(outerButtonsUnchecked.getWidth(), outerButtonsUnchecked.getHeight());
                // outer buttons selected
            } else {
                Image innerButtonsUnchecked = new Image(new TextureRegion(optionsIconsAtlas.findRegion("innerUnchecked"), 0, 0, 170, 85));
                innerButtonsUnchecked.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        playButtonClick(prefs);
                        innerButtonsPressed = true;
                    }
                });

                Image outerButtonsChecked = new Image(new TextureRegion(optionsIconsAtlas.findRegion("outerChecked"), 0, 0, 170, 85));

                layoutButtonsTable.add(innerButtonsUnchecked).size(innerButtonsUnchecked.getWidth(), innerButtonsUnchecked.getHeight());
                layoutButtonsTable.add().spaceRight(25);
                layoutButtonsTable.add(outerButtonsChecked).size(outerButtonsChecked.getWidth(), outerButtonsChecked.getHeight());
            }
        } else {
            // inner buttons selected
            if (prefs.getBoolean("LeftButtons", false)) {
                Image leftButtonsChecked = new Image(new TextureRegion(optionsIconsAtlas.findRegion("leftButtonsChecked"), 0, 0, 170, 85));

                Image rightButtonsUnchecked = new Image(new TextureRegion(optionsIconsAtlas.findRegion("rightButtonsUnchecked"), 0, 0, 170, 85));
                rightButtonsUnchecked.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        playButtonClick(prefs);
                        rightButtonsPressed = true;
                    }
                });

                layoutButtonsTable.add(leftButtonsChecked).size(leftButtonsChecked.getWidth(), leftButtonsChecked.getHeight());
                layoutButtonsTable.add().spaceRight(25);
                layoutButtonsTable.add(rightButtonsUnchecked).size(rightButtonsUnchecked.getWidth(), rightButtonsUnchecked.getHeight());
                // outer buttons selected
            } else {
                Image rightButtonsChecked = new Image(new TextureRegion(optionsIconsAtlas.findRegion("rightButtonsChecked"), 0, 0, 170, 85));

                Image leftButtonsUnchecked = new Image(new TextureRegion(optionsIconsAtlas.findRegion("leftButtonsUnchecked"), 0, 0, 170, 85));
                leftButtonsUnchecked.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        playButtonClick(prefs);
                        leftButtonsPressed = true;
                    }
                });
                layoutButtonsTable.add(leftButtonsUnchecked).size(leftButtonsUnchecked.getWidth(), leftButtonsUnchecked.getHeight());
                layoutButtonsTable.add().spaceRight(25);
                layoutButtonsTable.add(rightButtonsChecked).size(rightButtonsChecked.getWidth(), rightButtonsChecked.getHeight());
            }
        }

        headerTable.add(optionsHeaderText).size(optionsHeaderText.getWidth(), optionsHeaderText.getHeight());

        optionsFrameTable.add(optionsFrame).size(optionsFrame.getWidth(), optionsFrame.getHeight());

        optionsMusicTextTable.add(musicText).size(musicText.getWidth(), musicText.getHeight());
        optionsMusicTextTable.add().spaceRight(80).padLeft(20);
        optionsMusicTextTable.add(soundText).size(soundText.getWidth(), soundText.getHeight());
        optionsHyperTextTable.add(hyperModeText).size(hyperModeText.getWidth(), hyperModeText.getHeight());
        backButtonTable.add(backButtonImage).size(backButtonImage.getWidth(), backButtonImage.getHeight());


        stage.addActor(optionsFrameTable);
        stage.addActor(headerTable);
        stage.addActor(optionsMusicTextTable);
        stage.addActor(optionsHyperTextTable);
        stage.addActor(optionsButtonLayoutTextTable);
        stage.addActor(backButtonTable);

        stage.addActor(onMusicButtonsTable);
        stage.addActor(onSoundButtonsTable);
        stage.addActor(hyperModeButtonsTable);
        stage.addActor(layoutButtonsTable);
    }

    public void draw() {
        stage.draw();
    }

    public void playButtonClick(Preferences prefs) {
        if (prefs.getBoolean("SoundOn", true)) {
            buttonClickSound.play();
        }
    }

    public boolean isBackBtnPressed() {
        return backBtnPressed;
    }

    public boolean isMusicOnPressed() {
        return musicOnPressed ;
    }

    public void setMusicOnPressed(boolean musicOnPressed) {
        this.musicOnPressed = musicOnPressed;
    }

    public boolean isMusicOffPressed() {
        return musicOffPressed ;
    }

    public void setMusicOffPressed(boolean musicOffPressed) {
        this.musicOffPressed = musicOffPressed;
    }

    public boolean isSoundOnPressed() {
        return soundOnPressed ;
    }

    public void setSoundOnPressed(boolean soundOnPressed) {
        this.soundOnPressed = soundOnPressed;
    }

    public boolean isHyperModeOnPressed() {
        return hyperModeOnPressed ;
    }

    public void setHyperModeOnPressed(boolean hyperModeOnPressed) {
        this.hyperModeOnPressed = hyperModeOnPressed;
    }

    public boolean isHyperModeOffPressed() {
        return hyperModeOffPressed ;
    }

    public void setHyperModeOffPressed(boolean hyperModeOffPressed) {
        this.hyperModeOffPressed = hyperModeOffPressed;
    }

    public boolean isSoundOffPressed() {
        return soundOffPressed ;
    }

    public void setSoundOffPressed(boolean soundOffPressed) {
        this.soundOffPressed = soundOffPressed;
    }

    public boolean isInnerButtonsPressed() {
        return innerButtonsPressed ;
    }

    public void setInnerButtonsPressed(boolean innerButtonsPressed) {
        this.innerButtonsPressed = innerButtonsPressed;
    }

    public boolean isOuterButtonsPressed() {
        return outerButtonsPressed ;
    }

    public void setOuterButtonsPressed(boolean outerButtonsPressed) {
        this.outerButtonsPressed = outerButtonsPressed;
    }

    public boolean isLeftButtonsPressed() {
        return leftButtonsPressed ;
    }

    public void setLeftButtonsPressed(boolean leftButtonsPressed) {
        this.leftButtonsPressed = leftButtonsPressed;
    }

    public boolean isRightButtonsPressed() {
        return rightButtonsPressed ;
    }

    public void setRightButtonsPressed(boolean rightButtonsPressed) {
        this.rightButtonsPressed = rightButtonsPressed;
    }

    public boolean isTouchScreenPressed() {
        return touchScreenPressed ;
    }

    public void setTouchScreenPressed(boolean touchScreenPressed) {
        this.touchScreenPressed = touchScreenPressed;
    }

    public boolean isTouchScreenCheckedPressed() {
        return touchScreenCheckedPressed ;
    }

    public void setTouchScreenCheckedPressed(boolean touchScreenCheckedPressed) {
        this.touchScreenCheckedPressed = touchScreenCheckedPressed;
    }

    public void dispose() {
        stage.dispose();
        generator.dispose();
        optionsFont.dispose();
    }
}

