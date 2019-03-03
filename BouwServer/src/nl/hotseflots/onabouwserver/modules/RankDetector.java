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
        for (String group : groups) {

            if (group.contains("Eigenaar")) {
                return "Eigenaar";
            } else if (group.contains("Admin")) {
                return "Admin";
            } else if (group.contains("Moderator")) {
                return "Moderator";
            } else if (group.contains("Onameril")) {
                return "Onameril";
            } else if (group.contains("Rando")) {
                return "Rando";
            } else {
                return null;
            }
        }
        return null;
    }
}
