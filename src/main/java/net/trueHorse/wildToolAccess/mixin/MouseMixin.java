package net.trueHorse.wildToolAccess.mixin;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.client.Mouse;
import net.trueHorse.wildToolAccess.config.WildToolAccessConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.collection.DefaultedList;
import net.trueHorse.wildToolAccess.duck.PlayerInventoryAccess;
import net.trueHorse.wildToolAccess.duck.InGameHudAccess;

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