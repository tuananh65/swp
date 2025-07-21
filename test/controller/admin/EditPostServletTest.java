package controller.admin;

import jakarta.servlet.http.Part;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Collection;
import java.util.Collections;

public class EditPostServletTest {

    // ========================
    // Class giả lập Part (không cần Mockito)
    // ========================
    
    // tạo class FakePart vì Part là interface nên không thể khởi tạo trực tiếp
    private static class FakePart implements Part {
        private final String header;

        public FakePart(String header) {
            this.header = header;
        }

        //Không có getHeader() thì test getFileName(part) sẽ không hoạt động đúng.
        // dùng để lấy ra thông tin như tên file gốc người dùng upload.
        @Override
        public String getHeader(String name) {
            if ("content-disposition".equalsIgnoreCase(name)) {
                return header;
            }
            return null;
        }

        // Các method còn lại không cần dùng, để trống
        // java bắt overide các method trong interfaceNu
        public void delete() {}
        public String getContentType() { return null; }
        public String getName() { return null; }
        public long getSize() { return 0; }
        public void write(String fileName) {}
        public String getSubmittedFileName() { return null; }
        public java.io.InputStream getInputStream() { return null; }
        public Collection<String> getHeaderNames() { return Collections.emptyList(); }
        public Collection<String> getHeaders(String name) { return Collections.emptyList(); }
    }

    // ========================
    // Hàm cần test (copy từ servlet)
    // ========================
    // Vì hàm nằm trong servlet không khởi tạo test được nên copy sang
    private String getFileName(Part part) {
        if (part == null) return null;
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp == null) return null;

        for (String cd : contentDisp.split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf(File.separator) + 1);
            }
        }
        return null;
    }

    // ========================
    // TEST CASES
    // ========================
    
    
    //Lấy file như bình thường
    @Test
    public void testGetFileName_NormalCase() {
        Part part = new FakePart("form-data; name=\"file\"; filename=\"image.png\"");
        assertEquals("image.png", getFileName(part));
    }

    // Tên file có full đường dẫn
    @Test
    public void testGetFileName_FilenameWithPath() {
        Part part = new FakePart("form-data; name=\"file\"; filename=\"C:\\\\Users\\\\test\\\\image.jpg\"");
        assertEquals("image.jpg", getFileName(part));
    }

    // Có file name nhưng mà nó rỗng
    @Test
    public void testGetFileName_EmptyFilename() {
        Part part = new FakePart("form-data; name=\"file\"; filename=\"\"");
        assertEquals("", getFileName(part));
    }

    // header là null
    @Test
    public void testGetFileName_NullHeader() {
        Part part = new FakePart(null);
        assertNull(getFileName(part));
    }
    
    // Không có trường File name
    @Test
    public void testGetFileName_NoFilenameField() {
        Part part = new FakePart("form-data; name=\"file\";");
        assertNull(getFileName(part));
    }
    
    // Trong file có ký tự đặc biệt
    @Test
    public void testGetFileName_SpecialCharacters() {
        Part part = new FakePart("form-data; name=\"file\"; filename=\"ảnh_đẹp🌸.jpg\"");
        assertEquals("ảnh_đẹp🌸.jpg", getFileName(part));
    }

    // File name thiếu dấu " và \  
    @Test
    public void testGetFileName_WithoutQuotes() {
        Part part = new FakePart("form-data; name=file; filename=image.jpeg");
        assertEquals("image.jpeg", getFileName(part));
    }
}
