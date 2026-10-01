package collegio.com;

public class Faculty {

    private String facultyId;
    private String name;
    private String department;
    private String designation;
    private String contact;

    public Faculty(String facultyId, String name, String department,
                   String designation, String contact) {

        this.facultyId = facultyId;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.contact = contact;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public String getContact() {
        return contact;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}