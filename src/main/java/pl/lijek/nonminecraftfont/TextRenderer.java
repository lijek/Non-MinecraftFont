package pl.lijek.nonminecraftfont;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;
import java.util.Scanner;

public class TextRenderer {
    public final int[] colorCode = new int[32];
    public String fontName;
    public boolean bold, italic;
    UnicodeFont font;
    Font awtFont;

    public TextRenderer() {
        try {
            File configFolder = new File("config");
            configFolder.mkdirs();
            File config = new File(configFolder, "nonminecraftfont.txt");

            if (!config.exists()) {
                FileWriter configWriter = new FileWriter(config, false);
                configWriter.write("font: Arial\n");
                configWriter.write("bold: false\n");
                configWriter.write("italic: false\n");
                configWriter.close();
            }

            Scanner configReader = new Scanner(config);
            while (configReader.hasNextLine()) {
                String data = configReader.nextLine();
                if(data.contains("font:"))
                    fontName = data.split(": ")[1];
                else if(data.contains("bold:"))
                    bold = Boolean.parseBoolean(data.split(": ")[1]);
                else if(data.contains("italic:"))
                    italic = Boolean.parseBoolean(data.split(": ")[1]);
            }
            configReader.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(fontName);
        System.out.println(bold);
        System.out.println(italic);

        awtFont = new Font(fontName, Font.PLAIN, 100);
        font = new UnicodeFont(awtFont, 100, bold, italic);

        font.addAsciiGlyphs();
        font.getEffects().add(new ColorEffect());
        try {
            font.loadGlyphs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i >> 0 & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    public void drawText(int x, int y, String text) {
        font.drawString(x, y, text);
    }

    public void drawText(int x, int y, String text, Color colour) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, -2f, 0.0f);
        GL11.glScalef(0.1f, 0.1f, 1.0f);
        GL11.glTranslatef(x * 9, y * 9, 0.0f);
        font.drawString(x, y, text, colour);
        GL11.glPopMatrix();
    }

    public void drawText(int x, int y, String text, int colour) {
        drawText(x, y, text, new Color(colour));
    }

    public void drawText(int x, int y, String text, int colour, boolean shadow) {
        if (shadow) {
            int var6 = colour & -16777216;
            colour = (colour & 16579836) >> 2;
            colour = colour + var6;
        }
        if (!text.contains("§")) {
            drawText(x, y, text, colour);
            return;
        }

        String[] coloredText = text.split("(?=§)");
        int xOffset = 0;

        for (String coloredFragment : coloredText) {
            char code;
            if(coloredFragment.startsWith("§"))
                code = coloredFragment.toLowerCase(Locale.ROOT).charAt(1);
            else
                code = ' ';
            if (coloredFragment.length() <= 0)
                continue;
            coloredFragment = coloredFragment.startsWith("§") ? coloredFragment.substring(2) : coloredFragment;
            drawText(x + xOffset, y, coloredFragment, getColorFromColorCode(code, shadow));
            xOffset += getTextWidth(coloredFragment);
        }
    }

    public int getColorFromColorCode(char code) {
        int color = "0123456789abcdef".indexOf(String.valueOf(code).charAt(0));
        if (color < 0 || color > 15) {
            color = 15;
        }

        return colorCode[color];
    }

    public int getColorFromColorCode(char code, boolean shadow) {
        int color = "0123456789abcdef".indexOf(String.valueOf(code).charAt(0));
        if (color < 0 || color > 15) {
            color = 15;
        }

        return colorCode[color + (shadow ? 16 : 0)];
    }

    public int getTextWidth(String text) {
        if (text != null)
            return font.getWidth(text) / 10;
        else
            return 0;
    }
}
