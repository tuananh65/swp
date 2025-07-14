package controller.instructor;

import dal.AnswerDAO;
import dal.QuestionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import model.Question;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import model.Answer;
import model.ImportResult;

@WebServlet("/instructor/question-import")
@MultipartConfig
public class QuestionImportServlet extends HttpServlet {

    private QuestionDAO questionDAO;
    private AnswerDAO answerDAO;

    @Override
    public void init() throws ServletException {
        questionDAO = new QuestionDAO();
        answerDAO = new AnswerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("cancel".equals(action)) {
            request.getSession().removeAttribute("parsedQuestions");
            response.sendRedirect("question-import");
            return;
        }

        // Render trang upload
        request.getRequestDispatcher("/instructor/question-import.jsp").forward(request, response);
    }

    @Override
    

protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    

    String step = request.getParameter("step");
    if ("confirm".equals(step)) {
        // Handle Confirm Import
        handleConfirmImport(request, response);
        return;
    }

    // Step 1: Upload và Parse CSV
    Part filePart = request.getPart("importFile");
    if (filePart == null || filePart.getSize() == 0) {
        request.setAttribute("errorMessage", "No file selected.");
        request.getRequestDispatcher("/instructor/question-import.jsp").forward(request, response);
        return;
    }

    List<Question> parsedQuestions = new ArrayList<>();
    List<ImportResult> validationResults = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream()))) {
        String line;
        int rowNumber = 0;
        while ((line = reader.readLine()) != null) {
            rowNumber++;
            if (rowNumber == 1) {
                continue; // Skip header
            }
            String[] cols = line.split(",");
            if (cols.length < 5) {
                validationResults.add(new ImportResult(rowNumber, "Not enough columns.", false));
                continue;
            }

            try {
                Question q = new Question();
                q.setCourseID(Integer.parseInt(cols[0].trim()));
                q.setContent(cols[1].trim());
                q.setPoints(Integer.parseInt(cols[2].trim()));
                q.setQuestionType(cols[3].trim());

                List<Answer> answers = new ArrayList<>();
                for (int i = 4; i < cols.length; i += 2) {
                    if (i + 1 >= cols.length) {
                        break;
                    }
                    String answerContent = cols[i].trim();
                    boolean isCorrect = Boolean.parseBoolean(cols[i + 1].trim());
                    if (!answerContent.isEmpty()) {
                        Answer a = new Answer();
                        a.setContent(answerContent);
                        a.setCorrect(isCorrect);
                        answers.add(a);
                    }
                }
                
// Nếu là Short Answer và không có câu trả lời, thêm một câu trả lời mặc định
if (q.getQuestionType().equalsIgnoreCase("Short Answer") && answers.isEmpty()) {
    Answer defaultAnswer = new Answer();
    defaultAnswer.setContent("No specific answer");
    defaultAnswer.setCorrect(true);  // Mặc định là đúng
    answers.add(defaultAnswer);
}
                q.setAnswers(answers);

                // Simple validation
                if (q.getContent().isEmpty()) {
                    validationResults.add(new ImportResult(rowNumber, "Missing question content.", false));
                } else if (!q.getQuestionType().equalsIgnoreCase("Short Answer")
                        && !q.getQuestionType().equalsIgnoreCase("Multiple Choice")) {
                    validationResults.add(new ImportResult(rowNumber, "Invalid question type.", false));
                } else if (q.getQuestionType().equalsIgnoreCase("Multiple Choice") && answers.size() < 2) {
                    validationResults.add(new ImportResult(rowNumber, "MCQ needs at least 2 answers.", false));
                } else {
                    parsedQuestions.add(q);
                    validationResults.add(new ImportResult(rowNumber, "Valid.", true));
                }

            } catch (Exception ex) {
                validationResults.add(new ImportResult(rowNumber, "Parse error: " + ex.getMessage(), false));
            }
        }

    } catch (Exception e) {
        request.setAttribute("errorMessage", "Failed to read file: " + e.getMessage());
        request.getRequestDispatcher("/instructor/question-import.jsp").forward(request, response);
        return;
    }

    // Debug: Log số lượng parsedQuestions trước khi lưu vào session
    System.out.println("Parsed Questions Size: " + parsedQuestions.size());

    // Save in session for confirm step
    HttpSession session = request.getSession();
    session.setAttribute("parsedQuestions", parsedQuestions);
    session.setAttribute("validationResults", validationResults);

    // Pass parsedQuestions to JSP
    request.setAttribute("parsedQuestions", parsedQuestions);
    request.setAttribute("validationResults", validationResults);
    request.getRequestDispatcher("/instructor/question-import.jsp").forward(request, response);
}



    /**
     * Xử lý bước xác nhận và thực hiện batch insert
     */
    /**
     * Handle Confirm Import: batch insert Questions + Answers
     */
    private void handleConfirmImport(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    @SuppressWarnings("unchecked")
    List<Question> parsedQuestions = (List<Question>) request.getSession().getAttribute("parsedQuestions");

    // Debug: In parsedQuestions trước khi xử lý
    System.out.println("Number of parsed questions to import: " + (parsedQuestions != null ? parsedQuestions.size() : 0));

    if (parsedQuestions == null || parsedQuestions.isEmpty()) {
        request.setAttribute("errorMessage", "⚠️ No data to import. Please upload the file again.");
        request.getRequestDispatcher("/instructor/question-import.jsp").forward(request, response);
        return;
    }

    List<ImportResult> importResults = new ArrayList<>();
    int rowNumber = 1;

    for (Question q : parsedQuestions) {
        rowNumber++;
        try {
            // Log question details to ensure we're processing it
            System.out.println("Importing question: " + q.getContent() + ", Type: " + q.getQuestionType());

            int questionID = questionDAO.addQuestionAndGetID(q);
            if (questionID > 0 && q.getAnswers() != null && !q.getAnswers().isEmpty()) {
                for (Answer a : q.getAnswers()) {
                    a.setQuestionID(questionID);
                }
                answerDAO.addAnswersBatch(q.getAnswers());
            }
            importResults.add(new ImportResult(rowNumber, "✅ Imported successfully", true));
        } catch (Exception e) {
            e.printStackTrace();
            importResults.add(new ImportResult(rowNumber, "❌ Failed: " + e.getMessage(), false));
        }
    }

    // Debug: Log the import results
    System.out.println("Import Results: " + importResults.size() + " questions processed.");

    // Xóa parsedQuestions sau khi insert xong
    request.getSession().removeAttribute("parsedQuestions");

    // Hiển thị kết quả
    request.setAttribute("importResults", importResults);
    request.getRequestDispatcher("/instructor/question-import.jsp").forward(request, response);
}



}
