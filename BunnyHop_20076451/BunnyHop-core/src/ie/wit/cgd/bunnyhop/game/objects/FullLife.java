package ie.wit.cgd.bunnyhop.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ie.wit.cgd.bunnyhop.game.Assets;
import ie.wit.cgd.bunnyhop.util.Constants;
import ie.wit.cgd.bunnyhop.game.WorldController;
public class FullLife extends AbstractGameObject{

    private TextureRegion   regFullLife;
    public boolean          collected;

    public FullLife() {
        init();
    }

    private void init() {
        dimension.set(0.5f, 0.5f);
        regFullLife = Assets.instance.fullLife.fullLife;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    public void render(SpriteBatch batch) {
        if (collected) return;

        TextureRegion reg = null;
        reg = regFullLife;
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
                scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
                false, false);
    }

    public int getLife() {
        if(WorldController.lives < Constants.LIVES_START){
        	return Constants.LIVES_START;
        }
        else return 0;
    }

}
