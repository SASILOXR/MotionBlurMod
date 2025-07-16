package com.sasiloxr.motionblur;

import com.sasiloxr.motionblur.command.CommandMotionBlur;
import com.sasiloxr.motionblur.resource.MotionBlurPostManager;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(
    modid = MotionBlurMod.MODID,
    name = MotionBlurMod.NAME,
    version = MotionBlurMod.VERSION,
    acceptedMinecraftVersions = "1.8.9")
public class MotionBlurMod {
  public static final String MODID = "NewMotionBlur";
  public static final String VERSION = "1.0";
  public static final String NAME = "NewMotionBlur";
  private Map domainResourceManager;
  Minecraft mc = Minecraft.getMinecraft();
  public boolean isNew = true;
  public double blurMount = 6;
  public boolean enabled = false;
  public boolean adaptMode;
  public double lowMount;
  public double highMount;
  public double lowFPS;
  public double highFPS;
  private File configFile;
  private Configuration config;
  public boolean switchMount;

  @Mod.Instance(MotionBlurMod.MODID)
  public static MotionBlurMod INSTANCE;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    this.configFile = event.getSuggestedConfigurationFile();
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    ClientCommandHandler.instance.registerCommand(new CommandMotionBlur());
    config = new Configuration(this.configFile);
    loadConfig();
    MinecraftForge.EVENT_BUS.register(INSTANCE);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {}

  public void saveConfig() {
    updateConfig(config, false);
    config.save();
  }

  private void loadConfig() {
    config.load();
    updateConfig(config, true);
  }

  private void updateConfig(Configuration config, boolean load) {
    Property prop1 = config.get("MotionBlur", "isNew", true);
    Property prop2 = config.get("MotionBlur", "blurMount", 0.0);
    Property prop3 = config.get("MotionBlur", "enabled", false);
    Property prop4 = config.get("MotionBlur", "adaptMode", false);
    Property prop5 = config.get("MotionBlur", "lowMount", 3);
    Property prop6 = config.get("MotionBlur", "lowFPS", 144);
    Property prop7 = config.get("MotionBlur", "highMount", 5);
    Property prop8 = config.get("MotionBlur", "highFPS", 255);
    if (!load) {
      prop3.setValue(INSTANCE.enabled);
      prop2.setValue(INSTANCE.blurMount);
      prop1.setValue(INSTANCE.isNew);
      prop4.setValue(INSTANCE.adaptMode);
      prop5.setValue(INSTANCE.lowMount);
      prop6.setValue(INSTANCE.lowFPS);
      prop7.setValue(INSTANCE.highMount);
      prop8.setValue(INSTANCE.highFPS);
    } else {
      INSTANCE.enabled = prop3.getBoolean();
      INSTANCE.blurMount = prop2.getDouble();
      INSTANCE.isNew = prop1.getBoolean();
      INSTANCE.adaptMode = prop4.getBoolean();
      INSTANCE.lowMount = prop5.getDouble();
      INSTANCE.lowFPS = prop4.getDouble();
      INSTANCE.highMount = prop4.getDouble();
      INSTANCE.highFPS = prop4.getDouble();
    }
  }

  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
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
        this.domainResourceManager.put("motionblur", new MotionBlurPostManager());
      }

      int FPS = Minecraft.getDebugFPS();

      if (FPS <= INSTANCE.lowFPS) {
        if (INSTANCE.blurMount != INSTANCE.lowMount) {
          INSTANCE.switchMount = true;
          INSTANCE.blurMount = INSTANCE.lowMount;
        }
      } 

      if (FPS >= INSTANCE.highFPS){
        if (INSTANCE.blurMount == INSTANCE.highMount) {
          INSTANCE.switchMount = true;
          INSTANCE.blurMount = INSTANCE.highMount;
        }
      }

      if ((MotionBlurMod.INSTANCE.enabled
              && !mc.entityRenderer.isShaderActive()
              && mc.inGameHasFocus)
          || switchMount) {
        mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
        switchMount = false;
      }
    }
  }
}
