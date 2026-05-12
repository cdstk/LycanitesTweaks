package lycanitestweaks.compat.entitymodification.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;

public class INFFlying extends AbstractToggleState {

    public static final String TYPE_VALUE = ModLoadedUtil.ICEANDFIRE_MODID + ":flying";

    @Override
    public String getOptionLangKey() {
        return "creature.status.flying";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityAmphithere) {
            EntityAmphithere amphithere = (EntityAmphithere) entity;
            if(amphithere.isFlying()) {
                amphithere.onGround = true;
                amphithere.setFlying(false);
            }
            else {
                amphithere.onGround = false;
                amphithere.setFlying(true);
            }
        }
        else if(entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            if(dragon.isFlying()) {
                dragon.onGround = true;
                dragon.setHovering(false);
                dragon.setFlying(false);
            }
            else {
                dragon.onGround = false;
                dragon.setHovering(true);
                dragon.setFlying(true);
            }
        }
        else if(entity instanceof EntityHippogryph) {
            EntityHippogryph hippogryph = (EntityHippogryph) entity;
            if(hippogryph.isFlying()) {
                hippogryph.onGround = true;
                hippogryph.setHovering(false);
                hippogryph.setFlying(false);
            }
            else {
                hippogryph.onGround = false;
                hippogryph.setHovering(true);
                hippogryph.setFlying(true);
            }
        }
        else if(entity instanceof EntityMyrmexRoyal) {
            EntityMyrmexRoyal myrmexRoyal = (EntityMyrmexRoyal) entity;
            if(myrmexRoyal.isFlying()) {
                myrmexRoyal.onGround = true;
                myrmexRoyal.setFlying(false);
            }
            else {
                myrmexRoyal.onGround = false;
                myrmexRoyal.setFlying(true);
            }
        }
        else if(entity instanceof EntityStymphalianBird) {
            EntityStymphalianBird stymphalianBird = (EntityStymphalianBird) entity;
            if(stymphalianBird.isFlying()) {
                stymphalianBird.onGround = true;
                stymphalianBird.setFlying(false);
            }
            else {
                stymphalianBird.onGround = false;
                stymphalianBird.setFlying(true);
            }
        }
    }
}
