package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.twofactorauth.AuthenticationDetails;
import nl.hotseflots.onabouwserver.twofactorauth.MCAuth;
import nl.hotseflots.onabouwserver.twofactorauth.Options;
import nl.hotseflots.onabouwserver.twofactorauth.TOTP;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import sun.java2d.loops.FillRect;

import java.security.GeneralSecurityException;

public class AsyncPlayerChat implements Listener {

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (MCAuth.hasTwofactorauth(event.getPlayer().getUniqueId()))
        {
            final AuthenticationDetails authenticationDetails = MCAuth.getAuthenticationDetails(event.getPlayer().getUniqueId());
            event.setCancelled(true);
            new BukkitRunnable()
            {
                public void run()
                {
                    String validCode;

                    try
                    {
                        validCode = TOTP.generateCurrentNumberString(authenticationDetails.getKey());
                    }
                    catch (GeneralSecurityException e)
                    {
                        e.printStackTrace(); return;
                    }
                    if (validCode.equals(event.getMessage()))
                    {
                        if (authenticationDetails.isSetup()) {
                            MCAuth.saveAuthenticationDetails(event.getPlayer().getUniqueId(), authenticationDetails);
                        }
                        MCAuth.unloadAuthenticationDetails(event.getPlayer().getUniqueId());
                        event.getPlayer().sendMessage(Messages.MCAUTH_VALID_CODE);
                    }
                    else
                    {
                        authenticationDetails.attempts += 1;
                        event.getPlayer().sendMessage(Messages.MCAUTH_INVALID_CODE);
                        if (authenticationDetails.attempts > Options.MAX_TRIES.getIntValue()) {
                            if (!authenticationDetails.isSetup())
                            {
                                new BukkitRunnable()
                                {
                                    public void run()
                                    {
                                        event.getPlayer().kickPlayer(Messages.MCAUTH_FAIL_MESSAGE);
                                    }
                                }.runTask(Main.plugin);
                            }
                            else
                            {
                                MCAuth.unloadAuthenticationDetails(event.getPlayer().getUniqueId());
                                event.getPlayer().sendMessage(Messages.MCAUTH_SETUP_FAIL);
                            }
                        }
                    }
                }
            }.runTaskAsynchronously(Main.plugin);
        }
    }
}
