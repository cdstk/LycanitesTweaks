package lycanitestweaks.mixin.lycanitestweakscore.assetload;

import com.google.gson.JsonObject;
import com.lycanitesmobs.core.spawner.MobSpawn;
import lycanitestweaks.LycanitesTweaks;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import static net.minecraft.world.chunk.storage.AnvilChunkLoader.readWorldEntityPos;

@Mixin(MobSpawn.class)
public abstract class MobSpawn_AdditionalMixin {

    @Unique
    private final static String SET_NBT = LycanitesTweaks.MODID + ":setNBT";
    @Unique
    private final static String DO_INITIAL_SPAWN = LycanitesTweaks.MODID + ":doInitialSpawn";
    @Unique
    private String lycanitesTweaks$nbtString = "";
    @Unique
    private Boolean lycanitesTweaks$doInitialSpawn = null;

    @Inject(
            method = "loadFromJSON",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsMobSpawn_loadFromJSONSetNBT(JsonObject json, CallbackInfo ci){
        if (json.has(SET_NBT)) {
            this.lycanitesTweaks$nbtString = json.get(SET_NBT).getAsString();
        }
        if (json.has(DO_INITIAL_SPAWN)) {
            this.lycanitesTweaks$doInitialSpawn = json.get(DO_INITIAL_SPAWN).getAsBoolean();
        }
    }

    // Based on CommandEntityData
    @Inject(
            method = "onSpawned",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsMobSpawn_onSpawnedSetNBT(EntityLiving entityLiving, EntityPlayer player, CallbackInfo ci){
        if (lycanitesTweaks$doInitialSpawn != null && !this.lycanitesTweaks$doInitialSpawn) {
            entityLiving.onInitialSpawn(entityLiving.getEntityWorld().getDifficultyForLocation(new BlockPos(entityLiving)), null);
        }
        if(!this.lycanitesTweaks$nbtString.isEmpty()){
            NBTTagCompound entityNBT = CommandBase.entityToNBT(entityLiving);
            NBTTagCompound copyNBT = entityNBT.copy();
            NBTTagCompound jsonNBT = new NBTTagCompound();

            try {
                jsonNBT = JsonToNBT.getTagFromJson(this.lycanitesTweaks$nbtString);
            }
            catch (NBTException nbtexception) {
                LycanitesTweaks.LOGGER.log(Level.WARN,"Failed to read NBT from JSON string: {}", nbtexception.getMessage());
            }

            UUID uuid = entityLiving.getUniqueID();
            entityNBT.merge(jsonNBT);
            entityLiving.setUniqueId(uuid);

            if (entityNBT.equals(copyNBT)) {
                LycanitesTweaks.LOGGER.log(Level.WARN,"Entity NBT already equals JSON NBT: {}", entityNBT.toString());
            }
            else {
                entityLiving.readFromNBT(entityNBT);
                if (entityNBT.hasKey("Passengers", 9)) {
                    NBTTagList nbttaglist = entityNBT.getTagList("Passengers", 10);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        BlockPos pos = entityLiving.getPosition();
                        Entity passenger = readWorldEntityPos(nbttaglist.getCompoundTagAt(i), entityLiving.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), true);

                        if (passenger != null) {
                            passenger.startRiding(entityLiving, true);
                        }
                    }
                }
            }
        }
        if (lycanitesTweaks$doInitialSpawn != null) {
            if (this.lycanitesTweaks$doInitialSpawn) {
                entityLiving.onInitialSpawn(entityLiving.getEntityWorld().getDifficultyForLocation(new BlockPos(entityLiving)), null);
            }
            entityLiving.setHealth(entityLiving.getMaxHealth());
        }
    }
}
