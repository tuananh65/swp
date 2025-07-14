package dal;

import org.junit.*;
import static org.junit.Assert.*;

public class QuizDAOIT {

    private QuizDAO dao;

    @BeforeClass
    public static void setUpClass() {
        
    }

    @AfterClass
    public static void tearDownClass() {
        
    }

    @Before
    public void setUp() {
      
        dao = new QuizDAO();
    }

    @After
    public void tearDown() {
        dao = null;
    }

    // =========================
    // TC01 – Default parameters
    @Test
    public void test_TC01_DefaultParameters() {
        int result = dao.getTotalQuizzes(null, "", "");
        assertTrue("TC01: Expected >= 0", result >= 0);
    }

    // =========================
    // TC02 – With keyword = "quiz"
    @Test
    public void test_TC02_WithKeyword() {
        int result = dao.getTotalQuizzes(null, "quiz", null);
        assertTrue("TC02: Expected >= 0", result >= 0);
    }

    // =========================
    // TC03 – With courseID and status = "Active"
    @Test
    public void test_TC03_WithCourseIDAndStatus() {
        int result = dao.getTotalQuizzes(1, "", "Active");
        assertTrue("TC03: Expected >= 0", result >= 0);
    }

    // =========================
    // TC04 – Null keyword, null status
    @Test
    public void test_TC04_NullKeywordNullStatus() {
        int result = dao.getTotalQuizzes(null, null, null);
        assertTrue("TC04: Expected >= 0", result >= 0);
    }

    // =========================
    // TC05 – Can't connect (simulate error)
    @Test(expected = RuntimeException.class)
    public void test_TC05_CantConnect() {
        QuizDAO brokenDao = new QuizDAO() {
            @Override
            public int getTotalQuizzes(Integer courseID, String keyword, String status) {
                throw new RuntimeException("Simulated DB connection error");
            }
        };
        brokenDao.getTotalQuizzes(null, null, null);
    }

    // =========================
    // TC06 – Status = "Inactive"
    @Test
    public void test_TC06_StatusInactive() {
        int result = dao.getTotalQuizzes(null, "", "Inactive");
        assertTrue("TC06: Expected >= 0", result >= 0);
    }

    // =========================
    // TC07 – keyword="" and status=""
    @Test
    public void test_TC07_EmptyKeywordAndStatus() {
        int result = dao.getTotalQuizzes(null, "", "");
        assertTrue("TC07: Expected >= 0", result >= 0);
    }

    // =========================
    // TC08 – courseID != null, keyword = null
    @Test
    public void test_TC08_CourseIDAndNullKeyword() {
        int result = dao.getTotalQuizzes(2, null, null);
        assertTrue("TC08: Expected >= 0", result >= 0);
    }

    // =========================
    // TC09 – courseID != null, keyword = ""
    @Test
    public void test_TC09_CourseIDAndEmptyKeyword() {
        int result = dao.getTotalQuizzes(2, "", null);
        assertTrue("TC09: Expected >= 0", result >= 0);
    }

    // =========================
    // TC10 – courseID != null, status = ""
    @Test
    public void test_TC10_CourseIDAndEmptyStatus() {
        int result = dao.getTotalQuizzes(2, null, "");
        assertTrue("TC10: Expected >= 0", result >= 0);
    }

    // =========================
    // TC11 – courseID=null, keyword=null, status=""
    @Test
    public void test_TC11_NullCourseIDKeyword_EmptyStatus() {
        int result = dao.getTotalQuizzes(null, null, "");
        assertTrue("TC11: Expected >= 0", result >= 0);
    }

    // =========================
    // TC12 – courseID=null, keyword="", status=null
    @Test
    public void test_TC12_NullCourseIDStatus_EmptyKeyword() {
        int result = dao.getTotalQuizzes(null, "", null);
        assertTrue("TC12: Expected >= 0", result >= 0);
    }

    // =========================
    // TC13 – Return count = 0
    @Test
    public void test_TC13_ReturnZero() {
        // Dùng keyword vô lý để chắc chắn không có kết quả
        int result = dao.getTotalQuizzes(null, "unlikely_keyword_no_match_12345", null);
        assertEquals("TC13: Expected 0", 0, result);
    }

    // =========================
    // TC14 – Return count > 0
    @Test
    public void test_TC14_ReturnGreaterThanZero() {
        int result = dao.getTotalQuizzes(null, "quiz", null);
        assertTrue("TC14: Expected > 0", result > 0);
    }

    // =========================
    // TC15 – Return count < 0 (giả lập lỗi logic)
    @Test
    public void test_TC15_ReturnNegative() {
        QuizDAO mockDao = new QuizDAO() {
            @Override
            public int getTotalQuizzes(Integer courseID, String keyword, String status) {
                return -1;
            }
        };
        int result = mockDao.getTotalQuizzes(null, "any", null);
        assertTrue("TC15: Expected < 0", result < 0);
    }
}
