#ifndef GAMEWIDGET_H
#define GAMEWIDGET_H

#include <QWidget>
#include "shared/mainwindowwidget.h"
#include "mainwindow.h"

namespace Ui {
class GameWidget;
}

class GameWidget : public MainWindowWidget
{
    Q_OBJECT

public:
    explicit GameWidget(MainWindow *parent = 0);
    ~GameWidget();

private:
    Ui::GameWidget *ui;
};

#endif // GAMEWIDGET_H
