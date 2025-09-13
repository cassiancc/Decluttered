package cc.cassian.decluttered.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;
import cc.cassian.decluttered.duck.InGameHudAccess;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void scrollInAccessBar(long window, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (((InGameHudAccess) client.inGameHud).getOpenAccessBar() != null) {
            ((InGameHudAccess) client.inGameHud).getOpenAccessBar().scrollInAccessBar(vertical);
            ci.cancel();
        }
    }
}