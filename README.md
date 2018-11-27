BUNNYHOP IMPLEMENTATION
CERIANNE WALSH		20076451		CONSOLE GAME DEV
LEVEL-01 : BASIC BUNNYHOP GAME

LEVEL-02 : ADD LIVES
AddLife 		
- when collected
- checks if lives is less than Constant.LIVES_START
- if lives < LIVES_START : returns 1 and adds it to lives
- else return 0;

FullLife
- when collected, checks if lives is less than Constant.LIVES_START
- if lives < LIVES_START : sets lives to Constant.LIVES_START
- else return 0;
- is hidden high above the chain of coins
- can only reach with feather powerup

LEVEL-03 : KEY AND TREASURE CHEST
Key
- Key.collected == true
- Key GUI appears on screen

Treasure
- Can only be collected if key is collected
- If Key.collected != true, player will pass through the asset and a  
message comes up informing player that you must have the key to collect the treasure chest
- When collected: Key.collected is set to false and score goes up three times GoldCoin's score

LEVEL-04 : TIMER
Timer			
- when levelNum == 4	
- set timer to 50	
- when timer < 0 player loses life and initLevel()
			
LEVEL-05 : MINIMUM SCORE NEEDED
- When levelNum == 5	
- if score < = 325 set boolean needCoin to true

Goal
- if needCoin == true, collision doesn't occur
- message is displayed saying to collect more coins
- initLevel()
- else collision does occur

LEVEL-06 : FLAPPY BIRD BONUS LEVEL
- When levelNum == 6, set flappy to true and don't include timer like feather powerup
so it lasts the whole level

Pipes
- instead of rocks there are pipes on either end 

GoldCoin and AddLife
- gold coins and addLife hearts placed randomly between the pipes