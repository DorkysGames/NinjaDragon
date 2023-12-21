package com.mygdx.NinjaDragon.Sprites;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.NinjaDragon.NinjaDragon;
import com.mygdx.NinjaDragon.States.PlayState;

/**
 * Created by Brian on 9/22/2016.
 */
public class Dragon extends Sprite{

    public enum State {STANDING, FLYING_UP,FLYING_DOWN, SHIELDED, INVINCIBLE, FADING_INVINCIBILITY}
    private World world;
    public AssetManager manager;
    private Preferences prefs;
    public State currentState;
    private State previousState;
    private State previousAction;
    private float stateTimer;
    private float actionTimer;
    private int descendingCount;

    // dragon animations
    public Body b2body;
    private TextureRegion dragonStand;
    private TextureRegion dragonFlyDown;
    private TextureRegion dragonShieldedFlyDown;
    private Animation dragonFlap;
    private Animation dragonShieldedFlap;

    // dragon statuses
    private boolean dragonIsDead;
    private boolean dragonIsShielded;
    private boolean dragonIsInvincible;
    private boolean dragonFadingInvincibility;
    private boolean dragonVictory;

    public Dragon(PlayState state, TextureAtlas atlas){
        this.world = state.getWorld();
        this.manager = state.getAssetManager();
        this.prefs = state.getPreferences();
        defineDragon();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        actionTimer = 0;
        descendingCount = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        // adding dragon animations
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Dragon"), i * 141, 0, 141, 102));
        }
        for (int i = 2; i >= 1; i--) {
            frames.add(new TextureRegion(atlas.findRegion("Dragon"), i * 141, 0, 141, 102));
        }
        dragonFlap = new Animation(.08f, frames);
        frames.clear();

        // adding dragon shielded animations
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion("dragonShielded"), i * 141, 0, 141, 102));
        }
        for (int i = 2; i >= 1; i--) {
            frames.add(new TextureRegion(atlas.findRegion("dragonShielded"), i * 141, 0, 141, 102));
        }
        dragonShieldedFlap = new Animation(.08f, frames);
        frames.clear();

        dragonStand = new TextureRegion(atlas.findRegion("Dragon"), 0, 0, 141, 102);

        dragonFlyDown = new TextureRegion(atlas.findRegion("Dragon"), 141, 0, 141, 102);

        dragonShieldedFlyDown = new TextureRegion(atlas.findRegion("dragonShielded"), 141, 0, 141, 102);

        setBounds(274 / NinjaDragon.PPM, 700 / NinjaDragon.PPM, 141 / NinjaDragon.PPM, 102 / NinjaDragon.PPM);
        setRegion(dragonStand);
    }

    private void defineDragon(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(275 / NinjaDragon.PPM, 700 / NinjaDragon.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        // create the dragon tail
        PolygonShape tailShape = new PolygonShape();
        Vector2[] tailVertice = new Vector2[3];

        tailVertice[0] = new Vector2(15 , 40).scl(1 / NinjaDragon.PPM);
        tailVertice[1] = new Vector2(35 , 40).scl(1 / NinjaDragon.PPM);
        tailVertice[2] = new Vector2(35 , 25).scl(1 / NinjaDragon.PPM);

        tailShape.set(tailVertice);
        fdef.filter.categoryBits = NinjaDragon.DRAGON_BIT;
        fdef.filter.maskBits = NinjaDragon.GROUND_BIT | NinjaDragon.SPIKE_BIT | NinjaDragon.H_SHURIKEN_BIT | NinjaDragon.V_SHURIKEN_BIT | NinjaDragon.FINISHLINE_BIT;

        fdef.shape = tailShape;
        b2body.createFixture(fdef).setUserData(this);

        // create the dragon back body
        PolygonShape backBodyShape = new PolygonShape();
        Vector2[] backBodyVertice = new Vector2[5];

        backBodyVertice[0] = new Vector2(35 , 25).scl(1 / NinjaDragon.PPM);
        backBodyVertice[1] = new Vector2(35 , 40).scl(1 / NinjaDragon.PPM);
        backBodyVertice[2] = new Vector2(65 , 40).scl(1 / NinjaDragon.PPM);
        backBodyVertice[3] = new Vector2(67 , 13).scl(1 / NinjaDragon.PPM);
        backBodyVertice[4] = new Vector2(40 , 13).scl(1 / NinjaDragon.PPM);

        backBodyShape.set(backBodyVertice);
        fdef.shape = backBodyShape;
        b2body.createFixture(fdef).setUserData(this);

        // create the dragon front body
        PolygonShape frontBodyShape = new PolygonShape();
        Vector2[] frontBodyVertice = new Vector2[4];

        frontBodyVertice[0] = new Vector2(80,55).scl(1 / NinjaDragon.PPM);
        frontBodyVertice[1] = new Vector2(100 , 45).scl(1 / NinjaDragon.PPM);
        frontBodyVertice[2] = new Vector2(67 , 13).scl(1 / NinjaDragon.PPM);
        frontBodyVertice[3] = new Vector2(65 , 40).scl(1 / NinjaDragon.PPM);

        frontBodyShape.set(frontBodyVertice);
        fdef.shape = frontBodyShape;
        b2body.createFixture(fdef).setUserData(this);

        // create the dragon head
        PolygonShape headShape = new PolygonShape();
        Vector2[] headVertice = new Vector2[6];

        headVertice[0] = new Vector2(81 , 79).scl(1 / NinjaDragon.PPM);
        headVertice[1] = new Vector2(105 , 87).scl(1 / NinjaDragon.PPM);
        headVertice[2] = new Vector2(125 , 87).scl(1 / NinjaDragon.PPM);
        headVertice[3] = new Vector2(125 , 45).scl(1 / NinjaDragon.PPM);
        headVertice[4] = new Vector2(100 , 45).scl(1 / NinjaDragon.PPM);
        headVertice[5] = new Vector2(80 , 55).scl(1 / NinjaDragon.PPM);

        headShape.set(headVertice);
        fdef.shape = headShape;
        b2body.createFixture(fdef).setUserData(this);

        //dispose of shapes used to create body
        tailShape.dispose();
        backBodyShape.dispose();
        frontBodyShape.dispose();
        headShape.dispose();
    }


    public void update (float dt) {
        // sets Position for body and texture frame
        setPosition(b2body.getPosition().x - .01f, b2body.getPosition().y);
        if (!dragonIsDead){
            setRegion(getFrame(dt));
        }
    }

    private TextureRegion getFrame(float dt){
    currentState = getState();
        State currentAction = getAction();

        TextureRegion region;

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        if ((currentAction == State.STANDING || currentAction == State.FLYING_UP) && previousAction != State.FLYING_DOWN) {
            actionTimer = currentAction == State.FLYING_UP ? actionTimer + (dt * 2) : actionTimer + dt;
            previousAction = currentAction;
        } else {
            actionTimer = currentAction == previousAction ? actionTimer + dt : 0;
            previousAction = currentAction;
        }

        //set regions for actions
        if (dragonIsShielded) {
            switch(currentAction){
                case FLYING_DOWN:
                    region = dragonShieldedFlyDown;
                    break;
                default:
                    region = dragonShieldedFlap.getKeyFrame(actionTimer, true);
                    break;
            }
        } else {
            switch(currentAction){
                case FLYING_DOWN:
                    region = dragonFlyDown;
                    break;
                default:
                    region = dragonFlap.getKeyFrame(actionTimer, true);
                    break;
            }
        }

        return region;
    }

    // gets the current state the dragon is in
    public State getState(){
        if(dragonIsInvincible)
            return State.INVINCIBLE;
        if(dragonFadingInvincibility)
            return State.FADING_INVINCIBILITY;
        if(dragonIsShielded)
            return State.SHIELDED;
        else
            return State.STANDING;
    }

    // gets the current action for the dragon to determine the proper animation
    private State getAction(){
        if (prefs.getBoolean("TouchScreenOn", true)) {
            if(b2body.getLinearVelocity().y > 0) {
                descendingCount = 11;
                return State.FLYING_UP;
            }
            // put in descending count for the dragon flying down on touch screen
            if(b2body.getLinearVelocity().y < 0 || descendingCount <= 10) {
                if (b2body.getLinearVelocity().y < 0) {
                    descendingCount = 0;
                } else {
                    descendingCount++;
                }
                return State.FLYING_DOWN;
            }
            else {
                descendingCount++;
                return State.STANDING;
            }
        } else {
            if(b2body.getLinearVelocity().y > 0)
                return State.FLYING_UP;
            if(b2body.getLinearVelocity().y < 0)
                return State.FLYING_DOWN;
            else
                return State.STANDING;
        }
    }

    public void hit() {
        // if the dragon is not invincible, continue
        if (!dragonIsInvincible && !dragonFadingInvincibility) {
            if (dragonIsShielded) {
                dragonIsShielded = false;
                dragonFadingInvincibility = true;
            } else {
                if (!dragonIsDead) {
                    if (!dragonVictory) {
                    b2body.setLinearVelocity(0,0);
                        stopMusic("Chinatown");
                        stopMusic("Energetic_Sport_Symphony");
                        stopMusic("Positive_Dubstep");
                        stopMusic("Back_To_The_8_Bit");
                        stopMusic("Chiptune_Vengeance");
                    if (prefs.getBoolean("SoundOn", true)) {
                        manager.get("Audio/Sounds/HitSound.ogg", Sound.class).play();
                    }
                    dragonIsDead = true;
                    }
                }
            }
        }
    }

    public void isInvincible(){
        if (prefs.getBoolean("SoundOn", true)) {
            manager.get("Audio/Sounds/InvincibilityPowerUp.ogg", Sound.class).play();
        }
        dragonIsInvincible = true;
    }

    public void isNotInvincible(){
        dragonIsInvincible = false;
    }

    public void isFadingInvincibility(){
        dragonFadingInvincibility = true;
    }

    public void isNotFadingInvincibility(){
        if (prefs.getBoolean("SoundOn", true)) {
            manager.get("Audio/Sounds/PowerDown.ogg", Sound.class).play();
        }
        dragonFadingInvincibility = false;
    }

    public void isShielded(){
        if (prefs.getBoolean("SoundOn", true)) {
            manager.get("Audio/Sounds/ShieldPowerUp.ogg", Sound.class).play();
        }
        dragonIsShielded = true;
    }

    private void stopMusic(String songName) {
        if (manager.get("Audio/Music/" + songName + ".mp3", Music.class).isPlaying()) {
            manager.get("Audio/Music/" + songName + ".mp3", Music.class).pause();
        }
    }

    public boolean getInvincible() {
        return dragonIsInvincible;
    }

    public boolean getFadingInvincible() {
        return dragonFadingInvincibility;
    }

    public boolean getShielded() {
        return dragonIsShielded;
    }

    public boolean getIsDead() {
        return dragonIsDead;
    }

    public boolean getIsVictory() {
        return dragonVictory;
    }

    public void victory(){
        dragonVictory = true;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void setStateTimer(float stateTimer) {
        this.stateTimer = stateTimer;
    }

    public void dispose() {
        // set textures and animations to null for garbage collection
        dragonStand = null;
        dragonFlap = null;
        dragonShieldedFlap = null;
        dragonFlyDown = null;
        dragonShieldedFlyDown = null;
    }
}
