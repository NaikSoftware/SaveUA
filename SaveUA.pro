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
        mainmenu.cpp \
    test/testform.cpp \
    shared/screeninfo.cpp

HEADERS  += mainmenu.h \
    test/testform.h \
    shared/screeninfo.h

FORMS    += mainmenu.ui \
    test/testform.ui

RESOURCES += \
    images.qrc \
    fonts.qrc \
    styles.qrc
