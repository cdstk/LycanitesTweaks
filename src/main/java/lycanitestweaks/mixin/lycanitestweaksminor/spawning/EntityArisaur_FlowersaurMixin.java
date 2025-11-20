package lycanitestweaks.mixin.lycanitestweaksminor.spawning;

import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityArisaur;
import lycanitestweaks.handlers.ForgeConfigProvider;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityArisaur.class)
public abstract class EntityArisaur_FlowersaurMixin extends AgeableCreatureEntity {

    public EntityArisaur_FlowersaurMixin(World world) {
        super(world);
    }

    @Unique
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData) {
        if(this.getVariantIndex() == 0 && ForgeConfigProvider.getFlowersaurBiomes().contains(this.world.getBiome(this.getPosition()).getRegistryName())){
            this.setCustomNameTag("Flowersaur");
        }
        return super.onInitialSpawn(difficulty, livingData);
    }
}
