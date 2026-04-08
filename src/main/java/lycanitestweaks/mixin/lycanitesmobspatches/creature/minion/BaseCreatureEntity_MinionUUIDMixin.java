package lycanitestweaks.mixin.lycanitesmobspatches.creature.minion;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_MinionUUIDMixin extends EntityLiving {

    @Unique
    private final List<UUID> lycanitesTweaks$possibleMinionUUIDs = new ArrayList<>();

    @Shadow(remap = false) public abstract boolean addMinion(EntityLivingBase minion);

    public BaseCreatureEntity_MinionUUIDMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("HEAD")
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onLivingUpdateLoadPossibleMinions(CallbackInfo ci){
        if(this.lycanitesTweaks$possibleMinionUUIDs.isEmpty()) return;
        this.lycanitesTweaks$possibleMinionUUIDs.forEach(uuid -> {
            MinecraftServer server = this.getServer();
            if(server != null) {
                Entity entity = server.getEntityFromUuid(uuid);
                if(entity != null) {
                    this.lycanitesTweaks$addSavedMinion(entity);
                }
            }
        });
        this.lycanitesTweaks$possibleMinionUUIDs.clear();
    }

    @ModifyExpressionValue(
            method = "readEntityFromNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z", ordinal = 32)
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_readEntityFromNBTReplaceID(boolean original){
        return false;
    }

    @Inject(
            method = "readEntityFromNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z", ordinal = 32)
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_readEntityFromNBTAddMinions(NBTTagCompound nbtTagCompound, CallbackInfo ci, @Local(name = "minionId") NBTTagCompound minionId){
        MinecraftServer server = this.getServer();
        UUID minionUUID = minionId.getUniqueId(LycanitesEntityUtil.NBT_TAG_UUID);
        if(server != null && minionUUID != null) {
            Entity entity = server.getEntityFromUuid(minionUUID);
            if(entity == null) {
                this.lycanitesTweaks$possibleMinionUUIDs.add(minionUUID);
            }
            else {
                this.lycanitesTweaks$addSavedMinion(entity);
            }
        }
    }

    @ModifyExpressionValue(
            method = "writeEntityToNBT",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;minions:Ljava/util/List;", remap = false)
    )
    private List<EntityLivingBase> lycanitesTweaks_lycanitesMobsBaseCreatureEntity_writeEntityToNBTNullMinions(List<EntityLivingBase> original){
        return original.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    @WrapOperation(
            method = "writeEntityToNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setInteger(Ljava/lang/String;I)V", ordinal = 11)
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_writeEntityToNBTSaveUUID(NBTTagCompound minionId, String tagKey, int minionID, Operation<Void> original, @Local EntityLivingBase minion){
        minionId.setUniqueId(LycanitesEntityUtil.NBT_TAG_UUID, minion.getUniqueID());
    }

    @Unique
    private void lycanitesTweaks$addSavedMinion(Entity entity){
        if(entity instanceof EntityLivingBase) {
            this.addMinion((EntityLivingBase) entity);
        }
        if(entity instanceof BaseCreatureEntity) {
            ((BaseCreatureEntity) entity).setMasterTarget(this);
        }
    }
}
