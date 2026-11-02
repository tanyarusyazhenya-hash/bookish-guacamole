package com.winidy.astanaclock;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;

/**
 * Рисует на главном меню:
 *  - время Астаны (Казахстан, UTC+5) крупно, вместо логотипа MINECRAFT;
 *  - надпись «Здравствуйте, winidy» по центру экрана.
 *
 * Маппинги: Mojang (официальные). Minecraft 1.21.11, Fabric API 0.139.5+1.21.11.
 */
public class AstanaClockClient implements ClientModInitializer {

    // Астана живёт по UTC+5 круглый год (без перехода на летнее время).
    private static final ZoneOffset ASTANA = ZoneOffset.ofHours(5);
    private static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

    private static final String GREETING = "Здравствуйте, winidy";

    // Цвета в формате ARGB (с 1.21.6 первый байт — прозрачность, 0xFF = непрозрачно).
    private static final int GOLD = 0xFFFFD65A;
    private static final int GOLD_LIGHT = 0xFFFFE278;
    private static final int SHADOW = 0xFF281C04;

    // Размер текста (множитель к обычному шрифту).
    private static final float CLOCK_SCALE = 3.0f;
    private static final float GREETING_SCALE = 1.5f;

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof TitleScreen) {
                ScreenEvents.afterRender(screen).register(
                        (s, graphics, mouseX, mouseY, tickDelta) -> draw(graphics, s.width, s.height));
            }
        });
    }

    private static void draw(GuiGraphics g, int width, int height) {
        Font font = Minecraft.getInstance().font;

        // Кнопки ванильного меню начинаются на height/4 + 48.
        // Часы — выше кнопок, приветствие — между часами и кнопками.
        int clockY = height / 4 - 14;
        int greetingY = height / 4 + 48 - 22;

        String time = LocalTime.now(ASTANA).format(HH_MM);

        drawCentered(g, font, time, width / 2, clockY, CLOCK_SCALE, SHADOW, 1);
        drawCentered(g, font, time, width / 2, clockY, CLOCK_SCALE, GOLD, 0);

        drawCentered(g, font, GREETING, width / 2, greetingY, GREETING_SCALE, SHADOW, 1);
        drawCentered(g, font, GREETING, width / 2, greetingY, GREETING_SCALE, GOLD_LIGHT, 0);
    }

    /** Рисует строку по центру точки (cx, y) с масштабом; offset — сдвиг тени в пикселях. */
    private static void drawCentered(GuiGraphics g, Font font, String text,
                                     int cx, int y, float scale, int color, int offset) {
        g.pose().pushMatrix();
        g.pose().translate(cx + offset, y + offset);
        g.pose().scale(scale, scale);
        g.drawString(font, text, -font.width(text) / 2, 0, color, false);
        g.pose().popMatrix();
    }
}
