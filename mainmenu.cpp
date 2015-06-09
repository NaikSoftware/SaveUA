#include "mainmenu.h"
#include "ui_mainmenu.h"
#include <QPainter>
#include <iostream>

MainMenu::MainMenu(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainMenu)
{
    ui->setupUi(this);
    pixmapBg.load(":/res/img/main/bg0.jpg");
}

void MainMenu::paintEvent(QPaintEvent *pe)
{
    // Розтягуємо фонове зображення в залежності від розміру вікна
    QPixmap stretchedBg;
    QPainter paint(this);
    stretchedBg = pixmapBg.scaledToHeight(this->height());
    if (this->width() > stretchedBg.width()) {
        stretchedBg = pixmapBg.scaledToWidth(this->width());
    }
    paint.drawPixmap(this->width() / 2 - stretchedBg.width() / 2,
                     this->height() / 2 - stretchedBg.height() /2, stretchedBg);
    QWidget::paintEvent(pe);
}

MainMenu::~MainMenu()
{
    delete ui;
}
