package net.torchednova.neodimensions.itemsblocks;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.torchednova.neodimensions.NeoDimensions;
import net.torchednova.neodimensions.itemsblocks.blocks.DeeperDarkPortalBlock;

import java.util.HashMap;

public class Blocks {
    public static final HashMap<String, BlockEntry<?>> badvariable = new HashMap<>(){ { put("deeper_dark_portal", NeoDimensions.myRegistrate.block("deeper_dark_portal", DeeperDarkPortalBlock::new).register()); } };

    public static void register()
    {

    }


}
