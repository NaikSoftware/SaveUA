#ifndef SCREENINFO_H
#define SCREENINFO_H

class ScreenInfo
{
public:
    static void notifyChanged(int w, int h);
    static const int width();
    static const int height();
    static const int normalFontSize();
    static const int mediumFontSize();
    static const int largeFontSize();

private:
    static int mW, mH;
    static int mNormalFontSize;
    static int mMediumFontSize;
    static int mLargeFontSize;

};

#endif // SCREENINFO_H
