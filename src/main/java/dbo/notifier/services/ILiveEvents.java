package dbo.notifier.services;

import java.util.List;

public interface ILiveEvents {
    List<Integer> getList();

    void check();
}
