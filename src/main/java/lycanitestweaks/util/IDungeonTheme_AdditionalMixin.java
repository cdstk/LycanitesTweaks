package lycanitestweaks.util;

import net.minecraft.block.state.IBlockState;

import java.util.Random;

public interface IDungeonTheme_AdditionalMixin {

    /**
     * Returns a boss room exit block state for the provided pattern character. Use B for random.
     * @param patternChar The block character to convert to a block state. NOT USED CURRENTLY, always uses random
     * @param random The instance of random, used for characters that are random.
     * @return A block state for placing.
     */
    IBlockState lycanitesTweaks$getBossWall(char patternChar, Random random);
}
