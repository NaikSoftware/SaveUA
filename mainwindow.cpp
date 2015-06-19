#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "shared/screeninfo.h"
#include "menu/menuwidget.h"
#include "game/gamewidget.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    setCentralWidget(new MenuWidget((MainWindow*)this));
}

void MainWindow::paintEvent(QPaintEvent *pe)
{
    ScreenInfo::notifyChanged(width(), height());
    QWidget::paintEvent(pe);
}

void MainWindow::showGame(MainWindow::GameMode mode)
{
    setCentralWidget(new GameWidget((MainWindow*)this));
}

MainWindow::~MainWindow()
{
    delete ui;
}
