
package storage;

import java.util.LinkedHashMap;
import java.util.Map;

public class BufferPoolManager {

    private final int capacity;
    private final LinkedHashMap<Integer, Page> cache;

    public BufferPoolManager(int capacity) {
        this.capacity = capacity;

        cache = new LinkedHashMap<Integer, Page>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Page> eldest) {
                return size() > BufferPoolManager.this.capacity;
            }
        };
    }

    public void addPage(Page page) {
        cache.put(page.getPageId(), page);
    }

    public Page getPage(int pageId) {
        return cache.get(pageId);
    }

    public boolean containsPage(int pageId) {
        return cache.containsKey(pageId);
    }

    public void removePage(int pageId) {
        cache.remove(pageId);
    }

    public void displayCache() {
        for (Map.Entry<Integer, Page> entry : cache.entrySet()) {
            System.out.println("Page ID: " + entry.getKey());
        }
    }

    public int getCacheSize() {
        return cache.size();
    }
}
