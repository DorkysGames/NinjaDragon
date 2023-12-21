package com.mygdx.NinjaDragon.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.NinjaDragon.NinjaDragon;

/**
 * Created by Brian on 9/25/2016.
 */
public class PlayerButtons {
    private Stage stage;
    private boolean shieldButtonPressed, invincibilityButtonPressed, flyDownButtonPressed, flyUpButtonPressed, pauseButtonPressed;
    private Label shieldCount;
    private Label invincibilityCount;

    public PlayerButtons(Preferences prefs, BitmapFont playerButtonsFont, TextureAtlas atlas, int levelNumber, boolean isInfiniteMode) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        Table backgroundTable = new Table();
        backgroundTable.bottom().padLeft(480);

        if (isInfiniteMode) {
            if (levelNumber <=10) {
                Image greyBackground = new Image(new TextureRegion(atlas.findRegion("backgroundGrey"), 0, 0, 480, 200));
                backgroundTable.add(greyBackground).size(greyBackground.getWidth(), greyBackground.getHeight());
            } else {
                Image brownBackground = new Image(new TextureRegion(atlas.findRegion("backgroundBrown"), 0, 0, 480, 200));
                backgroundTable.add(brownBackground).size(brownBackground.getWidth(), brownBackground.getHeight());
            }
        } else {
        if (levelNumber <= 5 || (levelNumber >= 11 && levelNumber <= 15) || levelNumber >= 21) {
            Image greyBackground = new Image(new TextureRegion(atlas.findRegion("backgroundGrey"), 0, 0, 480, 200));
            backgroundTable.add(greyBackground).size(greyBackground.getWidth(), greyBackground.getHeight());
        } else {
            Image brownBackground = new Image(new TextureRegion(atlas.findRegion("backgroundBrown"), 0, 0, 480, 200));
            backgroundTable.add(brownBackground).size(brownBackground.getWidth(), brownBackground.getHeight());
        }
        }

        stage.addActor(backgroundTable);

        Table playerButtonsTable = new Table();
        // touch screen selected
        if (prefs.getBoolean("TouchScreenOn", true)) {
            // left buttons selected
            if (!prefs.getBoolean("LeftButtons", false)) {
                playerButtonsTable.bottom().padLeft(780).padBottom(5);
                Image shieldButton = new Image(new TextureRegion(atlas.findRegion("ShieldButton"), 0, 0, 125, 90));
                shieldButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        shieldButtonPressed = true;
                    }
                });

                Image invincibilityButton = new Image(new TextureRegion(atlas.findRegion("InvincibilityButton"), 0, 0, 125, 90));
                invincibilityButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        invincibilityButtonPressed = true;
                    }
                });

                // set the player buttons for flying up and down

                Image flyUpButton = new Image(new TextureRegion(atlas.findRegion("FlyUpButton"), 0, 0, 125, 90));
                flyUpButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        flyUpButtonPressed = true;
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        flyUpButtonPressed = false;
                    }
                });

                Image flyDownButton = new Image(new TextureRegion(atlas.findRegion("FlyDownButton"), 0, 0, 125, 90));
                flyDownButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        flyDownButtonPressed = true;
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        flyDownButtonPressed = false;
                    }
                });

                playerButtonsTable.add(invincibilityButton).size(invincibilityButton.getWidth(), invincibilityButton.getHeight());
                playerButtonsTable.add().spaceRight(10);
                playerButtonsTable.row().padTop(10);
                playerButtonsTable.add(shieldButton).size(shieldButton.getWidth(), shieldButton.getHeight());
                playerButtonsTable.add().spaceRight(10);

                stage.addActor(playerButtonsTable);

                Table shieldCountTable = new Table();
                shieldCountTable.padBottom(100).padLeft(580);
                shieldCount = new Label("x" + prefs.getInteger("shieldCount", 0), new Label.LabelStyle(playerButtonsFont, Color.GOLD));
                shieldCountTable.add(shieldCount);
                stage.addActor(shieldCountTable);

                Table invincibilityCountTable = new Table();
                invincibilityCountTable.padBottom(300).padLeft(580);
                invincibilityCount = new Label("x" + prefs.getInteger("invincibilityCount", 0), new Label.LabelStyle(playerButtonsFont, Color.GOLD));
                invincibilityCountTable.add(invincibilityCount);
                stage.addActor(invincibilityCountTable);

                Table pauseButtonTable = new Table();
                pauseButtonTable.padBottom(200).padLeft(400);
                Image pauseButton = new Image(new TextureRegion(atlas.findRegion("pauseButton"), 0, 0, 58, 60));
                pauseButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        pauseButtonPressed = true;
                    }
                });
                pauseButtonTable.add(pauseButton).size(pauseButton.getWidth(), pauseButton.getHeight());
                stage.addActor(pauseButtonTable);
                // right buttons selected
            } else {
                playerButtonsTable.bottom().padLeft(170).padBottom(5);

                Image shieldButton = new Image(new TextureRegion(atlas.findRegion("ShieldButton"), 0, 0, 125, 90));
                shieldButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        shieldButtonPressed = true;
                    }
                });

                Image invincibilityButton = new Image(new TextureRegion(atlas.findRegion("InvincibilityButton"), 0, 0, 125, 90));
                invincibilityButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        invincibilityButtonPressed = true;
                    }
                });

                // set the player buttons for flying up and down

                Image flyUpButton = new Image(new TextureRegion(atlas.findRegion("FlyUpButton"), 0, 0, 125, 90));
                flyUpButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        flyUpButtonPressed = true;
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        flyUpButtonPressed = false;
                    }
                });

                Image flyDownButton = new Image(new TextureRegion(atlas.findRegion("FlyDownButton"), 0, 0, 125, 90));
                flyDownButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        flyDownButtonPressed = true;
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        flyDownButtonPressed = false;
                    }
                });

                playerButtonsTable.add(invincibilityButton).size(invincibilityButton.getWidth(), invincibilityButton.getHeight());
                playerButtonsTable.add().spaceRight(200);
                playerButtonsTable.row().padTop(10);
                playerButtonsTable.add(shieldButton).size(shieldButton.getWidth(), shieldButton.getHeight());
                playerButtonsTable.add().spaceRight(200);

                stage.addActor(playerButtonsTable);

                Table shieldCountTable = new Table();
                shieldCountTable.padBottom(100).padLeft(365);
                shieldCount = new Label("x" + prefs.getInteger("shieldCount", 0), new Label.LabelStyle(playerButtonsFont, Color.GOLD));
                shieldCountTable.add(shieldCount);
                stage.addActor(shieldCountTable);

                Table invincibilityCountTable = new Table();
                invincibilityCountTable.padBottom(300).padLeft(365);
                invincibilityCount = new Label("x" + prefs.getInteger("invincibilityCount", 0), new Label.LabelStyle(playerButtonsFont, Color.GOLD));
                invincibilityCountTable.add(invincibilityCount);
                stage.addActor(invincibilityCountTable);

                Table pauseButtonTable = new Table();
                pauseButtonTable.padBottom(200).padLeft(550);
                Image pauseButton = new Image(new TextureRegion(atlas.findRegion("pauseButton"), 0, 0, 58, 60));
                pauseButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        pauseButtonPressed = true;
                    }
                });
                pauseButtonTable.add(pauseButton).size(pauseButton.getWidth(), pauseButton.getHeight());
                stage.addActor(pauseButtonTable);
            }
        } else {
            // inner buttons selected
            if (prefs.getBoolean("InnerButtons", true)) {
                // set the player buttons for shield and invincibility
                    playerButtonsTable.bottom().padLeft(480).padBottom(5);

                Image shieldButton = new Image(new TextureRegion(atlas.findRegion("ShieldButton"), 0, 0, 125, 90));
                shieldButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        shieldButtonPressed = true;
                    }
                });

                Image invincibilityButton = new Image(new TextureRegion(atlas.findRegion("InvincibilityButton"), 0, 0, 125, 90));
                invincibilityButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        invincibilityButtonPressed = true;
                    }
                });

                // set the player buttons for flying up and down

                Image flyUpButton = new Image(new TextureRegion(atlas.findRegion("FlyUpButton"), 0, 0, 125, 90));
                flyUpButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        flyUpButtonPressed = true;
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        flyUpButtonPressed = false;
                    }
                });

                Image flyDownButton = new Image(new TextureRegion(atlas.findRegion("FlyDownButton"), 0, 0, 125, 90));
                flyDownButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        flyDownButtonPressed = true;
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        flyDownButtonPressed = false;
                    }
                });

                playerButtonsTable.add(invincibilityButton).size(invincibilityButton.getWidth(), invincibilityButton.getHeight());
                playerButtonsTable.add().spaceRight(10);
                playerButtonsTable.add(flyUpButton).size(flyUpButton.getWidth(), flyUpButton.getHeight()).right();
                playerButtonsTable.row().padTop(10);
                playerButtonsTable.add(shieldButton).size(shieldButton.getWidth(), shieldButton.getHeight());
                playerButtonsTable.add().spaceRight(10);
                playerButtonsTable.add(flyDownButton).size(flyDownButton.getWidth(), flyDownButton.getHeight()).right();

                stage.addActor(playerButtonsTable);

                Table shieldCountTable = new Table();
                shieldCountTable.padBottom(100).padLeft(115);
                shieldCount = new Label("x" + prefs.getInteger("shieldCount", 0), new Label.LabelStyle(playerButtonsFont, Color.GOLD));
                shieldCountTable.add(shieldCount);
                stage.addActor(shieldCountTable);

                Table invincibilityCountTable = new Table();
                invincibilityCountTable.padBottom(300).padLeft(115);
                invincibilityCount = new Label("x" + prefs.getInteger("invincibilityCount", 0), new Label.LabelStyle(playerButtonsFont, Color.GOLD));
                invincibilityCountTable.add(invincibilityCount);
                stage.addActor(invincibilityCountTable);

                Table pauseButtonTable = new Table();
                pauseButtonTable.padBottom(200).padLeft(850);
                Image pauseButton = new Image(new TextureRegion(atlas.findRegion("pauseButton"), 0, 0, 58, 60));
                pauseButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        pauseButtonPressed = true;
                    }
                });
                pauseButtonTable.add(pauseButton).size(pauseButton.getWidth(), pauseButton.getHeight());
                stage.addActor(pauseButtonTable);
                // outer buttons selected
            } else {

                playerButtonsTable.bottom().padLeft(480).padBottom(5);

                Image shieldButton = new Image(new TextureRegion(atlas.findRegion("ShieldButton"), 0, 0, 125, 90));
                shieldButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        shieldButtonPressed = true;
                    }
                });

                Image invincibilityButton = new Image(new TextureRegion(atlas.findRegion("InvincibilityButton"), 0, 0, 125, 90));
                invincibilityButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        invincibilityButtonPressed = true;
                    }
                });

                // set the player buttons for flying up and down

                Image flyUpButton = new Image(new TextureRegion(atlas.findRegion("FlyUpButton"), 0, 0, 125, 90));
                flyUpButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        flyUpButtonPressed = true;
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        flyUpButtonPressed = false;
                    }
                });

                Image flyDownButton = new Image(new TextureRegion(atlas.findRegion("FlyDownButton"), 0, 0, 125, 90));
                flyDownButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        flyDownButtonPressed = true;
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        flyDownButtonPressed = false;
                    }
                });

                playerButtonsTable.add(invincibilityButton).size(invincibilityButton.getWidth(), invincibilityButton.getHeight());
                playerButtonsTable.add().spaceRight(200);
                playerButtonsTable.add(flyUpButton).size(flyUpButton.getWidth(), flyUpButton.getHeight()).right();
                playerButtonsTable.row().padTop(10);
                playerButtonsTable.add(shieldButton).size(shieldButton.getWidth(), shieldButton.getHeight());
                playerButtonsTable.add().spaceRight(200);
                playerButtonsTable.add(flyDownButton).size(flyDownButton.getWidth(), flyDownButton.getHeight()).right();

                stage.addActor(playerButtonsTable);

                Table shieldCountTable = new Table();
                shieldCountTable.padBottom(100).padLeft(365);
                shieldCount = new Label("x" + prefs.getInteger("shieldCount", 0), new Label.LabelStyle(playerButtonsFont, Color.GOLD));
                shieldCountTable.add(shieldCount);
                stage.addActor(shieldCountTable);

                Table invincibilityCountTable = new Table();
                invincibilityCountTable.padBottom(300).padLeft(365);
                invincibilityCount = new Label("x" + prefs.getInteger("invincibilityCount", 0), new Label.LabelStyle(playerButtonsFont, Color.GOLD));
                invincibilityCountTable.add(invincibilityCount);
                stage.addActor(invincibilityCountTable);

                Table pauseButtonTable = new Table();
                pauseButtonTable.padBottom(200).padLeft(550);
                Image pauseButton = new Image(new TextureRegion(atlas.findRegion("pauseButton"), 0, 0, 58, 60));
                pauseButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        pauseButtonPressed = true;
                    }
                });
                pauseButtonTable.add(pauseButton).size(pauseButton.getWidth(), pauseButton.getHeight());
                stage.addActor(pauseButtonTable);
            }
        }

    }

    public void draw() {
        stage.draw();
    }

    public boolean isShieldButtonPressed() {
        return shieldButtonPressed;
    }

    public void setShieldButtonPressed(Boolean shieldButtonPressed) {
        this.shieldButtonPressed = shieldButtonPressed;
    }

    public void updateShieldCount(Preferences prefs){
        shieldCount.setText(String.format("x" + prefs.getInteger("shieldCount"), 0));
    }

    public boolean isInvincibilityButtonPressed() {
        return invincibilityButtonPressed;
    }

    public void setInvincibilityButtonPressed(Boolean invincibilityButtonPressed) {
        this.invincibilityButtonPressed = invincibilityButtonPressed;
    }

    public void updateInvincibilityCount(Preferences prefs){
        invincibilityCount.setText(String.format("x" + prefs.getInteger("invincibilityCount"), 0));
    }

    public boolean isPauseButtonPressed() {
        return pauseButtonPressed;
    }

    public void setPauseButtonPressed(Boolean pauseButtonPressed) {
        this.pauseButtonPressed = pauseButtonPressed;
    }

    public boolean isFlyUpButtonPressed() {
        return flyUpButtonPressed;
    }

    public boolean isFlyDownButtonPressed() {
        return flyDownButtonPressed;
    }


    public void dispose() {
        stage.dispose();
    }
}
