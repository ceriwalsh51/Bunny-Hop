package ie.wit.cgd.bunnyhop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ie.wit.cgd.bunnyhop.game.objects.AbstractGameObject;
import ie.wit.cgd.bunnyhop.game.objects.BunnyHead;
import ie.wit.cgd.bunnyhop.game.objects.Clouds;
import ie.wit.cgd.bunnyhop.game.objects.Feather;
import ie.wit.cgd.bunnyhop.game.objects.Goal;
import ie.wit.cgd.bunnyhop.game.objects.GoldCoin;
import ie.wit.cgd.bunnyhop.game.objects.Mountains;
import ie.wit.cgd.bunnyhop.game.objects.Rock;
import ie.wit.cgd.bunnyhop.game.objects.WaterOverlay;
import ie.wit.cgd.bunnyhop.game.objects.AddLife;
import ie.wit.cgd.bunnyhop.game.objects.FullLife;
import ie.wit.cgd.bunnyhop.game.objects.Key;
import ie.wit.cgd.bunnyhop.game.objects.Treasure;
import ie.wit.cgd.bunnyhop.game.objects.GroundPipe;
import ie.wit.cgd.bunnyhop.game.objects.TopPipe;

public class Level {

	public static final String  TAG = Level.class.getName();

	public BunnyHead 			bunnyHead;
	public Array<GoldCoin> 		goldCoins;
	public Array<Feather> 		feathers;
	public Goal 				goal;
	public Array<AddLife> 		addLives;
	public Array<FullLife> 		fullLives;
	public Array<Key> 			keys;
	public Array<Treasure>		treasures;
	public Array<GroundPipe>	groundPipes;
	public Array<TopPipe>		topPipes;

	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0),                  	 // black
		ROCK(0, 255, 0),                 	 // green
		PLAYER_SPAWNPOINT(255, 255, 255),	 // white
		ITEM_FEATHER(255, 0, 255),       	 // purple
		ITEM_GOLD_COIN(255, 255, 0),     	 // yellow
		ITEM_GOAL(255, 0, 0),			 	 // red
		ITEM_ADDLIFE(0,0,255),			 	 // blue
		ITEM_FULLLIFE(255, 120, 0),		 	 // orange
		ITEM_KEY(0, 255, 255),				 // light blue
		ITEM_TREASURE(127, 87, 0),			 // brown
		ITEM_GROUNDPIPE(0, 120,0),			 // dark green
		ITEM_TOPPIPE(0, 125, 125); 			 // turquoise
		
		private int color;

		private BLOCK_TYPE(int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}

		public boolean sameColor(int color) {
			return this.color == color;
		}

		public int getColor() {
			return color;
		}
	}

	// objects
	public Array<Rock>  rocks;

	// decoration
	public Clouds       clouds;
	public Mountains    mountains;
	public WaterOverlay waterOverlay;

	public Level(String filename) {
		init(filename);
	}

	private void init(String filename) {

		//Player Character
		bunnyHead = null;
		goal = null;

		//Objects
		rocks = new Array<Rock>();   
		goldCoins = new Array<GoldCoin>();
		feathers = new Array<Feather>();
		addLives = new Array<AddLife>();
		fullLives = new Array<FullLife>();
		keys = new Array<Key>();
		treasures = new Array<Treasure>();
		groundPipes = new Array<GroundPipe>();
		topPipes = new Array<TopPipe>();

		// load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));

		// scan pixels from top-left to bottom-right
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				// height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;
				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				// find matching color value to identify block type at (x,y)
				// point and create the corresponding game object if there is
				// a match
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {                         // empty space
					// do nothing
				} else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {                   // rock
					obj = new Rock();
					offsetHeight = -3.9f;//-3.9
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					rocks.add((Rock) obj);					
				} else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {          // player spawn point
					obj = new BunnyHead();
					offsetHeight = 0.0f;//0
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					bunnyHead = (BunnyHead)obj;
				} else if(BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {                // feather
					obj = new Feather();
					offsetHeight = 0.0f;//-0.5
					obj.position.set(pixelX,baseHeight * obj.dimension.y+ offsetHeight);
					feathers.add((Feather)obj);
				} else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {             // gold coin
					obj = new GoldCoin();
					offsetHeight = 0.7f;//0.5
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					goldCoins.add((GoldCoin)obj);         
				} else if (BLOCK_TYPE.ITEM_GOAL.sameColor(currentPixel)) {					// goal
					obj = new Goal();
					offsetHeight = 5.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					goal = (Goal)obj;
				}else if(BLOCK_TYPE.ITEM_ADDLIFE.sameColor(currentPixel)) {				 	// add life
					obj = new AddLife();
					offsetHeight = 1.0f;//0.5
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					addLives.add((AddLife)obj);
				}else if(BLOCK_TYPE.ITEM_FULLLIFE.sameColor(currentPixel)) {				// full life
					obj = new FullLife();
					offsetHeight = 1.0f;//0.5
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					fullLives.add((FullLife)obj);
				} else if (BLOCK_TYPE.ITEM_KEY.sameColor(currentPixel)) {					// key
					obj = new Key();
					offsetHeight = 1.0f;//-0.5
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					keys.add((Key)obj);
				} else if (BLOCK_TYPE.ITEM_TREASURE.sameColor(currentPixel)) {             // treasure chest
					obj = new Treasure();
					offsetHeight = 0.5f;//0.5
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					treasures.add((Treasure)obj);  
				} else if (BLOCK_TYPE.ITEM_GROUNDPIPE.sameColor(currentPixel)) {             // treasure chest
					obj = new GroundPipe();
					offsetHeight = -1.5f;//0.5
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					groundPipes.add((GroundPipe)obj);  
				} else if (BLOCK_TYPE.ITEM_TOPPIPE.sameColor(currentPixel)) {             // treasure chest
					obj = new TopPipe();
					offsetHeight = 2.5f;//0.5
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					topPipes.add((TopPipe)obj);  
				} else {                                                                // unknown object/pixel color
					int r = 0xff & (currentPixel >>> 24); // red color channel
					int g = 0xff & (currentPixel >>> 16); // green color channel
					int b = 0xff & (currentPixel >>> 8);  // blue color channel
					int a = 0xff & currentPixel;          // alpha channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g
							+ "> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}

		// decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1, -1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0, -3.75f);

		// free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}

	public void render(SpriteBatch batch) {
		mountains.render(batch);                        // Draw Mountains
		for (Rock rock : rocks)                         // Draw Rocks
			rock.render(batch);                             
		for (GoldCoin goldCoin : goldCoins)             // Draw Gold Coins
			goldCoin.render(batch);             
		for (Feather feather : feathers)                // Draw Feathers
			feather.render(batch);  
		for (Key key : keys)							// Draw Keys
			key.render(batch);
		for (AddLife addLife : addLives)				//Draw AddLife
			addLife.render(batch);
		for (FullLife fullLife : fullLives)				//Draw FullLife
			fullLife.render(batch);
		for (Key key : keys)							// Draw Keys
			key.render(batch);
		for (Treasure treasure : treasures)				// Draw Treasure Chests
			treasure.render(batch);
		for(GroundPipe groundPipe : groundPipes)		// Draw GroundPipes
			groundPipe.render(batch);
		for(TopPipe topPipe : topPipes)					// Draw TopPipes
			topPipe.render(batch);
		bunnyHead.render(batch);                        // Draw Player Character
		waterOverlay.render(batch);                     // Draw Water Overlay
		clouds.render(batch);                           // Draw Clouds
		goal.render(batch);
	}
	
	public void update(float deltaTime) {
        bunnyHead.update(deltaTime);
        for (Rock rock : rocks)
            rock.update(deltaTime);
        for (GoldCoin goldCoin : goldCoins)
            goldCoin.update(deltaTime);
        for (Feather feather : feathers)
            feather.update(deltaTime);
        for (AddLife addLife : addLives)
            addLife.update(deltaTime);
        for (FullLife fullLife : fullLives)
        	fullLife.update(deltaTime);
        for (Key key : keys)
        	key.update(deltaTime);
        for (Treasure treasure : treasures)
        	treasure.update(deltaTime);
        for (GroundPipe groundPipe : groundPipes)
        	groundPipe.update(deltaTime);
        for (TopPipe topPipe : topPipes)
        	topPipe.update(deltaTime);
        clouds.update(deltaTime);
        goal.update(deltaTime);
    }
}
