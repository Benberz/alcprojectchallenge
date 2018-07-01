package google.scholar.myjournal;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class alcprojectchallenge extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // setting persistence on the application level, registered in manifest.
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
