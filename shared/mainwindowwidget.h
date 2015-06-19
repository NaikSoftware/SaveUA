#ifndef MAINWINDOWWIDGET_H
#define MAINWINDOWWIDGET_H

#include <QWidget>
#include "mainwindow.h"

class MainWindowWidget : public QWidget
{
    Q_OBJECT
public:
    explicit MainWindowWidget(MainWindow *parent = 0);

protected:
    MainWindow* getWindow();

private:
    MainWindow *mWindow;

};

#endif // MAINWINDOWWIDGET_H
