package com.sasiloxr.motionblur;

import com.sasiloxr.motionblur.command.CommandMotionBlur;
import com.sasiloxr.motionblur.resource.MotionBlurManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.util.Map;

@Mod(modid = MotionBlurMod.MODID, name = MotionBlurMod.NAME, version = MotionBlurMod.VERSION, acceptedMinecraftVersions = "1.8.9")
public class MotionBlurMod {
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";
    public static final String NAME = "MotionBlur";
    private Map domainResourceManager;
    Minecraft mc = Minecraft.getMinecraft();


    @Mod.Instance(MotionBlurMod.MODID)
    public static MotionBlurMod INSTANCE;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CommandMotionBlur());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (domainResourceManager == null) {
            try {
                for (Field field : SimpleReloadableResourceManager.class.getDeclaredFields()) {
                    if (field.getType() != Map.class) continue;
                    field.setAccessible(true);
                    this.domainResourceManager = (Map) field.get(mc.getResourceManager());
                    break;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        if (!this.domainResourceManager.containsKey("motionblur")) {
            this.domainResourceManager.put("motionblur", new MotionBlurManager());
        }
    }
}
