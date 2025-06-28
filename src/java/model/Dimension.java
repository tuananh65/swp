/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Dimension {
    private int dimensionId;
    private int subjectId;
    private String type;
    private String dimensionName;

    // Optional: nếu bạn cần truy xuất subject đầy đủ từ dimension
    private Subject subject;

    public Dimension() {
    }

    public Dimension(int dimensionId, int subjectId, String type, String dimensionName) {
        this.dimensionId = dimensionId;
        this.subjectId = subjectId;
        this.type = type;
        this.dimensionName = dimensionName;
    }

    public int getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
