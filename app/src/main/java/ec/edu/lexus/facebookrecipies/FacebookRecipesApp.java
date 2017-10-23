package ec.edu.lexus.facebookrecipies;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Alexis on 21/10/2017.
 */

public class FacebookRecipesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        DBTearDown();
    }
    private void initDB() {
        FlowManager.init(this);
    }

    private void DBTearDown() {
        FlowManager.destroy();
    }
}
