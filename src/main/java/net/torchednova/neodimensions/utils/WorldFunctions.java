package net.torchednova.neodimensions.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import static net.torchednova.neodimensions.NeoDimensions.LOGGER;

public class WorldFunctions{
    public static Vec3 makeSafePoint(Level l, Vec3 pos)
    {
        Vec3i StartPos = new Vec3i((int)pos.x - 1, (int)pos.y - 1, (int)pos.z - 1);

        for (int i = 0; i < 3; i++)
        {
            for (int ii = 0; ii < 3; ii++)
            {
                l.setBlock(new BlockPos(StartPos.getX() + ii, StartPos.getY(), StartPos.getZ() + i), Blocks.COBBLESTONE.defaultBlockState(), 3);
                //LOGGER.info(String.valueOf(StartPos.getX() + ii) + " | " +   String.valueOf(StartPos.getY()) + " | " + String.valueOf(StartPos.getZ() + i));
            }
        }

        for (int i = 1; i < 4; i++)
        {
            for (int ii = 0; ii < 3; ii++)
            {
                for (int iii = 0; iii < 3; iii++)
                {
                    l.setBlock(new BlockPos(StartPos.getX() + iii, StartPos.getY() + i, StartPos.getZ() + ii), Blocks.AIR.defaultBlockState(), 3);
                    //LOGGER.info(String.valueOf(StartPos.getX() + iii) + " | " +   String.valueOf(StartPos.getY() + i) + " | " + String.valueOf(StartPos.getZ() + ii));
                }
            }
        }
        l.setBlock(new BlockPos(StartPos.getX() + 1, StartPos.getY(), StartPos.getZ() + 1), net.torchednova.neodimensions.itemsblocks.Blocks.neodimensionsBlocks.get("deeper_dark_portal").getDefaultState(), 3);
        l.setBlock(new BlockPos(StartPos.getX() + 2, StartPos.getY() + 1, StartPos.getZ() + 2), Blocks.TORCH.defaultBlockState(), 3);
        l.setBlock(new BlockPos(StartPos.getX(), StartPos.getY() + 1, StartPos.getZ() + 2), Blocks.TORCH.defaultBlockState(), 3);
        l.setBlock(new BlockPos(StartPos.getX() + 2, StartPos.getY() + 1, StartPos.getZ()), Blocks.TORCH.defaultBlockState(), 3);
        l.setBlock(new BlockPos(StartPos.getX(), StartPos.getY() + 1, StartPos.getZ()), Blocks.TORCH.defaultBlockState(), 3);

        l.getBlockState(new BlockPos(StartPos));

        return null;
    }
}
