package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.petflag;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.pets.SummonSet;
import lycanitestweaks.util.ISummonSet_TargetFlagMixin;
import lycanitestweaks.util.ITameableCreatureEntity_TargetFlagMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SummonSet.class)
public abstract class SummonSet_FlagMixin implements ISummonSet_TargetFlagMixin {

    // BEHAVIOUR_ID byte is referenced elsewhere and only has 2 free bits to use

    @Unique
    private boolean lycanitesTweaks$doGriefing = false;
    @Unique
    private boolean lycanitesTweaks$targetBoss = false;

    @Inject(
            method = "applyBehaviour",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsSummonSet_applyBehaviourPetFlags(TameableCreatureEntity minion, CallbackInfo ci){
        if(minion instanceof ITameableCreatureEntity_TargetFlagMixin) {
            ((ITameableCreatureEntity_TargetFlagMixin) minion).lycanitesTweaks$setTargetBoss(this.lycanitesTweaks$shouldTargetBoss());
            ((ITameableCreatureEntity_TargetFlagMixin) minion).lycanitesTweaks$setDoGrief(this.lycanitesTweaks$shouldDoGrief());
        }
    }

    @Inject(
            method = "updateBehaviour",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsSummonSet_updateBehaviourPetFlags(TameableCreatureEntity minion, CallbackInfo ci){
        if(minion instanceof ITameableCreatureEntity_TargetFlagMixin){
            this.lycanitesTweaks$doGriefing = ((ITameableCreatureEntity_TargetFlagMixin) minion).lycanitesTweaks$shouldDoGrief();
            this.lycanitesTweaks$targetBoss = ((ITameableCreatureEntity_TargetFlagMixin) minion).lycanitesTweaks$shouldTargetBoss();
        }
    }

    @Inject(
            method = "setBehaviourByte",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsSummonSet_setBehaviourBytePetFlags(byte behaviour, CallbackInfo ci){
        this.lycanitesTweaks$doGriefing = (behaviour & ISummonSet_TargetFlagMixin.BEHAVIOUR_DO_GRIEF) < 0;
        this.lycanitesTweaks$targetBoss = (behaviour & ISummonSet_TargetFlagMixin.BEHAVIOUR_TARGET_BOSS) > 0;
    }

    @ModifyReturnValue(
            method = "getBehaviourByte",
            at = @At("RETURN"),
            remap = false
    )
    private byte lycanitesTweaks_lycanitesMobsSummonSet_getBehaviourBytePetFlags(byte original){
        if(this.lycanitesTweaks$shouldDoGrief()) original += ISummonSet_TargetFlagMixin.BEHAVIOUR_DO_GRIEF;
        if(this.lycanitesTweaks$shouldTargetBoss()) original += ISummonSet_TargetFlagMixin.BEHAVIOUR_TARGET_BOSS;
        return original;
    }

    @Override
    public boolean lycanitesTweaks$shouldTargetBoss() {
        return this.lycanitesTweaks$targetBoss;
    }

    @Override
    public void lycanitesTweaks$setTargetBoss(boolean set){
        this.lycanitesTweaks$targetBoss = set;
    }

    @Override
    public boolean lycanitesTweaks$shouldDoGrief() {
        return this.lycanitesTweaks$doGriefing;
    }

    @Override
    public void lycanitesTweaks$setDoGrief(boolean set){
        this.lycanitesTweaks$doGriefing = set;
    }
}
