package google.scholar.myjournal;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddJournalActivity extends AppCompatActivity {

    private static final String TAG = AddJournalActivity.class.getSimpleName();
    public static final String EXTRA_JOURNAL= "extraJournal";

    // Fields for views
    private EditText mTitleEditText;
    private EditText mEntryEditText;
    private Button mButton;

    private DatabaseReference mFirebaseDatabaseReference;
    public static final String JOURNAL_CHILD = "entries";
    private String userId;
    private String journalKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        // initialize the views
        initViews();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        userId = mFirebaseAuth.getUid();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL)) {
             // set the title to Details
            mButton.setText(R.string.update_button);
                // populate the UI
                // Set title of Detail page
            final JournalEntry mEntry = (JournalEntry) getIntent().getSerializableExtra(EXTRA_JOURNAL);
            populateUI(mEntry);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param mEntry the Journal Entry to populate the UI
     */

    private void populateUI(JournalEntry mEntry) {
        if (mEntry != null) {
            mTitleEditText.setText(mEntry.getTitle());
            mEntryEditText.setText(mEntry.getTextEntry());
            // get the journal entry key in the database
            journalKey = mEntry.getKey();
        }
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mTitleEditText = findViewById(R.id.edit_text_journal_title);
        mEntryEditText = findViewById(R.id.edit_text_journal_entry);

        mButton = findViewById(R.id.button_save);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new journal data into the underlying database.
     */
    private void onSaveButtonClicked() {
        final String title  = mTitleEditText.getText().toString();
        final String entry = mEntryEditText.getText().toString();
        final String date = new Date().toString();

        final JournalEntry journalEntry = new JournalEntry(date, title, entry);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (journalKey == null) {
                    // insert new journal entry
                    mFirebaseDatabaseReference.child(JOURNAL_CHILD + "/" + userId)
                            .push().setValue(journalEntry);
                } else {
                    //update journal entry
                    Map<String, Object> journalUpdate = new HashMap<>();
                    journalUpdate.put("title", title);
                    journalUpdate.put("textEntry", entry);
                    journalUpdate.put("date", date);

                    Log.d(TAG, "Entry Ref: " + JOURNAL_CHILD + "/" + userId + "/" + journalKey);

                    mFirebaseDatabaseReference.child(JOURNAL_CHILD + "/" + userId + "/" + journalKey)
                            .updateChildren(journalUpdate);
                }
                finish();
            }
        });
    }
}
