
package storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Arrays;

public class DiskManager {

    private final String directory = "data";

    public DiskManager() {
        new File(directory).mkdirs();
    }

    public void write(String fileName, byte[] data) throws IOException {

        try (FileOutputStream output =
                 new FileOutputStream(directory + "/" + fileName)) {
            output.write(data);
        }
    }

    public byte[] read(String fileName) throws IOException {

        try (FileInputStream input =
                 new FileInputStream(directory + "/" + fileName)) {
            return input.readAllBytes();
        }
    }

    public boolean exists(String fileName) {
        return new File(directory, fileName).exists();
    }

    public void writePage(String fileName, Page page) throws IOException {

        try (RandomAccessFile file =
                 new RandomAccessFile(directory + "/" + fileName, "rw")) {

            long position = (long) page.getPageId() * Page.PAGE_SIZE;

            file.seek(position);
            file.write(page.getData());
        }
    }

    public Page readPage(String fileName, int pageId) throws IOException {

        try (RandomAccessFile file =
                 new RandomAccessFile(directory + "/" + fileName, "r")) {

            long position = (long) pageId * Page.PAGE_SIZE;

            if (position + Page.PAGE_SIZE > file.length()) {
                throw new IOException("Page does not exist");
            }

            file.seek(position);

            byte[] data = new byte[Page.PAGE_SIZE];
            file.readFully(data);

            Page page = new Page(pageId);
            page.loadData(data);

            return page;
        }
    }
}
