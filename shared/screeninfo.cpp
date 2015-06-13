#include "screeninfo.h"

int ScreenInfo::mW = 0;
int ScreenInfo::mH = 0;
int ScreenInfo::mNormalFontSize = 0;
int ScreenInfo::mMediumFontSize = 0;
int ScreenInfo::mLargeFontSize = 0;

void ScreenInfo::notifyChanged(int w, int h)
{
    mW = w;
    mH = h;
    int max = w > h ? w : h;
    mNormalFontSize = max / 60;
    mMediumFontSize = max / 45;
    mLargeFontSize = max / 30;
}

const int ScreenInfo::normalFontSize()
{
    return mNormalFontSize;
}

const int ScreenInfo::mediumFontSize()
{
    return mMediumFontSize;
}

const int ScreenInfo::largeFontSize()
{
    return mLargeFontSize;
}
