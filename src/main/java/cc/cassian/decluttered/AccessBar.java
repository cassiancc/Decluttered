package cc.cassian.decluttered;

import java.util.ArrayList;

import cc.cassian.decluttered.tags.ModTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class AccessBar{

    private final MinecraftClient client;
    private final SoundEvent selectionSoundEvent;
    private final ArrayList<ItemStack> stacks = new ArrayList<>();
    private int selectedAccessSlotIndex = 0;
    private ItemStack lastSwappedOutTool =ItemStack.EMPTY;
    private final Identifier textures;

    public AccessBar(SoundEvent selectionSoundEvent, Identifier textures, MinecraftClient client){
        this.client = client;
        this.selectionSoundEvent = selectionSoundEvent;
        this.textures = textures;
    }

    public void updateAccessStacks(){
        if (client.player==null) return;
        ItemStack inv = client.player.getMainHandStack().getItem().getDefaultStack();
        stacks.clear();

        ArrayList<ItemStack> itemStacks = new ArrayList<>();


        itemStacks.add(inv);
        if (inv.isIn(ModTags.SWAPPABLE)) {
            stacks.clear();
            inv.streamTags().forEach((itemTagKey -> {
                var id = itemTagKey.id();
                if (id.getNamespace().equals("decluttered") && !id.getPath().equals("swappable")) {
                    Registries.ITEM.iterateEntries(itemTagKey).forEach(itemRegistryEntry -> {
                        var value = itemRegistryEntry.value().getDefaultStack();
                        itemStacks.add(value);
                    });
                }
            }));
        }

        stacks.addAll(itemStacks);
    }

    public void scrollInAccessBar(double scrollAmount) {
        int barSize = stacks.size();
        int slotsToScroll = (int)Math.signum(scrollAmount);
  
        selectedAccessSlotIndex = Math.floorMod(selectedAccessSlotIndex -slotsToScroll, barSize);
    }

    public void selectItem(){
        PlayerInventory inv = client.player.getInventory();
        int slotSwapIsLockedTo = Decluttered.CONFIG.lockSwappingToSlot;
        int slotToSwap = !(slotSwapIsLockedTo<1||slotSwapIsLockedTo>PlayerInventory.getHotbarSize()) ? slotSwapIsLockedTo-1 : inv.getSelectedSlot();
        ItemStack selectedHotbarSlotStack = inv.getStack(slotToSwap);
        ItemStack selectedAccessbarStack = stacks.get(selectedAccessSlotIndex);

        if(selectedAccessbarStack!=ItemStack.EMPTY){
            client.player.setStackInHand(Hand.MAIN_HAND, selectedAccessbarStack.copyWithCount(client.player.getMainHandStack().getCount()));

            client.getSoundManager().play(PositionedSoundInstance.master(selectionSoundEvent,1.0F,1.0F));
 
            lastSwappedOutTool = selectedHotbarSlotStack.copy();
        }
    }

    public void resetSelection(){
        PlayerInventory inv = client.player.getInventory();
        if(Decluttered.CONFIG.heldItemSelected) {
            updateAccessStacks();
            selectedAccessSlotIndex = stacks.indexOf(inv.getStack(inv.getSelectedSlot()));
        }else{
            selectedAccessSlotIndex = 0;
        }
    }

    public int getSelectedAccessSlotIndex() {
        return selectedAccessSlotIndex;
    }

    public void setSelectedAccessSlotIndex(int selectedAccessSlotIndex) {
        this.selectedAccessSlotIndex = selectedAccessSlotIndex;
    }

    public ArrayList<ItemStack> getStacks() {
        return stacks;
    }

    public Identifier getTextures() {
        return textures;
    }
}