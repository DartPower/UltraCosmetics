package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.particleeffects.*;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.util.ServerVersion;
import be.isach.ultracosmetics.util.UCMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Particle effect types.
 *
 * @author iSach
 * @since 12-18-2015
 */
public class ParticleEffectType extends CosmeticMatType<ParticleEffect> {

    private final static List<ParticleEffectType> ENABLED = new ArrayList<>();
    private final static List<ParticleEffectType> VALUES = new ArrayList<>();

    public static List<ParticleEffectType> enabled() {
        return ENABLED;
    }

    public static List<ParticleEffectType> values() {
        return VALUES;
    }

    public static ParticleEffectType valueOf(String s) {
        for (ParticleEffectType particleEffectType : VALUES) {
            if (particleEffectType.getConfigName().equalsIgnoreCase(s)) return particleEffectType;
        }
        return null;
    }

    public static void checkEnabled() {
        ENABLED.addAll(values().stream().filter(CosmeticType::isEnabled).collect(Collectors.toList()));
    }

    private Particles effect;
    private int repeatDelay;
    private double particleMultiplier;

    private ParticleEffectType(String permission, String configName, int repeatDelay, Particles effect, UCMaterial material, Class<? extends ParticleEffect> clazz, String defaultDesc, boolean supportsParticleMultiplier) {
        super(Category.EFFECTS, configName, permission, defaultDesc, material, clazz, ServerVersion.earliest());
        this.repeatDelay = repeatDelay;
        this.effect = effect;
        if (supportsParticleMultiplier) {
            String path = getCategory().getConfigPath() + "." + configName + ".Particle-Multiplier";
            if (!SettingsManager.getConfig().isDouble(path)) {
                particleMultiplier = 1;
                SettingsManager.getConfig().set(getCategory().getConfigPath() + "." + configName + ".Particle-Multiplier", 1.0, "A multiplier applied to the number", "of particles displayed. 1.0 is 100%");
            } else {
                particleMultiplier = SettingsManager.getConfig().getDouble(path);
            }
        }

        VALUES.add(this);
    }

    public Particles getEffect() {
        return effect;
    }

    public int getRepeatDelay() {
        return repeatDelay;
    }
    
    public double getParticleMultiplier() {
        return particleMultiplier;
    }

    public static void register() {
        new ParticleEffectType("ultracosmetics.particleeffects.snowcloud", "SnowCloud", 1, Particles.SNOW_SHOVEL, UCMaterial.SNOWBALL, ParticleEffectSnowCloud.class, "&7&oThe weather forecast\n&7&ois telling me it's snowing.", true);
        new ParticleEffectType("ultracosmetics.particleeffects.bloodhelix", "BloodHelix", 1, Particles.REDSTONE, UCMaterial.REDSTONE, ParticleEffectBloodHelix.class, "&7&oAncient legend says this magic\n&7&oempowers the blood of its user,\n&7&ogiving them godly powers..", true);
        new ParticleEffectType("ultracosmetics.particleeffects.frostlord", "FrostLord", 1, Particles.SNOW_SHOVEL, UCMaterial.PACKED_ICE, ParticleEffectFrostLord.class, "&7&oI am The Almighty Frostlord!", true);
        new ParticleEffectType("ultracosmetics.particleeffects.flamerings", "FlameRings", 1, Particles.FLAME, UCMaterial.BLAZE_POWDER, ParticleEffectFlameRings.class, "&7&oWatch out! They are hot!", false);
        new ParticleEffectType("ultracosmetics.particleeffects.greensparks", "GreenSparks", 1, Particles.VILLAGER_HAPPY, UCMaterial.EMERALD, ParticleEffectGreenSparks.class, "&7&oLittle and green sparkly sparks!", false);
        new ParticleEffectType("ultracosmetics.particleeffects.frozenwalk", "FrozenWalk", 1, Particles.SNOW_SHOVEL, UCMaterial.SNOWBALL, ParticleEffectFrozenWalk.class, "&7&oMy feet are so cold!", false);
        new ParticleEffectType("ultracosmetics.particleeffects.music", "Music", 4, Particles.FLAME, UCMaterial.MUSIC_DISC_MALL, ParticleEffectMusic.class, "&7&oMuch music!", true);
        new ParticleEffectType("ultracosmetics.particleeffects.enchanted", "Enchanted", 1, Particles.ENCHANTMENT_TABLE, UCMaterial.BOOK, ParticleEffectEnchanted.class, "&7&oBecome an almighty enchanter!", true);
        new ParticleEffectType("ultracosmetics.particleeffects.inferno", "Inferno", 1, Particles.FLAME, UCMaterial.NETHER_WART, ParticleEffectInferno.class, "&7&oEffect created by Satan himself!", true);
        new ParticleEffectType("ultracosmetics.particleeffects.angelwings", "AngelWings", 2, Particles.REDSTONE, UCMaterial.FEATHER, ParticleEffectAngelWings.class, "&7&oBecome an angel!", true);
        new ParticleEffectType("ultracosmetics.particleeffects.superhero", "SuperHero", 2, Particles.REDSTONE, UCMaterial.GLOWSTONE_DUST, ParticleEffectSuperHero.class, "&7&oBecome Superman!", true);
        new ParticleEffectType("ultracosmetics.particleeffects.santahat", "SantaHat", 2, Particles.REDSTONE, UCMaterial.BEACON, ParticleEffectSantaHat.class, "&7&oBecome Santa!", true);
        new ParticleEffectType("ultracosmetics.particleeffects.enderaura", "EnderAura", 1, Particles.PORTAL, UCMaterial.ENDER_EYE, ParticleEffectEnderAura.class, "&7&oThese mystic particle attach" + " to\n&7&oonly the most legendary of players!", true);
        new ParticleEffectType("ultracosmetics.particleeffects.flamefairy", "FlameFairy", 1, Particles.FLAME, UCMaterial.BLAZE_POWDER, ParticleEffectFlameFairy.class, "&7&oHEY!!", false);
        new ParticleEffectType("ultracosmetics.particleeffects.raincloud", "RainCloud", 1, Particles.DRIP_WATER, UCMaterial.LAPIS_LAZULI, ParticleEffectRainCloud.class, "&7&oThe weather forecast\n&7&ois telling me it's raining.", true);
        new ParticleEffectType("ultracosmetics.particleeffects.crushedcandycane", "CrushedCandyCane", 1, Particles.ITEM_CRACK, UCMaterial.WHITE_DYE, ParticleEffectCrushedCandyCane.class, "&7&oThere's no such thing as too much\n&7&oChristmas Candy. Do not listen\n&7&oto your dentist.", true);
        new ParticleEffectType("ultracosmetics.particleeffects.inlove", "InLove", 1, Particles.HEART, UCMaterial.RED_DYE, ParticleEffectInLove.class, "&7&oOMG wow I'm in love!", true);
        new ParticleEffectType("ultracosmetics.particleeffects.magicalrods", "MagicalRods", 3, Particles.REDSTONE, UCMaterial.BLAZE_ROD, ParticleEffectMagicalRods.class, "&7&oMay these magical rods protect you!", true);
        new ParticleEffectType("ultracosmetics.particleeffects.firewaves", "FireWaves", 4, Particles.FLAME, UCMaterial.GOLD_NUGGET, ParticleEffectFireWaves.class, "&7&oSome say theses waves of fire acts as a shield...", true);
    }
}
