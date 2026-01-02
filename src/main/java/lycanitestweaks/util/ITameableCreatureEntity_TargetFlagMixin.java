package lycanitestweaks.util;

import lycanitestweaks.LycanitesTweaks;

public interface ITameableCreatureEntity_TargetFlagMixin {

    byte PET_AI_TARGET_BOSS =  (byte) 1;
    byte PET_ABILITY_DO_GRIEF = (byte) 2;
    byte PET_COMMAND_TARGET_BOSS = (byte) 12;
    byte PET_COMMAND_DO_GRIEF = (byte) 13;
    String NBT_TARGET_BOSS = LycanitesTweaks.MODID + "_TargetBoss";
    String NBT_DO_GRIEF = LycanitesTweaks.MODID + "_DoGrief";

    boolean lycanitesTweaks$shouldTargetBoss();
    void lycanitesTweaks$setTargetBoss(boolean set);

    boolean lycanitesTweaks$shouldDoGrief();
    void lycanitesTweaks$setDoGrief(boolean set);
}
