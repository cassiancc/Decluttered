package cc.cassian.decluttered.duck;

import cc.cassian.decluttered.AccessBar;

public interface InGameHudAccess {

    AccessBar decluttered$getOpenAccessBar();

    void decluttered$closeOpenAccessbar(boolean select);

    void decluttered$openAccessbar(int num);

    boolean decluttered$isBarWithNumberOpen(int number);

    void decluttered$refreshAccessbars();
}
