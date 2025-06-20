package lycanitestweaks.mixin.lycanitestweaksmajor.beastiaryclient;

import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.IndexBeastiaryScreen;
import lycanitestweaks.capability.IPlayerMobLevelCapability;
import lycanitestweaks.capability.PlayerMobLevelCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.ForgeConfigProvider;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(IndexBeastiaryScreen.class)
public abstract class IndexBeastiaryScreenMixin extends BeastiaryScreen {

    @Unique
    private final HashMap<PlayerMobLevelsConfig.BonusCategory, Integer> lycanitesTweaks$pmlBonusCateogories = new HashMap<>();

    public IndexBeastiaryScreenMixin(EntityPlayer player) {
        super(player);
    }

    @Inject(
            method = "drawForeground",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsIndexBeastiaryScreen_drawForegroundPML(int mouseX, int mouseY, float partialTicks, CallbackInfo ci){
        if(!ForgeConfigHandler.clientFeaturesMixinConfig.beastiaryGUIPML) return;

        int xOffset = this.colLeftX;
        int yOffset = this.colLeftY;
        if(this.player.ticksExisted % 20 == 0){
            IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(this.player);
            if(pml != null) {
                for (PlayerMobLevelsConfig.BonusCategory category : PlayerMobLevelsConfig.getPmlBonusCategories().keySet()) {
                    lycanitesTweaks$pmlBonusCateogories.put(category, pml.getTotalLevelsForCategory(category, null, true));
                }
            }
        }
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(this.player);
        if(pml != null) {
            String text = I18n.format("gui.beastiary.index.mixin.category");
            this.getFontRenderer().drawString(text, xOffset, yOffset, 0xFFFFFF, true);
            yOffset += 4 + this.getFontRenderer().getWordWrappedHeight(text, this.colLeftWidth);
            for(PlayerMobLevelsConfig.BonusCategory category : ForgeConfigProvider.getPmlBonusCategoryClientRenderOrder()){
                if(lycanitesTweaks$pmlBonusCateogories.containsKey(category)){
                    if(PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(category))
                        this.drawTexture(new ResourceLocation(LycanitesMobs.modid, "textures/items/soulgazer.png"),xOffset - 20, yOffset - 4, 0, 1, 1, 16 ,16);
                    text = I18n.format("gui.beastiary.index.mixin." + category.name(), lycanitesTweaks$pmlBonusCateogories.get(category));
                    this.getFontRenderer().drawString(text, xOffset, yOffset, 0xFFFFFF, true);
                    yOffset += 4 + this.getFontRenderer().getWordWrappedHeight("", this.colLeftWidth);
                }
            }
            text = I18n.format("gui.beastiary.index.mixin.deathcooldown", pml.getDeathCooldown() / 20);
            this.getFontRenderer().drawString(text, xOffset, yOffset, 0xFFFFFF, true);
        }
    }
}
