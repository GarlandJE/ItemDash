package mnm.mods.itemdash.gui;

import static net.minecraft.util.text.TextFormatting.DARK_PURPLE;
import static net.minecraft.util.text.TextFormatting.RESET;

import mnm.mods.itemdash.LiteModItemDash;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import javax.annotation.Nonnull;

public class ItemIcon extends Gui {

    @Nonnull
    private final ItemStack item;
    private final RenderItem render;

    public ItemIcon(@Nonnull ItemStack item) {
        this.item = item;
        this.render = Minecraft.getMinecraft().getRenderItem();
    }

    public void renderAt(int x, int y) {
        RenderHelper.enableGUIStandardItemLighting();
        render.zLevel = 100;
        render.renderItemAndEffectIntoGUI(item, x, y);
        RenderHelper.disableStandardItemLighting();
        render.zLevel = 0;
    }

    public void renderTooltip(int x, int y) {
        Minecraft mc = Minecraft.getMinecraft();
        List<String> list = getTooltip(mc.player);
        drawHoveringText(list, x, y);
    }

    private List<String> getTooltip(EntityPlayer player) {
        List<String> tooltip = this.item.getTooltip(player, false);
        if (!tooltip.isEmpty()) {

            // add numerical id
            String name = tooltip.get(0);
            int numId = Item.getIdFromItem(this.item.getItem());
            if (item.getHasSubtypes()) {
                name = name + String.format(" (%d/%d)", numId, item.getMetadata());
            } else {
                name = name + String.format(" (%d)", numId);
            }
            tooltip.set(0, name);

            // string id goes at the end
            String id = Item.REGISTRY.getNameForObject(item.getItem()).toString();
            tooltip.add(TextFormatting.LIGHT_PURPLE + id);

            // itemdash options
            String option = "Press " + DARK_PURPLE + "%s" + RESET + " to %s";
            String favorites = String.format(option, "CTRL + F", "%s favorites");
            boolean fav = LiteModItemDash.getInstance().favorites.has(item);
            tooltip.add(String.format(favorites, fav ? "remove from" : "add to"));

//            tooltip.add(String.format(option, "CTRL + N", "customize"));
        }
        return tooltip;
    }

    protected void drawHoveringText(List<String> textLines, int x, int y) {
        if (!textLines.isEmpty()) {
            while (y < 16)
                y++;
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int k = 0;
            for (String s : textLines) {
                int l = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s);

                if (l > k) {
                    k = l;
                }
            }

            int j2 = x + 12;
            int k2 = y - 12;
            int i1 = 8;

            if (textLines.size() > 1) {
                i1 += 2 + (textLines.size() - 1) * 10;
            }

            j2 -= 28 + k;
            Minecraft mc = Minecraft.getMinecraft();
            assert mc.currentScreen != null;
            int height = mc.currentScreen.height;
            if (k2 + i1 + 6 > height) {
                k2 = height - i1 - 6;
            }

            this.zLevel = 300.0F;
            int j1 = 0xF0100010;
            this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 0x505000FF;
            int l1 = (k1 & 0xFEFEFE) >> 1 | k1 & 0xFF000000;
            this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < textLines.size(); ++i2) {
                String s1 = textLines.get(i2);
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            this.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    public ItemStack getStack(int button) {
        int size = 1;
        if (button == 0)
            size = item.getMaxStackSize();
        return new ItemStack(item.getItem(), size, item.getItemDamage());
    }

    public ItemStack getStack() {
        return this.item;
    }
}
