package cc.cassian.decluttered.mixin;

import cc.cassian.decluttered.Decluttered;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import cc.cassian.decluttered.AccessBar;
import cc.cassian.decluttered.DeclutteredSoundEvents;
import cc.cassian.decluttered.duck.InGameHudAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import static cc.cassian.decluttered.Decluttered.MOD_ID;

@Mixin(InGameHud.class)
public class InGameHudMixin implements InGameHudAccess{

    @Final @Shadow
    private MinecraftClient client;
    @Unique
    private final Identifier accessBarTextureSheet = Identifier.of(MOD_ID,"textures/gui/access_widgets0.png");
    @Unique
    private AccessBar accessBars;
    @Unique
    private AccessBar openAccessbar;

    @Shadow
    private PlayerEntity getCameraPlayer(){return null;}
    @Shadow
    private void renderHotbarItem(DrawContext context, int x, int y, RenderTickCounter renderTickCounter, PlayerEntity player, ItemStack stack, int seed){}

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initAccessBar(MinecraftClient client, CallbackInfo ci){
        accessBars = getAccessBarArray();
    }

    @Inject(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",shift = At.Shift.AFTER))
    public void renderAccessBar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(openAccessbar!=null){
            PlayerEntity playerEntity = this.getCameraPlayer();
            if (playerEntity != null) {
                openAccessbar.updateAccessStacks();

                Identifier barTextures;
                barTextures = openAccessbar.getTextures();

                int firstSlotXCoordinate = context.getScaledWindowWidth() / 2 -10+ Decluttered.CONFIG.xOffset;
                int yCoordinate = context.getScaledWindowHeight()/2 -54+ Decluttered.CONFIG.yOffset;
                context.getMatrices().pushMatrix();
                context.getMatrices().translate(0.0F, 0.0F);

                int xCoordinate;
                int spaceBetweenSlots = 20+ Decluttered.CONFIG.spaceBetweenSlots;
                
                if(openAccessbar.getStacks().size()==1){
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, barTextures, firstSlotXCoordinate, yCoordinate, 66, 0, 22, 22, 256, 256);
                }else{
                    int k;
                    for(k = 1; k < openAccessbar.getStacks().size()-1; ++k) {
                        xCoordinate = firstSlotXCoordinate + k * spaceBetweenSlots - spaceBetweenSlots*openAccessbar.getSelectedAccessSlotIndex();
                        context.drawTexture(RenderPipelines.GUI_TEXTURED, barTextures, xCoordinate, yCoordinate, 0, 0, 22, 22, 256, 256);
                    }
                    xCoordinate = firstSlotXCoordinate - spaceBetweenSlots*openAccessbar.getSelectedAccessSlotIndex();
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, barTextures, xCoordinate, yCoordinate, 22, 0, 22, 22, 256, 256);
                    xCoordinate = firstSlotXCoordinate + k * spaceBetweenSlots - spaceBetweenSlots*openAccessbar.getSelectedAccessSlotIndex();
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, barTextures, xCoordinate, yCoordinate, 44, 0, 22, 22, 256, 256);
                }
                context.drawTexture(RenderPipelines.GUI_TEXTURED, barTextures, firstSlotXCoordinate - 1, yCoordinate - 1, 0, 22, 24, 22, 256, 256);

                context.getMatrices().popMatrix();

                int seed =1;
                for(int i = 0; i < openAccessbar.getStacks().size(); ++i) {
                    xCoordinate = firstSlotXCoordinate + i * spaceBetweenSlots + 3 - spaceBetweenSlots*(openAccessbar.getSelectedAccessSlotIndex());
                    this.renderHotbarItem(context, xCoordinate, yCoordinate+3, tickCounter, playerEntity, openAccessbar.getStacks().get(i).getDefaultStack(),seed++);
                }

                if(Decluttered.CONFIG.itemInfoShown&&openAccessbar.getStacks().get(openAccessbar.getSelectedAccessSlotIndex())!= Items.AIR){
                    renderLabels(context, firstSlotXCoordinate, yCoordinate);
                }
            }
        }
    }

    @Unique
    private void renderLabels(DrawContext context, int i, int j){
        ItemStack selectedStack = openAccessbar.getStacks().get(openAccessbar.getSelectedAccessSlotIndex()).getDefaultStack();
        List<Text> tooltip = new ArrayList<>();
        MutableText name = (Text.literal("")).append(selectedStack.getName()).formatted(selectedStack.getRarity().getFormatting());
        if (selectedStack.getComponents().contains(DataComponentTypes.CUSTOM_NAME)) {
            name.formatted(Formatting.ITALIC);
        }
        tooltip.add(name);

        TextRenderer textRenderer = client.textRenderer;
        List<OrderedText>orderedToolTip = Lists.transform(tooltip, Text::asOrderedText);

        context.drawTextWithShadow(textRenderer, name, i+10+3-textRenderer.getWidth(name)/2, j-15, -1);
        for(int v=1;v<orderedToolTip.size();v++) {
            OrderedText text = orderedToolTip.get(v);
            if (text != null) {
                context.drawTextWithShadow(textRenderer, text, i + 10 + 3 - textRenderer.getWidth(text) / 2, j + 15 + 10 * v, -1);
            }
        }
    }

    @Override
    public void decluttered$closeOpenAccessbar(boolean select){
        if(select){
            this.openAccessbar.selectItem();
        }
        openAccessbar = null;
    }

    @Override
    public void decluttered$openAccessbar(int num){
        openAccessbar = accessBars;
        openAccessbar.resetSelection();
    }
    @Override
    public AccessBar decluttered$getOpenAccessBar() {
        return this.openAccessbar;
    }

    @Override
    public boolean decluttered$isBarWithNumberOpen(int number){
        return openAccessbar == accessBars;
    }

    @Override
    public void decluttered$refreshAccessbars() {
        accessBars = getAccessBarArray();
    }

    private AccessBar getAccessBarArray(){
        return new AccessBar(
                DeclutteredSoundEvents.selectInAccess1,
                        accessBarTextureSheet,
                        client);
    }
}
