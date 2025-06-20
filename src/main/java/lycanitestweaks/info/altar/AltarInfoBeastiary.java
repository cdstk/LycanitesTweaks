package lycanitestweaks.info.altar;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.info.AltarInfo;
import com.lycanitesmobs.core.info.CreatureKnowledge;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class AltarInfoBeastiary extends AltarInfo implements IAltarNoBoost{

    private final Block coreBlock;
    private final Block bodyBlock;
    private final int width;
    private final int height;

    public AltarInfoBeastiary(String name) {
        super(name);
        this.coreBlock = Blocks.REDSTONE_BLOCK;
        this.bodyBlock = Blocks.OBSIDIAN;
        this.width = this.height = ForgeConfigHandler.server.altarsConfig.beastiaryAltarObsidian;
    }

    // ==================================================
    //                     Checking
    // ==================================================
    /** Called first when checking for a valid altar, this should be fairly lightweight such as just checking if the first block checked is valid, a more in depth check if then done after. **/
    @Override
    public boolean quickCheck(Entity entity, World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this.coreBlock;
    }

    /** Called if the QuickCheck() is passed, this should check the entire altar structure and if true is returned, the altar will activate. **/
    @Override
    public boolean fullCheck(Entity entity, World world, BlockPos pos) {
        if(!this.quickCheck(entity, world, pos)) return false;

        int interactX = pos.getX();
        int interactY = pos.getY();
        int interactZ = pos.getZ();
        int levels = 0;

        for(int offset = 1; offset <= this.height; levels = offset++){
            int checkY = interactY - offset;
            if(checkY < 0) break;
            boolean checkNextLayer = true;

            for(int checkX = interactX - offset; checkX <= interactX + offset && checkNextLayer; ++checkX){
                for(int checkZ = interactZ - offset; checkZ <= interactZ + offset; ++checkZ){
                    Block block = world.getBlockState(new BlockPos(checkX, checkY, checkZ)).getBlock();

                    if(!(block == bodyBlock)){
                        checkNextLayer = false;
                        break;
                    }
                }
            }
            if(!checkNextLayer) break;
        }

        return (levels == this.height);
    }

    // ==================================================
    //                     Activate
    // ==================================================
    /** Called when this Altar should activate. This will typically destroy the Altar and summon a rare mob or activate an event such as a boss event. If false is returned then the activation did not work, this is the place to check for things like dimensions. **/
    @Override
    public boolean activate(Entity entity, World world, BlockPos pos, int variant) {
        if(world.isRemote) return true;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        EntityLivingBase entityCreature = this.createEntity(entity, world);
        if(entityCreature == null) return false;

        if(entityCreature instanceof BaseCreatureEntity) {
            this.clearAltar(world, pos);

            // Spawn Mini Boss:
            entityCreature.setLocationAndAngles(x, y - 2, z, 0, 0);

            world.spawnEntity(entityCreature);

            return true;
        }
        return false;
    }

    protected void clearAltar(World world, BlockPos pos){
        int y = MathHelper.floor(pos.getY());
        int x = MathHelper.floor(pos.getX());
        int z = MathHelper.floor(pos.getZ());

        for(int xTarget = x - this.width; xTarget <= x + this.width; ++xTarget) {
            for(int zTarget = z - this.width; zTarget <= z + this.width; ++zTarget) {
                for(int yTarget = y - this.height; yTarget <= y + this.height; ++yTarget) {
                    BlockPos clearPos = new BlockPos(xTarget, yTarget, zTarget);
                    IBlockState iblockstate = world.getBlockState(clearPos);
                    Block block = iblockstate.getBlock();
                    if (block == this.bodyBlock || block == this.coreBlock) {
                        world.setBlockToAir(clearPos);
                    }
                }
            }
        }
    }

    protected void clearSpawnArea(World world, BlockPos pos, EntityLivingBase spawnedEntity){
        if(ForgeEventFactory.getMobGriefingEvent(world, spawnedEntity)) {
            int y = pos.getY();
            int x = pos.getX();
            int z = pos.getZ();
            boolean flag = false;

            int size = 4;
            for (int xTarget = x - size; xTarget <= x + size; ++xTarget) {
                for (int zTarget = z - size; zTarget <= z + size; ++zTarget) {
                    for (int yTarget = y - size; yTarget <= y + size; ++yTarget) {
                        BlockPos clearPos = new BlockPos(xTarget, yTarget, zTarget);
                        IBlockState iblockstate = world.getBlockState(clearPos);
                        Block block = iblockstate.getBlock();
                        if (!block.isAir(iblockstate, world, clearPos) && EntityWither.canDestroyBlock(block) && world.getTileEntity(clearPos) == null && ForgeEventFactory.onEntityDestroyBlock(spawnedEntity, clearPos, iblockstate)) {
                            flag = world.setBlockToAir(clearPos) || flag;
                        }
                    }
                }
            }
            if (flag) {
                world.playEvent(null, 1022, pos, 0);
            }
        }
    }

    protected EntityLivingBase createEntity(Entity entity, World world){
        ExtendedPlayer extendedPlayer = null;

        if(entity instanceof EntityPlayer) extendedPlayer = ExtendedPlayer.getForPlayer((EntityPlayer) entity);

        if(extendedPlayer == null || extendedPlayer.selectedCreature == null) {
            if(extendedPlayer != null) extendedPlayer.getPlayer().sendStatusMessage(new TextComponentTranslation("message.altar.beastiary.null"), true);
            return null;
        }

        if(extendedPlayer.selectedCreature.isBoss()){
            extendedPlayer.getPlayer().sendStatusMessage(new TextComponentTranslation("message.altar.beastiary.boss"), true);
            return null;
        }

        CreatureKnowledge creatureKnowledge = extendedPlayer.getBeastiary().getCreatureKnowledge(extendedPlayer.selectedCreature.getName());
        if(creatureKnowledge != null && creatureKnowledge.rank < ForgeConfigHandler.server.altarsConfig.beastiaryAltarKnowledgeRank) {
            extendedPlayer.getPlayer().sendStatusMessage(new TextComponentTranslation("message.altar.beastiary.rank"), true);
            return null;
        }

        EntityLiving selectedEntity = null;

        try {
            selectedEntity = extendedPlayer.selectedCreature.entityClass.getConstructor(new Class[]{World.class}).newInstance(world);
        } catch (Exception e) {
            LycanitesTweaks.LOGGER.error("An exception occurred when trying to create a creature in the AltarInfoBeastiary: {}", e.toString());
        }

        if(selectedEntity instanceof BaseCreatureEntity){
            // Spawn Mini Boss:
            BaseCreatureEntity entityCreature = (BaseCreatureEntity) selectedEntity;
            // Create Mini Boss:
            if (checkDimensions && !entityCreature.isNativeDimension(world)) {
                extendedPlayer.getPlayer().sendStatusMessage(new TextComponentTranslation("message.altar.fail.native"), true);
                return null;
            }
            entityCreature.altarSummoned = true;

            entityCreature.onFirstSpawn();
            entityCreature.setSubspecies(extendedPlayer.selectedSubspecies);
            entityCreature.applyVariant(extendedPlayer.selectedVariant);

            return entityCreature;
        }
        extendedPlayer.getPlayer().sendStatusMessage(new TextComponentTranslation("message.altar.beastiary.null"), true);
        return null;
    }
}
