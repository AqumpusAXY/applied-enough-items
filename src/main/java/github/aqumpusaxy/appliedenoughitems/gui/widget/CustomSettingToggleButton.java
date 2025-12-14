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

package github.aqumpusaxy.appliedenoughitems.gui.widget;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.Icon;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.widgets.IconButton;
import appeng.core.localization.ButtonToolTips;
import com.mojang.blaze3d.systems.RenderSystem;
import github.aqumpusaxy.appliedenoughitems.config.CustomSortOrder;
import github.aqumpusaxy.appliedenoughitems.gui.CustomIcon;
import github.aqumpusaxy.appliedenoughitems.localization.CustomButtonToolTips;
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

import static github.aqumpusaxy.appliedenoughitems.config.ClientConfig.CUSTOM_SORT_ORDER;

public class CustomSettingToggleButton<T extends Enum<T>> extends IconButton {

    private static Map<EnumPair<?>, ButtonAppearance> appearances;
    private final ForgeConfigSpec.EnumValue<T> BUTTON_SETTING;
    private final IHandler<CustomSettingToggleButton<T>> ON_PRESS;
//    private final EnumSet<T> validValues;
    private T currentValue;

    @FunctionalInterface
    public interface IHandler<T extends CustomSettingToggleButton<?>> {
        void handle(T button, boolean backwards);
    }

    public CustomSettingToggleButton(ForgeConfigSpec.EnumValue<T> setting, IHandler<CustomSettingToggleButton<T>> onPress) {
        super(CustomSettingToggleButton::onPress);
        this.ON_PRESS = onPress;

        this.BUTTON_SETTING = setting;
        this.currentValue = BUTTON_SETTING.get();
        if (appearances == null) {
            appearances = new HashMap<>();
            registerApp(CustomIcon.SORT_BY_JEI.getBlitter(), CUSTOM_SORT_ORDER, CustomSortOrder.JEI,
                    CustomButtonToolTips.SortBy, CustomButtonToolTips.JEI);
            registerApp(Icon.SORT_BY_NAME.getBlitter(), CUSTOM_SORT_ORDER, CustomSortOrder.NAME,
                    CustomButtonToolTips.SortBy, CustomButtonToolTips.ItemName);
            registerApp(Icon.SORT_BY_AMOUNT.getBlitter(), CUSTOM_SORT_ORDER, CustomSortOrder.AMOUNT,
                    CustomButtonToolTips.SortBy, CustomButtonToolTips.NumberOfItems);
            registerApp(Icon.SORT_BY_MOD.getBlitter(), CUSTOM_SORT_ORDER, CustomSortOrder.MOD,
                    CustomButtonToolTips.SortBy, CustomButtonToolTips.Mod);
        }
    }

    private static void onPress(Button btn) {
        if (btn instanceof CustomSettingToggleButton) {
            ((CustomSettingToggleButton<?>) btn).triggerPress();
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

    private static <T extends Enum<T>> void registerApp(Blitter blitter, ForgeConfigSpec.EnumValue<T> setting, T val,
                                                        CustomButtonToolTips title, Component... tooltipLines) {
        var lines = new ArrayList<Component>();
        lines.add(title.text());
        Collections.addAll(lines, tooltipLines);

        appearances.put(
                new EnumPair<>(setting, val),
                new ButtonAppearance(blitter, null, lines));
    }

    private static <T extends Enum<T>> void registerApp(Blitter blitter, ForgeConfigSpec.EnumValue<T> setting, T val,
                                    CustomButtonToolTips title, CustomButtonToolTips hint) {
        registerApp(blitter, setting, val, title, hint.text());
    }

    @Nullable
    private CustomSettingToggleButton.ButtonAppearance getAppearance() {
        return appearances.get(new CustomSettingToggleButton.EnumPair<>(this.BUTTON_SETTING, this.currentValue));
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

    public T getCurrentValue() {
        return this.currentValue;
    }

    public T getNextValue(boolean backwards) {
        T[] values = this.currentValue.getDeclaringClass().getEnumConstants();
        int nextOrdinal = (this.currentValue.ordinal() + (backwards ? -1 : 1)) % values.length;
        return values[nextOrdinal];
    }

    public void toggleSetting(boolean backwards) {
        this.BUTTON_SETTING.set(getNextValue(backwards));
        this.currentValue = this.BUTTON_SETTING.get();
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

        if (this.BUTTON_SETTING == null || this.currentValue == null) {
            return Collections.emptyList();
        }

        var buttonAppearance = appearances.get(new EnumPair<>(this.BUTTON_SETTING, this.currentValue));
        if (buttonAppearance == null) {
            return Collections.singletonList(ButtonToolTips.NoSuchMessage.text());
        }

        return buttonAppearance.tooltipLines;
    }

    private static final class EnumPair<T extends Enum<T>> {

        final ForgeConfigSpec.EnumValue<T> SETTING;
        final T VALUE;

        public EnumPair(ForgeConfigSpec.EnumValue<T> setting, T value) {
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
            final EnumPair<?> other = (EnumPair<?>) obj;
            return other.SETTING == this.SETTING && other.VALUE == this.VALUE;
        }
    }

    private record ButtonAppearance(@Nullable Blitter blitter, @Nullable Item item, List<Component> tooltipLines) {}
}
