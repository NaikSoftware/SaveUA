#include "mainmenu.h"
#include <QApplication>
#include <QFontDatabase>
#include <QFile>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    QFontDatabase::addApplicationFont(":/res/fonts/albionic.ttf");
    QFile file(":/res/style/main.css");
    file.open(QFile::ReadOnly);
    QString style = QLatin1String(file.readAll());
    a.setStyleSheet(style);

    MainMenu w;
    w.showMaximized();
    //w.showFullScreen();

    return a.exec();
}
