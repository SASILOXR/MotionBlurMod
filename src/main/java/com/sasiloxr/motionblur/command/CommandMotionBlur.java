package com.sasiloxr.motionblur.command;

import com.sasiloxr.motionblur.MotionBlurMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.math.NumberUtils;

public class CommandMotionBlur extends CommandBase {

    Minecraft mc = Minecraft.getMinecraft();

    @Override
    public String getCommandName() {
        return "motionblur";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/motionblur";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText("Usage: /motionblur [old/new] [0-9]"));
        } else {
            boolean oldBlur = args[0].equals("old");
            boolean newBlur = args[0].equals("new");
            if (!(oldBlur || newBlur)) {
                sender.addChatMessage(new ChatComponentText("Invalid Option"));
                return;
            }

            if (oldBlur) {
                MotionBlurMod.INSTANCE.isNew = false;
            }

            if (newBlur) {
                MotionBlurMod.INSTANCE.isNew = true;
            }

            int blurMount = NumberUtils.toInt(args[1], -1);
            if (blurMount > 9 || blurMount < 0) {
                sender.addChatMessage(new ChatComponentText("Invalid Mount"));
                return;
            }
            MotionBlurMod.INSTANCE.blurMount = blurMount;

            if (blurMount == 0) {
                mc.entityRenderer.stopUseShader();
                MotionBlurMod.INSTANCE.enabled = false;
                MotionBlurMod.INSTANCE.saveConfig();
                return;
            }


            ShaderGroup shaderGroup = this.mc.entityRenderer.getShaderGroup();
            if (shaderGroup != null) {
                shaderGroup.deleteShaderGroup();
            }
            mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
            MinecraftForge.EVENT_BUS.register(this);
            MotionBlurMod.INSTANCE.enabled = true;
            MotionBlurMod.INSTANCE.saveConfig();
            IChatComponent imsg = new ChatComponentText("MotionBlur enabled");
            mc.ingameGUI.getChatGUI().printChatMessage(imsg);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (MotionBlurMod.INSTANCE.enabled && !mc.entityRenderer.isShaderActive() && mc.inGameHasFocus) {
            ShaderGroup shaderGroup = this.mc.entityRenderer.getShaderGroup();
            if (shaderGroup != null) {
                shaderGroup.deleteShaderGroup();
            }
            mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
        }
    }
}
