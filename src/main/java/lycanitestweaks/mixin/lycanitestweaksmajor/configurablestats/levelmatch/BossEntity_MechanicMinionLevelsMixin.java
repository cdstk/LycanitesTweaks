package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.levelmatch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAsmodeus;
import com.lycanitesmobs.core.entity.creature.EntityRahovart;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityAsmodeus.class,
        EntityRahovart.class
})
public abstract class BossEntity_MechanicMinionLevelsMixin extends BaseCreatureEntity {

    public BossEntity_MechanicMinionLevelsMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "updatePhases",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/CreatureInfo;createEntity(Lnet/minecraft/world/World;)Lnet/minecraft/entity/EntityLiving;"),
            remap = false
    )
    private EntityLiving lycanitestweaks_lycanitesMobsBossEntity_updatePhasesMinionLevels(EntityLiving minion){
        if(minion instanceof BaseCreatureEntity) ((BaseCreatureEntity)minion).applyLevel(this.getLevel());
        return minion;
    }
}
