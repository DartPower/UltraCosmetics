package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.BlockUtils;
import be.isach.ultracosmetics.util.Cuboid;
import be.isach.ultracosmetics.util.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instance of a parachute gadget summoned by a player.
 *
 * @author iSach
 * @since 10-12-2015
 */
public class GadgetParachute extends Gadget {

    List<Chicken> chickens = new ArrayList<>();
    boolean active;

    public GadgetParachute(UltraPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, GadgetType.valueOf("parachute"), ultraCosmetics);
    }


    @Override
    void onRightClick() {
        Location loc = getPlayer().getLocation();

        getPlayer().teleport(loc.clone().add(0, 35, 0));
        getPlayer().setVelocity(new Vector(0, 0, 0));

        getOwner().setCanBeHitByOtherGadgets(false);

        for (int i = 0; i < 20; i++) {
            Chicken chicken = (Chicken) getPlayer().getWorld().spawnEntity(getPlayer().getLocation().add(MathUtils.randomDouble(0, 0.5), 3, MathUtils.randomDouble(0, 0.5)), EntityType.CHICKEN);
            chickens.add(chicken);
            chicken.setLeashHolder(getPlayer());
        }
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> active = true, 5);
    }

    private void killParachute() {
        for (Chicken chicken : chickens) {
            chicken.setLeashHolder(null);
            chicken.remove();
        }
        MathUtils.applyVelocity(getPlayer(), new Vector(0, 0.15, 0));
        active = false;
        getOwner().setCanBeHitByOtherGadgets(true);
    }

    @EventHandler
    public void onLeashBreak(EntityUnleashEvent event) {
        if (chickens.contains(event.getEntity())) {
            event.getEntity().getNearbyEntities(1, 1, 1).stream().filter(ent -> ent instanceof Item
                    && ((Item) ent).getItemStack().getType() == BlockUtils.getOldMaterial("LEASH")).forEachOrdered(Entity::remove);
        }
    }

    @Override
    protected boolean checkRequirements(PlayerInteractEvent event) {
        Location loc1 = getPlayer().getLocation().add(2, 28, 2);
        Location loc2 = getPlayer().getLocation().clone().add(-2, 40, -2);
        Cuboid checkCuboid = new Cuboid(loc1, loc2);

        if (!checkCuboid.isEmpty()) {
            getPlayer().sendMessage(MessageManager.getMessage("Gadgets.Rocket.Not-Enough-Space"));
            return false;
        }
        return true;
    }

    @Override
    public void onUpdate() {
        if (active) {
            // isOnGround returns true if they're on a solid block and doesn't account for non-solid blocks (#362)
            if (!isNotOnAir(getPlayer()) && getPlayer().getVelocity().getY() < -0.3)
                MathUtils.applyVelocity(getPlayer(), getPlayer().getVelocity().add(new Vector(0, 0.1, 0)), true);
            if (isNotOnAir(getPlayer()))
                killParachute();
        }
    }

    private boolean isNotOnAir(Player p) {
        return p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR;
    }

    @Override
    public void onClear() {
        killParachute();
        HandlerList.unregisterAll(this);
    }
}
