package google.scholar.myjournal;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkArgument;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    /**
     * A custom {@link Matcher} which matches an item in a {@link RecyclerView} by its text.
     *
     * <p>
     * View constraints:
     * <ul>
     * <li>View must be a child of a {@link RecyclerView}
     * <ul>
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendant Of A RecyclerView with text " + itemText);
            }
        };
    }

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickAddJournalButton_opensAddJournalUi() throws Exception{
        // Click on the add journal button
        onView(withId(R.id.fab)).perform(click());

        // check if the add journal screen with the Add button is displayed
        onView(withId(R.id.button_save)).check(matches(isDisplayed()));
    }

    @Test
    public void  addJournalEntry() throws Exception {
        // test journal entries
        String newJournalTitle = "Espresso";
        String newJournalText = "UI testing for Android";

        //Click on the new journal button
        onView(withId(R.id.fab)).perform(click());

        // Add journal title and text
        onView(withId(R.id.edit_text_journal_title)).perform(typeText(newJournalTitle), closeSoftKeyboard());
        onView(withId(R.id.edit_text_journal_entry)).perform(typeText(newJournalText), closeSoftKeyboard());

        // save the journal
        onView(withId(R.id.button_save)).perform(click());

        // scroll journals list to the added note, by finding its text entry
        onView(withItemText(newJournalTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void updateJournalEntry() {

        String journalTitle = "Espresso";
        String editedJournalTitle = "Espresso Updated";

        // click on the select the entry to edit
        onView(withId(R.id.recycler_view_journals))
                .check(matches(withText(journalTitle)))
                .perform(click());

        // updated the title entry
        onView(withId(R.id.edit_text_journal_title)).perform(typeText(editedJournalTitle), closeSoftKeyboard());
        onView(withId(R.id.edit_text_journal_entry)).perform(typeText("just done."), closeSoftKeyboard());

        // scroll journals list to the added note, by finding its text entry
        onView(withItemText(editedJournalTitle)).check(matches(isDisplayed()));
    }
}
