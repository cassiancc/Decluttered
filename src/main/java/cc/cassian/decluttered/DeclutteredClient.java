package cc.cassian.decluttered;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import cc.cassian.decluttered.duck.InGameHudAccess;
import org.lwjgl.glfw.GLFW;

public class DeclutteredClient implements ClientModInitializer {

    private static KeyBinding access1Binding;
    private static boolean access1WasPressed;

    @Override
    public void onInitializeClient() {

        DeclutteredSoundEvents.registerAll();
        DeclutteredSoundEvents.updateSoundEventsAsConfigured();

        access1Binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.decluttered.access1",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            "key.categories.inventory"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            InGameHudAccess hudAcc = ((InGameHudAccess)client.inGameHud);

            if(!Decluttered.CONFIG.toggleMode){

                if(access1Binding.isPressed()!=access1WasPressed) {
                    onAccessBindingHeldStatusChanged(access1Binding, hudAcc);
                }

                access1WasPressed = access1Binding.isPressed();
            }else{
                while (access1Binding.wasPressed()) {
                    onToggleBarBindingPressed(1, hudAcc);
                }
            }
        });
    }

    private void onAccessBindingHeldStatusChanged(KeyBinding accessBinding, InGameHudAccess hudAcc){
        if(!MinecraftClient.getInstance().player.isSpectator()){
            if (accessBinding.isPressed()) {
                hudAcc.decluttered$openAccessbar(1);
            } else {
                if (hudAcc.decluttered$getOpenAccessBar() != null) {
                    hudAcc.decluttered$closeOpenAccessbar(true);
                }
            }
        }
    }
    
    private void onToggleBarBindingPressed(int barNum, InGameHudAccess hudAcc){
        if(!MinecraftClient.getInstance().player.isSpectator()){
            if (hudAcc.decluttered$getOpenAccessBar() != null) {
                if (hudAcc.decluttered$isBarWithNumberOpen(barNum)) {
                    hudAcc.decluttered$closeOpenAccessbar(true);
                } else {
                    hudAcc.decluttered$openAccessbar(barNum);
                }
            } else {
                hudAcc.decluttered$openAccessbar(barNum);
            }
        }
    }
}
