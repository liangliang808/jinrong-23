package com.thirdpart.uuid;

public class SofteemUuid {

    private static SofteemUuid ourInstance = new SofteemUuid();

    public static SofteemUuid getInstance() {
        return ourInstance;
    }

    private SofteemUuid() {
    }

    public void init(long centerId, long workerId) {
        idWorker = new SnowflakeIdWorker(workerId, centerId);
    }

    private SnowflakeIdWorker idWorker;

    public long getUUID() {
        return idWorker.nextId();
    }


}
