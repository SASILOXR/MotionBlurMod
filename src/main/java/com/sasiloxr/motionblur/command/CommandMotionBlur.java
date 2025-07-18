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
            sender.addChatMessage(new ChatComponentText("Usage: /motionblur [old/new] [0-9/adapt lowMount lowFPS highMount highFPS]"));
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

            boolean adaptMode = args[1].equals("adapt");

            if (adaptMode) {
              int lowMount = NumberUtils.toInt(args[2], -1);
              int lowFPS = NumberUtils.toInt(args[3], -1);
              int highMount = NumberUtils.toInt(args[4], -1);
              int highFPS = NumberUtils.toInt(args[5], -1);
              
              MotionBlurMod.INSTANCE.lowMount = lowMount;
              MotionBlurMod.INSTANCE.highMount = highMount;
              MotionBlurMod.INSTANCE.lowFPS = lowFPS;
              MotionBlurMod.INSTANCE.highFPS = highFPS;
              MotionBlurMod.INSTANCE.adaptMode = true;

            } else {
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
}
