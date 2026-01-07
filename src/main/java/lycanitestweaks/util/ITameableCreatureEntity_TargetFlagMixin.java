package lycanitestweaks.util;

import lycanitestweaks.LycanitesTweaks;

public interface ITameableCreatureEntity_TargetFlagMixin {

    byte PET_AI_TARGET_BOSS =  (byte) 1;
    byte PET_ABILITY_DO_GRIEF = (byte) 2;
    byte PET_ABILITY_INV_LEVELING = (byte) 4;
    byte PET_COMMAND_TARGET_BOSS = (byte) 12;
    byte PET_COMMAND_DO_GRIEF = (byte) 13;
    byte PET_COMMAND_INV_LEVELING = (byte) 14;
    String NBT_TARGET_BOSS = LycanitesTweaks.MODID + "_TargetBoss";
    String NBT_DO_GRIEF = LycanitesTweaks.MODID + "_DoGrief";
    String NBT_INVENTORY_LEVELING = LycanitesTweaks.MODID + "_InventoryLeveling";

    boolean lycanitesTweaks$shouldTargetBoss();
    void lycanitesTweaks$setTargetBoss(boolean set);

    boolean lycanitesTweaks$shouldDoGrief();
    void lycanitesTweaks$setDoGrief(boolean set);

    boolean lycanitesTweaks$shouldInventoryLevelup();
    void lycanitesTweaks$setInventoryLevelup(boolean set);
}
