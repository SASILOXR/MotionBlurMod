package com.sasiloxr.motionblur.command;

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
        MinecraftForge.EVENT_BUS.register(this);
        ShaderGroup shaderGroup = this.mc.entityRenderer.getShaderGroup();
        if (shaderGroup != null) {
            shaderGroup.deleteShaderGroup();
        }

//            for (Field field : mc.entityRenderer.getClass().getDeclaredFields()) {
//                String name = field.getName();
//                if (name.equals("theShaderGroup")) {
//                    field.setAccessible(true);
//                    field.set(mc.entityRenderer, new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), new ResourceLocation("motionblur", "motionblur")));
//                }
//
//            }
        mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
        mc.entityRenderer.getShaderGroup().createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        IChatComponent imsg = new ChatComponentText("motionblur enabled");
        mc.ingameGUI.getChatGUI().printChatMessage(imsg);
    }
}
