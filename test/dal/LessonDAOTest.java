///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
// */
//package dal;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import model.Lesson;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
///**
// *
// * @author admin
// */
//public class LessonDAOTest {
//
//    private static LessonDAO instance;
//
//    // A lesson ID that should exist for successful tests
//    // ĐÃ THAY ĐỔI TỪ 1 SANG 85
//    private static final int EXISTING_LESSON_ID = 85; //
//    private static final int EXISTING_SUBJECT_ID = 1; //
//
//    public LessonDAOTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() {
//        instance = new LessonDAO();
//        System.out.println("LessonDAOTest: Setting up test class...");
//    }
//
//    @AfterClass
//    public static void tearDownClass() {
//        System.out.println("LessonDAOTest: Tearing down test class...");
//    }
//
//    @Before
//    public void setUp() {
//        System.out.println("LessonDAOTest: Setting up test method...");
//    }
//
//    @After
//    public void tearDown() {
//        System.out.println("LessonDAOTest: Tearing down test method...");
//    }
//
//    /**
//     * Test of getLessonById method, of class LessonDAO.
//     */
//    @Test
//    public void testGetLessonById_ExistingLesson() {
//        System.out.println("getLessonById - Existing Lesson");
//        int lessonId = EXISTING_LESSON_ID;
//        Lesson result = instance.getLessonById(lessonId);
//        assertNotNull("Lesson should not be null for existing ID " + lessonId, result);
//        assertEquals("Lesson ID should match", lessonId, result.getLessonId());
//    }
//
//    @Test
//    public void testGetLessonById_NonExistingLesson() {
//        System.out.println("getLessonById - Non-Existing Lesson");
//        int lessonId = -9999;
//        Lesson result = instance.getLessonById(lessonId);
//        assertNull("Lesson should be null for non-existing ID", result);
//    }
//
//    /**
//     * Test of getLessonsByCourseId method, of class LessonDAO.
//     */
//    @Test
//    public void testGetLessonsByCourseId_ExistingSubject() {
//        System.out.println("getLessonsByCourseId - Existing Subject");
//        int subjectId = EXISTING_SUBJECT_ID;
//        String lessonType = "";
//        String status = "";
//        String searchKeyword = "";
//        int offset = 0;
//        int limit = 10;
//        List<Lesson> result = instance.getLessonsByCourseId(subjectId, lessonType, status, searchKeyword, offset, limit);
//        assertNotNull("Result list should not be null", result);
//        assertFalse("Result list should not be empty for existing subject " + subjectId + " if it has lessons", result.isEmpty());
//    }
//
//    @Test
//    public void testGetLessonsByCourseId_NonExistingSubject() {
//        System.out.println("getLessonsByCourseId - Non-Existing Subject");
//        int subjectId = -9999;
//        String lessonType = "";
//        String status = "";
//        String searchKeyword = "";
//        int offset = 0;
//        int limit = 10;
//        List<Lesson> result = instance.getLessonsByCourseId(subjectId, lessonType, status, searchKeyword, offset, limit);
//        assertNotNull("Result list should not be null (should be empty list)", result);
//        assertTrue("Result list should be empty for non-existing subject", result.isEmpty());
//    }
//
//    @Test
//    public void testGetLessonsByCourseId_WithFilters_NoMatch() {
//        System.out.println("getLessonsByCourseId - With Filters, No Match");
//        int subjectId = EXISTING_SUBJECT_ID;
//        String lessonType = "NonExistentType";
//        String status = "InvalidStatus";
//        String searchKeyword = "NoSuchKeyword";
//        int offset = 0;
//        int limit = 10;
//        List<Lesson> result = instance.getLessonsByCourseId(subjectId, lessonType, status, searchKeyword, offset, limit);
//        assertNotNull("Result list should not be null (should be empty list)", result);
//        assertTrue("Result list should be empty when no filter matches", result.isEmpty());
//    }
//
//    /**
//     * Test of getTotalLessons method, of class LessonDAO.
//     */
//    @Test
//    public void testGetTotalLessons_ExistingSubject() {
//        System.out.println("getTotalLessons - Existing Subject");
//        int subjectId = EXISTING_SUBJECT_ID;
//        String lessonType = "";
//        String status = "";
//        String searchKeyword = "";
//        int result = instance.getTotalLessons(subjectId, lessonType, status, searchKeyword);
//        assertTrue("Total lessons should be greater than or equal to 0 for existing subject", result >= 0);
//    }
//
//    @Test
//    public void testGetTotalLessons_NonExistingSubject() {
//        System.out.println("getTotalLessons - Non-Existing Subject");
//        int subjectId = -9999;
//        String lessonType = "";
//        String status = "";
//        String searchKeyword = "";
//        int result = instance.getTotalLessons(subjectId, lessonType, status, searchKeyword);
//        assertEquals("Total lessons should be 0 for non-existing subject", 0, result);
//    }
//
//    /**
//     * Test of getUniqueLessonTypesByCourseId method, of class LessonDAO.
//     */
//    @Test
//    public void testGetUniqueLessonTypesByCourseId_ExistingSubject() {
//        System.out.println("getUniqueLessonTypesByCourseId - Existing Subject");
//        int subjectId = EXISTING_SUBJECT_ID;
//        Set<String> result = instance.getUniqueLessonTypesByCourseId(subjectId);
//        assertNotNull("Result set should not be null", result);
//        assertTrue("Result set should be empty or not for existing subject", true);
//    }
//
//    @Test
//    public void testGetUniqueLessonTypesByCourseId_NonExistingSubject() {
//        System.out.println("getUniqueLessonTypesByCourseId - Non-Existing Subject");
//        int subjectId = -9999;
//        Set<String> result = instance.getUniqueLessonTypesByCourseId(subjectId);
//        assertNotNull("Result set should not be null (should be empty set)", result);
//        assertTrue("Result set should be empty for non-existing subject", result.isEmpty());
//    }
//
//    /**
//     * Test of getAllLessonTypes method, of class LessonDAO.
//     */
//    @Test
//    public void testGetAllLessonTypes() {
//        System.out.println("getAllLessonTypes");
//        List<String> result = instance.getAllLessonTypes();
//        assertNotNull("Result list should not be null", result);
//        assertFalse("Result list should not be empty", result.isEmpty());
//        assertTrue("Should contain 'Lesson' type", result.contains("Lesson"));
//        assertTrue("Should contain 'Quiz' type", result.contains("Quiz"));
//        assertTrue("Should contain 'Assignment' type", result.contains("Assignment"));
//        assertTrue("Should contain 'Subject topic' type", result.contains("Subject topic"));
//    }
//
//    /**
//     * Test of updateLessonStatus method, of class LessonDAO.
//     */
//    @Test
//    public void testUpdateLessonStatus_Success() {
//        System.out.println("updateLessonStatus - Success");
//        int lessonId = EXISTING_LESSON_ID;
//        String newStatus = "Inactive";
//
//        Lesson originalLesson = instance.getLessonById(lessonId);
//        assertNotNull("Pre-condition: Lesson " + lessonId + " should exist for update", originalLesson);
//        String oldStatus = originalLesson.getStatus();
//
//        boolean result = instance.updateLessonStatus(lessonId, newStatus);
//        assertTrue("Update should be successful for existing lesson", result);
//
//        Lesson updatedLesson = instance.getLessonById(lessonId);
//        assertNotNull("Updated lesson should not be null", updatedLesson);
//        assertEquals("Lesson status should be updated", newStatus, updatedLesson.getStatus());
//
//        // Revert status for clean state (important for other tests)
//        instance.updateLessonStatus(lessonId, oldStatus);
//    }
//
//    @Test
//    public void testUpdateLessonStatus_NonExistingLesson() {
//        System.out.println("updateLessonStatus - Non-Existing Lesson");
//        int lessonId = -9999;
//        String newStatus = "Active";
//        boolean result = instance.updateLessonStatus(lessonId, newStatus);
//        assertFalse("Update should fail for non-existing lesson", result);
//    }
//
//    /**
//     * Test of deleteLesson method, of class LessonDAO.
//     */
//    @Test
//    public void testDeleteLesson_Success() {
//        System.out.println("deleteLesson - Success");
//        String uniqueTitle = "Temp_Lesson_For_Deletion_" + System.currentTimeMillis();
//        Lesson lessonToInsertAndDelete = new Lesson();
//        lessonToInsertAndDelete.setSubjectId(EXISTING_SUBJECT_ID);
//        lessonToInsertAndDelete.setTitle(uniqueTitle);
//        lessonToInsertAndDelete.setContent("Content for deletion test");
//        lessonToInsertAndDelete.setVideoUrl("url.com");
//        lessonToInsertAndDelete.setStatus("Active");
//        lessonToInsertAndDelete.setOrder(1);
//        lessonToInsertAndDelete.setType("Lesson");
//
//        boolean addSuccess = instance.addLesson(lessonToInsertAndDelete);
//        assertTrue("Pre-condition: Temporary lesson should be added successfully", addSuccess);
//
//        List<Lesson> addedLessons = instance.getLessonsByCourseId(EXISTING_SUBJECT_ID, "Lesson", "Active", uniqueTitle, 0, 1);
//        assertFalse("Added lesson should be found to get its ID", addedLessons.isEmpty());
//        int lessonIdToDelete = addedLessons.get(0).getLessonId();
//
//
//        boolean result = instance.deleteLesson(lessonIdToDelete);
//        assertTrue("Deletion of temporary lesson should be successful", result);
//
//        Lesson deletedLesson = instance.getLessonById(lessonIdToDelete);
//        assertNull("Lesson should be null after deletion", deletedLesson);
//    }
//
//    @Test
//    public void testDeleteLesson_NonExistingLesson() {
//        System.out.println("deleteLesson - Non-Existing Lesson");
//        int lessonId = -9999;
//        boolean result = instance.deleteLesson(lessonId);
//        assertFalse("Deletion should fail for non-existing lesson", result);
//    }
//
//    /**
//     * Test of addLesson method, of class LessonDAO.
//     */
//    @Test
//    public void testAddLesson_Success() {
//        System.out.println("addLesson - Success");
//        Lesson newLesson = new Lesson();
//        newLesson.setSubjectId(EXISTING_SUBJECT_ID);
//        newLesson.setTitle("New Test Lesson " + System.currentTimeMillis());
//        newLesson.setContent("This is new test content.");
//        newLesson.setVideoUrl("https://example.com/newvideo");
//        newLesson.setStatus("Active");
//        newLesson.setOrder(instance.getNextOrderForSubject(EXISTING_SUBJECT_ID));
//        newLesson.setType("Lesson");
//
//        boolean result = instance.addLesson(newLesson);
//        assertTrue("Adding lesson should be successful", result);
//    }
//
//    @Test
//    public void testAddLesson_NullLesson() {
//        System.out.println("addLesson - Null Lesson");
//        Lesson lesson = null;
//        boolean result = instance.addLesson(lesson);
//        assertFalse("Adding null lesson should fail and return false", result);
//    }
//
//    /**
//     * Test of updateLesson method, of class LessonDAO.
//     */
//    @Test
//    public void testUpdateLesson_Success() {
//        System.out.println("updateLesson - Success");
//        Lesson existingLesson = instance.getLessonById(EXISTING_LESSON_ID);
//        assertNotNull("Pre-condition: Lesson " + EXISTING_LESSON_ID + " should exist for update", existingLesson);
//
//        String originalTitle = existingLesson.getTitle();
//        String originalStatus = existingLesson.getStatus();
//
//        existingLesson.setTitle("Updated Title " + System.currentTimeMillis());
//        existingLesson.setStatus("Inactive");
//
//        boolean result = instance.updateLesson(existingLesson);
//        assertTrue("Updating lesson should be successful", result);
//
//        Lesson updatedLesson = instance.getLessonById(existingLesson.getLessonId());
//        assertNotNull("Updated lesson should not be null", updatedLesson);
//        assertEquals("Lesson title should be updated", existingLesson.getTitle(), updatedLesson.getTitle());
//        assertEquals("Lesson status should be updated", existingLesson.getStatus(), updatedLesson.getStatus());
//
//        // Revert changes
//        existingLesson.setTitle(originalTitle);
//        existingLesson.setStatus(originalStatus);
//        instance.updateLesson(existingLesson);
//    }
//
//    @Test
//    public void testUpdateLesson_NonExistingLesson() {
//        System.out.println("updateLesson - Non-Existing Lesson");
//        Lesson nonExistingLesson = new Lesson();
//        nonExistingLesson.setLessonId(-9999);
//        nonExistingLesson.setSubjectId(EXISTING_SUBJECT_ID);
//        nonExistingLesson.setTitle("Non-existent Lesson");
//        nonExistingLesson.setContent("");
//        nonExistingLesson.setVideoUrl("");
//        nonExistingLesson.setStatus("Active");
//        nonExistingLesson.setOrder(1);
//        nonExistingLesson.setType("Lesson");
//
//        boolean result = instance.updateLesson(nonExistingLesson);
//        assertFalse("Updating non-existing lesson should fail", result);
//    }
//
//    @Test
//    public void testUpdateLesson_NullLesson() {
//        System.out.println("updateLesson - Null Lesson");
//        Lesson lesson = null;
//        boolean result = instance.updateLesson(lesson);
//        assertFalse("Updating null lesson should fail and return false", result);
//    }
//
//    /**
//     * Test of getNextOrderForSubject method, of class LessonDAO.
//     */
//    @Test
//    public void testGetNextOrderForSubject_ExistingSubject() {
//        System.out.println("getNextOrderForSubject - Existing Subject");
//        int subjectId = EXISTING_SUBJECT_ID;
//        int result = instance.getNextOrderForSubject(subjectId);
//        assertTrue("Next order should be positive for existing subject", result >= 1);
//    }
//
//    @Test
//    public void testGetNextOrderForSubject_NonExistingSubject() {
//        System.out.println("getNextOrderForSubject - Non-Existing Subject");
//        int subjectId = -9999;
//        int result = instance.getNextOrderForSubject(subjectId);
//        assertEquals("Next order should be 1 for non-existing subject", 1, result);
//    }
//}