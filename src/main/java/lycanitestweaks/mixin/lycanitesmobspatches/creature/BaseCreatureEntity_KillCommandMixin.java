package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_KillCommandMixin extends EntityLivingBase {

    @Shadow(remap = false) public abstract boolean isBoss();
    @Shadow(remap = false) public abstract boolean isRareVariant();
    @Shadow(remap = false) public int damageMax;
    @Shadow(remap = false) public float damageLimit;

    public BaseCreatureEntity_KillCommandMixin(World world) {
        super(world);
    }

    @Unique
    @Override
    public void onKillCommand(){
        if(this.isBoss() || this.isRareVariant()){
            this.damageMax = 0;
            this.damageLimit = 0;
            super.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
        }
        super.onKillCommand();
    }
}
