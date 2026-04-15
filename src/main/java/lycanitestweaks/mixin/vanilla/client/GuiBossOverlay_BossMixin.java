package lycanitestweaks.mixin.vanilla.client;

import lycanitestweaks.util.IBossInfo_LycanitesBossMixin;
import lycanitestweaks.util.IGuiBossOverlay_LycanitesBossMixin;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.UUID;

@Mixin(GuiBossOverlay.class)
public abstract class GuiBossOverlay_BossMixin implements IGuiBossOverlay_LycanitesBossMixin {

    @Shadow @Final private Map<UUID, BossInfoClient> mapBossInfos;

    @Unique
    @Override
    public void lycanitesTweaks$updateLycanitesBossInfo(UUID uuid, EntityLiving boss) {
        if(this.mapBossInfos.containsKey(uuid)) {
            if(this.mapBossInfos.get(uuid) instanceof IBossInfo_LycanitesBossMixin) {
                IBossInfo_LycanitesBossMixin lycanitesBossInfo = (IBossInfo_LycanitesBossMixin) this.mapBossInfos.get(uuid);
                lycanitesBossInfo.lycanitesTweaks$setLycanitesBoss(boss);
            }
        }
    }
}
