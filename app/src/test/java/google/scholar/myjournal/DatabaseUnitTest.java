package google.scholar.myjournal;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseUnitTest {

    // journal entry
    private final String title = "JUnit Test Title";
    private final String entry = "Android Junit runner test";
    private final String date = new Date().toString();

    @Mock
    DatabaseReference mFirebaseDatabaseReference;

    @Mock
    FirebaseAuth mFirebaseAuth;

    private static final String JOURNAL_CHILD = "entries";
    private String userId;
    private String journalKey;

    @Before
    public void setupDatabaseRef() {
        MockitoAnnotations.initMocks(this);
        userId = mFirebaseAuth.getUid();
    }

    @Test
    public void addJournal() { // first part of save button functionality
        final JournalEntry journalEntry = new JournalEntry(date, title, entry);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                journalKey = mFirebaseDatabaseReference.child(JOURNAL_CHILD + "/" + userId)
                        .push().getKey();
                mFirebaseDatabaseReference.setValue(journalEntry);
            }
        });
    }

    @Test
    public void updateJournal() { // second part of save button functionality

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //update journal entry
                Map<String, Object> journalUpdate = new HashMap<>();
                journalUpdate.put("title", title);
                journalUpdate.put("textEntry", entry);
                journalUpdate.put("date", date);

                mFirebaseDatabaseReference.child(JOURNAL_CHILD + "/" + userId + "/" + journalKey)
                        .updateChildren(journalUpdate);
            }
        });
    }
}
