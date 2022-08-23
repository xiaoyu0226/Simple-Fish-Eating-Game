package Persistence;

import model.BackgroundFish;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonReaderTest {
    @Test
    public void testJsonReaderFileNotExist() {
        try {
            JsonReader reader = new JsonReader("./data/noFileFound.json");
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testJsonReaderEmptyFile() {
        try {
            JsonReader reader = new JsonReader("./data/testReadEmptyFile.json");
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testReaderLoadScoreNoException() {
        try {
            JsonReader reader = new JsonReader("./data/testLoadFileNoException.json");
            assertEquals(0, reader.getPreviousScore());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderLoadDifficultyNoException() {
        try {
            JsonReader reader = new JsonReader("./data/testLoadFileNoException.json");
            assertEquals(1, reader.getPreviousDifficulty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderLoadPlayerNoException() {
        try {
            JsonReader reader = new JsonReader("./data/testLoadFileNoException.json");
            assertNotNull(reader.getPreviousPlayer());
            assertEquals(20, reader.getPreviousPlayer().getX());
            assertEquals(10, reader.getPreviousPlayer().getY());
            assertEquals("right", reader.getPreviousPlayer().getDirection());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderLoadPreyNoException() {
        try {
            JsonReader reader = new JsonReader("./data/testLoadFileNoException.json");
            assertEquals(5, reader.getPreviousPrey().size());

            List<Integer> index = new ArrayList<>();

            Set<BackgroundFish> listOfPrey = reader.getPreviousPrey();

            for (BackgroundFish prey: listOfPrey) {
                if (prey.getX() == prey.getY()) {
                    index.add(prey.getX());
                }
            }
            assertEquals(5, index.size());

            for (int i = 1; i <= 5; i++) {
                assertTrue(index.contains(i));
            }

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderLoadEnemyNoException() {
        try {
            JsonReader reader = new JsonReader("./data/testLoadFileNoException.json");
            assertEquals(5, reader.getPreviousEnemy().size());

            List<Integer> index = new ArrayList<>();

            Set<BackgroundFish> listOfEnemy = reader.getPreviousEnemy();

            for (BackgroundFish enemy: listOfEnemy) {
                if (enemy.getX() == enemy.getY()) {
                    assertTrue(index.add(enemy.getX()));
                }
            }
            assertEquals(5, index.size());

            for (int i = 1; i <= 5; i++) {
                assertTrue(index.contains(i));
            }

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
