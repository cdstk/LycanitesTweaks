package lycanitestweaks.compat.bloodmoon.spawner.trigger;

import com.google.gson.JsonObject;
import com.lycanitesmobs.core.spawner.Spawner;
import com.lycanitesmobs.core.spawner.trigger.PlayerSpawnTrigger;
import lumien.bloodmoon.server.BloodmoonHandler;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class BloodMoonTrigger extends PlayerSpawnTrigger {

    // Reset onTick
    // Flagged on success
    // Denied after all conditions
    public boolean configOnce = false;
    public boolean didOnce = false;

    /**
     * Constructor
     **/
    public BloodMoonTrigger(Spawner spawner) {
        super(spawner);
    }

    @Override
    public void loadFromJSON(JsonObject json) {
        if (json.has("oneSuccess")) {
            this.configOnce = json.get("oneSuccess").getAsBoolean();
        }
        super.loadFromJSON(json);
    }

    /**
     * Called every player tick.
     **/
    public void onTick(EntityPlayer player, long ticks) {
        if (!BloodmoonHandler.INSTANCE.isBloodmoonActive()) {
            if(this.didOnce){
                if(this.configOnce && ForgeConfigHandler.debug.debugLoggerAutomatic) LycanitesTweaks.LOGGER.log(Level.INFO, "Bloodmoon Trigger Reset: {} for: {}", this, this.spawner.name);
                this.didOnce = false;
            }
            return;
        }

        super.onTick(player, ticks);
    }

    @Override
    public boolean trigger(World world, EntityPlayer player, BlockPos triggerPos, int level, int chain) {
        boolean successfulSpawn = super.trigger(world, player, triggerPos, level, chain);
        if(successfulSpawn){
            if(this.configOnce && ForgeConfigHandler.debug.debugLoggerAutomatic) LycanitesTweaks.LOGGER.log(Level.INFO, "Bloodmoon Trigger Fired: {} for: {}", this, this.spawner.name);
            this.didOnce = true;
        }
        return successfulSpawn;
    }

    @Override
    public boolean triggerConditionsMet(World world, EntityPlayer player, BlockPos triggerPos) {
        if(this.configOnce && this.didOnce){
            if(ForgeConfigHandler.debug.debugLoggerAutomatic) LycanitesTweaks.LOGGER.log(Level.INFO, "Bloodmoon Trigger Already Spawned: {} for: {}", this, this.spawner.name);
            return false;
        }
        return super.triggerConditionsMet(world, player, triggerPos);
    }

    @Override
    public void applyToEntity(EntityLiving entityLiving) {
        super.applyToEntity(entityLiving);
        if(entityLiving != null)
            entityLiving.getEntityData().setBoolean("bloodmoonSpawned", true);
    }
}