package google.scholar.myjournal;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class JournalEntryViewModel extends ViewModel {

    // private static final String LOG_TAG = "JournalEntryViewModel";

    private List<JournalEntry> journalEntryList = new ArrayList<>();
    private FirebaseAuth mFirebaseAuth =  FirebaseAuth.getInstance();
    private String userId = mFirebaseAuth.getUid();


    private DatabaseReference JOURNAL_ENTRY_REF =
            FirebaseDatabase.getInstance().getReference().child("entries/" + userId);

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(JOURNAL_ENTRY_REF);

    @NonNull
    public LiveData<List<JournalEntry>> getEntryListLiveData() {
        return Transformations.map(liveData, new Deserializer());
    }

    private class Deserializer implements Function<DataSnapshot, List<JournalEntry>> {

        @Override
        public List<JournalEntry> apply(DataSnapshot dataSnapshot) {
            journalEntryList.clear();
            for(DataSnapshot snap : dataSnapshot.getChildren()){
                JournalEntry journalEntry = snap.getValue(JournalEntry.class);
                if (journalEntry != null) {
                    journalEntry.setKey(snap.getKey());
                }

                journalEntryList.add(journalEntry);
            }
            return journalEntryList;
        }
    }
}
