package net.torchednova.neodimensions.itemsblocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.torchednova.neodimensions.NeoDimensions;

public class CreativeTab {
    public static final DeferredRegister<CreativeModeTab> registerie = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NeoDimensions.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> mine = registerie.register(
            NeoDimensions.MODID + "-creativetab", () -> CreativeModeTab.builder()
                    .title(Component.literal("NeoDimensions"))
                    .icon(Blocks.neodimensionsBlocks.get("deeper_dark_portal")::asStack)
                    .displayItems((param, output) -> Blocks.neodimensionsBlocks.forEach((id, block) -> output.accept(block.asStack())))
                    .build()
    );

    public static void register(IEventBus event) {
        registerie.register(event);
    }
}
