package com.es.phoneshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 10;
    private final Map<String, Long> countMap = new ConcurrentHashMap<>();

    {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Timer(), 0, 1, TimeUnit.MINUTES);
    }

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionService.SingletonHelper.INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }

    private static class SingletonHelper {
        private static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }

    private class Timer implements Runnable {
        public void run() {
            countMap.clear();
        }
    }

}
