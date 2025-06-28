package controller;

import model.Subject;
import dal.SubjectDetailDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet("/subjectDetail")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class SubjectDetailServlet extends HttpServlet {
    private SubjectDetailDAO subjectDAO;

    @Override
    public void init() {
        subjectDAO = new SubjectDetailDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String idParam = request.getParameter("id");
        int id = 1;
        try {
            if (idParam != null && !idParam.isEmpty()) {
                id = Integer.parseInt(idParam);
                session.setAttribute("currentSubjectId", id);
            } else if (session.getAttribute("currentSubjectId") != null) {
                id = (Integer) session.getAttribute("currentSubjectId");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid subject ID");
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        Subject subject = subjectDAO.getSubjectById(id);
        request.setAttribute("subject", subject);
        request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer sessionId = (Integer) session.getAttribute("currentSubjectId");
        String idParam = request.getParameter("id");
        int id = sessionId != null ? sessionId : (idParam != null && !idParam.isEmpty() ? Integer.parseInt(idParam) : 1);

        if (!request.getContentType().startsWith("multipart/form-data")) {
            request.setAttribute("errorMessage", "Form must be multipart/form-data");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        String name = request.getParameter("name");
        String categoryIdParam = request.getParameter("categoryId");
        String featuredParam = request.getParameter("featured");
        String description = request.getParameter("description");
        String existingThumbnail = request.getParameter("existingThumbnail");
        String owner = request.getParameter("owner");
        String status = request.getParameter("status");
        String numberOfLessonParam = request.getParameter("numberOfLesson");
        Part thumbnailFile = request.getPart("thumbnailFile");

        if (name == null || name.trim().isEmpty() || categoryIdParam == null || status == null) {
            request.setAttribute("errorMessage", "Missing required parameters");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        int categoryId;
        int numberOfLesson = 0;
        try {
            categoryId = Integer.parseInt(categoryIdParam);
            if (numberOfLessonParam != null && !numberOfLessonParam.trim().isEmpty()) {
                numberOfLesson = Integer.parseInt(numberOfLessonParam);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid category ID or number of lessons.");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        if (name.length() > 255 || (description != null && description.length() > 65535)) {
            request.setAttribute("errorMessage", "Input data too long");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        boolean featured = "on".equalsIgnoreCase(featuredParam);
        String thumbnail = existingThumbnail != null ? existingThumbnail : "";

        try {
            if (thumbnailFile != null && thumbnailFile.getSize() > 0) {
                String fileName = Paths.get(thumbnailFile.getSubmittedFileName()).getFileName().toString();
                if (!fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    request.setAttribute("errorMessage", "Invalid file type. Only JPG, PNG, GIF allowed.");
                    request.setAttribute("subject", subjectDAO.getSubjectById(id));
                    request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
                    return;
                }
                if (thumbnailFile.getSize() > 10 * 1024 * 1024) {
                    request.setAttribute("errorMessage", "File size exceeds 10MB limit.");
                    request.setAttribute("subject", subjectDAO.getSubjectById(id));
                    request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
                    return;
                }
                String uploadPath = getServletContext().getRealPath("") + "uploads";
                Files.createDirectories(Paths.get(uploadPath));
                String filePath = uploadPath + "/" + fileName;
                thumbnailFile.write(filePath);
                thumbnail = "/uploads/" + fileName;
            }
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Failed to upload thumbnail: " + e.getMessage());
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        Subject subject = new Subject();
        subject.setSubjectId(id);
        subject.setName(name);
        subject.setCategoryId(categoryId);
        subject.setFeatured(featured);
        subject.setThumbnail(thumbnail);
        subject.setDescription(description != null ? description : "");
        subject.setOwner(owner != null ? owner : "");
        subject.setNumberOfLesson(numberOfLesson);
        subject.setStatus(status);

        boolean updated = subjectDAO.updateSubject(subject);
        if (updated) {
            session.setAttribute("currentSubjectId", id);
            response.sendRedirect(request.getContextPath() + "/subjectDetail?id=" + id + "&saved=true");
        } else {
            request.setAttribute("errorMessage", "Failed to update subject.");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
        }
    }
}
