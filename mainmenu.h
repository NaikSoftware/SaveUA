#ifndef MAINMENU_H
#define MAINMENU_H

#include <QMainWindow>

namespace Ui {
class MainMenu;
}

class MainMenu : public QMainWindow
{
    Q_OBJECT

public:
    // explicit - заборона автоматичного копіювання при MainMenu m = QWidget
    explicit MainMenu(QWidget *parent = 0);
    void paintEvent(QPaintEvent *);
    ~MainMenu();

private:
    Ui::MainMenu *ui;
    QPixmap pixmapBg, pixLogo;
};

#endif // MAINMENU_H
