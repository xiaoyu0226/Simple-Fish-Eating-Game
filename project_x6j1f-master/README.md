>## WaterTankSurvivalGame
>
>### Game Description
> This is a simple video game simulating an **exciting survival situation** in a fish tank.
> As a player, you are confined in a 2-dimensional fish tank and there is no way out!! 
> You have to survive in the tank and the only food you can consume are the preys.
> ***BUT BE CAREFUL!*** There are other carnivore fish around, and you have to swim around them to avoid being consumed.
> 
> Although this is a survival game, but it contains **no** violence and horror elements. 
> There is no age constriction on the player and this game is open to anyone who need some relaxing without being bord
> during their leisure time. 
> 
> ### Game Idea and Inspiration
> 
> This game is inspired by other games with survival and chasing elements. Although this is a simple version
> survival game, this can act as a simple base chase survival game that I want to possible
> improve in the future. Future considerations including improving the graphics of the game, add different types
> of enemy fish (each with their special swimming and spawning behaviours), 
> add some obstacles in the screen and the ultimate 
> goal is to let the enemy have some degree of **ACTIVE CHASING** of the player. 
>
> list of future game elements I want to learn (some maybe beyond this course):
> - Increase game graphics
> - Linking to database and achieve memory storage for multiple players
> - Game learn player behaviour
> - Game Increase Difficulty In a way that is player specific 


> ### User Stories
1. As a user, I want to play a game with increasing difficulty base on my score.
2. As a user, I want to increment and add new enemy in tank in game with increasing game difficulty. 
3. As a user, I want to be able to chase a prey and consume a prey.
4. As a user, I want to increment score with every prey I consumed. 
5. As a user, I want to refill and add new prey with random location and 
direction of moving to Game in tank whenever I consume a prey.
6. As a user, I want to use arrow key to change the direction of me moving.
7. As a user, I want to stay inside the allowed tank/area in game.
8. As a user, I want to distinguish myself, the enemy and the prey with different colour. 
9. As a user, I want to see my score and game difficulty displayed on screen.
10. As a user, I want to game over when I hit an enemy. 
11. As a user, I want to be able to quit and exit game whenever game over. 
12. As a user, I want to see the enemy and prey fish on the screen to change direction occasionally.
13. As a user, before the game start, I want to be prompted to load my previous game progress if it is 
present in memory and also have the option to start a new game without overriding my previously saved game progress. 
14. As a user, I want to be notified if I do not have any game progress in memory when I try to load it.
15. As a user, I want to be able to be prompted to save my game progress after game ends.
otherwise, I can choose to close the game without saving/overriding my game progress in memory.
16. As a user, I want to be notified on screen when I saved my game progress.
17. As a user, I want to be able to only regenerate the single enemy I previously hit into a new enemy when
I load and have other game characteristics stay the same. 
18. As a user, I want to be able to pause the game during play and resume if I want to.
19. As a user, I want to be able to increase game difficulty with
       increased number of enemy and be granted with extra score during game pause.
20. As a user, I want to be able to decrease game difficulty with
       decreased number of enemy and be punished with reduced score during game pause.
21. As a user, I want to be able to pause game and save&exit game when I do not want to play.
22. As a user, I want to be able to pause game and load previously saved progress if I want to.
23. As a user, I want to be able to see my game event log after I quit or save and quit the game.

> ### Phase 4: Task 2
> Thu Mar 31 22:52:06 PDT 2022  
> previous difficulty loaded with previous player and 9 enemy and 5 prey added to game  
> Thu Mar 31 22:52:06 PDT 2022  
> Difficulty level up! Increase 5 enemies to game.   
> Thu Mar 31 22:52:18 PDT 2022   
> You consumed a prey! A new prey added to game.   
> Thu Mar 31 22:52:21 PDT 2022   
> Decreased difficulty by 1, remove 5 enemy from the game.   
> Thu Mar 31 22:52:23 PDT 2022   
> Increased difficulty by 1, add 5 more enemy to game.   
> Thu Mar 31 22:52:27 PDT 2022   
> Decreased difficulty by 1, remove 5 enemy from the game.   
> Thu Mar 31 22:52:33 PDT 2022   
> You consumed a prey! A new prey added to game.   
> Thu Mar 31 22:52:33 PDT 2022   
> You consumed a prey! A new prey added to game.   
> Thu Mar 31 22:52:40 PDT 2022   
> You consumed a prey! A new prey added to game.   
> Thu Mar 31 22:52:46 PDT 2022     
> You consumed a prey! A new prey added to game.   
> Thu Mar 31 22:52:54 PDT 2022   
> You consumed a prey! A new prey added to game.   
> Thu Mar 31 22:52:54 PDT 2022   
> Difficulty level up! Increase 5 enemies to game.      
> Thu Mar 31 22:53:01 PDT 2022   
> Decreased difficulty by 1, remove 5 enemy from the game.   
> Thu Mar 31 22:53:03 PDT 2022   
> previous difficulty loaded with previous player and 9 enemy and 5 prey added to game   
> Thu Mar 31 22:53:05 PDT 2022   
> Difficulty level up! Increase 5 enemies to game.   
> 
  ### Phase 4: Task 3
- Make Fish into an abstract class, swim and handleHitTankEdge presented in both BackgroundFish Class and PlayerFish
class can be implemented once in the Fish class, this will remove duplication.
- Add association between Coordinate Class, BackgroundFish class and PlayerFish class. Refactor out the code in
both BackgroundFish and PlayerFish that are related to coordinate and coordinate changing. 
- Add association between Coordinate Class and GameInTank class, refactor part of the code in GameInTank class
that are related to generate random coordinate to Coordinate Class. 
- BackgroundFish can also access generates new coordinate functionality when to replace ate prey with different
coordinate. 
- generateFacing and generateFacingNoRepeat method in GameInTank class has many duplicate copied code, we need to 
improve coupling, hence further refactor of the code with duplication in the above methods is needed. 
- Duplication in disableStartScreenButton and disablePauseScreenButton method in GamePanel class, can remove duplication
by refactoring and possibly taking buttons as a list to improve coupling. With the refactored implementation,
loops can be used to make each button unfocused and removed. 
- Duplication in drawPreyFish and drawEnemyFish method in GamePanel Class, can refactor duplicated implementation
to improve coupling.
- Duplication in drawPauseScreenButtons and drawStartScreenButtons, need to refactor duplicate implementation and 
possibly using a list for start screen buttons and pause screen buttons to use loop to draw list of buttons. 
- Create a new class call ButtonEvents, refactor out all the button event implementation into this new class, 
GamePanel remains as a class mainly as a container for button and game screen, and we add association between
ButtonEvents class and GamePanel class. 
- Break association between GamePanel and JsonReader and JsonWriter, add association between JsonReader and JsonWriter
with new ButtonEvents Class, since this class will implementation for button triggered loading and saving. 
- Big portion of GameInTank class related to score and difficulty. Refactor out the portion of the GameInTank code that
is related to keeping track of score and difficulty, and make a new class GameLevelTracker that is responsible for 
functionality related to keep score, check difficulty level, level up and decrease difficulty as well as setting
difficulty and score. 
- Further, refactor portion of GameInTank class working with enemy fish and prey fish into a new class
called GameBackgroundFishTracker that has the functionality to monitor and update all the background fish. Now,
GameInTank will have association with PlayerFish, GameLevelTracker and GameBackgroundFishTracker, and these 
three classes together ensure the functionality of GameInTank class. 