package collegio.com;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventActivity extends AppCompatActivity {

    EditText eventTitle;
    EditText eventDate;
    EditText eventVenue;

    Button createEventButton;
    Button viewEventButton;
    Button registerEventButton;

    TextView eventOutput;

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventTitle = findViewById(R.id.eventTitle);
        eventDate = findViewById(R.id.eventDate);
        eventVenue = findViewById(R.id.eventVenue);

        createEventButton = findViewById(R.id.createEventButton);
        viewEventButton = findViewById(R.id.viewEventButton);
        registerEventButton = findViewById(R.id.registerEventButton);

        eventOutput = findViewById(R.id.eventOutput);

        createEventButton.setOnClickListener(v -> createEvent());

        viewEventButton.setOnClickListener(v -> viewEvent());

        registerEventButton.setOnClickListener(v -> registerEvent());
    }

    private void createEvent() {

        String title = eventTitle.getText().toString();
        String date = eventDate.getText().toString();
        String venue = eventVenue.getText().toString();

        if (title.isEmpty() || date.isEmpty() || venue.isEmpty()) {
            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        event = new Event(
                "E001",
                title,
                date,
                venue
        );

        Toast.makeText(this,
                "Event created successfully",
                Toast.LENGTH_SHORT).show();

        viewEvent();
    }

    private void viewEvent() {

        if (event == null) {
            eventOutput.setText("No event available.");
            return;
        }

        String output =
                "Event ID: " + event.getEventId() +
                        "\n\nTitle: " + event.getTitle() +
                        "\n\nDate: " + event.getDate() +
                        "\n\nVenue: " + event.getVenue();

        eventOutput.setText(output);
    }

    private void registerEvent() {

        if (event == null) {
            Toast.makeText(this,
                    "Please create an event first",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this,
                "Registered for " + event.getTitle(),
                Toast.LENGTH_LONG).show();
    }
}