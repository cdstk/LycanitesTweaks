package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityTroll;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityTroll.class)
public abstract class EntityTroll_GriefSetMixin extends TameableCreatureEntity {

    @Shadow(remap = false)
    public boolean griefing;

    public EntityTroll_GriefSetMixin(World world) {
        super(world);
    }

    @Unique
    @Override
    public void setPVP(boolean set) {
        super.setPVP(set);
        this.griefing = set && this.creatureInfo.getFlag("griefing", this.griefing);
    }
}
