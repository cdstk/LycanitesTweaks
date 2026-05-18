package lycanitestweaks.compat;

import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.shultrea.rin.SoManyEnchantments;
import com.shultrea.rin.properties.ArrowPropertiesProvider;
import com.shultrea.rin.properties.IArrowProperties;
import com.shultrea.rin.registry.EnchantmentRegistry;
import com.shultrea.rin.util.IEntityDamageSourceMixin;
import com.shultrea.rin.util.Types;
import com.shultrea.rin.util.compat.CompatUtil;
import com.shultrea.rin.util.compat.SpartanWeaponryCompat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;
import java.util.Set;

public abstract class SMEHandler {

    public static boolean doesEquipmentHaveType(Enchantment enchantment, Set<String> featureTypeSet){
        if(enchantment.type == Types.AXE && featureTypeSet.contains("axe")) return true;
        if(enchantment.type == Types.PICKAXE && featureTypeSet.contains("pickaxe")) return true;
        if(enchantment.type == Types.HOE && featureTypeSet.contains("hoe")) return true;
        if(enchantment.type == Types.SPADE && featureTypeSet.contains("shovel")) return true;
        return false;
    }

    // Copy paste handling for Lycanites Charges
    // This abomination should affect Charges shot from mobs and Charges shot by the Charge Staff (players)

    private static final ResourceLocation ARROWPROPERTIES_CAP_RESOURCE = new ResourceLocation(SoManyEnchantments.MODID + ":arrow_capabilities");

    private static final Random RANDOM = new Random();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof BaseProjectileEntity && !event.getObject().hasCapability(ArrowPropertiesProvider.ARROWPROPERTIES_CAP, null)) {
            event.addCapability(ARROWPROPERTIES_CAP_RESOURCE, new ArrowPropertiesProvider());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onChargeEntityJoinWorld(EntityJoinWorldEvent event) {
        if(event.getWorld().isRemote) return;
        if(!(event.getEntity() instanceof BaseProjectileEntity)) return;
        BaseProjectileEntity baseProjectileEntity = (BaseProjectileEntity) event.getEntity();
        EntityLivingBase shooter = baseProjectileEntity.getThrower();
        if(shooter == null || shooter instanceof EntityPlayer) return; // TODO Rework Charge Shooting Staff and Holding Offhand Bow

        ItemStack bow = shooter.getHeldItemMainhand();
        if(!(bow.getItem() instanceof ItemBow || CompatUtil.isSpartanWeaponryLoaded() && SpartanWeaponryCompat.itemIsCrossbow(bow.getItem()))) {
            return;
        }

        IArrowProperties properties = baseProjectileEntity.getCapability(ArrowPropertiesProvider.ARROWPROPERTIES_CAP, null);
        if(properties == null) return;

        //Avoid overwriting if properties were already set for this entity for whatever reason
        if(properties.getPropertiesHandled()) return;
        setChargeEnchantmentsFromStack(bow, baseProjectileEntity, properties);
        properties.setPropertiesHandled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onChargeHitHurt(LivingHurtEvent event) {
        if(!(event.getSource().getImmediateSource() instanceof BaseProjectileEntity)) return;
        if(!"thrown".equals(event.getSource().damageType)) return;

        BaseProjectileEntity baseProjectileEntity = (BaseProjectileEntity)event.getSource().getImmediateSource();
        EntityLivingBase victim = event.getEntityLiving();

        IArrowProperties properties = baseProjectileEntity.getCapability(ArrowPropertiesProvider.ARROWPROPERTIES_CAP, null);
        if(properties == null) return;

        if(EnchantmentRegistry.runeArrowPiercing.isEnabled()) {
            int pierceLevel = properties.getArmorPiercingLevel();
            if(pierceLevel > 0) {
                if(event.getSource() instanceof EntityDamageSource) {
                    float currPercent = ((IEntityDamageSourceMixin)event.getSource()).soManyEnchantments$getPiercingPercent();
                    float percent = Math.min(currPercent + 0.25F * (float)pierceLevel, 1.0F);
                    ((IEntityDamageSourceMixin)event.getSource()).soManyEnchantments$setPiercingPercent(percent);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onChargeHitDamage(LivingDamageEvent event) {
        if(!(event.getSource().getImmediateSource() instanceof BaseProjectileEntity)) return;
        if(!"thrown".equals(event.getSource().damageType)) return;

        BaseProjectileEntity baseProjectileEntity = (BaseProjectileEntity)event.getSource().getImmediateSource();
        EntityLivingBase victim = event.getEntityLiving();

        IArrowProperties properties = baseProjectileEntity.getCapability(ArrowPropertiesProvider.ARROWPROPERTIES_CAP, null);
        if(properties == null) return;

        if(EnchantmentRegistry.lesserFlame.isEnabled() || EnchantmentRegistry.advancedFlame.isEnabled() || EnchantmentRegistry.supremeFlame.isEnabled()) {
            int seconds = getFireSeconds(properties.getFlameLevel());
            if(seconds > 0) victim.setFire(seconds);
        }

        if(EnchantmentRegistry.extinguish.isEnabled()) {
            int flameLevel = properties.getFlameLevel();
            if(flameLevel == -1) victim.extinguish();
        }

        if(EnchantmentRegistry.dragging.isEnabled()) {
            float draggingPower = properties.getDraggingPower();
            if(draggingPower > 0) {
                double velocityMultiplier = -0.6F * draggingPower / MathHelper.sqrt(baseProjectileEntity.motionX * baseProjectileEntity.motionX + baseProjectileEntity.motionZ * baseProjectileEntity.motionZ);
                victim.addVelocity(baseProjectileEntity.motionX * velocityMultiplier, 0.1, baseProjectileEntity.motionZ * velocityMultiplier);
                victim.velocityChanged = true;
            }
        }

        if(EnchantmentRegistry.strafe.isEnabled()) {
            if(properties.getArrowResetsIFrames()) {
                victim.hurtResistantTime = 0;
            }
        }
    }

    private static int getFireSeconds(int tier) {
        switch(tier) {
            case 1:
                return 2;
            case 2:
                return 15;
            case 3:
                return 30;
        }
        return 0;
    }

    public static void setChargeEnchantmentsFromStack(ItemStack bow, BaseProjectileEntity baseProjectileEntity, IArrowProperties properties) {
        if(EnchantmentRegistry.powerless.isEnabled()) {
            int powerlessLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.powerless, bow);
            if(powerlessLevel > 0) {
                baseProjectileEntity.setDamage((int) (baseProjectileEntity.damage - 0.5D - powerlessLevel * 0.5D));
            }
        }

        if(EnchantmentRegistry.advancedPower.isEnabled()) {
            int advPowerLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.advancedPower, bow);
            if(advPowerLevel > 0) {
                baseProjectileEntity.setDamage((int) (baseProjectileEntity.damage + 1.25D + (double)advPowerLevel * 0.75D));
            }
        }

        if(EnchantmentRegistry.advancedPunch.isEnabled()) {
            int advPunchLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.advancedPunch, bow);
            if(advPunchLevel > 0) {
                baseProjectileEntity.knockbackChance += advPunchLevel * 2;
            }
        }

        if(EnchantmentRegistry.runeArrowPiercing.isEnabled()) {
            int runeArrowPiercingLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.runeArrowPiercing, bow);
            if(runeArrowPiercingLevel > 0) {
                properties.setArmorPiercingLevel(runeArrowPiercingLevel);
            }
        }

        if(EnchantmentRegistry.dragging.isEnabled()) {
            int draggingLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.dragging, bow);
            if(draggingLevel > 0) {
                properties.setDraggingPower(1.25F + draggingLevel * 1.75F);
            }
        }

        if(EnchantmentRegistry.strafe.isEnabled()) {
            int levelStrafe = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.strafe, bow);
            if(RANDOM.nextFloat() < 0.125F * levelStrafe) {
                properties.setArrowResetsIFrames(true);
            }
        }

        if(EnchantmentRegistry.lesserFlame.isEnabled()) {
            int levelLessFlame = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.lesserFlame, bow);
            if(levelLessFlame > 0) {
                //baseProjectileEntity.setFire(50);
                properties.setFlameLevel(1);
            }
        }

        if(EnchantmentRegistry.advancedFlame.isEnabled()) {
            int levelAdvFlame = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.advancedFlame, bow);
            if(levelAdvFlame > 0) {
                baseProjectileEntity.setFire(200);
                properties.setFlameLevel(2);
            }
        }

        if(EnchantmentRegistry.supremeFlame.isEnabled()) {
            int levelSupFlame = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.supremeFlame, bow);
            if(levelSupFlame > 0) {
                baseProjectileEntity.setFire(400);
                properties.setFlameLevel(3);
            }
        }

        if(EnchantmentRegistry.extinguish.isEnabled()) {
            int levelExtinguish = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.extinguish, bow);
            if(levelExtinguish > 0) {
                baseProjectileEntity.extinguish();
                properties.setFlameLevel(-1);
            }
        }
    }
}
