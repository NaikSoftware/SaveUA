#include "gamewidget.h"
#include "ui_gamewidget.h"

GameWidget::GameWidget(MainWindow *parent) :
    MainWindowWidget(parent),
    ui(new Ui::GameWidget)
{
    ui->setupUi(this);
}

GameWidget::~GameWidget()
{
    delete ui;
}
