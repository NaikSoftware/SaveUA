#ifndef MENUWIDGET_H
#define MENUWIDGET_H

#include <QWidget>
#include "mainwindow.h"
#include "shared/mainwindowwidget.h"

namespace Ui {
class MenuWidget;
}

class MenuWidget : public MainWindowWidget
{
    Q_OBJECT

public:
    explicit MenuWidget(MainWindow *parent = 0);
    void paintEvent(QPaintEvent *);
    ~MenuWidget();

private:
    Ui::MenuWidget *ui;
    QPixmap *mPixmapBg, *mPixLogo;

public slots:
    void showGameOnline();
    void showGameLocal();
};

#endif // MENUWIDGET_H
