package cc.cassian.decluttered.mixin;

import java.util.ArrayList;
import java.util.Collection;

import cc.cassian.decluttered.config.ModConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.collection.DefaultedList;
import cc.cassian.decluttered.duck.PlayerInventoryAccess;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements PlayerInventoryAccess{

    @Final
    @Shadow
    private DefaultedList<ItemStack> main;

//    @Inject(method = "setSelectedSlot", at = @At("HEAD"), cancellable = true)
//    private void scrollInAccessBar(int slot, CallbackInfo ci) {
//        MinecraftClient client = MinecraftClient.getInstance();
//        if(((InGameHudAccess)client.inGameHud).getOpenAccessBar()!=null){
//            ((InGameHudAccess)client.inGameHud).getOpenAccessBar().scrollInAccessBar(-slot);
//            ci.cancel();
//        }
//    }

    @Override
    public ArrayList<ItemStack> getAllMainStacksWithTag(TagKey<Item> tag){
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        for (ItemStack itemStack : main) {
            if (itemStack.isIn(tag)) {
                stacks.add(itemStack);
            }
        }
        return stacks;
    }

    @Override
    public ArrayList<ItemStack> getAllMainStacksOf(Collection<Item> items) {
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        for (ItemStack itemStack : main) {
            if (items.contains(itemStack.getItem())) {
                stacks.add(itemStack);
            }
        }
        return stacks;
    }
}
