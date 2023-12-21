package com.mygdx.NinjaDragon.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.NinjaDragon.NinjaDragon;
import com.mygdx.NinjaDragon.Sprites.Dragon;
import com.mygdx.NinjaDragon.Sprites.HorizontalShuriken;
import com.mygdx.NinjaDragon.Sprites.InvincibilityAnimation;
import com.mygdx.NinjaDragon.Sprites.Spikes;
import com.mygdx.NinjaDragon.Sprites.VerticalShuriken;
import com.mygdx.NinjaDragon.Tools.AchievementHandler;
import com.mygdx.NinjaDragon.Tools.AchievementNotification;
import com.mygdx.NinjaDragon.Tools.AdHandler;
import com.mygdx.NinjaDragon.Tools.B2WorldCreator;
import com.mygdx.NinjaDragon.Tools.GameOverButtons;
import com.mygdx.NinjaDragon.Tools.Hud;
import com.mygdx.NinjaDragon.Tools.PauseButtons;
import com.mygdx.NinjaDragon.Tools.PlayerButtons;
import com.mygdx.NinjaDragon.Tools.TutorialWindow;
import com.mygdx.NinjaDragon.Tools.VictoryButtons;
import com.mygdx.NinjaDragon.Tools.VideoNotification;
import com.mygdx.NinjaDragon.Tools.WorldContactListener;
import java.util.Random;

/**
 * Created by Brian on 9/27/2016.
 */
public class PlayState extends State{

    // user data in the game, saved high score, etc.
    private boolean victoryPreferences = false;
    private Vector3 touchPosition = new Vector3();

    // sprite batch
    private SpriteBatch sb2;
    //private Box2DDebugRenderer b2dr;

    // buttons, button variables, and hud in our game
    private PlayerButtons playerButtons;
    private GameOverButtons gameOverButtons;
    private boolean gameOverButtonsRendered = false;
    private VictoryButtons victoryButtons;
    private boolean victoryButtonsRendered = false;
    private PauseButtons pauseButtons;
    private boolean pauseButtonsRendered = false;
    private TutorialWindow tutorialWindow;
    private boolean tutorialRendered = false;
    private boolean showedTutorial = false;
    private VideoNotification videoNotification;
    private AchievementNotification achievementNotification;
    private AchievementHandler achievementHandler;
    private Hud hud;

    // variables for ads
    private int videoRewardFlag = 0;
    private boolean bannerActive = false;

    // animations for the game
    private InvincibilityAnimation invincibilityAnimation;
    private boolean noPowerups = true;
    private int achievementNumber = 0;

    // sound effects for the game
    private boolean scrollSoundPlayed = false;

    // sprites
    private Dragon dragon;

    // score variables
    private int loopScore = 0;
    private int finalScore = 0;
    private boolean newHighScore = false;

    // font used in the game
    private BitmapFont hudFont;
    private BitmapFont playerButtonsFont;
    private BitmapFont menuHeaderFont;
    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Ninja_Font.ttf"));

    // variables for tiled map and rendering map
    private TiledMap map;
    private OrthogonalTiledMapRenderer levelRenderer;

    // texture atlas's
    private TextureAtlas atlas = new TextureAtlas();

    // game components
    private World world;
    private Music music;
    private B2WorldCreator creator;

    // variables for time calculation
    private long gameOverDelay;
    private long startTime = System.nanoTime();
    private int frames = 0;

    // level number or infinite mode
    private boolean isInfiniteMode = false;
    private int levelNumber;
    private int persistedLevelNumber;

    // variables for hyper mode
    private boolean hyperModeFlag = false;
    private float hyperModeMultiplier = 1;

    // loading screen variables
    private boolean isLoading = true;
    private int loadingScreenCount = 0;

    // elements and arrays for spikes and spike pool
    private final Array<Spikes> activeSpikes = new Array<Spikes>();
    private Array<Rectangle> sortedSpikes;
    private Array<Body> spikeBodies = new Array();
    private Pool<Spikes> spikesPool = Pools.get(Spikes.class);
    private int spikeNumberIndex = 0;
    private int activeSpikeNumberIndex = 0;

    // elements and arrays for horizontal shurikens and shuriken pool
    private final Array<HorizontalShuriken> activeHorizontalShurikens = new Array<HorizontalShuriken>();
    private Array<Rectangle> sortedHorizontalShurikens;
    private Array<Body> horizontalShurikenBodies = new Array();
    private Pool<HorizontalShuriken> horizontalShurikenPool = Pools.get(HorizontalShuriken.class);
    private int horizontalShurikenNumberIndex = 0;
    private int activeHorizontalShurikenNumberIndex = 0;

    // elements and arrays for vertical shurikens and shuriken pool
    private final Array<VerticalShuriken> activeVerticalShurikens= new Array<VerticalShuriken>();
    private Array<Rectangle> sortedVerticalShurikens;
    private Array<Body> verticallShurikenBodies = new Array();
    private Pool<VerticalShuriken> verticalShurikenPool = Pools.get(VerticalShuriken.class);
    private int verticalShurikenNumberIndex = 0;
    private int activeVerticalShurikenNumberIndex = 0;

    PlayState(GameStateManager gsm, AssetManager manager, AdHandler handler, Preferences prefs, int levelNumber) {
        super(gsm, manager, handler, prefs);

        // references to atlas
        atlas = manager.get("LevelComponents/PlayStateTextures.atlas");
        TextureAtlas invincibilityAtlas = manager.get("LevelComponents/Invincibility.atlas");

        // references to game font
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        playerButtonsFont = generator.generateFont(parameter);
        parameter.size = 38;
        menuHeaderFont = generator.generateFont(parameter);
        parameter.size = 60;
        hudFont = generator.generateFont(parameter);

        // references to level number/infintie mode
        this.levelNumber = levelNumber;
        if (levelNumber == 0){
            Random rand = new Random();
            levelNumber = rand.nextInt(20) + 1;
            isInfiniteMode = true;
        }
        setPersistedLevelNumber(levelNumber);

        // setting for hyper mode
        if (prefs.getBoolean("hyperMode", false)) {
            hyperModeMultiplier = 1.4f;
            hyperModeFlag = true;
        }

        if (hyperModeFlag){
            prefs.putInteger("level" + this.levelNumber + "attemptCountH", prefs.getInteger("level" + this.levelNumber + "attemptCountH", 0) + 1);
        } else {
            prefs.putInteger("level" + this.levelNumber + "attemptCount", prefs.getInteger("level" + this.levelNumber + "attemptCount", 0) + 1);
        }
        prefs.flush();

        // game buttons and hud references
        hud = new Hud(isInfiniteMode, hudFont, atlas, prefs, hyperModeFlag, this.levelNumber);
        achievementHandler = new AchievementHandler();
        videoNotification = new VideoNotification(atlas);
        playerButtons = new PlayerButtons(prefs, playerButtonsFont, atlas, levelNumber, isInfiniteMode);
        gameOverButtons = new GameOverButtons(manager, isInfiniteMode, playerButtonsFont,menuHeaderFont, prefs, this.handler, false, atlas);

        // camera and viewport references
        float cameraScale = .417f;
        cam.setToOrtho(false, (NinjaDragon.WIDTH / cameraScale) / NinjaDragon.PPM, (NinjaDragon.HEIGHT / cameraScale) / NinjaDragon.PPM);
        Viewport gamePort = new StretchViewport((NinjaDragon.WIDTH / 2) / NinjaDragon.PPM, (NinjaDragon.HEIGHT / 2) / NinjaDragon.PPM, cam);
        cam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / (cameraScale * 2), 0);

        // textured map references
        TmxMapLoader mapLoader = new TmxMapLoader();
        if (isInfiniteMode) {
            map = mapLoader.load("LevelComponents/Levels/Infinite" + levelNumber + ".tmx");
        } else {
            map = mapLoader.load("LevelComponents/Levels/Level" + levelNumber + ".tmx");
        }
        levelRenderer = new OrthogonalTiledMapRenderer(map, 1 / NinjaDragon.PPM);
        MapProperties prop = map.getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int mapPixelWidth = mapWidth * tilePixelWidth;

        // sets gravity
        // references to game components and world
        sb2 = new SpriteBatch();
        world = new World(new Vector2(0,0), true);
        world.setContactListener(new WorldContactListener());
        creator = new B2WorldCreator(this);
        //b2dr = new Box2DDebugRenderer();

        // setting game obstacles from world creator
        sortedSpikes = creator.getSpikes();
        sortedHorizontalShurikens = creator.getHorizontalShurikens();
        sortedVerticalShurikens = creator.getVerticalShurikens();

        //references to music
        music = manager.get("Audio/Music/Asian_Ambient.mp3");
        if (music.isPlaying()) {
            music.pause();
        }
        Random rand = new Random();
        int trackNumber = rand.nextInt(5) + 1;
        switch (trackNumber) {
            case 1:
                music = manager.get("Audio/Music/Chinatown.mp3");
                break;
            case 2:
                music = manager.get("Audio/Music/Energetic_Sport_Symphony.mp3");
                break;
            case 3:
                music = manager.get("Audio/Music/Positive_Dubstep.mp3");
                break;
            case 4:
                music = manager.get("Audio/Music/Back_To_The_8_Bit.mp3");
                break;
            case 5:
                music = manager.get("Audio/Music/Chiptune_Vengeance.mp3");
                break;
            default:
                music = manager.get("Audio/Music/Chinatown.mp3");
                break;
        }
        music.stop();

        //create dragon in our game world
        dragon = new Dragon(this, atlas);

        // animation for powerups
        invincibilityAnimation = new InvincibilityAnimation(invincibilityAtlas);

        // sets the goal distance for normal mode
        if (!isInfiniteMode) {
            hud.setGoalDistance((mapPixelWidth - 1120) / 160);
        }
    }

    @Override
    protected void handleInput(float dt) {
        // if the dragon is not dead, handle input
        if(!dragon.getIsDead() && !tutorialRendered) {
            if (pauseButtonsRendered) {
                dragon.b2body.setLinearVelocity(0, 0);
            } else {
                if (prefs.getBoolean("TouchScreenOn", true)) {
                    float yTouchCoordinate = (cam.unproject(touchPosition.set(Gdx.input.getX(), (Gdx.input.getY()), 0)).y - .35f);
                        if (hyperModeFlag) {
                            // touch controls for hypermode, same as below touch controls
                            if (yTouchCoordinate > (dragon.b2body.getPosition().y + (8.4 * dt)) && yTouchCoordinate > -.4f && Gdx.input.isTouched())
                                dragon.b2body.setLinearVelocity(0, 840 * dt);
                            else if (yTouchCoordinate > (dragon.b2body.getPosition().y + (4.2 * dt)) && yTouchCoordinate > -.4f && Gdx.input.isTouched())
                                dragon.b2body.setLinearVelocity(0, 420 * dt);
                            else if (yTouchCoordinate < (dragon.b2body.getPosition().y - (8.4 * dt)) && yTouchCoordinate > -.4f && Gdx.input.isTouched())
                                dragon.b2body.setLinearVelocity(0, -840 * dt);
                            else if (yTouchCoordinate < (dragon.b2body.getPosition().y - (4.2 * dt)) && yTouchCoordinate > -.4f && Gdx.input.isTouched())
                                dragon.b2body.setLinearVelocity(0, -420 * dt);
                            else if ((yTouchCoordinate < (dragon.b2body.getPosition().y + (4.2 * dt))) && (yTouchCoordinate > (dragon.b2body.getPosition().y - (4.2 * dt)))
                                    && yTouchCoordinate > -.4f && (yTouchCoordinate != dragon.b2body.getPosition().y) && Gdx.input.isTouched()) {
                                dragon.b2body.setLinearVelocity(0, 0);
                                dragon.b2body.setTransform(dragon.b2body.getPosition().x, yTouchCoordinate, 0);
                            } else
                                dragon.b2body.setLinearVelocity(0, 0);
                            // movement on the x axis
                            dragon.b2body.setLinearVelocity(588 * dt, dragon.b2body.getLinearVelocity().y);
                        } else {
                            // all touch coordinates scale off of dt and have > -.4f to prevent movement on pause and powerup use
                            // if the touch coordinate is greater than the dragon, move up
                            if (yTouchCoordinate > (dragon.b2body.getPosition().y + (6 * dt)) && yTouchCoordinate > -.4f && Gdx.input.isTouched())
                                dragon.b2body.setLinearVelocity(0, 600 * dt);
                            // if the touch coordinate is greater than the dragon but close enough to not be in the above
                            else if (yTouchCoordinate > (dragon.b2body.getPosition().y + (3 * dt)) && yTouchCoordinate > -.4f && Gdx.input.isTouched())
                                dragon.b2body.setLinearVelocity(0, 300 * dt);
                                // if the touch coordinate is less than the dragon, move down
                            else if (yTouchCoordinate < (dragon.b2body.getPosition().y - (6 * dt)) && yTouchCoordinate > -.4f && Gdx.input.isTouched())
                                dragon.b2body.setLinearVelocity(0, -600 * dt);
                                // if the touch coordinate is less than the dragon but close enough to not be in the above
                            else if (yTouchCoordinate < (dragon.b2body.getPosition().y - (3 * dt)) && yTouchCoordinate > -.4f && Gdx.input.isTouched())
                                dragon.b2body.setLinearVelocity(0, -300 * dt);
                                // if the touch coordinate is close to the dragon than all of the above, move the dragon to the position of your finger
                            else if ((yTouchCoordinate < (dragon.b2body.getPosition().y + (3 * dt))) && (yTouchCoordinate > (dragon.b2body.getPosition().y - (3 * dt)))
                                    && yTouchCoordinate > -.4f && (yTouchCoordinate != dragon.b2body.getPosition().y) && Gdx.input.isTouched()) {
                                dragon.b2body.setLinearVelocity(0, 0);
                                dragon.b2body.setTransform(dragon.b2body.getPosition().x, yTouchCoordinate, 0);
                            } else
                                dragon.b2body.setLinearVelocity(0, 0);
                            // movement on the x axis
                            dragon.b2body.setLinearVelocity(420 * dt, dragon.b2body.getLinearVelocity().y);
                        }
                } else {
                    if (hyperModeFlag) {
                        if (playerButtons.isFlyUpButtonPressed())
                            dragon.b2body.setLinearVelocity(0, 840 * dt);
                        else if (playerButtons.isFlyDownButtonPressed())
                            dragon.b2body.setLinearVelocity(0, -840 * dt);
                        else
                            dragon.b2body.setLinearVelocity(0, 0);
                        // movement on the x axis
                        dragon.b2body.setLinearVelocity(588 * dt, dragon.b2body.getLinearVelocity().y);
                    } else {
                        if (playerButtons.isFlyUpButtonPressed())
                            dragon.b2body.setLinearVelocity(0, 600 * dt);
                        else if (playerButtons.isFlyDownButtonPressed())
                            dragon.b2body.setLinearVelocity(0, -600 * dt);
                        else
                            dragon.b2body.setLinearVelocity(0, 0);
                        // movement on the x axis
                        dragon.b2body.setLinearVelocity(420 * dt, dragon.b2body.getLinearVelocity().y);
                    }
                }

                // player uses invincibility power-up
                if (playerButtons.isInvincibilityButtonPressed()) {
                    if (!dragon.getInvincible() && prefs.getInteger("invincibilityCount", 0) > 0) {
                        dragon.isInvincible();
                        prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) - 1);
                        prefs.putInteger("invincibilityUsed", prefs.getInteger("invincibilityUsed", 0) + 1);

                        prefs.flush();
                        playerButtons.updateInvincibilityCount(prefs);
                        noPowerups = false;
                    } else {
                        playWarningSound();
                    }
                    playerButtons.setInvincibilityButtonPressed(false);
                }

                // player uses shield power-up
                if (playerButtons.isShieldButtonPressed()) {
                    if (!dragon.getShielded() && prefs.getInteger("shieldCount", 0) > 0) {
                        dragon.isShielded();
                        prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) - 1);
                        prefs.putInteger("shieldsUsed", prefs.getInteger("shieldsUsed", 0) + 1);
                        prefs.flush();
                        playerButtons.updateShieldCount(prefs);
                        noPowerups = false;
                    } else {
                        playWarningSound();
                    }
                    playerButtons.setShieldButtonPressed(false);
                }
                if (!dragon.getIsVictory()) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
                        playerButtons.setPauseButtonPressed(true);
                    }
                }
            }
        } else {
            if (dragon.b2body.getLinearVelocity().x != 0) {
                dragon.b2body.setLinearVelocity(0,0);
            }
        }

        // game over selections
        if(gameOver() && gameOverButtonsRendered){
            if (gameOverButtons.isBackToLevelSelectPressed()) {
                handler.showBannerAds(false);
                gsm.set(new LevelSelectState(gsm, manager, handler, prefs));
            } else if (gameOverButtons.isBackToMainMenuPressed()){
                handler.showBannerAds(false);
                gsm.set(new MainMenuState(gsm, manager, handler, prefs));
            } else if (gameOverButtons.isRestartLevelPressed()) {
            gsm.set(new PlayState(gsm, manager,handler, prefs, levelNumber));
            } else if (gameOverButtons.isVideoButtonPressed()) {
                gameOverButtons.setIsVideoButtonPressed(false);
                handler.playVideo();
                gameOverButtons.dispose();
                gameOverButtons = new GameOverButtons(manager, isInfiniteMode, playerButtonsFont,menuHeaderFont, prefs, handler, true, atlas);
                gameOverButtons.setInputListener();
            }
        }

        // victory selections
        if (victory() && victoryButtonsRendered) {
            if (victoryButtons.isBackToLevelSelectPressed()) {
                handler.showBannerAds(false);
                gsm.set(new LevelSelectState(gsm, manager, handler, prefs));
            } else if (victoryButtons.isRestartLevelPressed()) {
                gsm.set(new PlayState(gsm, manager,handler, prefs, levelNumber));
            } else if (victoryButtons.isNextLevelPressed()) {
                levelNumber++;
                gsm.set(new PlayState(gsm, manager,handler, prefs, levelNumber));
            }
        }

        // pause selections
        if (pauseButtonsRendered) {
            if (pauseButtons.isBackToLevelSelectPressed()) {
                handler.showBannerAds(false);
                if (music.isPlaying()) {
                    music.stop();
                }
                gsm.set(new LevelSelectState(gsm, manager, handler, prefs));
            } else if (pauseButtons.isBackToMainMenuPressed()){
                handler.showBannerAds(false);
                if (music.isPlaying()) {
                    music.stop();
                }
                gsm.set(new MainMenuState(gsm, manager, handler, prefs));
            } else if (pauseButtons.isResumeGamePressed()) {
                playerButtons.setPauseButtonPressed(false);
                if (prefs.getBoolean("MusicOn", true)) {
                    music.setLooping(true);
                    music.play();
                }
                pauseButtonsRendered = false;
                pauseButtons.dispose();
                playerButtons.dispose();
                playerButtons = new PlayerButtons(prefs, playerButtonsFont, atlas, getPersistedLevelNumber(), isInfiniteMode);
            } else if (pauseButtons.isRestartLevelPressed()) {
                gsm.set(new PlayState(gsm, manager, handler, prefs, levelNumber));
                if (music.isPlaying()) {
                    music.stop();
                }
            }
        }

        if (tutorialRendered) {
            if (tutorialWindow.isGotItPressed()) {
                tutorialWindow.dispose();
                showedTutorial = true;
                tutorialRendered = false;
                prefs.flush();
                playerButtons = new PlayerButtons(prefs, playerButtonsFont, atlas, getPersistedLevelNumber(), isInfiniteMode);
            } else if (tutorialWindow.isDoNotShowAgainCheckedPressed()) {
                prefs.putBoolean("ShowTutorial", true);
                tutorialWindow.dispose();
                tutorialWindow = new TutorialWindow(manager, prefs, atlas, false);
            } else if (tutorialWindow.isDoNotShowAgainPressed()) {
                prefs.putBoolean("ShowTutorial", false);
                tutorialWindow.dispose();
                tutorialWindow = new TutorialWindow(manager, prefs, atlas, true);
            }
        }
    }

    @Override
    public void update(float dt) {
        world.step(1/60f, 6, 2);

        if (isLoading || gameOver() || victoryButtonsRendered || pauseButtonsRendered) {
            if (!bannerActive) {
        handler.showBannerAds(true);
                bannerActive = true;
            }
        } else {
            if (bannerActive) {
        handler.showBannerAds(false);
                bannerActive = false;
            }
        }

        if (!isLoading) {
            // player input
            handleInput(dt);
            // checks frames per second
                //logFrame();
            // checks if the dragon is invincible
            checkInvincibility();

            // methods for spike pool
            if(!gameOverButtonsRendered && !victoryButtonsRendered && !pauseButtonsRendered) {
            Spikes spikes;
            if (sortedSpikes.size != 0 && sortedSpikes.size != spikeNumberIndex && sortedSpikes.get(spikeNumberIndex).getX() / 160 < (dragon.getX() + 10) / 1.6) {
                spikes = spikesPool.obtain();
                spikes.init(this, sortedSpikes.get(spikeNumberIndex));
                activeSpikes.add(spikes);
                spikeBodies.add(spikes.b2body);
                spikeNumberIndex++;
            }

            if (sortedSpikes.size != 0 && sortedSpikes.size != activeSpikeNumberIndex && sortedSpikes.get(activeSpikeNumberIndex).getX() / 160 < (dragon.getX() - 4.8) / 1.6) {
                activeSpikes.get(0).reset();
                activeSpikeNumberIndex++;
            }

            int len = activeSpikes.size;
            for (int i = len; --i >= 0; ) {
                spikes = activeSpikes.get(i);
                if (!spikes.alive) {
                    activeSpikes.removeIndex(i);
                    spikesPool.free(spikes);
                }
            }

            // methods for horizontal shuriken pool
            HorizontalShuriken hShuriken;
            if (sortedHorizontalShurikens.size != 0 && sortedHorizontalShurikens.size != horizontalShurikenNumberIndex && sortedHorizontalShurikens.get(horizontalShurikenNumberIndex).getX() / 160 < (dragon.getX() + 10) / 1.6) {
                hShuriken = horizontalShurikenPool.obtain();
                hShuriken.init(this, atlas, sortedHorizontalShurikens.get(horizontalShurikenNumberIndex));
                activeHorizontalShurikens.add(hShuriken);
                horizontalShurikenBodies.add(hShuriken.b2body);
                horizontalShurikenNumberIndex++;
            }

            if (sortedHorizontalShurikens.size != 0 && sortedHorizontalShurikens.size != activeHorizontalShurikenNumberIndex && sortedHorizontalShurikens.get(activeHorizontalShurikenNumberIndex).getX() / 160 < (dragon.getX()) / 1.6) {
                activeHorizontalShurikens.get(0).reset();
                activeHorizontalShurikenNumberIndex++;
            }

            int len2 = activeHorizontalShurikens.size;
            for (int i = len2; --i >= 0; ) {
                hShuriken = activeHorizontalShurikens.get(i);
                if (!hShuriken.alive) {
                    activeHorizontalShurikens.removeIndex(i);
                    horizontalShurikenPool.free(hShuriken);
                }
            }

            // methods for vertical shuriken pool
            VerticalShuriken vShuriken;
            if (sortedVerticalShurikens.size != 0 && sortedVerticalShurikens.size != verticalShurikenNumberIndex && sortedVerticalShurikens.get(verticalShurikenNumberIndex).getX() / 160 < (dragon.getX() + 10) / 1.6) {
                vShuriken = verticalShurikenPool.obtain();
                vShuriken.init(this, atlas, sortedVerticalShurikens.get(verticalShurikenNumberIndex));
                activeVerticalShurikens.add(vShuriken);
                verticallShurikenBodies.add(vShuriken.b2body);
                verticalShurikenNumberIndex++;
            }

            if (sortedVerticalShurikens.size != 0 && sortedVerticalShurikens.size != activeVerticalShurikenNumberIndex && sortedVerticalShurikens.get(activeVerticalShurikenNumberIndex).getX() / 160 < (dragon.getX() - 4.8) / 1.6) {
                activeVerticalShurikens.get(0).reset();
                activeVerticalShurikenNumberIndex++;
            }

            int len3 = activeVerticalShurikens.size;
            for (int i = len3; --i >= 0; ) {
                vShuriken = activeVerticalShurikens.get(i);
                if (!vShuriken.alive) {
                    activeVerticalShurikens.removeIndex(i);
                    verticalShurikenPool.free(vShuriken);
                }
            }

                if (!isInfiniteMode) {
                    // sets distance achieved for level mode
                    hud.setDistanceAchieved((int) ((dragon.b2body.getPosition().x) / 1.6f));
                }
                // number of 160 px tiles (497) from the start to the point of teleport plus 275
                // determines when to set the dragons position back to start in infinite mode
                if (isInfiniteMode && dragon.b2body.getPosition().x >= 79795 / NinjaDragon.PPM) {
                    // if the dragon is set back to start, get the score that the player was at
                    // dragons position times 100 minus the original starting position divided by the pixels per tile plus the current loop score
                    loopScore = ((int) ((dragon.b2body.getPosition().x * NinjaDragon.PPM) - 275) / 160 + loopScore);
                    horizontalShurikenNumberIndex = 0;
                    activeHorizontalShurikenNumberIndex = 0;
                    verticalShurikenNumberIndex = 0;
                    activeVerticalShurikenNumberIndex = 0;
                    dragon.b2body.setTransform(275 / NinjaDragon.PPM, dragon.b2body.getPosition().y, 0);
                }
                // updates for dragon and shurikens
                dragon.update(dt);
                invincibilityAnimation.update(dragon);
                cam.position.x = dragon.b2body.getPosition().x + (300 / NinjaDragon.PPM);
            }
            if (pauseButtonsRendered) {
                for (HorizontalShuriken shuriken : activeHorizontalShurikens) {
                    shuriken.update(0, hyperModeMultiplier);
                }
                for (VerticalShuriken shuriken : activeVerticalShurikens) {
                    shuriken.update(0, hyperModeMultiplier);
                }
            } else {
                for (HorizontalShuriken shuriken : activeHorizontalShurikens) {
                    shuriken.update(dt, hyperModeMultiplier);
                }
                for (VerticalShuriken shuriken : activeVerticalShurikens) {
                    shuriken.update(dt, hyperModeMultiplier);
                }
            }

            if (isInfiniteMode && !pauseButtonsRendered) {
                if (!gameOver()) {
                    // updates the score for infinite mode
                    hud.updateScore((int) (((dragon.b2body.getPosition().x * NinjaDragon.PPM) - 275) / 160) + loopScore);
                } else {
                    if (!gameOverButtonsRendered) {
                        // if the game is over, set the final score the player achieved
                        hud.updateScore((int) (((dragon.b2body.getPosition().x * NinjaDragon.PPM) - 275) / 160) + loopScore);
                        finalScore = (int) ((((dragon.b2body.getPosition().x * NinjaDragon.PPM) - 275) / 160) + loopScore);
                    }
                }
            }
        } else {
            // if they have it to show the tutorial and it hasn't been showed yet, render it
            if (prefs.getBoolean("ShowTutorial", true) && !showedTutorial) {
                    if (!tutorialRendered) {
                        tutorialWindow = new TutorialWindow(manager, prefs, atlas, false);
                        tutorialRendered = true;
                    }
                    handleInput(dt);
            } else {
                loadingScreenCount++;
            }
            cam.position.x = dragon.b2body.getPosition().x + (300 / NinjaDragon.PPM);
            // wait for 1 second after the player loads the resources
            if (loadingScreenCount >= 60) {
                isLoading = false;
                if (prefs.getBoolean("MusicOn", true)) {
                    music.setLooping(true);
                    music.play();
                }
            }
        }

            // update camera every iteration of our render cycle
            cam.update();
            //tell our renderer to draw only what our camera can see in our game world
            levelRenderer.setView(cam);
    }

    @Override
    public void render(SpriteBatch sb) {

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render our game map
        levelRenderer.render();
        playerButtons.draw();

        // if the loading screen is up, draw
        if(isLoading) {
            hud.loadingStageDraw();
        }

            hud.draw();
        // sprite batch to draw dragon, shurikens, and power up animations
            sb2.begin();
            sb2.setProjectionMatrix(cam.combined);
            dragon.draw(sb2);

            for (HorizontalShuriken shuriken : activeHorizontalShurikens) {
                shuriken.draw(sb2);
            }
            for (VerticalShuriken shuriken : activeVerticalShurikens) {
                shuriken.draw(sb2);
            }

        if (dragon.getInvincible() || dragon.getFadingInvincible()) {
            invincibilityAnimation.draw(sb2);
        }
            sb2.end();

        // pause the game if the player pressed pause
        if (playerButtons.isPauseButtonPressed() && !isLoading){
            if (!pauseButtonsRendered) {
                pauseButtons = new PauseButtons(manager, isInfiniteMode, menuHeaderFont, prefs, atlas);
                pauseButtonsRendered = true;
                music.pause();
                playScrollSound();
            }
                pauseButtons.draw();
        }

        // if the game is over, draw the game over screen
        if(gameOver()){
            if (!victory()) {
                if (!gameOverButtonsRendered) {
                    gameOverDelay = System.nanoTime();
                    prefs.putInteger("deathCount", prefs.getInteger("deathCount", 0) + 1);
                    prefs.flush();
                    if (isInfiniteMode) {
                        if (finalScore > prefs.getInteger("highScore", 0)) {
                            prefs.putInteger("highScore", finalScore);
                            prefs.flush();
                            gameOverButtons.updateHighScore(prefs);
                            newHighScore = true;
                        }
                    }
                    if (pauseButtonsRendered) {
                        pauseButtons.dispose();
                        pauseButtonsRendered = false;
                        playerButtons.setPauseButtonPressed(false);
                    }
                    achievementNumber = achievementHandler.checkForCompletedAchievements(prefs, 0, noPowerups, finalScore);
                    if (achievementNumber != 0) {
                        playerButtons.updateInvincibilityCount(prefs);
                        playerButtons.updateShieldCount(prefs);
                        achievementNotification = new AchievementNotification(achievementNumber, atlas);
                    }
                    gameOverButtons.setInputListener();
                    gameOverButtonsRendered = true;
                }
                if (System.nanoTime() - gameOverDelay >= 1000000000) {
                    if (!scrollSoundPlayed) {
                        playScrollSound();
                        scrollSoundPlayed = true;
                    }

                    if (handler.getRewardEarned()) {
                        boolean rewardType = handler.getRewardType();
                        if (rewardType) {
                            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 1);
                            prefs.flush();
                            videoRewardFlag = 1;
                            playerButtons.updateInvincibilityCount(prefs);
                        } else {
                            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 1);
                            prefs.flush();
                            videoRewardFlag = 2;
                            playerButtons.updateShieldCount(prefs);
                        }
                        achievementNumber = 0;
                        handler.resetReward();
                    }
                    if (videoRewardFlag != 0) {
                        if (videoRewardFlag == 1) {
                            videoNotification.drawInvincibilityStage();
                        } else {
                            videoNotification.drawShieldStage();
                        }
                    }
                    if (achievementNumber != 0) {
                        achievementNotification.draw();
                    }
                    gameOverButtons.draw();
                    if (newHighScore) {
                        gameOverButtons.drawHighScoreImage();
                    }
                }
            }
        }

        // if the player won the level, display victory screen
        if (victory()){
            if (!victoryButtonsRendered) {
                if (pauseButtonsRendered) {
                    pauseButtons.dispose();
                    pauseButtonsRendered = false;
                    playerButtons.setPauseButtonPressed(false);
                }
            achievementNumber = achievementHandler.checkForCompletedAchievements(prefs,levelNumber,noPowerups,finalScore);
                if (achievementNumber != 0) {
                    playerButtons.updateInvincibilityCount(prefs);
                    playerButtons.updateShieldCount(prefs);
                    achievementNotification = new AchievementNotification(achievementNumber, atlas);
                }
            victoryButtons = new VictoryButtons(manager, menuHeaderFont, prefs, levelNumber, atlas);
            victoryButtonsRendered = true;
        }
        if (achievementNumber != 0) {
            achievementNotification.draw();
        }
        victoryButtons.draw();
        }


        if (tutorialRendered) {
            tutorialWindow.draw();
        }
    }

    // method to determine if the dragon is invincible
    private void checkInvincibility(){
        if(dragon.currentState == Dragon.State.INVINCIBLE && dragon.getStateTimer() >= 5) {
            dragon.isNotInvincible();
            dragon.isFadingInvincibility();
        } else if(dragon.currentState == Dragon.State.FADING_INVINCIBILITY && dragon.getStateTimer() >= 1.5) {
            dragon.isNotFadingInvincibility();
            dragon.setStateTimer(0);
        }
    }

    private boolean gameOver(){
        return dragon.getIsDead();
    }

    // method to check if the player won the level and log the user data
    private boolean victory() {
        if(dragon.getIsVictory()) {
            if (!victoryPreferences) {
                if (hyperModeFlag) {
                    prefs.putBoolean("hyperLevel" + levelNumber + "Cleared", true);
                } else {
                    prefs.putBoolean("level" + levelNumber + "Cleared", true);
                }
                prefs.flush();
                music.pause();
                if (prefs.getBoolean("SoundOn", true)) {
                    manager.get("Audio/Sounds/GongSound.ogg", Sound.class).play();
                }
                victoryPreferences = true;
            }
            return true;
        }
        return false;
    }

    // dispse method for all of the resources in the play state
    @Override
    public void dispose() {
        map.dispose();
        hudFont.dispose();
        playerButtonsFont.dispose();
        menuHeaderFont.dispose();

        generator.dispose();
        levelRenderer.dispose();
        //b2dr.dispose();

        // disposing of polygon shapes from pooled objects
        for(int i = spikesPool.getFree(); i > 0; i--) {
            spikesPool.obtain().getBody().dispose();
        }

        for(int i = horizontalShurikenPool.getFree(); i > 0; i--) {
            horizontalShurikenPool.obtain().getBody().dispose();
        }

        for(int i = verticalShurikenPool.getFree(); i > 0; i--) {
            verticalShurikenPool.obtain().getBody().dispose();
        }
        // clearing obtained objects in pools
        spikesPool = null;
        horizontalShurikenPool = null;
        verticalShurikenPool = null;

        //disposing array data and polygon objects
        creator.dispose();
        for (int i = activeSpikes.size; i > 0; i--) {
            activeSpikes.get(i - 1).getBody().dispose();
            activeSpikes.removeIndex(i - 1);
        }
        for (int i = activeHorizontalShurikens.size; i > 0; i--) {
            activeHorizontalShurikens.get(i - 1).getBody().dispose();
            activeHorizontalShurikens.removeIndex(i - 1);
        }
        for (int i = activeVerticalShurikens.size; i > 0; i--) {
            activeVerticalShurikens.get(i - 1).getBody().dispose();
            activeVerticalShurikens.removeIndex(i - 1);
        }

        // destroying horizontal shurikens
        for (int i = horizontalShurikenBodies.size; i > 0; i--) {
            Body body = horizontalShurikenBodies.get(i - 1);
            body.setUserData(null);
            world.destroyBody(body);
            horizontalShurikenBodies.removeIndex(i - 1);
            body = null;
        }

        // destroying vertical shurikens
        for (int i = verticallShurikenBodies.size; i > 0; i--) {
            Body body = verticallShurikenBodies.get(i - 1);
            body.setUserData(null);
            world.destroyBody(body);
            verticallShurikenBodies.removeIndex(i - 1);
            body = null;
        }

        // destroying spikes
        for (int i = spikeBodies.size; i > 0; i--) {
            Body body = spikeBodies.get(i - 1);
            body.setUserData(null);
            world.destroyBody(body);
            spikeBodies.removeIndex(i - 1);
            body = null;
        }

        // diestroying ground, ceiling, and finish line
        for (int i = creator.getGroundCeilingFinish().size; i > 0; i--) {
            Body body = creator.getGroundCeilingFinish().get(i - 1);
            body.setUserData(null);
            world.destroyBody(body);
            creator.getGroundCeilingFinish().removeIndex(i - 1);
            body = null;
        }

        // destroying dragon
        dragon.dispose();
        dragon.b2body.setUserData(null);
        world.destroyBody(dragon.b2body);
        dragon.b2body = null;

        //System.out.println("Bodies remaining " + world.getBodyCount());

        //disposing of buttons and stages
        playerButtons.dispose();
        invincibilityAnimation.dispose();
        if (victoryButtons != null) {
            victoryButtons.dispose();
        }
        if (gameOverButtons != null) {
            gameOverButtons.dispose();
        }
        if (pauseButtons != null) {
            pauseButtons.dispose();
        }
        if (videoNotification != null) {
            videoNotification.dispose();
        }
        if (achievementNotification != null) {
            achievementNotification.dispose();
        }
        if (tutorialWindow != null) {
            tutorialWindow.dispose();
        }
        hud.dispose();

 // dispose of the play states sprite batch and world
        world.dispose();
        sb2.dispose();
        sb2 = null;
    }

    // method for logging frames per second
    private void logFrame() {
        frames++;
        if (System.nanoTime() - startTime >= 1000000000) {
            System.out.println(frames);
            frames = 0;
            startTime = System.nanoTime();
        }
    }

    // plays the scroll sound for game over or pause pop up
    private void playScrollSound() {
        if (prefs.getBoolean("SoundOn", true)) {
            manager.get("Audio/Sounds/AncientScrollAppear.ogg", Sound.class).play();
        }
    }

    // plays the warning sound for having no shileds or invincibility
    private void playWarningSound() {
        if (prefs.getBoolean("SoundOn", true)) {
            manager.get("Audio/Sounds/Warning.ogg", Sound.class).play();
        }
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    public Preferences getPreferences(){
        return prefs;
    }

    public AssetManager getAssetManager(){
        return manager;
    }

    private void setPersistedLevelNumber(int persistedLevelNumber) {
        this.persistedLevelNumber = persistedLevelNumber;
    }

    private int getPersistedLevelNumber() {
        return persistedLevelNumber;
    }
}

