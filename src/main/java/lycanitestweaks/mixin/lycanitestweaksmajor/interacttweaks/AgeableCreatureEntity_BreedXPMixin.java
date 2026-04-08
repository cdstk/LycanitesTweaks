package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableCreatureEntity.class)
public abstract class AgeableCreatureEntity_BreedXPMixin extends BaseCreatureEntity {

    public AgeableCreatureEntity_BreedXPMixin(World world) {
        super(world);
    }

    @Inject(
            method = "procreate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    private void lycanitesTweaks_lycanitesAgeableCreatureEntity_procreateDropXP(AgeableCreatureEntity partner, CallbackInfo ci){
        if (this.world.getGameRules().getBoolean("doMobLoot")){
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, this.getRNG().nextInt(7) + 1));
        }
    }
}
