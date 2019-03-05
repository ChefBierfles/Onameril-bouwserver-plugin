package nl.hotseflots.onabouwserver.modules;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.tehkode.permissions.PermissionsUserData;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class RankDetector {

    /*
    Get the highest rank that the player has and return it
     */
    public static String getRank(UUID uuid) {

        String[] groups = PermissionsEx.getUser(Bukkit.getPlayer(uuid)).getGroupNames();
        String ranks = "";
        for (String group : groups) {
            ranks += group + " ";
        }

        if (ranks.contains("Owner")) {
            return "Owner";
        } else if (ranks.contains("Admin")) {
            return "Admin";
        } else if (ranks.contains("Moderator")) {
            return "Moderator";
        } else if (ranks.contains("Onameril")) {
            return "Onameril";
        } else if (ranks.contains("Rando")) {
            return "Rando";
        } else {
            return null;
        }
    }
}
