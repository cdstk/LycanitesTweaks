package lycanitestweaks.compat.bloodmoon.spawner.condition;

import com.lycanitesmobs.core.spawner.condition.SpawnCondition;
import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BloodMoonSpawnCondition extends SpawnCondition {

    public boolean isMet(World world, EntityPlayer player, BlockPos position) {
        return BloodmoonHandler.INSTANCE.isBloodmoonActive();
    }
}
