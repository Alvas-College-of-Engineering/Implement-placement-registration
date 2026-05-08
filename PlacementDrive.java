import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a placement drive with company information and eligibility criteria.
 */
public class PlacementDrive {
    private String companyName;
    private double minimumCgpa;
    private List<String> allowedBranches;
    private List<String> requiredSkills;
    private LocalDate driveDate;

    /**
     * Default constructor.
     */
    public PlacementDrive() {
        this.allowedBranches = new ArrayList<>();
        this.requiredSkills = new ArrayList<>();
    }

    /**
     * Parameterized constructor.
     */
    public PlacementDrive(String companyName, double minimumCgpa, List<String> allowedBranches, List<String> requiredSkills, LocalDate driveDate) {
        this.companyName = companyName;
        this.minimumCgpa = minimumCgpa;
        this.allowedBranches = allowedBranches != null ? new ArrayList<>(allowedBranches) : new ArrayList<>();
        this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>();
        this.driveDate = driveDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getMinimumCgpa() {
        return minimumCgpa;
    }

    public void setMinimumCgpa(double minimumCgpa) {
        this.minimumCgpa = minimumCgpa;
    }

    public List<String> getAllowedBranches() {
        return new ArrayList<>(allowedBranches);
    }

    public void setAllowedBranches(List<String> allowedBranches) {
        this.allowedBranches = allowedBranches != null ? new ArrayList<>(allowedBranches) : new ArrayList<>();
    }

    public List<String> getRequiredSkills() {
        return new ArrayList<>(requiredSkills);
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>();
    }

    public LocalDate getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(LocalDate driveDate) {
        this.driveDate = driveDate;
    }

    /**
     * Returns the eligibility criteria in readable format.
     */
    public String getEligibilityCriteria() {
        String skillsText = requiredSkills.isEmpty() ? "None" : String.join(", ", requiredSkills);
        return "Min CGPA: " + minimumCgpa + ", Branches: " + String.join(", ", allowedBranches) + ", Skills: " + skillsText;
    }
}
