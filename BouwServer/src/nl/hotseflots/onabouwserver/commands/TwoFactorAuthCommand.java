package nl.hotseflots.onabouwserver.commands;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.AuthenticationDetails;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.QRMap;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TOTP;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.command.CommandExecutor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

public class TwoFactorAuthCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        /*
        Check if the sender has the permissions to setup TwoFA and
        check if the sender is an instance of player.
         */
        if ((sender.hasPermission("bouwserver.2fa.setup")) && ((sender instanceof Player))) {

            /*
            Check if the 2FA module is enabled in the config file
             */
            if (Main.getInstance().getConfig().getString("Modules.TwoFA.Module").equalsIgnoreCase("enabled")) {

                /*
                Cast the sender to a player object
                 */
                Player player = (Player) sender;

                TwoFA.attemptDataLoad(player.getUniqueId());

                if (TwoFA.hasTwofactorauth(player.getUniqueId())) {
                    TwoFA.unloadAuthenticationDetails(player.getUniqueId());
                    player.sendMessage(Messages.MCAUTH_SETUP_ALREADY_ENABLED.getMessage());
                    return true;
                }

                AuthenticationDetails authenticationDetails = new AuthenticationDetails(player.getUniqueId().toString(), TOTP.generateBase32Secret(), true);
                TwoFA.addAuthenticationDetauls(player.getUniqueId(), authenticationDetails);

                try {
                    URL url = new URL(TOTP.qrImageUrl("minecraftserver", authenticationDetails.getKey()));
                    BufferedImage image = ImageIO.read(url);
                    ItemStack i = new ItemStack(Material.MAP, 1);
                    ItemMeta imeta = i.getItemMeta();
                    MapView view = Bukkit.createMap(player.getWorld());
                    view.getRenderers().clear();
                    view.addRenderer(new QRMap(image));
                    i.setDurability(view.getId());
                    imeta.setDisplayName(Messages.MCAUTH_QRMAP_NAME.getMessage());
                    i.setItemMeta(imeta);
                    player.setItemInHand(i);
                    player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), 90.0F));

                    sender.sendMessage(Messages.MCAUTH_SETUP_QRMAP.getMessage().replace("%code%", authenticationDetails.getKey()));
                } catch (IOException e) {
                    sender.sendMessage(Messages.MCAUTH_SETUP_CODE.getMessage().replace("%code%", authenticationDetails.getKey()));
                    e.printStackTrace();
                }
                sender.sendMessage(Messages.MCAUTH_SETUP_VALIDATE.getMessage());
            }
        }
        return true;
    }
}
