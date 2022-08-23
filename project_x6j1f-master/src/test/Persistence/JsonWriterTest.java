package Persistence;

import model.BackgroundFish;
import model.GameInTank;
import model.PlayerFish;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    private GameInTank game;

    @BeforeEach
    public void runBefore() {
        game = new GameInTank();
        PlayerFish player = new PlayerFish(10, 20);

        Set<BackgroundFish> prey = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            prey.add(new BackgroundFish(i, i, "right"));
        }

        Set<BackgroundFish> enemy = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            enemy.add(new BackgroundFish(i, i, "left"));
        }

        game.setPrey(prey);
        game.setEnemy(enemy);
        game.setPlayer(player);
    }
    @Test
    public void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/\0thisInvalidFile.json");
            writer.open();
            fail("FileNotFoundException was expected");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    public void testWriterEnemyToFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriteEnemy.json");
            writer.open();
            JSONObject json = new JSONObject();

            Set<BackgroundFish> enemy = new HashSet<>();

            for (int i = 1; i <= 5; i++) {
                enemy.add(new BackgroundFish(i, i, "left"));
            }

            json.put("Enemy", writer.enemyToJson(enemy));
            writer.saveToFile(json.toString());
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteEnemy.json");

            Set<BackgroundFish> previousEnemy = reader.getPreviousEnemy();
            assertEquals(5, previousEnemy.size());

            List<Integer> coordinate = new ArrayList<>();
            for (BackgroundFish fish : previousEnemy) {
                coordinate.add(fish.getX());
                assertEquals("left", fish.getDirection());
            }

            for (int i = 1; i <= 5; i++) {
                assertTrue(coordinate.contains(i));
            }

       } catch (IOException e) {
            fail("IOException was not expected");
        }
    }

    @Test
    public void testWriterPreyToFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWritePrey.json");
            writer.open();
            JSONObject json = new JSONObject();

            Set<BackgroundFish> prey = new HashSet<>();

            for (int i = 1; i <= 5; i++) {
                prey.add(new BackgroundFish(i, i, "right"));
            }

            json.put("Prey", writer.preyToJson(prey));
            writer.saveToFile(json.toString());
            writer.close();

            JsonReader reader = new JsonReader("./data/testWritePrey.json");

            Set<BackgroundFish> previousPrey = reader.getPreviousPrey();
            assertEquals(5, previousPrey.size());

            List<Integer> coordinate = new ArrayList<>();
            for (BackgroundFish fish : previousPrey) {
                coordinate.add(fish.getX());
                assertEquals("right", fish.getDirection());
            }

            for (int i = 1; i <= 5; i++) {
                assertTrue(coordinate.contains(i));
            }

        } catch (IOException e) {
            fail("IOException was not expected");
        }
    }

    @Test
    public void testWriterPlayerToFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWritePlayer.json");
            writer.open();
            JSONObject json = new JSONObject();

            PlayerFish player = new PlayerFish(10, 20);

            json.put("Player", writer.playerToJson(player));
            writer.saveToFile(json.toString());
            writer.close();

            JsonReader reader = new JsonReader("./data/testWritePlayer.json");

            assertNotNull(reader.getPreviousPlayer());
            assertEquals(10, reader.getPreviousPlayer().getX());
            assertEquals(20, reader.getPreviousPlayer().getY());
            assertEquals(PlayerFish.DEFAULT_DIRECTION, reader.getPreviousPlayer().getDirection());
        } catch (IOException e) {
            fail("IOException was not expected");
        }
    }

    @Test
    public void testWriteGameToFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriteNewFile.json");
            writer.open();

            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteNewFile.json");
            assertEquals(game.getPrey().size(), reader.getPreviousEnemy().size());
            assertEquals(game.getEnemy().size(), reader.getPreviousPrey().size());
            assertEquals(game.getScore(), reader.getPreviousScore());
            assertEquals(game.getDifficulty(), reader.getPreviousDifficulty());
            assertNotNull(reader.getPreviousPlayer());

        } catch (IOException e) {
            fail("IOException was not expected");
        }
    }

}
