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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerplus.R;
import com.example.travelplannerplus.entities.Destination;
import com.example.travelplannerplus.entities.DestinationActivity;
import com.example.travelplannerplus.repository.DestinationRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DestinationDetails extends AppCompatActivity {
    public static final String DESTINATION_ID = "destinationId";
    private int destinationId = -1;
    private DestinationRepository destinationRepository;
    private EditText editDestinationTitle;
    private EditText editDestinationLodging;
    private EditText editDestinationStartDate;
    private EditText editDestinationEndDate;
    private DestinationActivityAdapter destinationActivityAdapter;

    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ui_destination_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sdf.setLenient(false);
        destinationRepository = new DestinationRepository(getApplication());
        destinationId = getIntent().getIntExtra(DESTINATION_ID, -1);
        editDestinationTitle = findViewById(R.id.editDestinationTitle);
        editDestinationLodging = findViewById(R.id.editDestinationLodging);
        editDestinationStartDate = findViewById(R.id.editDestinationStartDate);
        editDestinationEndDate = findViewById(R.id.editDestinationEndDate);
        RecyclerView destinationActivityRecyclerView = findViewById(R.id.activityRecyclerView);
        FloatingActionButton activityFAB = findViewById(R.id.activityFAB);

        destinationActivityAdapter = new DestinationActivityAdapter(destinationActivity -> {
            Intent intent = new Intent(DestinationDetails.this, DestinationActivityDetails.class);
            intent.putExtra(DestinationActivityDetails.DESTINATION_ID, destinationId);
            intent.putExtra(DestinationActivityDetails.ACTIVITY_ID, destinationActivity.getDestinationActivityId());
            startActivity(intent);
        });

        destinationActivityRecyclerView.setAdapter(destinationActivityAdapter);
        destinationActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityFAB.setOnClickListener(v -> {
            if (destinationId == -1) {
                Toast.makeText(this, "You must save a Destination before adding an Activity!", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(DestinationDetails.this, DestinationActivityDetails.class);
            intent.putExtra(DestinationActivityDetails.DESTINATION_ID, destinationId);
            intent.putExtra(DestinationActivityDetails.ACTIVITY_ID, -1);
            startActivity(intent);
        });

        if (destinationId != -1) {  // -1 is always a new Destination

            Destination destinationExists = destinationRepository.getDestinationById(destinationId);

            if (destinationExists != null) {  //  If Destination exists, pull attributes
                if (destinationExists.getDestinationTitle() != null) {
                    editDestinationTitle.setText(destinationExists.getDestinationTitle());
                }
                if (destinationExists.getDestinationLodging() != null) {
                    editDestinationLodging.setText(destinationExists.getDestinationLodging());
                }
                if (formatScreenDate(destinationExists.getDestinationStartDate()) != null) {
                    editDestinationStartDate.setText(formatScreenDate(destinationExists.getDestinationStartDate()));
                }
                if (formatScreenDate(destinationExists.getDestinationEndDate()) != null) {
                    editDestinationEndDate.setText(formatScreenDate(destinationExists.getDestinationEndDate()));
                }
            } else {
                Toast.makeText(this, "Destination not found.", Toast.LENGTH_SHORT).show();
                finish();
            }
            loadDestinationActivity();
        }
    }

    //  Destination Details Menu Controller
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ui_destination_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.destination_save) {
            saveDestination();
            return true;
        }
        if (item.getItemId() == R.id.destination_delete) {
            if (destinationId == -1) {
                Toast.makeText(this, "You must save a Destination before attempting to delete!", Toast.LENGTH_LONG).show();
                return true;
            }
            deleteDestination();
            return true;
        }
        if (item.getItemId() == R.id.destination_alert_start) {
            String startDate = editDestinationStartDate.getText().toString().trim();
            long parsedStartDate = parseDate(startDate);
            if (destinationId == -1) {
                Toast.makeText(this, "You must save the Destination before setting an alert!", Toast.LENGTH_LONG).show();
                return true;
            }
            createDestinationAlert("START", parsedStartDate);
            return true;
        }
        if (item.getItemId() == R.id.destination_alert_end) {
            String endDate = editDestinationEndDate.getText().toString().trim();
            long parsedEndDate = parseDate(endDate);
            if (destinationId == -1) {
                Toast.makeText(this, "You must save the Destination before setting an alert!", Toast.LENGTH_LONG).show();
                return true;
            }
            createDestinationAlert("END", parsedEndDate);
            return true;
        }
        if (item.getItemId() == R.id.destination_share) {
            if (destinationId == -1) {
                Toast.makeText(this, "You must save the Destination before sharing!", Toast.LENGTH_LONG).show();
                return true;
            }
            shareDestination();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Destination Save/Add Controller
    public void saveDestination() {
        String destinationTitle = editDestinationTitle.getText().toString().trim();
        String destinationLodging = editDestinationLodging.getText().toString().trim();
        String startDate = editDestinationStartDate.getText().toString().trim();
        String endDate = editDestinationEndDate.getText().toString().trim();

        if (TextUtils.isEmpty(destinationTitle)) {
            Toast.makeText(this, "Please enter a valid Destination title...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(destinationLodging)) {
            Toast.makeText(this, "Please enter valid Lodging...", Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidDate(startDate)) {
            Toast.makeText(this, "Start Date must be formatted as MM/DD/YYYY...", Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidDate(endDate)) {
            Toast.makeText(this, "End Date must be formatted as MM/DD/YYYY...", Toast.LENGTH_LONG).show();
            return;
        }
        //  Above checks for a valid date input first, then parses it next:
        long parsedStartDate = parseDate(startDate);
        long parsedEndDate = parseDate(endDate);

        if (parsedStartDate >= parsedEndDate) {
            Toast.makeText(this, "End date must come after Start Date", Toast.LENGTH_LONG).show();
            return;
        }
        // Inserts new Destination here
        if (destinationId == -1) {
            Destination destination = new Destination(0, destinationTitle, destinationLodging, parsedStartDate, parsedEndDate);
            destinationRepository.insert(destination);
            Toast.makeText(this, "Destination successfully stored...", Toast.LENGTH_SHORT).show();
        } else {  //  Edit an existing Destination here
            Destination destination = new Destination(destinationId, destinationTitle, destinationLodging, parsedStartDate, parsedEndDate);
            destinationRepository.update(destination);
            Toast.makeText(this, "Destination successfully updated...", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void deleteDestination() {
        int destinationActivityCount = destinationRepository.countDestinationActivities(destinationId);

        //  Validation to prevent Destinations with Activities from being deleted
        if (destinationActivityCount > 0) {
            Toast.makeText(this, "Destination with an activity cannot be deleted...", Toast.LENGTH_LONG).show();
            return;
        }

        Destination destinationDelete = destinationRepository.getDestinationById(destinationId);

        if (destinationDelete != null) {
            destinationRepository.delete(destinationDelete);
            Toast.makeText(this, "Destination successfully deleted...", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void loadDestinationActivity() {
        if (destinationId == -1) {
            return;
        }
        List<DestinationActivity> destinationActivity = destinationRepository.getActivityForDestinations(destinationId);
        destinationActivityAdapter.setDestinationActivity(destinationActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDestinationActivity();
    }

    private void createDestinationAlert(String dateType, long date) {
        String destinationTitle = editDestinationTitle.getText().toString().trim();
        long trigger = timeTrigger(date);

        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra(AlertReceiver.DESTINATION_TITLE, destinationTitle);
        intent.putExtra(AlertReceiver.DATE_TYPE, dateType);
        int alertTypeCode;
        if ("START".equals(dateType)) {  // Prevents start/end date alarms from overwriting each other
            alertTypeCode = 1;
        } else {
            alertTypeCode = 2;
        }
        int requestCode = destinationId * 10 + alertTypeCode;  //  Multiply creates unique code

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
            if ("START".equals(dateType)) {
                Toast.makeText(this, "Destination Start Date alert has been set!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Destination End Date alert has been set!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Helper Methods
    private long parseDate(String inputDate) {  // Helper method for parsing the Date input
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
            return "";  // resets input to blank input
        } else {
            return sdf.format(new Date(inputScreenDate));
        }
    }

    private boolean isValidDate(String dateInput) {
        if (dateInput.length() < 10)  {
            return false;
        } else {
            try {
                sdf.parse(dateInput);
                return true;
            } catch (Exception e) {
                return false;
            }
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

    private void shareDestination() {
        String title = editDestinationTitle.getText().toString().trim();
        String lodging = editDestinationLodging.getText().toString().trim();
        String startDate = editDestinationStartDate.getText().toString().trim();
        String endDate = editDestinationEndDate.getText().toString().trim();
        List<DestinationActivity> destinationActivityList = destinationRepository.shareDestinationActivity(destinationId);
        String sharedDestinationActivity = "";

        if (destinationActivityList != null && !destinationActivityList.isEmpty()) {
            for (DestinationActivity destinationActivity : destinationActivityList) {
                sharedDestinationActivity += "\nActivity: " + destinationActivity.getDestinationActivityTitle() + "\nActivity Date: " + formatScreenDate(destinationActivity.getDestinationActivityDate());
            }
        } else {
            sharedDestinationActivity = "\n Activity: None";
        }
        String destinationDetails = "Destination Details\n" + "Title: " + title + "\n" + "Lodging: " + lodging + "\n" + "Start Date: " + startDate + "\n" + "End Date: " + endDate + sharedDestinationActivity;
        Intent intent = new Intent(Intent.ACTION_SEND);  // Full send
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Destination Details");
        intent.putExtra(Intent.EXTRA_TEXT, destinationDetails);
        startActivity(Intent.createChooser(intent, "Share Destination details..."));
    }
}
