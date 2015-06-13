#include "mainmenu.h"
#include "ui_mainmenu.h"
#include "shared/screeninfo.h"
#include <QPainter>
#include <QDebug>

MainMenu::MainMenu(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainMenu)
{
    ui->setupUi(this);
    pixmapBg.load(QString(":/res/img/main/bg%1.jpg").arg(rand() % 4));
    pixLogo.load(":/res/img/main/logo.png");
}

void MainMenu::paintEvent(QPaintEvent *pe)
{
    int w = this->width();
    int h = this->height();
    // Розтягуємо фонове зображення в залежності від розміру вікна
    QPixmap stretchedBg = pixmapBg.scaledToHeight(h);
    if (w > stretchedBg.width()) {
        stretchedBg = pixmapBg.scaledToWidth(w);
    }
    QPainter paint(this);
    paint.drawPixmap(w / 2 - stretchedBg.width() / 2,
                     h / 2 - stretchedBg.height() / 2, stretchedBg);
    QWidget::paintEvent(pe);
    // Зміна розміру інших віджетів
    ScreenInfo::notifyChanged(w, h);
    QFont largeFont("a_Albionic", ScreenInfo::largeFontSize());
    for (QWidget *child : ui->mainButtonsBlock->findChildren<QWidget*>()) {
        child->setFont(largeFont);
    }
    int logoW = w < 720 ? 180 : ui->mainButtonsBlock->width() / 1.5;
    ui->logo->setPixmap(pixLogo.scaledToWidth(logoW));
}

MainMenu::~MainMenu()
{
    delete ui;
}
