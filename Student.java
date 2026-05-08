import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student with personal and academic details.
 */
public class Student {
    private String studentId;
    private String name;
    private String email;
    private String branch;
    private double cgpa;
    private List<String> skills;
    private String resumePath;

    /**
     * Default constructor.
     */
    public Student() {
        this.skills = new ArrayList<>();
        this.resumePath = "";
    }

    /**
     * Parameterized constructor for student initialization.
     */
    public Student(String studentId, String name, String email, String branch, double cgpa, List<String> skills, String resumePath) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.cgpa = cgpa;
        this.skills = skills != null ? new ArrayList<>(skills) : new ArrayList<>();
        this.resumePath = resumePath != null ? resumePath : "";
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public List<String> getSkills() {
        return new ArrayList<>(skills);
    }

    public void setSkills(List<String> skills) {
        this.skills = skills != null ? new ArrayList<>(skills) : new ArrayList<>();
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath != null ? resumePath : "";
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", branch='" + branch + '\'' +
                ", cgpa=" + cgpa +
                ", skills=" + skills +
                ", resumePath='" + resumePath + '\'' +
                '}';
    }
}
