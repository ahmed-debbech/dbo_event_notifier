package dbo.notifier.services;

import dbo.notifier.services.enumeration.EventType;

import java.util.List;

public interface ILiveEvents {
    List<Integer> getList();

    void check();

}
