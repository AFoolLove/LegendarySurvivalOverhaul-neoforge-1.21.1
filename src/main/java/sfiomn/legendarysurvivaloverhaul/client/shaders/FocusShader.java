package sfiomn.legendarysurvivaloverhaul.client.shaders;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul.LOGGER;

public class FocusShader {
    public static final ResourceLocation BLUR_SHADER = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "shaders/post/blobs2.json");
    private static final Field shaders = ObfuscationReflectionHelper.findField(PostChain.class, "passes");

    public static boolean useMixin = false;
    public boolean isActive = false;
    public PostChain instance;

    public FocusShader() {
    }

    public void render(float intensity) {
        if (intensity > 0) {
            if (!useMixin) {
                PostChain currentEffect = Minecraft.getInstance().gameRenderer.currentEffect();
                if (currentEffect == null || !currentEffect.getName().equals(BLUR_SHADER.toString())) {
                    try {
                        Minecraft.getInstance().gameRenderer.loadEffect(BLUR_SHADER);
                    } catch (NullPointerException e) {
                        return;
                    }
                }
            } else {
                if (!isActive) {
                    isActive = true;
                    instance = loadBlurEffect();
                }
            }
            updateIntensity(intensity);
        }
    }

    public void stopRender() {
        if (!useMixin) {
            PostChain currentEffect = Minecraft.getInstance().gameRenderer.currentEffect();
            if (currentEffect != null && currentEffect.getName().equals(BLUR_SHADER.toString())) {
                Minecraft.getInstance().gameRenderer.shutdownEffect();
            }
        } else {
            if (isActive) {
                isActive = false;
                if (instance != null) {
                    instance.close();
                }
            }
        }
    }

    private PostChain loadBlurEffect() {
        try {
            Minecraft minecraft = Minecraft.getInstance();
            PostChain blurEffect = new PostChain(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getMainRenderTarget(), BLUR_SHADER);
            blurEffect.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
            return blurEffect;
        } catch (IOException ioexception) {
            LOGGER.warn("Failed to load shader: {}", BLUR_SHADER, ioexception);
        } catch (JsonSyntaxException jsonsyntaxexception) {
            LOGGER.warn("Failed to parse shader: {}", BLUR_SHADER, jsonsyntaxexception);
        }
        return null;
    }

    @OnlyIn(value = Dist.CLIENT)
    public void updateIntensity(float intensity) {

        Uniform shaderRadius;
        try {
            if (!useMixin) {
                shaderRadius = ((List<PostPass>) shaders.get(Minecraft.getInstance().gameRenderer.currentEffect())).getFirst().getEffect().getUniform("Radius");
            } else {
                shaderRadius = ((List<PostPass>) shaders.get(instance)).getFirst().getEffect().getUniform("Radius");
            }
        } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
            shaderRadius = null;
        }

        if (shaderRadius != null) {
            shaderRadius.set(intensity);
        }
    }
}
