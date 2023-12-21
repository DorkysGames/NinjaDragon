package com.mygdx.NinjaDragon.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.NinjaDragon.NinjaDragon;
import com.mygdx.NinjaDragon.States.PlayState;

/**
 * Created by Brian on 10/2/2016.
 */

public class HorizontalShuriken extends Sprite implements Pool.Poolable {

    private float stateTime;
    public Body b2body;
    public boolean alive;
    private CircleShape horizontalShuriken;
    private BodyDef bdef;
    private FixtureDef fdef;
    private Vector2 velocity;
    private Animation shurikenAnimation;

    public HorizontalShuriken() {
    }

    // update the velocity, position and animation of the shuriken
    public void update (float dt, float hyperModeMultiplier){
        velocity.x = dt * -420 * hyperModeMultiplier;
        b2body.setLinearVelocity(velocity);
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2 - .005f, b2body.getPosition().y - getHeight() / 2);
        setRegion(shurikenAnimation.getKeyFrame(stateTime, true));
    }

    private void InitializeAnimation(TextureAtlas atlas, Rectangle bounds){
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 0; i < 3; i++)
            frames.add(new TextureRegion(atlas.findRegion("Shuriken"), i * 121, 0, 121, 120));
        shurikenAnimation = new Animation(.05f, frames);
        frames.clear();

        setBounds((bounds.getX() + 80) / NinjaDragon.PPM, (bounds.getY() + 80) / NinjaDragon.PPM, 121 / NinjaDragon.PPM, 120 / NinjaDragon.PPM);
    }

    private void setVelocity() {
        velocity = new Vector2(-7,0);
    }

    public void init(PlayState state, TextureAtlas atlas, Rectangle bounds) {
        World world = state.getWorld();
        this.alive = false;

        if (horizontalShuriken == null) {
            bdef = new BodyDef();
            fdef = new FixtureDef();
            bdef.position.set((bounds.getX() + 80) / NinjaDragon.PPM, (bounds.getY() + 80) / NinjaDragon.PPM);
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);
            createBody();
            fdef.shape = getBody();
            fdef.filter.categoryBits = NinjaDragon.H_SHURIKEN_BIT;
            fdef.filter.maskBits = NinjaDragon.DRAGON_BIT | NinjaDragon.GROUND_BIT;
            fdef.isSensor = true;
            b2body.createFixture(fdef).setUserData(this);
            setVelocity();
            b2body.setActive(true);
            InitializeAnimation(atlas, bounds);
        } else {
            bdef.position.set((bounds.getX() + 80) / NinjaDragon.PPM, (bounds.getY() + 80) / NinjaDragon.PPM);
            b2body = world.createBody(bdef);
            b2body.createFixture(fdef).setUserData(this);
            setVelocity();
        }
        alive = true;
    }

    private void createBody(){
        CircleShape shape = new CircleShape();
        shape.setRadius(58 / NinjaDragon.PPM);
        setBody(shape);
    }

    private void setBody(CircleShape horizontalShuriken) {
        this.horizontalShuriken = horizontalShuriken;
    }

    public CircleShape getBody() {
        return horizontalShuriken;
    }

    @Override
    public void reset() {
        b2body.setTransform(0,0,0);
        b2body.setLinearVelocity(0,0);
        alive = false;
    }
}
