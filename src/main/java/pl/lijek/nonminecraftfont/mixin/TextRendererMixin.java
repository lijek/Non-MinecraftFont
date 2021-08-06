package pl.lijek.nonminecraftfont.mixin;

import net.minecraft.client.render.TextRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.lijek.nonminecraftfont.NonMinecraftFont;

@Mixin(TextRenderer.class)
public abstract class TextRendererMixin {

    protected abstract void drawText(int x, int y, String text, int colour);

    @Inject(at = @At("HEAD"), method = "drawText(Ljava/lang/String;IIIZ)V", cancellable = true)
    private void drawText(String text, int x, int y, int colour, boolean shadow, CallbackInfo ci) {
        if (text == null) {
            ci.cancel();
            return;
        }
        NonMinecraftFont.textRenderer.drawText(x, y, text, colour, shadow);
        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "getTextWidth", cancellable = true)
    private void getTextWidth(String string, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(NonMinecraftFont.textRenderer.getTextWidth(string));
        cir.cancel();
    }
}
