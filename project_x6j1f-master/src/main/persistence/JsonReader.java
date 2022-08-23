package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import model.BackgroundFish;
import model.PlayerFish;
import org.json.*;

// Represents a reader that reads previous game characteristics
// citation: learned from JsonSerializationDemo provided in Class CPSC210
public class JsonReader {
    private String source;
    private JSONObject jsonObject;

    // EFFECTS: constructs reader to read from source file
    //          - get the jsonObject corresponding to the source file
    public JsonReader(String source) throws IOException {
        this.source = source;
        this.jsonObject = new JSONObject(readFile(this.source));
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS:  get the previous game difficulty and return it
    public int getPreviousDifficulty() throws IOException {
        int previousDifficulty = this.jsonObject.getInt("Difficulty");
        return previousDifficulty;
    }

    // EFFECTS:  get the previous game score and return it
    public int getPreviousScore() throws IOException {
        int previousScore = this.jsonObject.getInt("Score");
        return previousScore;
    }

    // EFFECTS:  get the previous game player and return it
    public PlayerFish getPreviousPlayer() throws IOException {
        JSONObject player = this.jsonObject.getJSONObject("Player");
        PlayerFish playerFish = new PlayerFish(player.getInt("x"), player.getInt("y"));
        playerFish.setDirection(player.getString("Direction"));
        return playerFish;
    }

    // EFFECTS:  get the previous game list of prey and return it
    public Set<BackgroundFish> getPreviousPrey() throws IOException {
        JSONArray preyList = this.jsonObject.getJSONArray("Prey");

        Set<BackgroundFish> preys = new HashSet<>();

        for (Object prey: preyList) {
            JSONObject jsonPrey = (JSONObject) prey;
            BackgroundFish preyToAdd = new BackgroundFish(jsonPrey.getInt("x"),
                    jsonPrey.getInt("y"),
                    jsonPrey.getString("Direction"));
            preys.add(preyToAdd);
        }
        return preys;
    }

    // EFFECTS:  get the previous game list of enemy and return it
    public Set<BackgroundFish> getPreviousEnemy() throws IOException {
        JSONArray enemyList = jsonObject.getJSONArray("Enemy");

        Set<BackgroundFish> enemies = new HashSet<>();

        for (Object enemy: enemyList) {
            JSONObject jsonEnemy = (JSONObject) enemy;
            BackgroundFish enemyToAdd = new BackgroundFish(jsonEnemy.getInt("x"),
                    jsonEnemy.getInt("y"),
                    jsonEnemy.getString("Direction"));
            enemies.add(enemyToAdd);
        }
        return enemies;
    }
}

