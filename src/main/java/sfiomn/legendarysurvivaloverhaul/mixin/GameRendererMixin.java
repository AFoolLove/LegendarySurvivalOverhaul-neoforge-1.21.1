package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderBlurOverlay;
import sfiomn.legendarysurvivaloverhaul.client.shaders.FocusShader;

@Mixin({GameRenderer.class})
public class GameRendererMixin {
    @Inject(method = {"render"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V",
                    shift = At.Shift.BEFORE
            )}
    )
    public void afterPostEffect(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (!FocusShader.useMixin) {
            FocusShader.useMixin = true;
        }
        if (RenderBlurOverlay.focusShader != null && RenderBlurOverlay.focusShader.isActive) {
            RenderBlurOverlay.focusShader.instance.process(deltaTracker.getGameTimeDeltaTicks());
        }
    }
}
