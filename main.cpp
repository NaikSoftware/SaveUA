#include "mainmenu.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainMenu w;
    w.showMaximized();
    //w.showFullScreen();

    return a.exec();
}
