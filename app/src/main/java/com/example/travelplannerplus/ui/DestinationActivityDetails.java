package com.example.travelplannerplus.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.travelplannerplus.R;
import com.example.travelplannerplus.entities.Destination;
import com.example.travelplannerplus.entities.DestinationActivity;
import com.example.travelplannerplus.repository.DestinationRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DestinationActivityDetails extends AppCompatActivity {
    public static final String DESTINATION_ID = "destinationId";
    public static final String ACTIVITY_ID = "destinationActivityId";
    private int destinationId = -1;
    private int destinationActivityId = -1;
    private DestinationRepository destinationRepository;
    private EditText editActivityTitle;
    private EditText editActivityDate;
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ui_activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sdf.setLenient(false);
        destinationRepository = new DestinationRepository(getApplication());
        destinationId = getIntent().getIntExtra(DESTINATION_ID, -1);
        destinationActivityId = getIntent().getIntExtra(ACTIVITY_ID, -1);
        editActivityTitle = findViewById(R.id.editActivityTitle);
        editActivityDate = findViewById(R.id.editActivityDate);

        if (destinationId == -1) {
            Toast.makeText(this, "You must save a Destination before adding an Activity!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (destinationActivityId != -1) {
            DestinationActivity activityExists = destinationRepository.getDestinationActivityById(destinationActivityId);
            if (activityExists != null) {
                if (activityExists.getDestinationActivityTitle() != null) {
                    editActivityTitle.setText(activityExists.getDestinationActivityTitle());
                }
                if (activityExists.getDestinationActivityDate() != 0) {
                    editActivityDate.setText(formatScreenDate(activityExists.getDestinationActivityDate()));
                }
            } else {
                Toast.makeText(this, "Activity not found!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if (getSupportActionBar() != null) {  // Enables return to associated Destination instead of new Destination
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ui_activity_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {  // Prevents going back to blank DestinationDetails page
            finish();
            return true;
        }
        if (item.getItemId() == R.id.activity_save) {
            saveDestinationActivity();
            return true;
        }
        if (item.getItemId() == R.id.activity_delete) {
            if (destinationActivityId == -1) {
                Toast.makeText(this, "You must save the activity before deleting!", Toast.LENGTH_LONG).show();
                return true;
            }
            deleteDestinationActivity();
            return true;
        }
        if (item.getItemId() == R.id.activity_alert) {
            String activityDate = editActivityDate.getText().toString().trim();
            long parsedActivityDate = parseDate(activityDate);
            if (destinationActivityId == -1) {
                Toast.makeText(this, "You must save the activity before setting an alert!", Toast.LENGTH_LONG).show();
                return true;
            }
            createDestinationActivityAlert(parsedActivityDate);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveDestinationActivity() {
        String activityTitle = editActivityTitle.getText().toString().trim();
        String activityDate = editActivityDate.getText().toString().trim();

        if (TextUtils.isEmpty(activityTitle)) {
            Toast.makeText(this, "Please enter a valid Activity title...", Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidDate(activityDate)) {
            Toast.makeText(this, "Date must be formatted as MM/DD/YYYY...", Toast.LENGTH_LONG).show();
            return;
        }

        long parsedActivityDate = parseDate(activityDate);
        Destination destination = destinationRepository.getDestinationById(destinationId);
        long destinationStartDate = destination.getDestinationStartDate();
        long destinationEndDate = destination.getDestinationEndDate();

        if (parsedActivityDate < destinationStartDate || parsedActivityDate > destinationEndDate) {
            Toast.makeText(this, "Activity must occur within the associated Destination dates!", Toast.LENGTH_LONG).show();
            return;
        }

        if (destinationActivityId == -1) {
            // New Destination Activity
            DestinationActivity destinationActivity = new DestinationActivity(0, destinationId, activityTitle, parsedActivityDate);
            destinationRepository.insert(destinationActivity);
            Toast.makeText(this, "Activity successfully stored...", Toast.LENGTH_SHORT).show();
        } else {  //  Edit an existing Destination here
            DestinationActivity destinationActivity = new DestinationActivity(destinationActivityId, destinationId, activityTitle, parsedActivityDate);
            destinationRepository.update(destinationActivity);
            Toast.makeText(this, "Activity successfully updated...", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void deleteDestinationActivity() {
        if (destinationActivityId == -1) {
            Toast.makeText(this, "Save Activity before attempting delete!", Toast.LENGTH_SHORT).show();
            return;
        }

        DestinationActivity destinationActivity = destinationRepository.getDestinationActivityById(destinationActivityId);

        if (destinationActivity != null) {
            destinationRepository.delete(destinationActivity);
            Toast.makeText(this, "Activity successfully deleted...", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void createDestinationActivityAlert(long activityDate) {
        String activityTitle = editActivityTitle.getText().toString().trim();
        long trigger = timeTrigger(activityDate);
        int requestCode = destinationActivityId * 10 + 1;  //  Multiply creates unique code
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra(AlertReceiver.ACTIVITY_TITLE, activityTitle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
            Toast.makeText(this, "Activity alert has been set!", Toast.LENGTH_SHORT).show();
        }
    }

    private long timeTrigger(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    // Helper Methods
    private long parseDate(String inputDate) {
        if (TextUtils.isEmpty(inputDate)) {
            return 0;
        }
        try {
            Date parsedDate = sdf.parse(inputDate);

            if (parsedDate == null) {
                return 0;
            } else {
                return parsedDate.getTime();
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String formatScreenDate(long inputScreenDate) {
        if (inputScreenDate == 0) {
            return "";
        } else {
            return sdf.format(new Date(inputScreenDate));
        }
    }

    private boolean isValidDate(String dateInput) {
        if (dateInput.length() < 10) {
            return false;
        }
        try {
            sdf.parse(dateInput);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
