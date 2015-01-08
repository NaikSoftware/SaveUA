package ua.naiksoftware.waronline.desktop;

import java.util.Locale;

import ua.naiksoftware.waronline.res.Lng;
import ua.naiksoftware.waronline.res.Words;
import static ua.naiksoftware.waronline.res.Words.*;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Naik on 08.12.14.
 */
public class Language implements Lng {

    private ObjectMap<Words, String> en = new ObjectMap<Words, String>() {
        {
            put(PASS_AND_PLAY, "Pass-and-Play");
            put(PLAY_ONLINE, "Play Online");
            put(SETTINGS, "Settings");
            put(ABOUT, "About");
            put(BACK, "Back");
            put(NEXT, "Next");
            put(SELECT_MAP, "Select map");
            put(ADD, "Add");
            put(EDIT, "Edit");
            put(DELETE, "Delete");
            put(SOUND, "Sound");
            put(MUSIC_EFFECTS, "Music effects");
            put(BUILD_MAP, "Build map");
            put(INPUT_MAP_NAME, "Input map name");
            put(INPUT_MAP_SIZE, "Input map size");
            put(OK, "Ok");
            put(CANCEL, "Cancel");
            put(EXIT, "Exit");
            put(LOCATE_OBJECTS, "Locate objects");
            put(UNITS_GAMER, "Locate units gamer");
            put(FREE_UNITS, "Locate free units");
            put(MIN_TWO_GAMERS_REQUIRED, "Should be no less\nthan two players.");
            put(SAVE_MAP_AND_EXIT, "Save map and exit?");
            put(SIZE, "Size");
            put(MAX_GAMERS, "Maximum gamers");
        }
    };

    private ObjectMap<Words, String> ua = new ObjectMap<Words, String>() {
        {
            put(PASS_AND_PLAY, "Передай-i-Грай");
            put(PLAY_ONLINE, "Грати Онлайн");
            put(SETTINGS, "Налаштування");
            put(ABOUT, "Про гру");
            put(BACK, "Назад");
            put(NEXT, "Далi");
            put(SELECT_MAP, "Виберiть мапу");
            put(ADD, "Додати");
            put(EDIT, "Редагувати");
            put(DELETE, "Вилучити");
            put(SOUND, "Музика");
            put(MUSIC_EFFECTS, "Звуковi ефекти");
            put(BUILD_MAP, "Побудуйте мапу");
            put(INPUT_MAP_NAME, "Введiть iм'я мапи");
            put(INPUT_MAP_SIZE, "Введiть розмiр мапи");
            put(OK, "Добре");
            put(CANCEL, "Скасувати");
            put(EXIT, "Вийти");
            put(LOCATE_OBJECTS, "Розташуйте на мапi iншi об'екти");
            put(UNITS_GAMER, "Розташуйте юнiтiв гравця");
            put(FREE_UNITS, "Розташуйте вiльних юнiтiв");
            put(MIN_TWO_GAMERS_REQUIRED, "Повинно бути не менше\n   двох гравцiв.");
            put(SAVE_MAP_AND_EXIT, "Зберегти мапу\n   та вийти?");
            put(SIZE, "Розмiр");
            put(MAX_GAMERS, "Максимум гравцiв");
        }
    };

    private ObjectMap<Words, String> ru = new ObjectMap<Words, String>() {
        {
            put(PASS_AND_PLAY, "Передай-и-Играй");
            put(PLAY_ONLINE, "Играть Онлайн");
            put(SETTINGS, "Настройки");
            put(ABOUT, "Об игре");
            put(BACK, "Назад");
            put(NEXT, "Далее");
            put(SELECT_MAP, "Выберите карту");
            put(ADD, "Добавить");
            put(EDIT, "Редактировать");
            put(DELETE, "Удалить");
            put(SOUND, "Музыка");
            put(MUSIC_EFFECTS, "Звуковые эффекты");
            put(BUILD_MAP, "Постройте карту");
            put(INPUT_MAP_NAME, "Введите имя карты");
            put(INPUT_MAP_SIZE, "Введите размер карты");
            put(OK, "Ok");
            put(CANCEL, "Отмена");
            put(EXIT, "Выйти");
            put(LOCATE_OBJECTS, "Расположите на карте другие обьекты");
            put(UNITS_GAMER, "Расположите юнитов игрока");
            put(FREE_UNITS, "Расположите свободных юнитов");
            put(MIN_TWO_GAMERS_REQUIRED, "Должно быть не менее\n   двух игроков.");
            put(SAVE_MAP_AND_EXIT, "Сохранить карту\n   и выйти?");
            put(SIZE, "Размер");
            put(MAX_GAMERS, "Максимум игроков");
        }
    };

    @Override
    public String get(Words key) {
        ObjectMap<Words, String> l;
        String tag = Locale.getDefault().toLanguageTag().toUpperCase();
        if (tag.contains("UA")) {
            l = ua;
        } else if (tag.contains("RU")) {
            l = ru;
        } else {
            l = en;
        }
        return l.get(key);
    }
}
