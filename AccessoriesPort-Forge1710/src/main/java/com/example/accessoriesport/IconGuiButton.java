package com.example.accessoriesport;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.Gui;

/**
 * Compact vanilla-style icon button used by the cosmetic armor UI.
 * The complete framed icon is drawn at 15x15 pixels.
 */
public class IconGuiButton extends GuiButton {
    public static final int ICON_HELMET = 0;
    public static final int ICON_EYE = 1;

    private final int icon;

    public IconGuiButton(int id, int x, int y, int width, int height, int icon) {
        super(id, x, y, width, height, "");
        this.icon = icon;
    }

    @Override
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        super.drawButton(minecraft, mouseX, mouseY);
        if (!drawButton) return;

        int x = xPosition + (width - 15) / 2;
        int y = yPosition + (height - 15) / 2;

        // 15x15 vanilla inventory-slot frame, including its bevel.
        Gui.drawRect(x, y, x + 15, y + 15, 0xFF252525);
        Gui.drawRect(x + 1, y + 1, x + 14, y + 14, 0xFF777777);
        Gui.drawRect(x + 1, y + 1, x + 13, y + 2, 0xFFD0D0D0);
        Gui.drawRect(x + 1, y + 1, x + 2, y + 13, 0xFFB8B8B8);
        Gui.drawRect(x + 2, y + 3, x + 13, y + 13, 0xFF8B8B8B);
        Gui.drawRect(x + 12, y + 3, x + 13, y + 13, 0xFF555555);

        String[] pixels = icon == ICON_HELMET ? HELMET : EYE;
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                int color = pixelColor(pixels[row].charAt(col));
                if (color != 0) {
                    Gui.drawRect(x + 2 + col, y + 2 + row,
                        x + 3 + col, y + 3 + row, color);
                }
            }
        }
    }

    private int pixelColor(char pixel) {
        switch (pixel) {
            case 'D': return 0xFF073B42;
            case 'S': return 0xFF07545A;
            case 'T': return 0xFF11B9B1;
            case 'C': return 0xFF39E0D1;
            case 'W': return 0xFFB5FFF0;
            case 'G': return 0xFF367A42;
            case 'L': return 0xFF82C65C;
            case 'H': return 0xFFC1E69A;
            case 'B': return 0xFF244B22;
            case 'P': return 0xFF102E1D;
            default: return 0;
        }
    }

    private static final String[] HELMET = {
        "....SSS....",
        "...STTTS...",
        "..STCCCTS..",
        ".STCWWCCTS.",
        ".STCWWCCTS.",
        ".STCCCCCTS.",
        ".SCCDDCCCS.",
        "..SDD.DDS..",
        "..S.DD.DS..",
        "...S..S....",
        "....SS....."
    };

    private static final String[] EYE = {
        "....GGG....",
        "...GLLLG...",
        "..GLLLLLG..",
        ".GLLHHLLLG.",
        "GLLHHHLLLGG",
        "GLLLBBLLLGG",
        ".GLLBBLLLG.",
        "..GLLLPLG..",
        "...GLLLG...",
        "....GGG....",
        ".....G....."
    };
}
