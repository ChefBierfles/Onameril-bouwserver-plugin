package nl.hotseflots.onabouwserver.commands;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.twofactorauth.AuthenticationDetails;
import nl.hotseflots.onabouwserver.twofactorauth.QRMap;
import nl.hotseflots.onabouwserver.twofactorauth.TOTP;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.command.CommandExecutor;
import java.awt.image.BufferedImage;
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
import org.bukkit.map.MapView;

public class TwoFACommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if ((sender.hasPermission("2fa.setup")) && ((sender instanceof Player)))
        {
            Player player = (Player)sender;
            Main.plugin.attemptDataLoad(player.getUniqueId());
            if (Main.plugin.hasTwofactorauth(player.getUniqueId()))
            {
                Main.plugin.unloadAuthenticationDetails(player.getUniqueId());
                player.sendMessage(Messages.MCAUTH_SETUP_ALREADY_ENABLED);
                return true;
            }
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(player.getUniqueId().toString(), TOTP.generateBase32Secret(), true);
            Main.plugin.addAuthenticationDetauls(player.getUniqueId(), authenticationDetails);
            try
            {
                URL url = new URL(TOTP.qrImageUrl("minecraftserver", authenticationDetails.getKey()));
                BufferedImage image = ImageIO.read(url);

                ItemStack i = new ItemStack(Material.MAP, 1);
                MapView view = Bukkit.createMap(player.getWorld());
                view.getRenderers().clear();
                view.addRenderer(new QRMap(image));
                i.setDurability(view.getId());
                player.getInventory().setItemInMainHand(i);
                player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), 90.0F));

                sender.sendMessage(Messages.MCAUTH_SETUP_QRMAP.replace("%code%", authenticationDetails.getKey()));
            }
            catch (IOException e)
            {
                sender.sendMessage(Messages.MCAUTH_SETUP_CODE.replace("%code%", authenticationDetails.getKey()));
                e.printStackTrace();
            }
            sender.sendMessage(Messages.MCAUTH_SETUP_VALIDATE);
        }
        return true;
    }
}
