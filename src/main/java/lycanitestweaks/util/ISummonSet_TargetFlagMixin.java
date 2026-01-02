package lycanitestweaks.util;

public interface ISummonSet_TargetFlagMixin {

    byte BEHAVIOUR_TARGET_BOSS = (byte) 64;
    byte BEHAVIOUR_DO_GRIEF = (byte) -128;

    boolean lycanitesTweaks$shouldTargetBoss();
    void lycanitesTweaks$setTargetBoss(boolean set);

    boolean lycanitesTweaks$shouldDoGrief();
    void lycanitesTweaks$setDoGrief(boolean set);
}
