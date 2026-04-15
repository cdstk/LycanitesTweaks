package lycanitestweaks.mixin.vanilla.client;

import lycanitestweaks.util.IBossInfo_LycanitesBossMixin;
import lycanitestweaks.util.LycanitesMobsWrapper;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(BossInfoClient.class)
public abstract class BossInfoClient_LycanitesMixin extends BossInfo implements IBossInfo_LycanitesBossMixin {

    @Unique
    EntityLiving lycanitesTweaks$lycanitesBoss = null;

    public BossInfoClient_LycanitesMixin(UUID uniqueIdIn, ITextComponent nameIn, BossInfo.Color colorIn, BossInfo.Overlay overlayIn) {
        super(uniqueIdIn, nameIn, colorIn, overlayIn);
    }

    @Unique
    @Override
    public boolean lycanitesTweaks$isLycanitesBoss() {
        return LycanitesMobsWrapper.isLycanitesEntity(this.lycanitesTweaks$lycanitesBoss);
    }

    @Unique
    @Override
    public void lycanitesTweaks$setLycanitesBoss(EntityLiving entity) {
        if(LycanitesMobsWrapper.isLycanitesEntity(entity)) {
            this.lycanitesTweaks$lycanitesBoss = entity;
        }
    }

    @Unique
    @Override
    public EntityLiving lycanitesTweaks$getLycanitesEntity() {
        return this.lycanitesTweaks$lycanitesBoss;
    }
}
