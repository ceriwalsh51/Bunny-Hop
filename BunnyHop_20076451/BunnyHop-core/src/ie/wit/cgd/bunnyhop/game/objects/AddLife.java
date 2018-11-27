package ie.wit.cgd.bunnyhop.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ie.wit.cgd.bunnyhop.game.Assets;
import ie.wit.cgd.bunnyhop.util.Constants;
import ie.wit.cgd.bunnyhop.game.WorldController;

public class AddLife extends AbstractGameObject {
    private TextureRegion   regAddLife;
    public boolean          collected;

    public AddLife() {
        init();
    }

    private void init() {
        dimension.set(0.5f, 0.5f);
        regAddLife = Assets.instance.addLife.addLife;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    public void render(SpriteBatch batch) {
        if (collected) return;

        TextureRegion reg = null;
        reg = regAddLife;
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
                scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
                false, false);
    }

    public int getLife() {
        if(WorldController.lives < Constants.LIVES_START){
        	return 1;
        }
        else return 0;
    }
}