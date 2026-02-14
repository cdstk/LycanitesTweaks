package lycanitestweaks.util;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.Variant;
import com.lycanitesmobs.core.item.ChargeItem;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;

public abstract class LycanitesEntityUtil {

    // ========== Group Limit Spawn Check ==========
    /** Checks for nearby entities of this type, mobs use this so that too many don't spawn in the same area. Returns true if the mob should spawn. **/
    public static boolean checkSpawnGroupLimit(World world, BlockPos position, double checkRange, int checkCap, EnumCreatureType creatureType) {
        if(checkRange <= 0 || creatureType == null) return true;

        int mobCountLimit = (checkCap != -1) ? checkCap : creatureType.getMaxNumberOfCreature();

        // Count Entities:
        List<EntityLiving> targets = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(position)
                        .grow(checkRange, checkRange, checkRange)
                , entityLiving -> entityLiving.isCreatureType(creatureType, true));
        return targets.size() <= mobCountLimit;
    }

    // Used by Vanilla Spawner, not used by JSON or Mob Spawner
    public static final EntityLiving.SpawnPlacementType IN_WATER_REDUCED = EnumHelper.addSpawnPlacementType(LycanitesTweaks.MODID + ":IN_WATER_REDUCED", new BiPredicate<IBlockAccess, BlockPos>() {
        @Override
        public boolean test(IBlockAccess iBlockAccess, BlockPos blockPos) {
            boolean inWater = iBlockAccess.getBlockState(blockPos).getMaterial() == Material.WATER
                    && iBlockAccess.getBlockState(blockPos.down()).getMaterial() == Material.WATER
                    && !iBlockAccess.getBlockState(blockPos.up()).isNormalCube();
            boolean biomeChance = true;
            if(iBlockAccess instanceof World){
                World world = (World) iBlockAccess;
                biomeChance = BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.OCEAN)
                        ? world.rand.nextInt(ForgeConfigHandler.minorFeaturesConfig.waterMonsterSpawnRateOcean) == 0
                        : world.rand.nextInt(ForgeConfigHandler.minorFeaturesConfig.waterMonsterSpawnRateOther) == 0;
            }
            return inWater && biomeChance;

        }
    });

//    public static final EntityLiving.SpawnPlacementType IN_AIR_REDUCED = EnumHelper.addSpawnPlacementType(LycanitesTweaks.MODID + ":IN_AIR_REDUCED", new BiPredicate<IBlockAccess, BlockPos>() {
//        @Override
//        public boolean test(IBlockAccess iBlockAccess, BlockPos blockPos) {
//            boolean isValid = false;
//            if(iBlockAccess instanceof World){
//                EntityPlayer player = ((World) iBlockAccess).getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 128, false);
//                if(player != null){
//
//                }
//            }
//            return isValid;
//        }
//    });

    public static boolean shouldLevelFromStack(TameableCreatureEntity creature){
        if(creature instanceof ITameableCreatureEntity_TargetFlagMixin && !((ITameableCreatureEntity_TargetFlagMixin) creature).lycanitesTweaks$shouldInventoryLevelup()) {
            return false;
        }
        return creature.isTamed() && !creature.isTemporary && !creature.isPetType("minion");
    }

    /**
     * Based on Equipment Infusing, performs leveling if possible.
     */
    public static ItemStack attemptLevelingFromStack(TameableCreatureEntity creature, ItemStack itemStack) {
        if(itemStack == null || itemStack.isEmpty()) return itemStack;

        if (itemStack.getItem() instanceof ChargeItem) {
            if(creature.getExperience() < creature.creatureStats.getExperienceForNextLevel()) {
                if(creature.isLevelingChargeItem(itemStack)){
                    while (!itemStack.isEmpty()) {
                        creature.addExperience(creature.getExperienceFromChargeItem(itemStack));
                        itemStack.shrink(1);
                    }
                }
            }
        }
        return itemStack;
    }

    /**
     * Determines how much experience the creature needs in order to level up.
     * @return Experience required for a level up.
     */
    public static int calculateExperienceForNextLevel(int baseNumber, int level) {
        // Natural Log
        if(ForgeConfigHandler.server.chargeExpConfig.modifiedExperienceCalc){
            if(ForgeConfigHandler.server.chargeExpConfig.calcLogStart > 0
                    && level > ForgeConfigHandler.server.chargeExpConfig.calcLogStart) {
                return (int) (baseNumber * (1D + Math.round(Math.log(level) * ForgeConfigHandler.server.chargeExpConfig.calcLogMultiplier)));
            }
        }
        // Vanilla Lycanites
        return baseNumber * (1 + Math.round((level - 1) * ForgeConfigHandler.server.chargeExpConfig.calcLinearMultiplier));
    }

    /**
     * Check uses for determining legacy or intended behavior via config
     * @return Whether to apply extra multipliers such as Variant and NBT
     */
    public static boolean shouldApplyExtraMultipliers(BaseCreatureEntity creature){
        if(creature.isTamed()) {
            if(!ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.variantStatReceivers) return false;

            if (creature.isBoundPet()) {
                if (creature.getPetEntry().getType().equals("familiar")) return false;
                return ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.variantStatsSoulbound;
            }

            if (creature.isMinion()) return ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.variantStatsSummoned;

            return ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.variantStatsTamed;
        }
        return true;
    }

    /** Utility to test if a custom addition via json replacement is loaded, Assimilated Darkling **/
    public static boolean isCustomCreatureLoaded(boolean config, String creatureName, int subspeciesIndex, int variantIndex){
        if(config){
            CreatureInfo creatureInfo = CreatureManager.getInstance().getCreature(creatureName);
            if(creatureInfo == null) return false;
            if(creatureInfo.getSubspecies(subspeciesIndex).index != subspeciesIndex) return false;
            if(creatureInfo.getSubspecies(subspeciesIndex).getVariant(variantIndex).index != variantIndex) return false;
            return true;
        }
        return false;
    }

    // mfw Lycanites config for no flying mount doesn't catch mobs whose flight check considers landed state
    public static boolean isPracticallyFlying(BaseCreatureEntity entity){
        return (entity.isFlying() || entity.flySoundSpeed > 0);
    }

    public static double getAutoDropPickupDistance(Entity holdingEntity, Entity pickupVictim){
        // If rider is player, always use lycanites default
        if(!(holdingEntity instanceof RideableCreatureEntity && holdingEntity.getControllingPassenger() instanceof EntityPlayer)){
            if(ForgeConfigHandler.mixinPatchesConfig.fixPickupRange) {
                if (ForgeConfigHandler.mixinPatchesConfig.pickUpDistance == -1) {
                    if (holdingEntity instanceof BaseCreatureEntity) {
                        double[] pickupOffset = ((BaseCreatureEntity) holdingEntity).getPickupOffset(pickupVictim);
                        return Math.sqrt(((BaseCreatureEntity) holdingEntity).getMeleeAttackRange((EntityLivingBase) pickupVictim, 1D))
                                + Math.sqrt(pickupOffset[0] * pickupOffset[0] + pickupOffset[1] * pickupOffset[1] + pickupOffset[2] * pickupOffset[2]);
                    }
                }
                else {
                    return ForgeConfigHandler.mixinPatchesConfig.pickUpDistance;
                }
            }
        }
        return 32D;
    }

    // Extracted from com.lycanitesmobs.core.entity.BaseCreatureEntity
    /** Returns the default starting level to use. **/
    public static int getStartingLevel(EntityLivingBase entity) {
        int startingLevelMin = Math.max(1, CreatureManager.getInstance().config.startingLevelMin);
        if(CreatureManager.getInstance().config.startingLevelMax > startingLevelMin) {
            return startingLevelMin + entity.getRNG().nextInt(CreatureManager.getInstance().config.startingLevelMax - startingLevelMin);
        }
        if(CreatureManager.getInstance().config.levelPerDay > 0 && CreatureManager.getInstance().config.levelPerDayMax > 0) {
            int day = (int)Math.floor(entity.getEntityWorld().getTotalWorldTime() / 23999D);
            double levelGain = Math.min(CreatureManager.getInstance().config.levelPerDay * day, CreatureManager.getInstance().config.levelPerDayMax);
            startingLevelMin += (int)Math.floor(levelGain);
        }
        if(CreatureManager.getInstance().config.levelPerLocalDifficulty > 0) {
            double levelGain = entity.getEntityWorld().getDifficultyForLocation(entity.getPosition()).getAdditionalDifficulty();
            startingLevelMin += Math.max(0, (int)Math.floor(levelGain - 1.5D));
        }
        return startingLevelMin;
    }

    /** Attempt to get a level limited stat. **/
    public static int getEffectDurationLevelLimited(BaseCreatureEntity creature, int duration, int levelCap) {
        String statName = "EFFECT";
        double statValue = creature.creatureInfo.effectDuration;

        // Wild:
        if(shouldApplyExtraMultipliers(creature)) {
            statValue *= getDifficultyMultiplier(creature.getEntityWorld(), statName);
            statValue *= getVariantMultiplier(creature, statName);
            if(creature.extraMobBehaviour != null) {
                statValue *= creature.extraMobBehaviour.multiplierEffect;
                statValue += creature.extraMobBehaviour.boostEffect;
            }
        }

        statValue *= getLevelMultiplier(statName, creature, levelCap);

        // Going to suck when I forget to sync with the Mixin stat caps
        if(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.capEffectDurationRatio > 0) {
            statValue = Math.min(statValue,
                    ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.capEffectDurationRatio
                            * creature.creatureInfo.effectDuration
                            * getVariantMultiplier(creature, statName)
            );
        }
        return (int)Math.round(duration * statValue * 20);
    }

    public static int getEffectAmplifierLevelLimited(BaseCreatureEntity creature, float amplifier, int levelCap) {
        String statName = "AMPLIFIER";
        double statValue = creature.creatureInfo.effectAmplifier;

        // Wild:
        if(shouldApplyExtraMultipliers(creature)) {
            statValue *= getDifficultyMultiplier(creature.getEntityWorld(), statName);
            statValue *= getVariantMultiplier(creature, statName);
        }

        statValue *= getLevelMultiplier(statName, creature, levelCap);
        // Lycanites doesn't use amplifier scale
        return (int) Math.round(statValue);
    }

    // Extracted methods from Lycanites
    /**
     * Returns a difficulty stat multiplier for the provided stat name and the world that the entity is in.
     * @param world The world to check difficulty from.
     * @param stat The name of the stat to get the multiplier for. The entire string must be Upper Case
     * @return The stat multiplier.
     */
    public static double getDifficultyMultiplier(World world, String stat) {
        EnumDifficulty difficulty = world.getDifficulty();
        String difficultyName = "EASY";
        if(difficulty.getId() >= 3)
            difficultyName = "HARD";
        else if(difficulty == EnumDifficulty.NORMAL)
            difficultyName = "NORMAL";
        return CreatureManager.getInstance().getDifficultyMultiplier(difficultyName, stat);
    }

    /**
     * Returns a variant stat multiplier for the provided stat name and the variant that the entity is.
     * @param creature The creature to check
     * @param stat The name of the stat to get the multiplier for. The entire string must be Upper Case
     * @return The stat multiplier.
     */
    public static double getVariantMultiplier(BaseCreatureEntity creature, String stat) {
        if(creature.getVariant() != null && Variant.STAT_MULTIPLIERS.containsKey(creature.getVariant().rarity.toUpperCase(Locale.ENGLISH) + "-" + stat)) {
            return Variant.STAT_MULTIPLIERS.get(creature.getVariant().rarity.toUpperCase(Locale.ENGLISH) + "-" + stat);
        }
        return 1;
    }

    /**
     * Returns a level stat multiplier for the provided stat name and the creature's current level.
     * @param stat The name of the stat to get the multiplier for. The entire string must be Upper Case
     * @param creature The creature to check
     * @param maxLevel The max level scaling can go up to
     * @return The stat multiplier.
     */
    public static double getLevelMultiplier(String stat, BaseCreatureEntity creature, int maxLevel) {
        double statLevel = Math.max(0, Math.min(maxLevel - 1, creature.getLevel() - 1));
        return 1 + (statLevel * CreatureManager.getInstance().getLevelMultiplier(stat));
    }
}
