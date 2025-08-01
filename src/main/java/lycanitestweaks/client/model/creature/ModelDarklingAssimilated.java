package lycanitestweaks.client.model.creature;

import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.client.model.template.ModelTemplateInsect;
import com.lycanitesmobs.core.entity.creature.EntityDarkling;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDarklingAssimilated extends ModelTemplateInsect {

	// ==================================================
  	//                    Constructors
  	// ==================================================
    public ModelDarklingAssimilated() {
        this(1.0F);
    }

    public ModelDarklingAssimilated(float shadowSize) {
    	// Load Model:
        this.initModel("darkling_assimilated", LycanitesMobs.modInfo, "entity/darkling");

        // Scaling:
    	this.lookHeadScaleX = 0;
		this.lookHeadScaleY = 0;
		this.mouthScaleX = 1F;
		this.mouthScaleY = 0.1F;

    	// Trophy:
        this.trophyScale = 1.0F;
    }


	// ==================================================
	//                 Animate Part
	// ==================================================
	@Override
	public void animatePart(String partName, EntityLiving entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
		super.animatePart(partName, entity, time, distance, loop, lookY, lookX, scale);

		// Bobbing/Latching:
		if(partName.equals("body") && entity instanceof EntityDarkling) {
			if(((EntityDarkling)entity).hasLatchTarget()) {
				this.rotate(-90F, 0, 0);
			}
			else {
				float bob = MathHelper.cos(time * 0.6662F + (float) Math.PI) * 0.3F * distance;
				if (bob < 0)
					bob += -bob * 2;
				translate(0, bob, 0);
			}
		}
	}
}
