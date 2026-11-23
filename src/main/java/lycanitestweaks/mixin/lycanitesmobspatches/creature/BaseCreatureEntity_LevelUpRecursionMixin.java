package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.CreatureStats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_LevelUpRecursionMixin extends EntityLiving {

    @Shadow(remap = false) @Final protected static DataParameter<Integer> EXPERIENCE;

    @Shadow(remap = false) public CreatureStats creatureStats;
    @Shadow(remap = false) protected int experience;

    @Shadow(remap = false) public abstract int getLevel();
    @Shadow(remap = false) public abstract void applyLevel(int level);
    @Shadow(remap = false) public abstract void setLevel(int level);
    @Shadow(remap = false) public abstract void addLevel(int level);

    public BaseCreatureEntity_LevelUpRecursionMixin(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "updateLevelExperience",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_updateLevelExperienceFixRecursion(CallbackInfo ci){
        if(!this.getEntityWorld().isRemote) {
            // Process loop the raw data except getExperienceForNextLevel requirements
            int nextRequirement = this.creatureStats.getExperienceForNextLevel();
            while(this.experience >= nextRequirement) {
                this.setLevel(this.getLevel() + 1);
                this.experience -= nextRequirement;
                nextRequirement = this.creatureStats.getExperienceForNextLevel();
            }
            // Data sync when done
            this.applyLevel(this.getLevel());
            this.dataManager.set(EXPERIENCE, this.experience);
        }
        ci.cancel();
    }
}
