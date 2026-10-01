package collegio.com;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FacultyActivity extends AppCompatActivity {

    EditText facultyName;
    EditText facultyDepartment;
    EditText facultyDesignation;
    EditText facultyContact;

    Button viewProfileButton;
    Button updateProfileButton;

    TextView facultyOutput;

    Faculty faculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        facultyName = findViewById(R.id.facultyName);
        facultyDepartment = findViewById(R.id.facultyDepartment);
        facultyDesignation = findViewById(R.id.facultyDesignation);
        facultyContact = findViewById(R.id.facultyContact);

        viewProfileButton = findViewById(R.id.viewProfileButton);
        updateProfileButton = findViewById(R.id.updateProfileButton);

        facultyOutput = findViewById(R.id.facultyOutput);

        viewProfileButton.setOnClickListener(v -> viewProfile());

        updateProfileButton.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {

        String name = facultyName.getText().toString();
        String department = facultyDepartment.getText().toString();
        String designation = facultyDesignation.getText().toString();
        String contact = facultyContact.getText().toString();

        if (name.isEmpty() || department.isEmpty()
                || designation.isEmpty() || contact.isEmpty()) {

            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        faculty = new Faculty(
                "F001",
                name,
                department,
                designation,
                contact
        );

        Toast.makeText(this,
                "Faculty profile updated",
                Toast.LENGTH_SHORT).show();

        viewProfile();
    }

    private void viewProfile() {

        if (faculty == null) {
            facultyOutput.setText("No faculty profile available.");
            return;
        }

        String output =
                "Faculty ID: " + faculty.getFacultyId() +
                        "\n\nName: " + faculty.getName() +
                        "\n\nDepartment: " + faculty.getDepartment() +
                        "\n\nDesignation: " + faculty.getDesignation() +
                        "\n\nContact: " + faculty.getContact();

        facultyOutput.setText(output);
    }
}