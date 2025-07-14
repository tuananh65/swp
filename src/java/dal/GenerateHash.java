/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import utils.PasswordUtils;

/**
 *
 * @author Admin
 */
public class GenerateHash {
    public static void main(String[] args) {
        String password = "Minhdz14092005@";
        String hashed = PasswordUtils.hashPassword(password);
        System.out.println("✅ Generated BCrypt hash: " + hashed);
    }
}
