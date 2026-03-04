package com.example.travelplannerplus;

import static org.junit.Assert.assertEquals;
import android.content.Intent;
import android.widget.EditText;
import com.example.travelplannerplus.ui.DestinationActivityDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 36)
public class DestinationActivityDetailsTest {
    @Test
    public void saveActivity_noTitle_showErrorToast() {
        Intent intent = new Intent();
        intent.putExtra(DestinationActivityDetails.DESTINATION_ID, 1);
        DestinationActivityDetails screen =
                Robolectric.buildActivity(DestinationActivityDetails.class, intent)
                        .setup()
                        .get();
        EditText activity = screen.findViewById(R.id.editActivityTitle);
        EditText dateField = screen.findViewById(R.id.editActivityDate);
        activity.setText("");  // Test trigger
        dateField.setText("01/01/2026");
        screen.saveDestinationActivity();
        assertEquals("Please enter a valid Activity title...", ShadowToast.getTextOfLatestToast());
    }
}