package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_DPSLimitHPScaleMixin extends EntityLiving {

    @Shadow(remap = false)
    public int damageMax;
    @Shadow(remap = false)
    public float damageLimit;

    public BaseCreatureEntity_DPSLimitHPScaleMixin(World world) {
        super(world);
    }

    @Unique
    @Override
    public void setHealth(float health) {
        if(this.damageLimit != 0F) {
            IAttributeInstance maxHealth = this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

            double dpsLimit = BaseCreatureEntity.BOSS_DAMAGE_LIMIT;
            for (AttributeModifier modifier : maxHealth.getModifiersByOperation(1)) {
                dpsLimit += BaseCreatureEntity.BOSS_DAMAGE_LIMIT * modifier.getAmount();
            }
            for (AttributeModifier modifier : maxHealth.getModifiersByOperation(2)) {
                dpsLimit *= 1.0D + modifier.getAmount();
            }
            if (dpsLimit > BaseCreatureEntity.BOSS_DAMAGE_LIMIT) {
                this.damageMax = (int) dpsLimit;
                this.damageLimit = (float) dpsLimit;
            }
        }

        super.setHealth(health);
    }
}
