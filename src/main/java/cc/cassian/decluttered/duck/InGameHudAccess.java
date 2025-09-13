package cc.cassian.decluttered.duck;

import cc.cassian.decluttered.AccessBar;

public interface InGameHudAccess {

    AccessBar getOpenAccessBar();

    void closeOpenAccessbar(boolean select);

    void openAccessbar(int num);

    boolean isBarWithNumberOpen(int number);

    void refreshAccessbars();
}
