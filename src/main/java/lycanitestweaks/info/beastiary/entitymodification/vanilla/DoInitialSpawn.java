package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import com.google.gson.JsonObject;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class DoInitialSpawn extends AbstractEntityModification {

    @Override
    public boolean isDefaultModification() {
        return false;
    }

    @Override
    public boolean takesUserInput() {
        return false;
    }

    @Override
    public boolean refreshEntityData() {
        return true;
    }

    @Override
    public String getOptionLangKey() {
        return "Respawn";
    }

    @Override
    public String getActionLangKey() {
        return "";
    }

    @Override
    public void generateDefaultJSON(JsonObject json) {

    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityLiving) {
            EntityPlayer player = LycanitesTweaks.PROXY.getClientPlayer();
            if(player != null) ((EntityLiving) entity).onInitialSpawn(player.getEntityWorld().getDifficultyForLocation(player.getPosition()), null);
        }
    }
}
