package lycanitestweaks.mixin.battletowers;

import atomicstryker.battletowers.common.AS_EntityGolem;
import lycanitestweaks.util.IAS_EntityGolem_ModifyMixin;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AS_EntityGolem.class)
public abstract class AS_EntityGolem_ModifyMixin extends EntityMob implements IAS_EntityGolem_ModifyMixin {

    @Shadow(remap = false) @Final private static DataParameter<Boolean> AWAKE;
    @Shadow(remap = false) private int towerID;
    @Shadow(remap = false) protected abstract void updateGolemType();

    public AS_EntityGolem_ModifyMixin(World world) {
        super(world);
    }

    @Unique
    @Override
    public void lycanitesTweaks$modifyTowerType(int towerType) {
        this.towerID = towerType;
        this.updateGolemType();
    }

    @Unique
    @Override
    public void lycanitesTweaks$setAwake(boolean awake) {
        this.dataManager.set(AWAKE, awake);
    }
}
