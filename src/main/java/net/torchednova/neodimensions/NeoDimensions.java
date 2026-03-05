package net.torchednova.neodimensions;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.registries.*;
import net.torchednova.neodimensions.deeper_dark.PlayerTracker;
import net.torchednova.neodimensions.deeper_dark.PlayerTrackerController;
import net.torchednova.neodimensions.itemsblocks.Blocks;
import net.torchednova.neodimensions.itemsblocks.CreativeTab;
import org.slf4j.Logger;
import net.minecraft.world.entity.ai.attributes.Attributes;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


@Mod(NeoDimensions.MODID)
public class NeoDimensions {
    public static final String MODID = "neodimensions";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final MyRegistrate myRegistrate = MyRegistrate.create(MODID).defaultCreativeTab((ResourceKey<CreativeModeTab>)null);

    public NeoDimensions(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        myRegistrate.registerEventListeners(modEventBus);

        Blocks.register();
        CreativeTab.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
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

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event)
    {
        if (event.getLevel().dimension().location().toString().equals("neodimensions:deeper_dark"))
        {
            if (event.getEntity() instanceof LivingEntity lv && Settings.mineMobs.contains(lv.getType().getDescriptionId()))
            {
                AttributeInstance attInst = lv.getAttribute(Attributes.MAX_HEALTH);
                if (attInst != null)
                {
                    attInst.setBaseValue(attInst.getBaseValue() * Settings.mineMobHealthMul);
                    lv.setHealth(lv.getMaxHealth());
                }

                attInst = lv.getAttribute(Attributes.ATTACK_DAMAGE);
                if (attInst != null)
                {
                    attInst.setBaseValue(attInst.getBaseValue() * Settings.mineMobDamMul);
                }
            }
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
