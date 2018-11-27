package ie.wit.cgd.bunnyhop.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ie.wit.cgd.bunnyhop.game.Assets;

public class GroundPipe extends AbstractGameObject{
	 private TextureRegion   regGroundPipe;

	    public GroundPipe() {
	        init();
	    }

	    private void init() {
	        dimension.set(0.5f, 0.5f);
	        regGroundPipe = Assets.instance.groundPipe.groundPipe;
	        // Set bounding box for collision detection
	        bounds.set(0, 0, dimension.x * 3, dimension.y * 11.5f);
	    }

	    public void render(SpriteBatch batch) {
	        TextureRegion reg = null;
	        reg = regGroundPipe;
	        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x * 3, dimension.y * 11.5f, scale.x,
	                scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
	                false, false);
	    }

}
