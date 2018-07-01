package google.scholar.myjournal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds journal data and the Context
    private List<JournalEntry> mJournalEntries;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new JournalViewHolder that holds the view for each entry
     */
    public JournalAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    /**
     * facilitates the individual journal click event
     */
    public interface ItemClickListener {
        void onItemClickListener(JournalEntry entry);
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new JournalViewHolder that holds the view for each task
     */

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the journal entry layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.journal_entry_layout, parent, false);

        return new JournalViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        // Determine the values of the wanted data
        JournalEntry journalEntry = mJournalEntries.get(position);
        String title = journalEntry.getTitle();
        String entryDate = journalEntry.getDate();

        // set values
        holder.journalTitleView.setText(title);
        holder.journalEntryDateView.setText(entryDate);
    }

    /**
     * returns the number of items to display
     * @return 0
     */

    @Override
    public int getItemCount() {
        if (mJournalEntries == null) {
            return 0;
        }
        return mJournalEntries.size();
    }

    public List<JournalEntry> getJournals() {
        return mJournalEntries;
    }

    /**
     * When data changes, this method updates the list of journal Entries
     * and notifies the adapter to use the new values on it
     */
    public void setJournalEntries(List<JournalEntry> journalEntries) {
        mJournalEntries = journalEntries;
        notifyDataSetChanged();
    }

    // Inner class for creating ViewHolders
    public class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variable foe the journal entry textviews
        TextView journalTitleView;
        TextView journalEntryDateView;

        /**
         * Constructor for the JournalViewHolder
         * @param view
         */

        public JournalViewHolder(View view) {
            super(view);

            journalTitleView = view.findViewById(R.id.text_view_entry_title);
            journalEntryDateView = view.findViewById(R.id.text_view_entry_date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onItemClickListener(mJournalEntries.get(getAdapterPosition()));
        }
    }
}
