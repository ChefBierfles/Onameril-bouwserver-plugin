package nl.hotseflots.onabouwserver.modules.TwoFactorAuth;

import nl.hotseflots.onabouwserver.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class TwoFA {

    private static HashMap<UUID, AuthenticationDetails> loadedAuthenticationDetails;

    public static void dataGenerator()
    {
        loadedAuthenticationDetails = new HashMap<>();
        File dataDir = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data");
        if (!dataDir.isDirectory()) {
            dataDir.mkdir();
        }
    }

    public static void attemptDataLoad(UUID uuid)
    {
        File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + uuid.toString() + ".yml");
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
        File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + uuid.toString() + ".yml");
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
        if (Main.getInstance().getConfig().getString("Modules.TwoFA.Module").equalsIgnoreCase("enabled")) {
            File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + uuid.toString() + ".yml");
            return (loadedAuthenticationDetails.containsKey(uuid) && (userPath.exists() && Bukkit.getPlayer(uuid).hasPermission("bouwserver.2fa.setup")));
        }
        return false;
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
