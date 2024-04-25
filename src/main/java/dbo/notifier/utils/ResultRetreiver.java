package dbo.notifier.utils;

import java.util.HashMap;
import java.util.Map;

public class ResultRetreiver {
    private static ResultRetreiver INSTANCE;

    private Map<Integer, Object> pids;

    public Map<Integer, Object> getPids() {
        return pids;
    }

    public void add(int pid, Object o) {
        pids.put(pid, o);
    }

    private ResultRetreiver() {
        pids = new HashMap<>();
    }

    public static ResultRetreiver getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ResultRetreiver();
        }

        return INSTANCE;
    }

    public Object waitFor(int pid) {
        long i = 30;
        while(i>0){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i--;
            if((ResultRetreiver.getInstance().getPids().containsKey(pid))) break;
        }
        if(i <= 0) return null;

        return ResultRetreiver.getInstance().getPids().get(pid);
    }
}
