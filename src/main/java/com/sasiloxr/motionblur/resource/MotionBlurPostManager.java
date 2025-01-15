package com.sasiloxr.motionblur.resource;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MotionBlurPostManager implements IResourceManager {

    @Override
    public Set<String> getResourceDomains() {
        return null;
    }

    @Override
    public IResource getResource(ResourceLocation location) throws IOException {
        return new MotionBlurPostResource();
    }

    @Override
    public List<IResource> getAllResources(ResourceLocation location) throws IOException {
        return null;
    }
}
