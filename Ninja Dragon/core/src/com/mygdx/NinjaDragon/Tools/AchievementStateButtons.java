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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.NinjaDragon.NinjaDragon;

/**
 * Created by Brian on 9/16/2016.
 */
public class AchievementStateButtons {
    private Stage stage;
    private boolean backBtnPressed;
    private Sound buttonClickSound;
    private TextureAtlas achievementIconsAtlas;
    private FreeTypeFontGenerator generator;
    private BitmapFont rewardsFont;

    public AchievementStateButtons(AssetManager manager, Preferences prefs) {
        Viewport viewport = new StretchViewport(NinjaDragon.WIDTH, NinjaDragon.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, NinjaDragon.batch);
        TextureAtlas menuIconsAtlas = manager.get("menuTextures.atlas");
        achievementIconsAtlas = manager.get("achievementTextures.atlas");
        Gdx.input.setInputProcessor(stage);
        buttonClickSound = manager.get("Audio/Sounds/buttonClickSound.ogg", Sound.class);

        Table backgroundTable = new Table();
        backgroundTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2, 0, 0);
        Image background = new Image(new TextureRegion(menuIconsAtlas.findRegion("mainMenuBackground"), 0, 0, 480, 800));
        backgroundTable.add(background).size(background.getWidth(), background.getHeight());
        stage.addActor(backgroundTable);

        Table scrollTable = new Table();
        scrollTable.setBounds(NinjaDragon.WIDTH / 2, NinjaDragon.HEIGHT / 2 + 25, 0, 0);
        Image scrollTop = new Image(new TextureRegion(achievementIconsAtlas.findRegion("scrollTop"), 0, 0, 480, 111));
        scrollTable.add(scrollTop).size(scrollTop.getWidth(), scrollTop.getHeight());
        scrollTable.row().padTop(450);
        Image scrollBottom = new Image(new TextureRegion(achievementIconsAtlas.findRegion("scrollBottom"), 0, 0, 480, 111));
        scrollTable.add(scrollBottom).size(scrollBottom.getWidth(), scrollBottom.getHeight());
        stage.addActor(scrollTable);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Quarto_Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        rewardsFont = generator.generateFont(parameter);

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

        backButtonTable.add(backButtonImage).size(backButtonImage.getWidth(), backButtonImage.getHeight());
        stage.addActor(backButtonTable);

        Table achievementsTable = new Table();

        for (int i = 1; i < 10; i++) {
            if (prefs.getBoolean("Achievement" + i, false)) {
                createCompleteAchievement(getAchievementText(i), rewardsFont, achievementsTable);
            } else {
                createIncompleteAchievement(getAchievementText(i), rewardsFont, achievementsTable);
            }
        }

        for (int i = 10; i < 19; i++) {
            if (prefs.getBoolean("Achievement" + i, false)) {
                createCompleteAchievement(getAchievementCompleteText(i), rewardsFont, achievementsTable);
            } else {
                createIncompleteAchievement(getAchievementInProgressText(i, prefs), rewardsFont, achievementsTable);
            }
        }

        final ScrollPane scroller = new ScrollPane(achievementsTable);
        final Table table = new Table();
        table.setBounds(0, 150, 480 ,550);
        table.add(scroller).fill().expand();

        stage.addActor(table);
    }

    public void draw() {
        stage.draw();
        stage.act();
    }

    private String getAchievementText(int achievementNumber) {
        String achievementText = "";
        switch (achievementNumber) {
            case 1:  achievementText = "\n               Complete Level 1\n\n               Reward\n               1 Shield + 1 Invincibility";
                break;
            case 2:  achievementText = "               Complete Level 10\n\n               Reward\n               2 Shields + 2 Invincibility";
                break;
            case 3:  achievementText = "               Complete Level 20\n\n               Reward\n               3 Shields + 3 Invincibility";
                break;
            case 4:  achievementText = "               Complete Level 20 \n               (without using powerups)\n\n               Reward\n               Unlock Secret Level 21";
                break;
            case 5:  achievementText = "               Complete Secret Level 25\n\n               Reward\n               Unlock Hyper Mode";
                break;
            case 6:  achievementText = "               Score 50 In Infinite Mode\n\n               Reward\n               1 Shield + 1 Invincibility";
                break;
            case 7:  achievementText = "               Score 100 In Infinite Mode\n\n               Reward\n               2 Shields + 2 Invincibility";
                break;
            case 8:  achievementText = "               Score 250 In Infinite Mode\n\n               Reward\n               3 Shields + 3 Invincibility";
                break;
            case 9:  achievementText = "               Score 500 In Infinite Mode\n\n               Reward\n               Unlock Secret Level 25\n               (Must also complete level 24)";
                break;
        }
    return achievementText;
    }

    private String getAchievementCompleteText(int achievementNumber) {
        String achievementText = "";
        switch (achievementNumber) {
            case 10:  achievementText = "               Use 10 shields (10/10)\n\n               Reward\n               1 Shield";
                break;
            case 11:  achievementText = "               Use 25 shields (25/25)\n\n               Reward\n               3 Shields";
                break;
            case 12:  achievementText = "               Use 50 shields (50/50)\n\n               Reward\n               5 Shields";
                break;
            case 13:  achievementText = "               Use 10 invincibility (10/10)\n\n               Reward\n               1 Invincibility";
                break;
            case 14:  achievementText = "               Use 25 invincibility (25/25)\n\n               Reward\n               3 Invincibility";
                break;
            case 15:  achievementText = "               Use 50 invincibility (50/50)\n\n               Reward\n               5 Invincibility";
                break;
            case 16:  achievementText = "               Die 100 Times (100/100)\n\n               Reward\n               1 Shield + 1 Invincibility";
                break;
            case 17:  achievementText = "               Die 250 Times (250/250)\n\n               Reward\n               2 Shields + 2 Invincibility";
                break;
            case 18:  achievementText = "               Die 500 Times (500/500)\n\n               Reward\n               3 Shields + 3 Invincibility";
                break;
        }
        return achievementText;
    }

    private String getAchievementInProgressText(int achievementNumber, Preferences prefs) {
        String achievementText = "";
        switch (achievementNumber) {
            case 10:  achievementText = "               Use 10 shields (" + prefs.getInteger("shieldsUsed", 0) + "/10)\n\n               Reward\n               1 Shield";
                break;
            case 11:  achievementText = "               Use 25 shields (" + prefs.getInteger("shieldsUsed", 0) + "/25)\n\n               Reward\n               3 Shields";
                break;
            case 12:  achievementText = "               Use 50 shields (" + prefs.getInteger("shieldsUsed", 0) + "/50)\n\n               Reward\n               5 Shields";
                break;
            case 13:  achievementText = "               Use 10 invincibility (" + prefs.getInteger("invincibilityUsed", 0) + "/10)\n\n               Reward\n               1 Invincibility";
                break;
            case 14:  achievementText = "               Use 25 invincibility (" + prefs.getInteger("invincibilityUsed", 0) + "/25)\n\n               Reward\n               3 Invincibility";
                break;
            case 15:  achievementText = "               Use 50 invincibility (" + prefs.getInteger("invincibilityUsed", 0) + "/50)\n\n               Reward\n               5 Invincibility";
                break;
            case 16:  achievementText = "               Die 100 Times (" + prefs.getInteger("deathCount", 0) + "/100)\n\n               Reward\n               1 Shield + 1 Invincibility";
                break;
            case 17:  achievementText = "               Die 250 Times (" + prefs.getInteger("deathCount", 0) + "/250)\n\n               Reward\n               2 Shields + 2 Invincibility";
                break;
            case 18:  achievementText = "               Die 500 Times (" + prefs.getInteger("deathCount", 0) + "/500)\n\n               Reward\n               3 Shields + 3 Invincibility";
                break;
        }
        return achievementText;
    }

    private void createIncompleteAchievement(String achievementText, BitmapFont achievementFont, Table achievementsTable) {
        Stack stack = new Stack();

        Image incompleteAchievement = new Image(new TextureRegion(achievementIconsAtlas.findRegion("achievementIncomplete"), 0, 0, 342, 132));
        stack.add(incompleteAchievement);
        Label achievementTextLabel = new Label(achievementText, new Label.LabelStyle(achievementFont, Color.BLACK));
        stack.add(achievementTextLabel);
        achievementTextLabel.setAlignment(Align.top);
        achievementsTable.row();
        achievementsTable.add(stack);
    }

    private void createCompleteAchievement(String achievementText, BitmapFont achievementFont, Table achievementsTable) {
        Stack stack = new Stack();

        Image completeAchievement = new Image(new TextureRegion(achievementIconsAtlas.findRegion("achievementComplete"), 0, 0, 342, 132));
        stack.add(completeAchievement);
        Label achievementTextLabel = new Label(achievementText, new Label.LabelStyle(achievementFont, Color.BLACK));
        stack.add(achievementTextLabel);
        achievementTextLabel.setAlignment(Align.top);
        achievementsTable.row();
        achievementsTable.add(stack);
    }

    public void playButtonClick(Preferences prefs) {
        if (prefs.getBoolean("SoundOn", true)) {
            buttonClickSound.play();
        }
    }

    public boolean isBackBtnPressed() {
        return backBtnPressed;
    }

    public void dispose() {
        generator.dispose();
        rewardsFont.dispose();
        stage.dispose();
    }
}