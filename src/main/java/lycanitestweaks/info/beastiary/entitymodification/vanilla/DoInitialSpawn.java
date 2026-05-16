package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class DoInitialSpawn extends AbstractToggleState {

    public static final String TYPE_VALUE = "onInitialSpawn";

    @Override
    public boolean refreshEntityData() {
        return true;
    }

    @Override
    public String getOptionLangKey() {
        return "gui.bestiary.button.respawn";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityLiving) {
            EntityPlayer player = LycanitesTweaks.PROXY.getClientPlayer();
            if(player != null) ((EntityLiving) entity).onInitialSpawn(player.getEntityWorld().getDifficultyForLocation(player.getPosition()), null);
        }
    }
}
