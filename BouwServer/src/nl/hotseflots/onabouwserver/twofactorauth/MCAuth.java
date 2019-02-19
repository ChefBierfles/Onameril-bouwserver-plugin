package nl.hotseflots.onabouwserver.twofactorauth;

import nl.hotseflots.onabouwserver.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class MCAuth {

    public static HashMap<UUID, AuthenticationDetails> loadedAuthenticationDetails;

    public static void dataGenerator()
    {
        Main.plugin.saveDefaultConfig();
        File dataDir = new File(Main.plugin.getDataFolder() + File.separator + "data");
        if (!dataDir.isDirectory()) {
            dataDir.mkdir();
        }
    }

    public static void attemptDataLoad(UUID uuid)
    {
        File userPath = new File(Main.plugin.getDataFolder() + File.separator + "data" + File.separator + uuid.toString() + ".yml");
        if (!userPath.exists()) {
            return;
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(userPath);
        String name = yaml.getString("name");
        String key = yaml.getString("key");
        loadedAuthenticationDetails.put(uuid, new AuthenticationDetails(name, key, false));
    }

    public static void addAuthenticationDetauls(UUID uuid, AuthenticationDetails authenticationDetails)
    {
        loadedAuthenticationDetails.put(uuid, authenticationDetails);
    }

    public static void saveAuthenticationDetails(UUID uuid, AuthenticationDetails authenticationDetails)
    {
        File userPath = new File(Main.plugin.getDataFolder() + File.separator + "data" + File.separator + uuid.toString() + ".yml");
        if (!userPath.exists()) {
            try
            {
                userPath.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(userPath);
        yaml.set("name", authenticationDetails.getName());
        yaml.set("key", authenticationDetails.getKey());
        try
        {
            yaml.save(userPath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean hasTwofactorauth(UUID uuid)
    {
        return loadedAuthenticationDetails.containsKey(uuid);
    }

    public static AuthenticationDetails getAuthenticationDetails(UUID uuid)
    {
        return (AuthenticationDetails)loadedAuthenticationDetails.get(uuid);
    }

    public static void unloadAuthenticationDetails(UUID uuid)
    {
        loadedAuthenticationDetails.remove(uuid);
    }
}
