#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    void paintEvent(QPaintEvent *pe);
    enum GameMode {
        ONLINE, LOCAL_MULTIPLAYER
    };

    ~MainWindow();

private:
    Ui::MainWindow *ui;

public slots:
    void showGame(GameMode mode);
};

#endif // MAINWINDOW_H
