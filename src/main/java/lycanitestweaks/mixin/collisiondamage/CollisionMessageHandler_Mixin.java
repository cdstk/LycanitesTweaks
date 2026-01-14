package lycanitestweaks.mixin.collisiondamage;

import collision.packets.PacketCollisionS;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PacketCollisionS.CollisionMessageHandler.class)
public abstract class CollisionMessageHandler_Mixin {

    @ModifyArg(
            method = "lambda$onMessage$0",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z"),
            index = 0
    )
    private static DamageSource lycanitesTweaks_collisionDamageCollisionMessageHandler_onMessageWithInstability(DamageSource source, @Local(argsOnly = true) EntityPlayer player) {
        PotionBase instability = ObjectManager.getEffect("instability");
        if (instability != null && player.isPotionActive(instability)) {
            return new DamageSource(source.getDamageType()) {
                @Override
                public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                    return super.getDeathMessage(entityLivingBaseIn)
                            .appendSibling(new TextComponentTranslation("death.attack.collision.instability"))
                            .appendSibling(new TextComponentTranslation(instability.getName())
                            );
                }

            }.setDamageBypassesArmor();
        }
        return source;
    }
}
