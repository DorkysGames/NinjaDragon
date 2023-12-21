package com.mygdx.NinjaDragon.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.NinjaDragon.NinjaDragon;
import com.mygdx.NinjaDragon.Sprites.Dragon;
import com.mygdx.NinjaDragon.Sprites.VerticalShuriken;

/**
 * Created by Brian on 10/2/2016.
 */

public class WorldContactListener implements ContactListener{
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        // collision detection for when 2 fixtures collide in box2d
        switch (cDef){
            case NinjaDragon.GROUND_BIT | NinjaDragon.V_SHURIKEN_BIT:
                if(fixA.getFilterData().categoryBits == NinjaDragon.V_SHURIKEN_BIT)
                    ((VerticalShuriken)fixA.getUserData()).reverseVelocity(true);
                else
                    ((VerticalShuriken)fixB.getUserData()).reverseVelocity(true);
                break;
            case NinjaDragon.DRAGON_BIT | NinjaDragon.H_SHURIKEN_BIT:
                if(fixA.getFilterData().categoryBits == NinjaDragon.DRAGON_BIT)
                    ((Dragon) fixA.getUserData()).hit();
                else
                    ((Dragon) fixB.getUserData()).hit();
                break;
            case NinjaDragon.DRAGON_BIT | NinjaDragon.V_SHURIKEN_BIT:
                if(fixA.getFilterData().categoryBits == NinjaDragon.DRAGON_BIT)
                    ((Dragon) fixA.getUserData()).hit();
                else
                    ((Dragon) fixB.getUserData()).hit();
                break;
            case NinjaDragon.DRAGON_BIT | NinjaDragon.FINISHLINE_BIT:
                if(fixA.getFilterData().categoryBits == NinjaDragon.DRAGON_BIT)
                ((Dragon) fixA.getUserData()).victory();
                else
                ((Dragon) fixB.getUserData()).victory();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        // collision detection for when 2 fixtures collide in box2d
        switch (cDef){
            case NinjaDragon.DRAGON_BIT | NinjaDragon.SPIKE_BIT:
                if(fixA.getFilterData().categoryBits == NinjaDragon.DRAGON_BIT)
                    ((Dragon) fixA.getUserData()).hit();
                else
                    ((Dragon) fixB.getUserData()).hit();
                break;
            case NinjaDragon.DRAGON_BIT | NinjaDragon.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == NinjaDragon.DRAGON_BIT)
                    ((Dragon) fixA.getUserData()).hit();
                else
                    ((Dragon) fixB.getUserData()).hit();
                break;
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
