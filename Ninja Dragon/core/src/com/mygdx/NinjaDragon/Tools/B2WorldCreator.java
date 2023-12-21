package com.mygdx.NinjaDragon.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.NinjaDragon.NinjaDragon;
import com.mygdx.NinjaDragon.States.PlayState;
/**
 * Created by Brian on 9/24/2016.
 */
public class B2WorldCreator {
    private Array<Rectangle> sortedSpikes;
    private Array<Rectangle> sortedHorizontalShurikens;
    private Array<Rectangle> sortedVerticalShurikens;
    private Array<Body> groundCeilingFinish;
    private PolygonShape shape;

    public B2WorldCreator(PlayState state) {
        World world = state.getWorld();
        TiledMap map = state.getMap();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Body body;
        shape = new PolygonShape();

        // create ground and ceiling bodies/fixtures
        groundCeilingFinish = new Array<Body>();
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / NinjaDragon.PPM, (rect.getY() + rect.getHeight() / 2) / NinjaDragon.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / NinjaDragon.PPM, rect.getHeight() / 2 / NinjaDragon.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = NinjaDragon.GROUND_BIT;
            body.createFixture(fdef);
            groundCeilingFinish.add(body);
        }

        // create spike bodies/fixtures
        sortedSpikes = new Array<Rectangle>();
        Array<Rectangle> spikes = new Array<Rectangle>();
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            spikes.add(rect);
        }
        sortedSpikes = bubbleSort(spikes);


        //create all horizontal Shurikens
        sortedHorizontalShurikens = new Array<Rectangle>();
        Array<Rectangle> horizontalShurikens = new Array<Rectangle>();
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            horizontalShurikens.add(rect);
        }
        sortedHorizontalShurikens = bubbleSort(horizontalShurikens);

        //create all vertical Shurikens
        sortedVerticalShurikens = new Array<Rectangle>();
        Array<Rectangle> verticalShurikens = new Array<Rectangle>();
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            verticalShurikens.add(rect);
        }
        sortedVerticalShurikens = bubbleSort(verticalShurikens);

        // create finish line bodies/fixtures
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / NinjaDragon.PPM, (rect.getY() + rect.getHeight() / 2) / NinjaDragon.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / NinjaDragon.PPM, rect.getHeight() / 2 / NinjaDragon.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = NinjaDragon.FINISHLINE_BIT;
            body.createFixture(fdef);
            groundCeilingFinish.add(body);
        }

        setSpikes(sortedSpikes);
        setHorizontalShurikens(sortedHorizontalShurikens);
        setVerticalShurikens(sortedVerticalShurikens);
    }

    public Array<Rectangle> getSpikes(){
        return sortedSpikes;
    }

    public void setSpikes(Array<Rectangle> sortedSpikes) {
        this.sortedSpikes = sortedSpikes;
    }

    public Array<Rectangle> getHorizontalShurikens(){
        return sortedHorizontalShurikens;
    }

    private void setHorizontalShurikens(Array<Rectangle> sortedHorizontalShurikens) {
        this.sortedHorizontalShurikens = sortedHorizontalShurikens;
    }

    public Array<Rectangle> getVerticalShurikens(){
        return sortedVerticalShurikens;
    }

    private void setVerticalShurikens(Array<Rectangle> sortedVerticalShurikens) {
        this.sortedVerticalShurikens = sortedVerticalShurikens;
    }

    public Array<Body> getGroundCeilingFinish () {
        return groundCeilingFinish;
    }

    private Array<Rectangle> bubbleSort(Array<Rectangle> rectArray) {
        int n = rectArray.size;
        Rectangle temp;
        for(int i = 0; i < n; i++) {
            for(int j = 1; j < (n-i); j++){
                if(rectArray.get(j-1).getX() > rectArray.get(j).getX()){
                    // swap the elements
                    temp = rectArray.get(j-1);
                    rectArray.set(j-1, rectArray.get(j));
                    rectArray.set(j, temp);
                }
            }
        }
        return rectArray;
    }

    public void dispose() {
        for (int i = sortedSpikes.size; i > 0; i--) {
            sortedSpikes.removeIndex(i - 1);
        }
        sortedSpikes = null;
        for (int i = sortedHorizontalShurikens.size; i > 0; i--) {
            sortedHorizontalShurikens.removeIndex(i - 1);
        }
        sortedHorizontalShurikens = null;
        for (int i = sortedVerticalShurikens.size; i > 0; i--) {
            sortedVerticalShurikens.removeIndex(i - 1);
        }
        sortedVerticalShurikens = null;

        shape.dispose();
    }
}
