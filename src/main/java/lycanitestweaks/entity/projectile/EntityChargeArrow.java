package lycanitestweaks.entity.projectile;

import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityChargeArrow extends EntityArrow {

    public EntityChargeArrow(World world) {
        super(world);
        this.setDamage(0);
        this.setSilent(true);
    }

    public EntityChargeArrow(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.setDamage(0);
        this.setSilent(true);
    }

    public EntityChargeArrow(World world, EntityLivingBase shooter) {
        super(world, shooter);
        this.setDamage(0);
        this.setSilent(true);
    }

    public EntityChargeArrow(World world, EntityLivingBase shooter, BaseProjectileEntity projectile) {
        super(world, shooter);
        this.setSilent(true);
        this.setDamage(projectile.damage);
    }

    @Override
    public void onUpdate() {
        this.setDead();
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
}
