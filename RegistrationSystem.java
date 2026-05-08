import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles student registration and eligibility checking for placement drives.
 */
public class RegistrationSystem {
    private final List<Student> students;
    private final Map<String, Student> studentMap;
    private final Map<String, PlacementDrive> placementDrives;

    public RegistrationSystem() {
        this.students = new ArrayList<>();
        this.studentMap = new HashMap<>();
        this.placementDrives = new HashMap<>();
    }

    /**
     * Registers a student if the student ID is not already used.
     */
    public boolean registerStudent(Student student) {
        if (student == null || student.getStudentId() == null) {
            return false;
        }
        if (studentMap.containsKey(student.getStudentId())) {
            return false; // Duplicate student ID not allowed.
        }
        students.add(student);
        studentMap.put(student.getStudentId(), student);
        return true;
    }

    public void addPlacementDrive(PlacementDrive drive) {
        if (drive != null && drive.getCompanyName() != null) {
            placementDrives.put(drive.getCompanyName(), drive);
        }
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public List<PlacementDrive> getAllPlacementDrives() {
        return new ArrayList<>(placementDrives.values());
    }

    public PlacementDrive getPlacementDrive(String companyName) {
        return placementDrives.get(companyName);
    }

    public List<Student> getEligibleStudents(String companyName) {
        PlacementDrive drive = placementDrives.get(companyName);
        if (drive == null) {
            return new ArrayList<>();
        }
        List<Student> eligibleStudents = new ArrayList<>();
        for (Student student : students) {
            if (isEligible(student, drive)) {
                eligibleStudents.add(student);
            }
        }
        return eligibleStudents;
    }

    public boolean isEligible(Student student, PlacementDrive drive) {
        if (student == null || drive == null) {
            return false;
        }
        boolean cgpaOk = student.getCgpa() >= drive.getMinimumCgpa();
        boolean branchOk = drive.getAllowedBranches().stream()
                .anyMatch(branch -> branch.equalsIgnoreCase(student.getBranch()));
        return cgpaOk && branchOk;
    }
}
