package com.winidy.astanaclock;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

/**
 * Рисует на главном меню:
 *  - время Астаны (Казахстан, UTC+5) крупно, вместо логотипа MINECRAFT;
 *  - надпись «Здравствуйте, winidy» над кнопками.
 *
 * Положение считается от верхней кнопки меню, поэтому надписи не налезают на кнопки,
 * даже если другие моды (например, Mod Menu) сдвигают кнопки.
 *
 * Маппинги: Mojang (официальные). Minecraft 1.21.11.
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

    // Размер текста (множитель к обычному шрифту) и высота строки обычного шрифта.
    private static final float CLOCK_SCALE = 3.0f;
    private static final float GREETING_SCALE = 1.5f;
    private static final int LINE = 9;

    // Отступы в пикселях интерфейса: приветствие над кнопками, часы над приветствием.
    private static final int GAP_GREETING = 12;
    private static final int GAP_CLOCK = 6;

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof TitleScreen) {
                ScreenEvents.afterRender(screen).register(
                        (s, graphics, mouseX, mouseY, tickDelta) -> draw(s, graphics));
            }
        });
    }

    /** Верхний край самой верхней «большой» кнопки меню. */
    private static int buttonsTop(Screen screen) {
        int top = Integer.MAX_VALUE;
        for (GuiEventListener child : screen.children()) {
            if (child instanceof AbstractWidget w && w.visible && w.getWidth() >= 98) {
                top = Math.min(top, w.getY());
            }
        }
        // Запасной вариант, если кнопок не нашлось: обычная высота ванильных кнопок.
        return top == Integer.MAX_VALUE ? screen.height / 4 + 48 : top;
    }

    private static void draw(Screen screen, GuiGraphics g) {
        Font font = Minecraft.getInstance().font;
        int cx = screen.width / 2;

        int greetingH = Math.round(LINE * GREETING_SCALE);
        int clockH = Math.round(LINE * CLOCK_SCALE);

        int greetingY = buttonsTop(screen) - GAP_GREETING - greetingH;
        int clockY = Math.max(4, greetingY - GAP_CLOCK - clockH);

        String time = LocalTime.now(ASTANA).format(HH_MM);

        drawCentered(g, font, time, cx, clockY, CLOCK_SCALE, SHADOW, 1);
        drawCentered(g, font, time, cx, clockY, CLOCK_SCALE, GOLD, 0);

        drawCentered(g, font, GREETING, cx, greetingY, GREETING_SCALE, SHADOW, 1);
        drawCentered(g, font, GREETING, cx, greetingY, GREETING_SCALE, GOLD_LIGHT, 0);
    }

    /** Рисует строку по центру точки (cx) с верхним краем y и масштабом; offset — сдвиг тени. */
    private static void drawCentered(GuiGraphics g, Font font, String text,
                                     int cx, int y, float scale, int color, int offset) {
        g.pose().pushMatrix();
        g.pose().translate(cx + offset, y + offset);
        g.pose().scale(scale, scale);
        g.drawString(font, text, -font.width(text) / 2, 0, color, false);
        g.pose().popMatrix();
    }
}
