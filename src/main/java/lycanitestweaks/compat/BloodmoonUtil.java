package lycanitestweaks.compat;

import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;
import java.util.Set;

public abstract class BloodmoonUtil {

    // Samples valid players for compat with Scaling Health Issues

    private static final Set<EntityPlayer> EXPERIENCING_PLAYER = new HashSet<>();

    public static boolean canExperienceBloodmoon(EntityPlayer player) {
        return BloodmoonHandler.INSTANCE.isBloodmoonActive() && EXPERIENCING_PLAYER.contains(player);
    }

    public static void addExperiencing(EntityPlayer entityPlayer) {
        EXPERIENCING_PLAYER.add(entityPlayer);
    }

    public static void resetExperiencing() {
        EXPERIENCING_PLAYER.clear();
    }
}
