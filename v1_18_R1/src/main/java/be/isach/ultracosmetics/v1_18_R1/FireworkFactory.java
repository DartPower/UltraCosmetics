package be.isach.ultracosmetics.v1_18_R1;

import be.isach.ultracosmetics.v1_18_R1.customentities.CustomEntityFirework;
import be.isach.ultracosmetics.version.IFireworkFactory;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author RadBuilder
 */
public class FireworkFactory implements IFireworkFactory {
    @Override
    public void spawn(Location location, FireworkEffect effect, Player... players) {
        CustomEntityFirework.spawn(location, effect, players);
    }
}
