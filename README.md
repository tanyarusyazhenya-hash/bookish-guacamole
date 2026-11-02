# Astana Clock — мод для Minecraft 1.21.11 (Fabric)

На главном меню рисует время Астаны (UTC+5, часы и минуты) и надпись
«Здравствуйте, winidy». Работает вместе с ресурспаком Gold & Black Menu.

Проект основан на официальном шаблоне FabricMC/fabric-example-mod (ветка 1.21.11).
Автосборка (GitHub Actions) лежит в `.github/workflows/build.yml`.

## Как получить готовый мод
1. Загрузи все файлы этого проекта в свой репозиторий на GitHub.
2. Открой вкладку **Actions**, дождись зелёной галочки у последнего запуска (3–6 минут).
3. Зайди в запуск, внизу в разделе **Artifacts** скачай `astanaclock-mod`.
4. Распакуй скачанный zip: внутри `astanaclock-1.0.0.jar` — это и есть мод.

## Что нужно в игре
- Minecraft Java 1.21.11
- Fabric Loader 0.20.1 или новее
- Fabric API 0.141.6+1.21.11 (Modrinth или CurseForge)
- Файлы `astanaclock-1.0.0.jar` и Fabric API — в папку `.minecraft/mods`

## Если в репозиторий не загрузилась папка .github
Создай файл вручную: **Add file → Create new file**, в поле имени введи
`.github/workflows/build.yml` и вставь туда содержимое файла `build.yml` из этого проекта.

## Настройки внешнего вида
Размер, цвета и положение текста — в начале файла
`src/client/java/com/winidy/astanaclock/AstanaClockClient.java`.
