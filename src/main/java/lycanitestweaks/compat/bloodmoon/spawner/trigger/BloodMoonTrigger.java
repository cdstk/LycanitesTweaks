package lycanitestweaks.compat.bloodmoon.spawner.trigger;

import com.lycanitesmobs.core.spawner.Spawner;
import com.lycanitesmobs.core.spawner.trigger.WorldSpawnTrigger;
import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BloodMoonTrigger extends WorldSpawnTrigger {

    /** Constructor **/
    public BloodMoonTrigger(Spawner spawner) {
        super(spawner);
    }

    /** Called every world tick from where players are active. **/
    public void onTick(World world, BlockPos position, long ticks) {
        if(!BloodmoonHandler.INSTANCE.isBloodmoonActive()) return;

        super.onTick(world, position, ticks);
    }
}
