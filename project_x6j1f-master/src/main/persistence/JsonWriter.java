package persistence;

import model.BackgroundFish;
import model.GameInTank;
import model.PlayerFish;
import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Set;

// Represents a writer that save previous game characteristics
// citation: learned from JsonSerializationDemo provided in Class CPSC210
public class JsonWriter {
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(GameInTank game) {
        JSONObject json = new JSONObject();

        json.put("Difficulty", game.getDifficulty());
        json.put("Score", game.getScore());
        json.put("Player", playerToJson(game.getPlayer()));
        json.put("Enemy", enemyToJson(game.getEnemy()));
        json.put("Prey", preyToJson(game.getPrey()));
        saveToFile(json.toString());
    }

    // MODIFIES: this
    // EFFECTS: turn the set of enemy fish into JSON array and return it
    public JSONArray enemyToJson(Set<BackgroundFish> enemy) {
        JSONArray jsonArray = new JSONArray();

        for (BackgroundFish enemyFish: enemy) {
            JSONObject fish = new JSONObject();
            fish.put("Direction", enemyFish.getDirection());
            fish.put("x", enemyFish.getX());
            fish.put("y", enemyFish.getY());
            jsonArray.put(fish);
        }
        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: turn the set of prey fish into JSON array and return it
    public JSONArray preyToJson(Set<BackgroundFish> prey) {
        JSONArray jsonArray = new JSONArray();

        for (BackgroundFish preyFish: prey) {
            JSONObject fish = new JSONObject();
            fish.put("Direction", preyFish.getDirection());
            fish.put("x", preyFish.getX());
            fish.put("y", preyFish.getY());
            jsonArray.put(fish);
        }
        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: turn the player fish into JSON object and return it
    public JSONObject playerToJson(PlayerFish player) {
        JSONObject json = new JSONObject();

        json.put("Direction", player.getDirection());
        json.put("x", player.getX());
        json.put("y", player.getY());

        return json;
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    public void saveToFile(String json) {
        writer.print(json);
    }
}

