package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ScorePanel;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameInTankTest {
    private GameInTank game;
    private Coordinate coordinate1;
    private Coordinate coordinate2;
    private String facingUp;
    private String facingRight;
    private String facingDown;

    @BeforeEach
    void runBefore() {
        game = new GameInTank();
        coordinate1 = new Coordinate(GameInTank.WIDTH / 4, GameInTank.HEIGHT / 4);
        coordinate2 = new Coordinate(GameInTank.WIDTH / 2, GameInTank.HEIGHT / 4);
        facingUp = "up";
        facingRight = "right";
        facingDown = "down";
    }

    @Test
    void testGameInTank() {
        assertNotNull(game);
        assertFalse(game.getPause());
        assertFalse(game.getLoadingError());
        assertFalse(game.getNoMemory());
        assertTrue(game.showStartScreen());
        assertFalse(game.getSavedProgress());
        assertFalse(game.getEndGame());
        assertEquals(GameInTank.HEIGHT, game.getHeight());
        assertEquals(GameInTank.WIDTH, game.getWidth());
        assertNotNull(game.getPlayer());
        assertEquals(GameInTank.WIDTH / 2, game.getPlayer().getX());
        assertEquals(GameInTank.HEIGHT / 2, game.getPlayer().getY());
        assertEquals(PlayerFish.DEFAULT_DIRECTION, game.getPlayer().getDirection());
        assertEquals(0, game.getScore());
        assertEquals(1, game.getDifficulty());
        assertNotNull(game.getEnemy());
        assertNotNull(game.getPrey());
        assertEquals(0, game.getPrey().size());
        assertEquals(0, game.getEnemy().size());
        assertEquals(0, game.getNumTick());
    }

    @Test
    void testInitialization() {
        game.initialization();
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getEnemy().size());
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getPrey().size());
    }

    @Test
    void testGenerateRandomCoordinate() {
        assertNotNull(game.generateRandomCoordinate());
        assertEquals( Coordinate.class, game.generateRandomCoordinate().getClass());
    }

    @Test
    void testGenerateFish() {
        BackgroundFish newFish = game.generateFish(coordinate1);
        List<String> listDirections = new ArrayList<>();
        listDirections.add("up");
        listDirections.add("down");
        listDirections.add("right");
        listDirections.add("left");

        assertNotNull(newFish);
        assertEquals(coordinate1.getX(), newFish.getX());
        assertEquals(coordinate1.getY(), newFish.getY());
        assertEquals(String.class, newFish.getDirection().getClass());
        assertTrue(listDirections.contains(newFish.getDirection()));
    }

    @Test
    void testGenerateFacing() {
        String facing = game.generateFacing();
        List<String> listDirections = new ArrayList<>();
        listDirections.add("up");
        listDirections.add("down");
        listDirections.add("right");
        listDirections.add("left");

        assertNotNull(facing);
        assertTrue(listDirections.contains(facing));
    }

    @Test
    void testGenerateFacingNoRepeats() {
        List<String> listDirections = new ArrayList<>();
        listDirections.add("up");
        listDirections.add("down");
        listDirections.add("right");
        listDirections.add("left");

        for (int i = 0; i < listDirections.size(); i++) {
            assertNotEquals(listDirections.get(i), game.generateFacingNoRepeat(listDirections.get(i)));
            assertTrue(listDirections.contains(game.generateFacingNoRepeat(listDirections.get(i))));
        }
    }

    @Test
    void testCheckDifficulty() {
        game.initialization();

        assertEquals(1, game.getDifficulty());
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getEnemy().size());

        for (int i = GameInTank.LEVEL_UP_REQUIREMENT - 1; i < GameInTank.LEVEL_UP_REQUIREMENT + 1; i++) {
            game.setScore(i);
            game.checkDifficulty();
            assertEquals(i / GameInTank.LEVEL_UP_REQUIREMENT + 1, game.getDifficulty());
            assertEquals((i / GameInTank.LEVEL_UP_REQUIREMENT + 1) * GameInTank.INITIAL_FISH_NUMBER,
                    game.getEnemy().size());
        }

        game.setScore(5 * GameInTank.LEVEL_UP_REQUIREMENT);
        game.checkDifficulty();
        assertEquals(6, game.getDifficulty());
        assertEquals(6 * GameInTank.INITIAL_FISH_NUMBER, game.getEnemy().size());
    }

    @Test
    void testTickNoHitEnemy() {
        game.setScore(GameInTank.LEVEL_UP_REQUIREMENT - 1);

        for (int i = 1; i <= GameInTank.INITIAL_FISH_NUMBER; i++) {
            game.getPrey().add(new BackgroundFish(coordinate1.getX(), coordinate1.getY(), facingUp));
            game.getEnemy().add(new BackgroundFish(coordinate2.getX(), coordinate2.getY(), facingRight));
        }

        for (int i = 1; i <= 3; i++) {
            game.tick();

            assertEquals(GameInTank.WIDTH / 2 + i * PlayerFish.SPEED, game.getPlayer().getX());
            assertEquals(GameInTank.HEIGHT / 2, game.getPlayer().getY());

            for (BackgroundFish prey : game.getPrey()) {
                assertEquals(coordinate1.getX(), prey.getX());
                assertEquals(coordinate1.getY() - i * BackgroundFish.SPEED, prey.getY());
                assertEquals(facingUp, prey.getDirection());
            }
            for (BackgroundFish enemy : game.getEnemy()) {
                assertEquals(coordinate2.getX() + i * BackgroundFish.SPEED, enemy.getX());
                assertEquals(coordinate2.getY(), enemy.getY());
                assertEquals(facingRight, enemy.getDirection());
            }

            assertFalse(game.getEndGame());
            assertEquals(1, game.getDifficulty());
            assertEquals(i, game.getNumTick());
        }
    }

        @Test
        void testTickHitEnemy() {
            game.setScore(GameInTank.LEVEL_UP_REQUIREMENT - 1);
            game.getPlayer().setDirection(facingDown);
            game.getPlayer().setX(coordinate2.getX());
            game.getPlayer().setY(coordinate2.getY() - PlayerFish.SPEED);

            for (int i = 1; i <= GameInTank.INITIAL_FISH_NUMBER; i++) {
                game.getPrey().add(new BackgroundFish(coordinate1.getX(), coordinate1.getY(), facingUp));
                game.getEnemy().add(new BackgroundFish(coordinate2.getX(), coordinate2.getY(), facingUp));
            }

            game.tick();
            assertEquals(coordinate2.getY(), game.getPlayer().getY());

            assertTrue(game.playerHitEnemy());
            assertTrue(game.getEndGame());
            assertEquals(1, game.getDifficulty());
            assertEquals(0, game.getNumTick());
        }

    @Test
    void testCheckChangeFishListDirectionNoChange() {
        game.initialization();
        game.setNumTick(GameInTank.NUMBER_TICKS_CHANGE_DIRECTION - 1);

        List<String> preyOldFacing = new ArrayList<>();
        List<String> preyCurrentFacing = new ArrayList<>();
        List<String> enemyOldFacing = new ArrayList<>();
        List<String> enemyCurrentFacing = new ArrayList<>();

        for (BackgroundFish prey: game.getPrey()) {
            preyOldFacing.add(prey.getDirection());
        }

        for (BackgroundFish enemy: game.getEnemy()) {
            enemyOldFacing.add(enemy.getDirection());
        }

        game.checkChangeFishListDirection();

        for (BackgroundFish prey: game.getPrey()) {
            preyCurrentFacing.add(prey.getDirection());
        }

        for (BackgroundFish enemy: game.getEnemy()) {
            enemyCurrentFacing.add(enemy.getDirection());
        }

        for (int i = 0; i < GameInTank.INITIAL_FISH_NUMBER; i++) {
            assertEquals(preyOldFacing.get(i), preyCurrentFacing.get(i));
            assertEquals(enemyOldFacing.get(i), enemyCurrentFacing.get(i));
        }

        assertEquals(GameInTank.NUMBER_TICKS_CHANGE_DIRECTION - 1, game.getNumTick());
    }

    @Test
    void testCheckChangeFishListDirectionChange() {
        game.initialization();
        game.setNumTick(GameInTank.NUMBER_TICKS_CHANGE_DIRECTION);

        List<String> preyOldFacing = new ArrayList<>();
        List<String> preyCurrentFacing = new ArrayList<>();
        List<String> enemyOldFacing = new ArrayList<>();
        List<String> enemyCurrentFacing = new ArrayList<>();

        for (BackgroundFish prey: game.getPrey()) {
            preyOldFacing.add(prey.getDirection());
            }

        for (BackgroundFish enemy: game.getEnemy()) {
            enemyOldFacing.add(enemy.getDirection());
        }

        game.checkChangeFishListDirection();

        for (BackgroundFish prey: game.getPrey()) {
            preyCurrentFacing.add(prey.getDirection());
        }

        for (BackgroundFish enemy: game.getEnemy()) {
             enemyCurrentFacing.add(enemy.getDirection());
            }

        for (int i = 0; i < GameInTank.INITIAL_FISH_NUMBER; i++) {
            assertNotEquals(preyOldFacing.get(i), preyCurrentFacing.get(i));
            assertNotEquals(enemyOldFacing.get(i), enemyCurrentFacing.get(i));
        }

        assertEquals(0, game.getNumTick());
    }

    @Test
    void testSwimListFishNotHitBoundary() {
        Set<BackgroundFish> listOfFish = new HashSet<>();

        for (int i = 1; i <= GameInTank.INITIAL_FISH_NUMBER; i++) {
            listOfFish.add(new BackgroundFish(coordinate1.getX(),coordinate1.getY(), facingUp));
        }
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, listOfFish.size());

        game.swimListFish(listOfFish);
        for (BackgroundFish fish: listOfFish) {
            assertEquals(coordinate1.getX(), fish.getX());
            assertEquals(coordinate1.getY() - BackgroundFish.SPEED, fish.getY());
            assertEquals(facingUp, fish.getDirection());
            fish.setDirection(facingRight);
            assertEquals(facingRight, fish.getDirection());
        }

        game.swimListFish(listOfFish);
        for (BackgroundFish fish: listOfFish) {
            assertEquals(coordinate1.getX() + BackgroundFish.SPEED, fish.getX());
            assertEquals(coordinate1.getY() - BackgroundFish.SPEED, fish.getY());
            assertEquals(facingRight, fish.getDirection());
        }
    }

    @Test
    void testSwimListFishHitBoundary() {
        Set<BackgroundFish> listOfFish = new HashSet<>();
        BackgroundFish fishToHitBoundary = new BackgroundFish(GameInTank.WIDTH / 2, 2, facingUp);

        listOfFish.add(fishToHitBoundary);
        for (int i = 1; i < GameInTank.INITIAL_FISH_NUMBER; i++) {
            listOfFish.add(new BackgroundFish(coordinate1.getX(),coordinate1.getY(), facingUp));
        }
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, listOfFish.size());

        game.swimListFish(listOfFish);

        for (BackgroundFish fish: listOfFish) {
            if (Objects.equals(fish.getDirection(), facingUp)) {
                assertNotEquals(fishToHitBoundary, fish);
                assertEquals(coordinate1.getX(), fish.getX());
                assertEquals(coordinate1.getY() - BackgroundFish.SPEED, fish.getY());
            } else {
                assertEquals(ScorePanel.LBL_HEIGHT, fishToHitBoundary.getY());
                assertEquals(facingDown, fishToHitBoundary.getDirection());
            }
        }
    }

    @Test
    void testPlayerHitEnemyHitNone() {
        for (int i = 1; i <= GameInTank.INITIAL_FISH_NUMBER; i++) {
            game.getEnemy().add(game.generateFish(coordinate2));
        }
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getEnemy().size());

        assertFalse(game.playerHitEnemy());
    }

    @Test
    void testPlayerHitEnemyHit() {
        BackgroundFish enemyToHit = new BackgroundFish( GameInTank.WIDTH / 2,
                GameInTank.HEIGHT / 2, "up");
        game.getEnemy().add(enemyToHit);

        for (int i = 1; i < GameInTank.INITIAL_FISH_NUMBER; i++) {
            game.getEnemy().add(game.generateFish(coordinate2));
        }
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getEnemy().size());

        assertTrue(game.playerHitEnemy());
    }

    @Test
    void testCheckEatPreyNoneAte() {
        for (int i = 1; i <= GameInTank.INITIAL_FISH_NUMBER; i++) {
            game.getPrey().add(game.generateFish(coordinate1));
        }
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getPrey().size());
        assertEquals(0, game.getScore());

        Set<BackgroundFish> oldPreyList = game.getPrey();
        game.checkEatPrey();
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getPrey().size());
        assertEquals(oldPreyList, game.getPrey());
        assertEquals(0, game.getScore());
    }

    @Test
    void testCheckEatPreyAtePrey() {
        BackgroundFish preyToEat = new BackgroundFish( GameInTank.WIDTH / 2,
                GameInTank.HEIGHT / 2, "up");
        game.getPrey().add(preyToEat);

        for (int i = 1; i < GameInTank.INITIAL_FISH_NUMBER; i++) {
            game.getPrey().add(game.generateFish(coordinate1));
        }
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getPrey().size());

        game.checkEatPrey();
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getPrey().size());
        assertFalse(game.getPrey().contains(preyToEat));
        assertEquals(1, game.getScore());

        Set<BackgroundFish> oldPreyList = game.getPrey();
        game.checkEatPrey();
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getPrey().size());
        assertEquals(oldPreyList, game.getPrey());
        assertEquals(1, game.getScore());
    }

    @Test
    public void testSetDifficulty() {
        game.setDifficulty(4);
        assertEquals(4, game.getDifficulty());
    }

    @Test
    public void testSetPlayer() {
        PlayerFish player = new PlayerFish(10, 20);
        player.setDirection("up");

        game.setPlayer(player);
        assertEquals(10, game.getPlayer().getX());
        assertEquals(20, game.getPlayer().getY());
        assertEquals("up", player.getDirection());
    }

    @Test
    public void testSetEnemy() {
        Set<BackgroundFish> enemy = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            enemy.add(new BackgroundFish(i, i, "left"));
        }

        game.setEnemy(enemy);

        List<Integer> coordinate = new ArrayList<>();
        for (BackgroundFish fish : game.getEnemy()) {
            coordinate.add(fish.getX());
            assertEquals("left", fish.getDirection());
        }

        for (int i = 1; i <= 5; i++) {
            assertTrue(coordinate.contains(i));
        }
    }

    @Test
    public void testSetPrey() {
        Set<BackgroundFish> prey = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            prey.add(new BackgroundFish(i, i, "right"));
        }

        game.setPrey(prey);
        List<Integer> coordinate = new ArrayList<>();
        for (BackgroundFish fish : game.getPrey()) {
            coordinate.add(fish.getX());
            assertEquals("right", fish.getDirection());
        }

        for (int i = 1; i <= 5; i++) {
            assertTrue(coordinate.contains(i));
        }
    }

    @Test
    public void testIncreaseDifficulty() {
        for (int i = 1; i <= 3; i++) {
            game.increaseDifficulty();
            assertEquals(1 + i, game.getDifficulty());
            assertEquals(i * GameInTank.INITIAL_FISH_NUMBER, game.getScore());
        }
    }

    @Test
    public void testDecreaseDifficulty() {
        for (int i = 1; i <= 3; i++) {
            game.increaseDifficulty();
        }

        Set<BackgroundFish> enemies = new HashSet<>();
        for (int i = 1; i <= 4 * GameInTank.INITIAL_FISH_NUMBER; i++) {
            enemies.add(new BackgroundFish(50, 50, "up"));
        }
        game.setEnemy(enemies);

        for (int i = 1; i <= 3; i++) {
            game.decreaseDifficulty();
            assertEquals((4 - i) * GameInTank.INITIAL_FISH_NUMBER, game.getEnemy().size());
        }

        game.decreaseDifficulty();
        assertEquals(GameInTank.INITIAL_FISH_NUMBER, game.getEnemy().size());
    }

    @Test
    public void testDecreaseDifficultyDecreaseEnemies() {
        for (int i = 1; i <= 3; i++) {
            game.increaseDifficulty();
        }


    }
    @Test
    public void testPauseResumeGame() {
        assertFalse(game.getPause());

        game.setPause();
        assertTrue(game.getPause());

        game.resume();
        assertFalse(game.getPause());
    }

    @Test
    public void testSetNoMemoryError() {
        assertFalse(game.getNoMemory());
        game.setNoMemoryError();

        assertTrue(game.getNoMemory());
    }

    @Test
    public void testSetLoadingError() {
        assertFalse(game.getLoadingError());
        game.setLoadingError();

        assertTrue(game.getLoadingError());
    }

    @Test
    public void testSavedProgress() {
        assertFalse(game.getSavedProgress());
        game.savedProgress();

        assertTrue(game.getSavedProgress());

        game.unsavedProgress();
        assertFalse(game.getSavedProgress());
    }

    @Test
    public void testCloseStartScreen() {
        assertTrue(game.showStartScreen());
        game.closeStartScreen();

        assertFalse(game.showStartScreen());
    }
}
