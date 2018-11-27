package ie.wit.cgd.bunnyhop.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ie.wit.cgd.bunnyhop.game.Assets;

public class Key extends AbstractGameObject{

	private TextureRegion   		regKey;
	public static boolean           collected;

	public Key() {
		init();
	}

	private void init() {
		dimension.set(0.5f, 0.5f);
		regKey = Assets.instance.key.key;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}

	public void render(SpriteBatch batch) {
		if (collected) return;
		TextureRegion reg = null;
		reg = regKey;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
				scale.y*2, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
				false, false);
	}
}
