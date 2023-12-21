package com.mygdx.NinjaDragon.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.NinjaDragon.NinjaDragon;
import com.mygdx.NinjaDragon.States.PlayState;

/**
 * Created by Brian on 9/24/2016.
 */
public class Spikes implements Pool.Poolable{
    public Body b2body;
    public boolean alive;
    public PolygonShape spikes;
    private BodyDef bdef;
    private FixtureDef fdef;

    public Spikes() {

    }

    public void init(PlayState state, Rectangle bounds) {
        World world = state.getWorld();
        this.alive = false;

        if (spikes == null) {
            bdef = new BodyDef();
            fdef = new FixtureDef();
            bdef.position.set((bounds.getX() + 80) / NinjaDragon.PPM, (bounds.getY() + 80) / NinjaDragon.PPM);
            b2body = world.createBody(bdef);
            createBody();
            fdef.shape = getBody();
            fdef.filter.categoryBits = NinjaDragon.SPIKE_BIT;
            b2body.createFixture(fdef);
        } else {
            bdef.position.set((bounds.getX() + 80) / NinjaDragon.PPM, (bounds.getY() + 80) / NinjaDragon.PPM);
            b2body = world.createBody(bdef);
            b2body.createFixture(fdef);
        }
        alive = true;
    }

    private void createBody(){

        PolygonShape spikes = new PolygonShape();
        Vector2[] vertice = new Vector2[8];
        vertice[0] = new Vector2(80, 80).scl(1 / NinjaDragon.PPM);
        vertice[1] = new Vector2(80, -80).scl(1 / NinjaDragon.PPM);
        vertice[2] = new Vector2(15, -80).scl(1 / NinjaDragon.PPM);
        vertice[3] = new Vector2(-79, -61).scl(1 / NinjaDragon.PPM);
        vertice[4] = new Vector2(-80, -59).scl(1 / NinjaDragon.PPM);
        vertice[5] = new Vector2(-80, 59).scl(1 / NinjaDragon.PPM);
        vertice[6] = new Vector2(-79, 61).scl(1 / NinjaDragon.PPM);
        vertice[7] = new Vector2(15, 80).scl(1 / NinjaDragon.PPM);
        spikes.set(vertice);
        setBody(spikes);
    }

    private void setBody(PolygonShape spikes) {
        this.spikes = spikes;
    }

    public PolygonShape getBody() {
        return spikes;
    }

    @Override
    public void reset() {
    alive = false;
    }
}
