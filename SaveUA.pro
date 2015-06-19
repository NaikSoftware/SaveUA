#-------------------------------------------------
#
# Project created by QtCreator 2015-06-09T12:03:18
#
#-------------------------------------------------

QT       += core gui

CONFIG += c++11

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = SaveUA
TEMPLATE = app


SOURCES += main.cpp\
    shared/screeninfo.cpp \
    game/gamewidget.cpp \
    menu/menuwidget.cpp \
    mainwindow.cpp \
    shared/mainwindowwidget.cpp

HEADERS  += \
    shared/screeninfo.h \
    game/gamewidget.h \
    menu/menuwidget.h \
    mainwindow.h \
    shared/mainwindowwidget.h

FORMS    += \
    game/gamewidget.ui \
    menu/menuwidget.ui \
    mainwindow.ui

RESOURCES += \
    images.qrc \
    fonts.qrc \
    styles.qrc
