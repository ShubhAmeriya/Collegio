package collegio.com;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NoticeActivity extends AppCompatActivity {

    EditText noticeTitle;
    EditText noticeDescription;
    EditText noticeDate;

    Button addNoticeButton;
    Button viewNoticeButton;
    Button deleteNoticeButton;

    TextView noticeOutput;

    Notice notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        noticeTitle = findViewById(R.id.noticeTitle);
        noticeDescription = findViewById(R.id.noticeDescription);
        noticeDate = findViewById(R.id.noticeDate);

        addNoticeButton = findViewById(R.id.addNoticeButton);
        viewNoticeButton = findViewById(R.id.viewNoticeButton);
        deleteNoticeButton = findViewById(R.id.deleteNoticeButton);

        noticeOutput = findViewById(R.id.noticeOutput);

        addNoticeButton.setOnClickListener(v -> addNotice());

        viewNoticeButton.setOnClickListener(v -> viewNotice());

        deleteNoticeButton.setOnClickListener(v -> deleteNotice());
    }

    private void addNotice() {

        String title = noticeTitle.getText().toString();
        String description = noticeDescription.getText().toString();
        String date = noticeDate.getText().toString();

        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        notice = new Notice(
                "N001",
                title,
                description,
                date
        );

        Toast.makeText(this,
                "Notice added successfully",
                Toast.LENGTH_SHORT).show();

        viewNotice();
    }

    private void viewNotice() {

        if (notice == null) {
            noticeOutput.setText("No notice available.");
            return;
        }

        String output =
                "Notice ID: " + notice.getNoticeId() +
                        "\n\nTitle: " + notice.getTitle() +
                        "\n\nDescription: " + notice.getDescription() +
                        "\n\nDate Posted: " + notice.getDatePosted();

        noticeOutput.setText(output);
    }

    private void deleteNotice() {

        notice = null;

        noticeOutput.setText("No notice available.");

        Toast.makeText(this,
                "Notice deleted",
                Toast.LENGTH_SHORT).show();
    }
}