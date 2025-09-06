package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.levelmatch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAsmodeus;
import com.lycanitesmobs.core.entity.creature.EntityAstaroth;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityAsmodeus.class,
        EntityAstaroth.class
})
public abstract class BaseCreatureEntitys_DeathMinionLevelsMixin extends BaseCreatureEntity {

    public BaseCreatureEntitys_DeathMinionLevelsMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "onDeath",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/CreatureInfo;createEntity(Lnet/minecraft/world/World;)Lnet/minecraft/entity/EntityLiving;", remap = false)
    )
    private EntityLiving lycanitestweaks_lycanitesMobsBaseCreatureEntity_spawnAllyLevels(EntityLiving minion){
        if(minion instanceof BaseCreatureEntity) ((BaseCreatureEntity)minion).applyLevel(this.getLevel());
        return minion;
    }
}
