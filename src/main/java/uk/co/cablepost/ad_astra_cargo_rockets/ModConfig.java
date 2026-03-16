package uk.co.cablepost.ad_astra_cargo_rockets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "ad_astra_cargo_rockets.json");

    public static ConfigData INSTANCE = new ConfigData();

    public static class ConfigData {
        public Map<String, Integer> validDestinations = new HashMap<>();
        public Map<String, Double> fuels = new HashMap<>();

        public ConfigData() {
            // Default Destinations
            validDestinations.put("minecraft:overworld", 0);
            validDestinations.put("ad_astra:earth_orbit", 1);
            validDestinations.put("ad_astra:moon", 1);
            validDestinations.put("ad_astra:moon_orbit", 1);
            validDestinations.put("ad_astra:mars", 2);
            validDestinations.put("ad_astra:mars_orbit", 2);
            validDestinations.put("ad_astra:mercury", 3);
            validDestinations.put("ad_astra:mercury_orbit", 3);
            validDestinations.put("ad_astra:venus", 3);
            validDestinations.put("ad_astra:venus_orbit", 3);
            validDestinations.put("ad_astra:glacio", 4);
            validDestinations.put("ad_astra:glacio_orbit", 4);

            // Default Fuels (Fuel ID -> Performance Multiplier)
            // Higher value means less fuel is required to launch.
            fuels.put("ad_astra:fuel", 1.0);
            fuels.put("ad_astra:cryo_fuel", 3.0);
        }
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, ConfigData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
