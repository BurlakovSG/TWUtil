package ru.d51x.twutil;

import android.tw.john.TWUtil;

/**
 * Created by Dmitriy on 09.02.2015.
 */
public class TWSetting extends TWUtil {
    private static int mCount;
    private static TWSetting mTW;

    static {
        mCount = 0;
        mTW = new TWSetting(17);
    }

    public TWSetting(int paramInt) {
        super(paramInt);
    }

    public static TWSetting open() {
        int i = mCount;
        mCount = i + 1;
        if (i == 0) {
            if (mTW.open(new short[]{(short) 258, (short) 259, (short) 260, (short) 262, (short) 264, (short) 265, (short) 266, (short) 267, (short) 272, (short) 274, (short) 1539, (short) 1541}) != 0) {
                mCount--;
                return null;
            }
            mTW.start();
        }
        return mTW;
    }

    public void close() {
        if (mCount > 0) {
            int i = mCount - 1;
            mCount = i;
            if (i == 0) {
                stop();
                super.close();
            }
        }
    }
}