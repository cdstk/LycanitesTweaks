package lycanitestweaks.compat;

import meldexun.reachfix.util.ReachFixUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class ReachFixHandler {

    public static double getEntityReach(EntityPlayer player, EnumHand hand) {
        return ReachFixUtil.getEntityReach(player, hand);
    }
}
