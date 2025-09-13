package cc.cassian.decluttered.mixin;

import cc.cassian.decluttered.Decluttered;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import cc.cassian.decluttered.AccessBar;
import cc.cassian.decluttered.duck.InGameHudAccess;
import cc.cassian.decluttered.config.ModConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Final
    @Shadow
    public GameOptions options;
    @Final
    @Shadow
    public InGameHud inGameHud;
    
    @Inject(method = "handleInputEvents", at = @At(value = "HEAD"))
    private void handleAccessbarSelectInput(CallbackInfo info){
        if(((InGameHudAccess)inGameHud).getOpenAccessBar()!=null&& Decluttered.CONFIG.leftClickSelect&&this.options.attackKey.wasPressed()){
            ((InGameHudAccess)inGameHud).closeOpenAccessbar(true);
            ((KeyBindingAccess)options.attackKey).setTimesPressed(0);
        }
    }

    @Inject(method = "openGameMenu", at = @At("HEAD"),cancellable = true)
    public void pauseMenuOrCloseAccess(boolean bl, CallbackInfo info){
        if(((InGameHudAccess)inGameHud).getOpenAccessBar()!=null&& Decluttered.CONFIG.escClose){
            ((InGameHudAccess)inGameHud).closeOpenAccessbar(false);
            info.cancel();
        }
    }

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void closeBarOnScreenSwitch(Screen screen, CallbackInfo info){
        if(((InGameHudAccess)this.inGameHud).getOpenAccessBar()!=null){
            ((InGameHudAccess)this.inGameHud).closeOpenAccessbar(false);
        }
    }

    @Inject(method = "handleInputEvents",at = @At("HEAD"))
    private void handleAccessbarNumberKeySelection(CallbackInfo ci){
        AccessBar openAccessbar = ((InGameHudAccess)inGameHud).getOpenAccessBar();
        if(!Decluttered.CONFIG.scrollWithNumberKeys||openAccessbar==null) return;

        for(int i = 0; i < 9; ++i) {
            if (options.hotbarKeys[i].wasPressed()) {
                openAccessbar.setSelectedAccessSlotIndex(Math.min(i,openAccessbar.getStacks().size()-1));
                ((KeyBindingAccess)options.hotbarKeys[i]).setTimesPressed(0);
            }
        }
    }
}
