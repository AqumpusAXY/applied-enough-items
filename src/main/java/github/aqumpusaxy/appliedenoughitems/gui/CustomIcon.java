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

package github.aqumpusaxy.appliedenoughitems.gui;

import appeng.client.gui.style.Blitter;
import github.aqumpusaxy.appliedenoughitems.AppliedEnoughItems;
import net.minecraft.resources.ResourceLocation;

public enum CustomIcon {

    SORT_BY_JEI(0, 0);

    public final int X;
    public final int Y;
    public final int WIDTH;
    public final int HEIGHT;

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AppliedEnoughItems.MODID,
        "textures/guis/states.png");
    public static final int TEXTURE_WIDTH = 16;
    public static final int TEXTURE_HEIGHT = 16;

    CustomIcon(int x, int y) {
        this.X = x;
        this.Y = y;
        this.WIDTH = 16;
        this.HEIGHT = 16;
    }

    public Blitter getBlitter() {
        return Blitter.texture(TEXTURE, TEXTURE_WIDTH, TEXTURE_HEIGHT)
                .src(X, Y, WIDTH, HEIGHT);
    }
}
