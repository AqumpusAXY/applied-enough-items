package github.AqumpusAXY.appliedenoughitems.mixin.jei;

import github.AqumpusAXY.appliedenoughitems.sort.JEISortComparator;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.transfer.IRecipeTransferManager;
import mezz.jei.api.registration.IRuntimeRegistration;
import mezz.jei.api.runtime.*;
import mezz.jei.common.config.*;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.core.util.LoggedTimer;
import mezz.jei.gui.bookmarks.BookmarkList;
import mezz.jei.gui.config.IBookmarkConfig;
import mezz.jei.gui.config.ILookupHistoryConfig;
import mezz.jei.gui.config.IngredientTypeSortingConfig;
import mezz.jei.gui.config.ModNameSortingConfig;
import mezz.jei.gui.events.GuiEventHandler;
import mezz.jei.gui.filter.IFilterTextSource;
import mezz.jei.gui.ingredients.IListElement;
import mezz.jei.gui.ingredients.IListElementInfo;
import mezz.jei.gui.ingredients.IngredientFilter;
import mezz.jei.gui.input.ClientInputHandler;
import mezz.jei.gui.input.CombinedRecipeFocusSource;
import mezz.jei.gui.input.ICharTypedHandler;
import mezz.jei.gui.input.handlers.DragRouter;
import mezz.jei.gui.input.handlers.UserInputRouter;
import mezz.jei.gui.overlay.IngredientListOverlay;
import mezz.jei.gui.overlay.bookmarks.BookmarkOverlay;
import mezz.jei.gui.overlay.bookmarks.history.LookupHistory;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.gui.startup.GuiConfigData;
import mezz.jei.gui.startup.JeiEventHandlers;
import mezz.jei.gui.startup.JeiGuiStarter;
import mezz.jei.gui.startup.ResourceReloadHandler;
import mezz.jei.gui.util.FocusUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Comparator;
import java.util.List;

@Mixin(value = JeiGuiStarter.class, remap = false)
public class JeiGuiStarterMixin {

    @Inject(method = "start",
            at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void start(
            IRuntimeRegistration registration,
            CallbackInfoReturnable<JeiEventHandlers> cir,

            LoggedTimer timer,
            IConnectionToServer serverConnection,
            Textures textures,
            IInternalKeyMappings keyMappings,
            IScreenHelper screenHelper,
            IRecipeTransferManager recipeTransferManager,
            IRecipeManager recipeManager,
            IIngredientManager ingredientManager,
            IEditModeConfig editModeConfig,
            IJeiHelpers jeiHelpers,
            IIngredientVisibility ingredientVisibility,
            IColorHelper colorHelper,
            IModIdHelper modIdHelper,
            IFocusFactory focusFactory,
            IGuiHelper guiHelper,
            IFilterTextSource filterTextSource,
            Minecraft minecraft,
            ClientLevel level,
            RegistryAccess registryAccess,
            List<IListElementInfo<?>> ingredientList,
            GuiConfigData configData,
            ModNameSortingConfig modNameSortingConfig,
            IngredientTypeSortingConfig ingredientTypeSortingConfig,
            IClientToggleState toggleState,
            IBookmarkConfig bookmarkConfig,
            ILookupHistoryConfig lookupHistoryConfig,
            IJeiClientConfigs jeiClientConfigs,
            IClientConfig clientConfig,
            IIngredientGridConfig ingredientListConfig,
            IIngredientGridConfig bookmarkListConfig,
            IIngredientFilterConfig ingredientFilterConfig,
            Comparator<IListElement<?>> ingredientComparator,
            IngredientFilter ingredientFilter,
            IIngredientFilter ingredientFilterApi,
            LookupHistory lookupHistory,
            IngredientListOverlay ingredientListOverlay,
            BookmarkList bookmarkList,
            BookmarkOverlay bookmarkOverlay,
            GuiEventHandler guiEventHandler,
            RecipesGui recipesGui,
            CombinedRecipeFocusSource recipeFocusSource,
            List<ICharTypedHandler> charTypedHandlers,
            FocusUtil focusUtil,
            UserInputRouter userInputRouter,
            DragRouter dragRouter,
            ClientInputHandler clientInputHandler,
            ResourceReloadHandler resourceReloadHandler) {

        lookupHistory.addSourceListChangedListener(() -> JEISortComparator.setHistoryElements(lookupHistory.getElements()));
    }
}
