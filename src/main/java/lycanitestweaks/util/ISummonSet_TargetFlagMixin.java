package lycanitestweaks.util;

public interface ISummonSet_TargetFlagMixin {

    // The two free bytes
    byte BEHAVIOUR_TARGET_BOSS = (byte) 64;
    byte BEHAVIOUR_DO_GRIEF = (byte) -128;

    boolean lycanitesTweaks$shouldTargetBoss();
    void lycanitesTweaks$setTargetBoss(boolean set);

    boolean lycanitesTweaks$shouldDoGrief();
    void lycanitesTweaks$setDoGrief(boolean set);

    // Can't sync without expanding the update packet
//    boolean lycanitesTweaks$shouldInventoryLevelup();
//    void lycanitesTweaks$setInventoryLevelup(boolean set);
}
