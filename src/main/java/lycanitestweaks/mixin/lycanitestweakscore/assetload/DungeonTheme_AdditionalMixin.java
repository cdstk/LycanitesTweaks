package lycanitestweaks.mixin.lycanitestweakscore.assetload;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lycanitesmobs.core.dungeon.definition.DungeonTheme;
import com.lycanitesmobs.core.dungeon.definition.ThemeBlock;
import com.lycanitesmobs.core.dungeon.instance.SectorInstance;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.util.IDungeonTheme_AdditionalMixin;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(DungeonTheme.class)
public abstract class DungeonTheme_AdditionalMixin implements IDungeonTheme_AdditionalMixin {

    @Shadow(remap = false)
    public abstract IBlockState getBlockState(SectorInstance sectorInstance, char patternChar, Random random, List<ThemeBlock> blockList);

    @Unique
    private final static String BOSS_ROOM_EXIT = LycanitesTweaks.MODID + ":bossExitBlocks";
    /** A list of blocks to use for the exit of the Boss Room. Optional, defaults to iron bars. **/
    @Unique
    public List<ThemeBlock> lycanitesTweaks$bossExitBlocks = new ArrayList<>();

    @Inject(
            method = "loadFromJSON",
            at = @At(value = "TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsDungeonTheme_loadFromJSONAdditional(JsonObject json, CallbackInfo ci){
        if(json.has(BOSS_ROOM_EXIT)) {
            for(JsonElement jsonElement : json.get(BOSS_ROOM_EXIT).getAsJsonArray()) {
                ThemeBlock themeBlock = new ThemeBlock();
                themeBlock.loadFromJSON(jsonElement.getAsJsonObject());
                this.lycanitesTweaks$bossExitBlocks.add(themeBlock);
            }
        }
        else {
            ThemeBlock themeBlock = new ThemeBlock();
            themeBlock.blockId = "minecraft:iron_bars";
            this.lycanitesTweaks$bossExitBlocks.add(themeBlock);
        }
    }

    @Unique
    public IBlockState lycanitesTweaks$getBossWall(char patternChar, Random random){
        return this.getBlockState(null, patternChar, random, this.lycanitesTweaks$bossExitBlocks);
    }
}
