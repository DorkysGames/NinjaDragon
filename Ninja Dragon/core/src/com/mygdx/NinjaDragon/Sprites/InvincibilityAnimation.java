package com.mygdx.NinjaDragon.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.NinjaDragon.NinjaDragon;

/**
 * Created by Brian on 11/25/2016.
 */

public class InvincibilityAnimation extends Sprite {
    private Animation dragonInvincibilityAnimation;
    private Animation dragonFadingInvincibilityAnimation;

    public InvincibilityAnimation(TextureAtlas atlas){
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // adding dragon invincibility animation
        for(int i = 1; i < 9; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Invincibility" + i), 0, 0, 200, 153));
        }
        dragonInvincibilityAnimation = new Animation(.1f, frames);
        frames.clear();

        // adding dragon fading invincibility animation
        for(int i = 1; i < 9; i++) {
            if (i == 1 || i == 4 || i == 7) {
                frames.add(new TextureRegion(atlas.findRegion("Invincibility0"), 0, 0, 200, 153));
            }
            frames.add(new TextureRegion(atlas.findRegion("Invincibility" + i), 0, 0, 200, 153));
        }
        dragonFadingInvincibilityAnimation = new Animation(.1f, frames);
        frames.clear();

        setBounds(274 / NinjaDragon.PPM, 700 / NinjaDragon.PPM, 200 / NinjaDragon.PPM, 153 / NinjaDragon.PPM);
    }

    public void update(Dragon dragon) {
        // sets Position for texture frame
        if (dragon.currentState == Dragon.State.INVINCIBLE) {
            setPosition(dragon.b2body.getPosition().x - .27f, dragon.b2body.getPosition().y - .27f);
            setRegion(dragonInvincibilityAnimation.getKeyFrame(dragon.getStateTimer(), true));
        } else if (dragon.currentState ==  Dragon.State.FADING_INVINCIBILITY) {
            setPosition(dragon.b2body.getPosition().x - .27f, dragon.b2body.getPosition().y - .27f);
            setRegion(dragonFadingInvincibilityAnimation.getKeyFrame(dragon.getStateTimer(), true));
        }
    }

    public void dispose() {
        // set textures and animations to null for garbage collection
        dragonInvincibilityAnimation = null;
        dragonFadingInvincibilityAnimation = null;
    }
}
