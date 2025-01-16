package com.sasiloxr.motionblur.resource;

import com.sasiloxr.motionblur.MotionBlurMod;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.Locale;

public class MotionBlurPostResource implements IResource {
    private static final String JSON = "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"motionblur\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}";

    @Override
    public ResourceLocation getResourceLocation() {
        return null;
    }

    @Override
    public InputStream getInputStream() {
        boolean isNew = MotionBlurMod.INSTANCE.isNew;
        double blurMount = MotionBlurMod.INSTANCE.blurMount;
        if (isNew) {
            return IOUtils.toInputStream(String.format(Locale.ENGLISH, JSON, 1 - blurMount / 10, 0.0, 0.0));
        } else {
            return IOUtils.toInputStream(String.format(Locale.ENGLISH, JSON, blurMount / 10, 1.0, 0.0));
        }
    }

    @Override
    public boolean hasMetadata() {
        return false;
    }

    @Override
    public <T extends IMetadataSection> T getMetadata(String p_110526_1_) {
        return null;
    }

    @Override
    public String getResourcePackName() {
        return "";
    }
}
