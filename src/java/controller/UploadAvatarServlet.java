package controller;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import model.User;

@WebServlet("/uploadAvatar")
@MultipartConfig
public class UploadAvatarServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userID = Integer.parseInt(request.getParameter("userID"));
        Part avatarPart = request.getPart("avatar");

        String fileName = Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
        String uploadPath = getServletContext().getRealPath("/images");
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        String fullPath = uploadPath + File.separator + fileName;
        avatarPart.write(fullPath);

        User user = userDAO.getUserById(userID);
        user.setUserAvatarUrl("/images/" + fileName);
        userDAO.updateUser(user);
        
        response.setStatus(HttpServletResponse.SC_OK);
       
  

    }
}
