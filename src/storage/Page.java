
package storage;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Page {

    public static final int PAGE_SIZE = 4096;

    private int pageId;
    private byte[] data;

    public Page(int pageId) {
        this.pageId = pageId;
        this.data = new byte[PAGE_SIZE];
    }

    public int getPageId() {
        return pageId;
    }

    public byte[] getData() {
        return data;
    }

    public void writeData(byte[] input) {

        if (input.length > PAGE_SIZE) {
            throw new IllegalArgumentException("Page size exceeded");
        }

        Arrays.fill(data, (byte) 0);

        System.arraycopy(input, 0, data, 0, input.length);
    }

    public void loadData(byte[] input) {
        if (input.length != PAGE_SIZE) {
            throw new IllegalArgumentException("Invalid page size");
        }

        data = Arrays.copyOf(input, PAGE_SIZE);
    }

    public byte[] serialize() {

        ByteBuffer buffer = ByteBuffer.allocate(PAGE_SIZE + 4);

        buffer.putInt(pageId);
        buffer.put(data);

        return buffer.array();
    }
}
