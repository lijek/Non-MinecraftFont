package pl.lijek.nonminecraftfont.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lijek.nonminecraftfont.NonMinecraftFont;
import pl.lijek.nonminecraftfont.TextRenderer;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/texture/TextureManager;)V"), method = "init")
    private void init(CallbackInfo ci){
        NonMinecraftFont.textRenderer = new TextRenderer();
    }
}
