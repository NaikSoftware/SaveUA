#include "mainwindowwidget.h"

MainWindowWidget::MainWindowWidget(MainWindow *parent) : QWidget(parent)
{
    mWindow = parent;
}

MainWindow *MainWindowWidget::getWindow()
{
    return mWindow;
}

