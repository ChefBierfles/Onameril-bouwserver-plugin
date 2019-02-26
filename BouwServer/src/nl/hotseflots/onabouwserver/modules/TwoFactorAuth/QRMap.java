package nl.hotseflots.onabouwserver.modules.TwoFactorAuth;

import org.bukkit.event.Listener;

import java.awt.Image;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class QRMap extends MapRenderer {

    private Image qrCode;

    public QRMap(Image qrCode) {
        this.qrCode = qrCode;
    }

    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        mapCanvas.drawImage(0, 0, this.qrCode);
    }
}
