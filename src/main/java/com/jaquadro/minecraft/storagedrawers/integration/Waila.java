package com.jaquadro.minecraft.storagedrawers.integration;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.config.ClientConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.ui.ItemStackElement;

@WailaPlugin(StorageDrawers.MOD_ID)
public class Waila implements IWailaPlugin
{
    @Override
    public void registerClient (IWailaClientRegistration registration) {
        if (!ClientConfig.INTEGRATION.enableWaila.get())
            return;

        registration.addConfig(StorageDrawers.rl("display.content"), true);
        registration.addConfig(StorageDrawers.rl("display.stacklimit"), true);
        registration.addConfig(StorageDrawers.rl("display.status"), true);
        registration.addConfig(StorageDrawers.rl("display.items"), true);

        WailaDrawer provider = new WailaDrawer();
        registration.registerBlockComponent(provider, BlockDrawers.class);
    }

    public static class WailaDrawer implements IBlockComponentProvider
    {
        @Override
        @NotNull
        public IElement getIcon (BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
            return ItemStackElement.of(new ItemStack(accessor.getBlock()));
        }

        @Override
        public void appendTooltip (ITooltip currenttip, BlockAccessor accessor, IPluginConfig config) {
            BlockEntityDrawers blockEntityDrawers = (BlockEntityDrawers) accessor.getBlockEntity();

            DrawerOverlay overlay = new DrawerOverlay();
            overlay.showContent = config.get(StorageDrawers.rl("display.content"));
            overlay.showStackLimit = config.get(StorageDrawers.rl("display.stacklimit"));
            overlay.showStatus = config.get(StorageDrawers.rl("display.status"));
            currenttip.addAll(overlay.getOverlay(blockEntityDrawers));

            if (config.get(StorageDrawers.rl("display.items"))) {
                currenttip.remove(Identifiers.UNIVERSAL_ITEM_STORAGE);
            }
        }

        @Override
        public int getDefaultPriority() {
            return TooltipPosition.TAIL;
        }

        @Override
        public ResourceLocation getUid () {
            return StorageDrawers.rl("storage_info");
        }
    }
}
