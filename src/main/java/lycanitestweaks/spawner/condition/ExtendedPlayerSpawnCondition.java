package lycanitestweaks.spawner.condition;

import com.google.gson.JsonObject;
import com.lycanitesmobs.core.spawner.condition.PlayerSpawnCondition;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class ExtendedPlayerSpawnCondition extends PlayerSpawnCondition {

    /** The minimum allowed y height. **/
    public int yMin = -1;
    /** The maximum allowed y height. **/
    public int yMax = -1;
    /** The minimum light level that the player must be in. **/
    public int blockLightLevelMin = -1;
    /** The maximum light level that the player must be in. **/
    public int blockLightLevelMax = -1;
    /** If true, positions that can see the sky are allowed. **/
    public boolean surface = true;
    /** If true positions that can't see the sky are allowed. **/
    public boolean underground = true;

    public int typeCheckCap = -1;
    public double typeCheckRange = 128D;
    public EnumCreatureType creatureType = null;

    @Override
    public void loadFromJSON(JsonObject json) {
        if(json.has("yMin")) this.yMin = json.get("yMin").getAsInt();
        if(json.has("yMax")) this.yMax = json.get("yMax").getAsInt();
        if(json.has("blockLightLevelMin")) this.blockLightLevelMin = json.get("blockLightLevelMin").getAsInt();
        if(json.has("blockLightLevelMax")) this.blockLightLevelMax = json.get("blockLightLevelMax").getAsInt();
        if(json.has("surface")) this.surface = json.get("surface").getAsBoolean();
        if(json.has("underground")) this.underground = json.get("underground").getAsBoolean();
        if(json.has("creatureTypeMobCap")) this.typeCheckCap = json.get("creatureTypeMobCap").getAsInt();
        if(json.has("creatureTypeCheckRange")) this.typeCheckRange = json.get("creatureTypeCheckRange").getAsDouble();
        if(json.has("creatureTypeToCheck")){
            switch (json.get("creatureTypeToCheck").getAsString().toLowerCase()) {
                case "monster":
                    this.creatureType = EnumCreatureType.MONSTER;
                    break;
                case "creature":
                    this.creatureType = EnumCreatureType.CREATURE;
                    break;
                case "ambient":
                    this.creatureType = EnumCreatureType.AMBIENT;
                    break;
                case "water_creature":
                    this.creatureType = EnumCreatureType.WATER_CREATURE;
                    break;
            }
        }
        super.loadFromJSON(json);
    }


    @Override
    public boolean isMet(World world, EntityPlayer player, BlockPos position) {
        if(!this.surface && world.canSeeSky(position)) return false;
        if(!this.underground && !world.canSeeSky(position)) return false;
        if(this.yMin != -1 && position.getY() < this.yMin) return false;
        if(this.yMax != -1 && position.getY() > this.yMax) return false;
        if(this.blockLightLevelMin != -1 && world.getLightFor(EnumSkyBlock.BLOCK, position) < blockLightLevelMin) return false;
        if(this.blockLightLevelMax != -1 && world.getLightFor(EnumSkyBlock.BLOCK, position) > this.blockLightLevelMax) return false;
        if(this.creatureType != null && !LycanitesEntityUtil.checkSpawnGroupLimit(world, position, this.typeCheckRange, this.typeCheckCap, this.creatureType)) return false;

        return super.isMet(world, player, position);
    }
}
