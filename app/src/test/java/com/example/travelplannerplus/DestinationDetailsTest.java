package com.example.travelplannerplus;

import static org.junit.Assert.assertEquals;
import android.widget.EditText;
import com.example.travelplannerplus.ui.DestinationDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 36)
public class DestinationDetailsTest {
    @Test
    public void saveDestination_noLodging_showErrorToast() {
        DestinationDetails screen = Robolectric.buildActivity(DestinationDetails.class)
                .setup()
                .get();
        EditText destination = screen.findViewById(R.id.editDestinationTitle);
        EditText lodging = screen.findViewById(R.id.editDestinationLodging);
        EditText startDate = screen.findViewById(R.id.editDestinationStartDate);
        EditText endDate = screen.findViewById(R.id.editDestinationEndDate);
        destination.setText("Taniti Island");
        lodging.setText("");  // Test trigger
        startDate.setText("01/01/2026");
        endDate.setText("03/01/2026");
        screen.saveDestination();
        assertEquals("Please enter valid Lodging...", ShadowToast.getTextOfLatestToast());
    }
}