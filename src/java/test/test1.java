/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author maitu
 */
import dal.CourseDAO;
import java.util.List;
import model.Course;

public class test1 {
    public static void main(String[] args) {
        CourseDAO dao = new CourseDAO();
        List<Course> courses = dao.getFeaturedCourses();

        for (Course c : courses) {
            System.out.println("Featured: " + c.isFeatured());
            
        }
    }
}

