package com.mygdx.NinjaDragon.States;

import com.badlogic.gdx.graphics.SpriteBatch;

import java.util.Stack;

/**
 * Created by Brian on 9/14/2016.
 */
public class GameStateManager {

    private Stack<State> states;

    public GameStateManager(){
        states = new Stack<State>();
    }

    public void push (State state) {
        states.push(state);
    }

    public void set(State state) {
        // anytime we pop a state off of our stack, we don't plan on using that state again
        states.pop().dispose();
        states.push(state);
    }

    public void update(float dt) {
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb) {
        states.peek().render(sb);
    }
}
