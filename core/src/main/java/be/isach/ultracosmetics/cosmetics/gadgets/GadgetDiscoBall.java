package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents an instance of a discoball gadget summoned by a player.
 *
 * @author iSach
 * @since 08-03-2015
 */
public class GadgetDiscoBall extends Gadget {

    public static final List<GadgetDiscoBall> DISCO_BALLS = new ArrayList<>();

    private Random r = new Random();
    private int i = 0;
    private double i2 = 0;
    private ArmorStand armorStand;
    private boolean running = false;

    public GadgetDiscoBall(UltraPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, GadgetType.valueOf("discoball"), ultraCosmetics);
    }

    @Override
    void onRightClick() {
        armorStand = (ArmorStand) getPlayer().getWorld().spawnEntity(getPlayer().getLocation().add(0, 3, 0), EntityType.ARMOR_STAND);
        armorStand.setMetadata("NO_INTER", new FixedMetadataValue(getUltraCosmetics(), ""));
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setSmall(false);
        armorStand.setHelmet(ItemFactory.createColored("STAINED_GLASS", (byte) 3, " "));
        running = true;
        DISCO_BALLS.add(this);
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), this::clean, 400);
    }

    @Override
    protected boolean checkRequirements(PlayerInteractEvent event) {
        if (GadgetDiscoBall.DISCO_BALLS.size() > 0) {
            getPlayer().sendMessage(MessageManager.getMessage("Gadgets.DiscoBall.Already-Active"));
            return false;
        }
        if (getPlayer().getLocation().add(0, 4, 0).getBlock() != null && getPlayer().getLocation().add(0, 4, 0).getBlock().getType() != Material.AIR) {
            getPlayer().sendMessage(MessageManager.getMessage("Gadgets.DiscoBall.Not-Space-Above"));
            return false;
        }
        return true;
    }

    @Override
    public void onUpdate() {
        if (armorStand == null) {
            return;
        }
        if (armorStand != null && armorStand.isValid() && running) {
            armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.2, 0));

            if (UltraCosmeticsData.get().getServerVersion() == ServerVersion.v1_8_R3) {
                armorStand.setHelmet(ItemFactory.createColored("STAINED_GLASS", (byte) r.nextInt(15), " "));
            }

            UtilParticles.display(Particles.SPELL, armorStand.getEyeLocation(), 1, 1f);
            UtilParticles.display(Particles.SPELL_INSTANT, armorStand.getEyeLocation(), 1, 1f);
            Location loc = armorStand.getEyeLocation().add(MathUtils.randomDouble(-4, 4), MathUtils.randomDouble(-3, 3), MathUtils.randomDouble(-4, 4));
            Particles.NOTE.display(new Particles.NoteColor(r.nextInt(25)), loc, 128);
            double angle, angle2, x, x2, z, z2;
            angle = 2 * Math.PI * i / 100;
            x = Math.cos(angle) * 4;
            z = Math.sin(angle) * 4;

            drawParticleLine(armorStand.getEyeLocation().add(-.5d, -.5d, -.5d).clone().add(0.5, 0.5, 0.5).clone().add(x, 0, z), armorStand.getEyeLocation().add(-.5d, -.5d, -.5d).clone().add(0.5, 0.5, 0.5), false, 20);

            i += 6;
            angle2 = 2 * Math.PI * i2 / 100;
            x2 = Math.cos(angle2) * 4;
            z2 = Math.sin(angle2) * 4;
            drawParticleLine(armorStand.getEyeLocation().add(-.5d, -.5d, -.5d).clone().add(0.5, 0.5, 0.5), armorStand.getEyeLocation().add(-.5d, -.5d, -.5d).clone().add(0.5, 0.5, 0.5).add(x2, 0, z2), true, 50);
            i2 += 0.4;

            new BukkitRunnable() {

                @Override
                public void run() {
                    for (Entity ent : loc.getWorld().getNearbyEntities(armorStand.getEyeLocation().add(-.5d, -.5d, -.5d), 7.5, 7.5, 7.5)) {
                        if (ent == null) {
                            break;
                        }
                        if (ent.isOnGround() && affectPlayers) {
                            MathUtils.applyVelocity(ent, new Vector(0, 0.3, 0));
                        }
                    }
                }
            }.runTask(getUltraCosmetics());

            try {
                for (Block b : BlockUtils.getBlocksInRadius(armorStand.getEyeLocation().add(-.5d, -.5d, -.5d), 10, false)) {
                    if (b.getType().toString().contains("WOOL") || b.getType().toString().contains("CARPET")) {
                        BlockUtils.setToRestore(b, b.getType(), (byte) r.nextInt(15), 4);
                    }
                }
            } catch (NullPointerException exc) {
                //...
            }
        } else {
            i = 0;
            i2 = 0;
            clean();
        }
    }

    private void clean() {
        running = false;
        if (armorStand != null) {
            armorStand.remove();
            armorStand = null;
        }
        DISCO_BALLS.remove(this);
    }

    @Override
    public void onClear() {
        clean();
    }

    public void drawParticleLine(Location a, Location b, boolean dust, int particles) {
        Location location = a.clone();
        Location target = b.clone();
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        link.normalize();

        float ratio = length / particles;
        Vector v = link.multiply(ratio);
        if (!dust)
            MathUtils.rotateAroundAxisX(v, i);
        else {
            MathUtils.rotateAroundAxisZ(v, i2 / 5);
            MathUtils.rotateAroundAxisX(v, i2 / 5);
        }
        Location loc = location.clone().subtract(v);
        int step = 0;
        for (int i = 0; i < particles; i++) {
            if (step >= (double) particles)
                step = 0;
            step++;
            loc.add(v);
            if (dust) {
                UtilParticles.display(MathUtils.random(255), MathUtils.random(255), MathUtils.random(255), loc);
            }
            // location.getWorld().spigot().playEffect(loc, Effect.POTION_SWIRL);
        }
    }
}
