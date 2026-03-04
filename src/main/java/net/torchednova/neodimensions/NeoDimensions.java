package net.torchednova.neodimensions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.registries.*;
import net.torchednova.neodimensions.deeper_dark.PlayerTracker;
import net.torchednova.neodimensions.deeper_dark.PlayerTrackerController;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.awt.*;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(NeoDimensions.MODID)
public class NeoDimensions {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "neodimensions";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    //public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    //public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    //public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public NeoDimensions(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        //BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }


    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Pre event)
    {

        if (event.getLevel().isClientSide) return;

        if (event.getLevel().dimension().location().toString().equals("neodimensions:deeper_dark"))
        {
            for (int i = 0; i < PlayerTrackerController.players.size(); i++)
            {
                PlayerTracker p = PlayerTrackerController.players.get(i);

                p.ticks++;
                if (p.ticks >= Settings.timeBetDamage)
                {
                    if (p.player == null)
                    {
                        i--;
                        PlayerTrackerController.removePlayer(p.player);
                        continue;
                    }
                    if (event.getLevel().getMaxLocalRawBrightness(p.player.blockPosition()) > Settings.mineLightLevel) return;
                    p.ticks = 0;
                    p.player.hurt(DamageTypes.Darkness, 2.0f);
                }
            }
        }
    }



    @SubscribeEvent
    public void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        if (event.getFrom().location().toString().equals("neodimensions:deeper_dark"))
        {
            PlayerTrackerController.removePlayer(event.getEntity());
        }
        else if (event.getTo().location().toString().equals("neodimensions:deeper_dark"))
        {
            PlayerTrackerController.addPlayer(event.getEntity());
        }
    }

    @SubscribeEvent
    public void PlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (event.getEntity().level().dimension().location().toString().equals("neodimensions:deeper_dark"))
        {
            PlayerTrackerController.removePlayer(event.getEntity());
        }
    }

    @SubscribeEvent
    public void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getEntity().level().dimension().location().toString().equals("neodimensions:deeper_dark"))
        {
            PlayerTrackerController.addPlayer(event.getEntity());
        }
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        PlayerTrackerController.init();

        for (Level l : event.getServer().getAllLevels())
        {
            if (l.dimension().location().toString().equals("neodimensions:deeper_dark"))
            {
                DamageTypes.initDark(l);
            }
        }
    }
}
