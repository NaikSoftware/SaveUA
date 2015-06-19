#include "menuwidget.h"
#include "ui_menuwidget.h"
#include "shared/screeninfo.h"
#include <QPainter>

MenuWidget::MenuWidget(MainWindow *parent) :
    MainWindowWidget(parent),
    ui(new Ui::MenuWidget)
{
    ui->setupUi(this);
    connect(ui->btnQuit, SIGNAL(clicked()), getWindow(), SLOT(close()));
    connect(ui->btnPlayOnline, SIGNAL(clicked()), this, SLOT(showGameOnline()));
    connect(ui->btnLocalMultiplayer, SIGNAL(clicked()), this, SLOT(showGameLocal()));
    mPixmapBg = new QPixmap(QString(":/res/img/main/bg%1.jpg").arg(rand() % 4));
    mPixLogo = new QPixmap(":/res/img/main/logo.png");
}

void MenuWidget::paintEvent(QPaintEvent *pe)
{
    int w = ScreenInfo::width();
    int h = ScreenInfo::height();
    // Розтягуємо фонове зображення в залежності від розміру вікна
    QPixmap stretchedBg = mPixmapBg->scaledToHeight(h);
    if (w > stretchedBg.width()) {
        stretchedBg = mPixmapBg->scaledToWidth(w);
    }
    QPainter paint(this);
    paint.drawPixmap(w / 2 - stretchedBg.width() / 2,
                     h / 2 - stretchedBg.height() / 2, stretchedBg);
    QWidget::paintEvent(pe);
    // Зміна розміру інших віджетів
    QFont largeFont("a_Albionic", ScreenInfo::largeFontSize());
    for (QWidget *child : ui->mainButtonsBlock->findChildren<QWidget*>()) {
        child->setFont(largeFont);
    }
    int logoW = w < 720 ? 180 : ui->mainButtonsBlock->width() / 1.5;
    ui->logo->setPixmap(mPixLogo->scaledToWidth(logoW));
}

void MenuWidget::showGameOnline()
{
    getWindow()->showGame(MainWindow::GameMode::ONLINE);
}

void MenuWidget::showGameLocal()
{
    getWindow()->showGame(MainWindow::GameMode::LOCAL_MULTIPLAYER);
}

MenuWidget::~MenuWidget()
{
    delete ui;
}
