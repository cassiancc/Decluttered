package cc.cassian.decluttered.config;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;

public class ModConfig extends WrappedConfig {

    @Comment("If enabled, you don't need to hold down the key to keep the access bar open.")
    public boolean toggleMode = false;
    @Comment("Left clicking will select current item.")
    public boolean leftClickSelect = true;
    @Comment("Pressing esc will close the access bar without selecting an item.")
    public boolean escClose = true;
    @Comment("You can use number keys to select items in access bars like you can in your hotbar.")
    public boolean scrollWithNumberKeys = true;
    @Comment("Horizontal offset of the bar from the default position")
    public int xOffset = 0;
    @Comment("Vertical offset of the bar from the default position")
    public int yOffset = 0;
    @Comment("Space left between bar slots")
    public int spaceBetweenSlots = 0;
    @Comment("Locks swapping to that hotbar slot. Values <1 and >hotbar size disable this option.")
    public int lockSwappingToSlot = 0;
    @Comment("Whether to render item labels")
    public boolean itemInfoShown = true;
    @Comment("When opening a bar your currently held item is selected, if it is contained in the bar.")
    public boolean heldItemSelected = true;
}
