package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.CustomProjectileEntity;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CustomProjectileEntity.class)
public abstract class CustomProjectileEntity_NameLookupMixin extends BaseProjectileEntity {

    public CustomProjectileEntity_NameLookupMixin(World world) {
        super(world);
    }

    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        else {
            String langKey = "entity." + this.entityName;
            if(!I18n.canTranslate(langKey)) {
                langKey += ".name";
            }
            return I18n.translateToLocal(langKey);
        }
    }
}
