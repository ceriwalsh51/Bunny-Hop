package ie.wit.cgd.bunnyhop.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;

import ie.wit.cgd.bunnyhop.game.objects.BunnyHead;
import ie.wit.cgd.bunnyhop.game.objects.BunnyHead.JUMP_STATE;
import ie.wit.cgd.bunnyhop.game.objects.Feather;
import ie.wit.cgd.bunnyhop.game.objects.FullLife;
import ie.wit.cgd.bunnyhop.game.objects.Goal;
import ie.wit.cgd.bunnyhop.game.objects.GoldCoin;
import ie.wit.cgd.bunnyhop.game.objects.Rock;
import ie.wit.cgd.bunnyhop.game.objects.Treasure;
import ie.wit.cgd.bunnyhop.util.CameraHelper;
import ie.wit.cgd.bunnyhop.game.objects.Rock;
import ie.wit.cgd.bunnyhop.game.objects.AddLife;
import ie.wit.cgd.bunnyhop.game.objects.Key;
import ie.wit.cgd.bunnyhop.game.objects.Treasure;
import ie.wit.cgd.bunnyhop.game.objects.GroundPipe;
import ie.wit.cgd.bunnyhop.game.objects.TopPipe;


import ie.wit.cgd.bunnyhop.util.Constants;

public class WorldController extends InputAdapter{

	private static final String TAG = WorldController.class.getName();

	public CameraHelper			cameraHelper;
	public Level    			level;
	public static int      		lives;
	public int      			score;
	private Rectangle r1 = 		new Rectangle();
	private Rectangle r2 = 		new Rectangle();
	private float				timeLeftGameOverDelay;
	public int					levelNum = 1;
	public float				timer = 0;
	public float 				timeLeftKeyMessage;
	public float 				timeLeftCoinMessage;
	public static boolean 		flappy = false;
	public boolean				needKey;
	public boolean				needCoin;

	private void initLevel() {
		needCoin =false;
		needKey = false;
		score = 0;
		level = new Level(String.format("levels/level-%02d.png",  levelNum));		
		cameraHelper.setTarget(level.bunnyHead);
		timer = Constants.TIMER_DURATION;
		
		if(levelNum == 6) {
			flappy = true;			
		}
		
	}


	public WorldController() {
		init();
	}


	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		timeLeftKeyMessage = 0;
		timeLeftCoinMessage = 0;
		initLevel();
		cameraHelper.setTarget(level.bunnyHead);


	}

	public void update(float deltaTime) {
		handleDebugInput(deltaTime);

		//Press 1-6 to reach each level separately
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) { 
			levelNum = 1;
			initLevel();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) { 
			levelNum = 2;
			initLevel();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) { 
			levelNum = 3;
			initLevel();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) { 
			levelNum = 4;
			initLevel();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) { 
			levelNum = 5;
			initLevel();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) { 
			levelNum = 6;
			initLevel();
		}


		if (isGameOver()) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0) init();
		} else {
			handleInputGame(deltaTime);
		}
		if (isGameWon()) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0) init();
		} else {
			handleInputGame(deltaTime);
		}

		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) {
			lives--;
			if (isGameOver()) timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
		
		//timers to display message to collect key/more coins
		if (timeLeftKeyMessage > 0) {
			timeLeftKeyMessage -= deltaTime;
			if (timeLeftKeyMessage < 0) {
				// disable power-up
				timeLeftKeyMessage = 0;
				setNeedKey(false);
			}
		}
		if(timeLeftCoinMessage > 0) {
			timeLeftCoinMessage -= deltaTime;
			if (timeLeftCoinMessage < 0) {
				// disable power-up
				timeLeftCoinMessage = 0;
				lives--;
				initLevel();
				
			}
		}
		
		//timer for level
		if(levelNum == 4){
			if (timer > 0) {
				timer -= deltaTime;
				if (timer < 0) {
					//lose life
					lives--;
					if (isGameOver()) timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
					else
						initLevel();
				}
			}
		}

		level.update(deltaTime);
		testCollisions();
	}

	private void handleDebugInput (float deltaTime) {

		if (Gdx.app.getType() != ApplicationType.Desktop) return;

		// Camera Controls (move)
		if (!cameraHelper.hasTarget(level.bunnyHead)) { 

			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);
		} 

		//Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed); //changed to zoomSpeed - was MoveSpeed
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);//""
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);

	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	private void handleInputGame(float deltaTime) {

		if (cameraHelper.hasTarget(level.bunnyHead)) {

			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			} else {
				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop) {
					level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
				}
			}

			// Bunny Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				level.bunnyHead.setJumping(true);
			} else {
				level.bunnyHead.setJumping(false);
			}
		}
	}

	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Keys.R) {                            // Reset game world
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		} else if (keycode == Keys.ENTER) {                 // Toggle camera follow
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.bunnyHead);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		return false;
	}

	private void onCollisionBunnyHeadWithRock(Rock rock) {
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f) {
			boolean hitLeftEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitLeftEdge) {
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else {
				bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
			}
			return;
		}

		switch (bunnyHead.jumpState) {
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
			break;
		}
	}

	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) {
		goldcoin.collected = true;
		score += goldcoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	};

	private void onCollisionBunnyWithTreasure(Treasure treasure) {
		if(Key.collected == true){
			treasure.collected = true;
			Key.collected = false;
			score += treasure.getScore();
			Gdx.app.log(TAG, "Treasure Chest collected");
		} else {
			setNeedKey(true);

		}
	};

	private void onCollisionBunnyWithFeather(Feather feather) {
		feather.collected = true;
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	};

	private void onCollisionBunnyWithKey(Key key){
		Key.collected = true;
		Gdx.app.log(TAG, "Key collected");
	};

	private void onCollisionBunnyWithGoal(Goal goal) {
		if(levelNum == 5){
			if(score>= Constants.MIN_SCORE){
				Goal.collected = true;
				Gdx.app.log(TAG, "Goal collected");
				if(levelNum < 6)
					levelNum++;

			} else {
				setNeedCoin(true);

			}
		} else {
			Goal.collected = true;
			Gdx.app.log(TAG, "Goal collected");
			if(levelNum < 6)
				levelNum++;
		}

	};

	private void onCollisionBunnyWithAddLife(AddLife addLife) {
		addLife.collected = true;
		lives += addLife.getLife();
		Gdx.app.log(TAG, "AddLife collected");
	};

	private void onCollisionBunnyWithFullLife(FullLife fullLife) {
		fullLife.collected = true;
		lives = Constants.LIVES_START;
		Gdx.app.log(TAG, "AddLife collected");
	};

	private void onCollisionBunnyWithGroundPipe(GroundPipe groundPipe) {
		lives--;		
		initLevel();
		Gdx.app.log(TAG, "GroundPipe Hit");
	};

	private void onCollisionBunnyWithTopPipe(TopPipe topPipe) {
		lives--;
		initLevel();
		Gdx.app.log(TAG, "TopPipe Hit");
	};

	private void testCollisions() {
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width,
				level.bunnyHead.bounds.height);

		// Test collision: Bunny Head <-> Rocks
		for (Rock rock : level.rocks) {
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyHeadWithRock(rock);
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks.
		}

		// Test collision: Bunny Head <-> Gold Coins
		for (GoldCoin goldCoin : level.goldCoins) {
			if (goldCoin.collected) continue;
			r2.set(goldCoin.position.x, goldCoin.position.y, goldCoin.bounds.width, goldCoin.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithGoldCoin(goldCoin);
			break;
		}

		//Test collision: Bunny Head <-> Treasure
		for (Treasure treasure : level.treasures) {
			if (treasure.collected) continue;
			r2.set(treasure.position.x, treasure.position.y, treasure.bounds.width, treasure.bounds.height);
			if (!r1.overlaps(r2)) continue;	
			onCollisionBunnyWithTreasure(treasure);			
			break;
		}

		// Test collision: Bunny Head <-> Feathers
		for (Feather feather : level.feathers) {
			if (feather.collected) continue;
			r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithFeather(feather);
			break;
		}

		//Test collision: Bunny Head <-> Keys
		for(Key key : level.keys){
			if(Key.collected) continue;
			r2.set(key.position.x, key.position.y, key.bounds.width, key.bounds.height);
			if(!r1.overlaps(r2)) continue;
			onCollisionBunnyWithKey(key);
			break;
		}

		//Test collision: Bunny Head <-> AddLives
		for(AddLife addLife : level.addLives) {
			if(addLife.collected) continue;
			r2.set(addLife.position.x, addLife.position.y, addLife.bounds.width, addLife.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithAddLife(addLife);
			break;
		}

		//Test collision: Bunny Head <-> FullLives
		for(FullLife fullLife : level.fullLives) {
			if(fullLife.collected) continue;
			r2.set(fullLife.position.x, fullLife.position.y, fullLife.bounds.width, fullLife.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithFullLife(fullLife);
			break;
		}

		//Test collision: BunnyHead <-> GroundPipe
		for(GroundPipe groundPipe : level.groundPipes) {
			r2.set(groundPipe.position.x, groundPipe.position.y, groundPipe.bounds.width, groundPipe.bounds.height);
			if(!r1.overlaps(r2)) continue;
			onCollisionBunnyWithGroundPipe(groundPipe);
			break;
		}

		//Test collision: BunnyHead <-> TopPipe
		for(TopPipe topPipe : level.topPipes) {
			r2.set(topPipe.position.x, topPipe.position.y, topPipe.bounds.width, topPipe.bounds.height);
			if(!r1.overlaps(r2)) continue;
			onCollisionBunnyWithTopPipe(topPipe);
			break;
		}

		//Test collision: Bunny Head<-> Goal
		Goal goal = level.goal;
		if(!Goal.collected) 
			r2.set(goal.position.x, goal.position.y, goal.bounds.width, goal.bounds.height);
		if(r1.overlaps(r2))
			onCollisionBunnyWithGoal(goal);
	}

	public boolean isGameOver() {
		return lives <= 0;
	}

	public boolean isGameWon() {
		if( Goal.collected) {
			return true;
		}
		else
			return false;
	}
	
	public boolean isPlayerInWater() {
		return level.bunnyHead.position.y < -5;
	}	

	public void setNeedKey(boolean pickedUp) {
		needKey = pickedUp;
		if (pickedUp) {
			timeLeftKeyMessage = Constants.TIME_DELAY_MESSAGE;
		}
	}

	public boolean needKey() {
		return needKey && timeLeftKeyMessage > 0;
	}

	public void setNeedCoin(boolean pickedUp) {
		needCoin = pickedUp;
		if (pickedUp) {
			timeLeftCoinMessage = Constants.TIME_DELAY_MESSAGE;
		}
	}

	public boolean needCoin() {
		return needCoin && timeLeftCoinMessage > 0;
	}

}