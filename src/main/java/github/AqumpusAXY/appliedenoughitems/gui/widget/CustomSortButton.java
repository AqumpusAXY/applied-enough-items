/*
 * This file has been modified for Applied Enough Items.
 * Modifications Copyright (c) 2025 AqumpusAXY.
 *
 * This combined work is licensed under the GNU General Public License v3.0,
 * as permitted by Section 3 of the GNU LGPL v3 (see LICENSE in project root).
 *
 * Original source: https://github.com/AppliedEnergistics/Applied-Energistics-2
 */

/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2015, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package github.AqumpusAXY.appliedenoughitems.gui.widget;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.Icon;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.widgets.IconButton;
import appeng.core.localization.ButtonToolTips;
import com.mojang.blaze3d.systems.RenderSystem;
import github.AqumpusAXY.appliedenoughitems.config.ClientConfig;
import github.AqumpusAXY.appliedenoughitems.config.CustomSortOrder;
import github.AqumpusAXY.appliedenoughitems.gui.CustomIcon;
import github.AqumpusAXY.appliedenoughitems.localization.CustomButtonToolTips;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CustomSortButton extends IconButton {

    private static Map<CustomSortButton.EnumPair, CustomSortButton.ButtonAppearance> appearances;
    private final ForgeConfigSpec.EnumValue<CustomSortOrder> BUTTON_SETTING = ClientConfig.CUSTOM_SORT_ORDER;
    private final CustomSortButton.IHandler ON_PRESS;
    private CustomSortOrder currentSortOrder = BUTTON_SETTING.get();

    @FunctionalInterface
    public interface IHandler {
        void handle(CustomSortButton button, boolean backwards);
    }

    public CustomSortButton(IHandler onPress) {
        super(CustomSortButton::onPress);
        this.ON_PRESS = onPress;
        if (appearances == null) {
            appearances = new HashMap<>();
            registerApp(CustomIcon.SORT_BY_JEI.getBlitter(), CustomSortOrder.JEI, CustomButtonToolTips.JEI);
            registerApp(Icon.SORT_BY_NAME.getBlitter(), CustomSortOrder.NAME, ButtonToolTips.ItemName);
            registerApp(Icon.SORT_BY_AMOUNT.getBlitter(), CustomSortOrder.AMOUNT, ButtonToolTips.NumberOfItems);
            registerApp(Icon.SORT_BY_MOD.getBlitter(), CustomSortOrder.MOD, ButtonToolTips.Mod);
        }
    }

    private static void onPress(Button btn) {
        if (btn instanceof CustomSortButton) {
            ((CustomSortButton) btn).triggerPress();
        }
    }

    private void triggerPress() {
        boolean backwards = false;
        // This isn't great, but we don't get any information about right-clicks
        // otherwise
        Screen currentScreen = Minecraft.getInstance().screen;
        if (currentScreen instanceof AEBaseScreen) {
            backwards = ((AEBaseScreen<?>) currentScreen).isHandlingRightClick();
        }
        ON_PRESS.handle(this, backwards);
    }

    private static void registerApp(Blitter blitter, CustomSortOrder val, Component... tooltipLines) {
        var lines = new ArrayList<Component>();
        lines.add(ButtonToolTips.SortBy.text());
        Collections.addAll(lines, tooltipLines);

        appearances.put(
                new EnumPair(ClientConfig.CUSTOM_SORT_ORDER, val),
                new ButtonAppearance(blitter, null, lines));
    }

    private static void registerApp(Blitter blitter, CustomSortOrder val, CustomButtonToolTips hint) {
        registerApp(blitter, val, hint.text());
    }

    private static void registerApp(Blitter blitter, CustomSortOrder val, ButtonToolTips hint) {
        registerApp(blitter, val, hint.text());
    }

    @Nullable
    private CustomSortButton.ButtonAppearance getAppearance() {
        return appearances.get(new CustomSortButton.EnumPair(this.BUTTON_SETTING, this.currentSortOrder));
    }

    /**
     * Dummy method to pass compilation - do not use this method.
     */
    @Override
    protected Icon getIcon() {
        return Icon.TOOLBAR_BUTTON_BACKGROUND;
    }

    protected Blitter getBlitter() {
        var app = getAppearance();
        if (app != null && app.blitter != null) {
            return app.blitter;
        }
        return Icon.TOOLBAR_BUTTON_BACKGROUND.getBlitter();
    }

    public CustomSortOrder getCurrentSortOrder() {
        return this.currentSortOrder;
    }

    public CustomSortOrder getNextSortOrder(boolean backwards) {
        return CustomSortOrder.values()[(this.currentSortOrder.ordinal() + (backwards ? -1 : 1)) % CustomSortOrder.values().length];
    }

    public void toggleSortOrder(boolean backwards) {
        this.BUTTON_SETTING.set(getNextSortOrder(backwards));
        this.currentSortOrder = this.BUTTON_SETTING.get();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {

        if (this.visible) {

            Blitter blitter = this.getBlitter();
            if (!this.active) {
                blitter.opacity(0.5f);
            }

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend(); // FIXME: This should be the _default_ state, but some vanilla widget disables

            if (isFocused()) {
                // Draw 1px border with 4 quads, don't rely on the background as it can be disabled.
                // top
                guiGraphics.fill(getX() - 1, getY() - 1, getX() + width + 1, getY(), 0xFFFFFFFF);
                // left
                guiGraphics.fill(getX() - 1, getY(), getX(), getY() + height, 0xFFFFFFFF);
                // right
                guiGraphics.fill(getX() + width, getY(), getX() + width + 1, getY() + height, 0xFFFFFFFF);
                // bottom
                guiGraphics.fill(getX() - 1, getY() + height, getX() + width + 1, getY() + height + 1, 0xFFFFFFFF);
            }

            Icon.TOOLBAR_BUTTON_BACKGROUND.getBlitter().dest(getX(), getY()).blit(guiGraphics);
            blitter.dest(getX(), getY()).blit(guiGraphics);

            RenderSystem.enableDepthTest();

            var item = this.getItemOverlay();
            if (item != null) {
                guiGraphics.renderItem(new ItemStack(item), getX(), getY());
            }
        }
    }

    @Override
    public List<Component> getTooltipMessage() {

        if (this.BUTTON_SETTING == null || this.currentSortOrder == null) {
            return Collections.emptyList();
        }

        var buttonAppearance = appearances.get(new EnumPair(this.BUTTON_SETTING, this.currentSortOrder));
        if (buttonAppearance == null) {
            return Collections.singletonList(ButtonToolTips.NoSuchMessage.text());
        }

        return buttonAppearance.tooltipLines;
    }

    private static final class EnumPair {

        final ForgeConfigSpec.EnumValue<CustomSortOrder> SETTING;
        final CustomSortOrder VALUE;

        public EnumPair(ForgeConfigSpec.EnumValue<CustomSortOrder> setting, CustomSortOrder value) {
            this.SETTING = setting;
            this.VALUE = value;
        }

        @Override
        public int hashCode() {
            return this.SETTING.hashCode() ^ this.VALUE.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final CustomSortButton.EnumPair other = (CustomSortButton.EnumPair) obj;
            return other.SETTING == this.SETTING && other.VALUE == this.VALUE;
        }
    }

    private record ButtonAppearance(@Nullable Blitter blitter, @Nullable Item item, List<Component> tooltipLines) {}
}
