
package storage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockManager {

    private final ConcurrentHashMap<String, ReentrantReadWriteLock> locks =
            new ConcurrentHashMap<>();

    private ReentrantReadWriteLock getLock(String resource) {
        return locks.computeIfAbsent(resource, key -> new ReentrantReadWriteLock());
    }

    public void acquireReadLock(String resource) {
        getLock(resource).readLock().lock();
    }

    public void releaseReadLock(String resource) {
        getLock(resource).readLock().unlock();
    }

    public void acquireWriteLock(String resource) {
        getLock(resource).writeLock().lock();
    }

    public void releaseWriteLock(String resource) {
        getLock(resource).writeLock().unlock();
    }
}
