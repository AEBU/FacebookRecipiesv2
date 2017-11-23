package ec.edu.lexus.facebookrecipies;

import android.app.Application;
import android.content.Intent;

import com.facebook.login.LoginManager;
import com.raizlabs.android.dbflow.config.FlowManager;

import ec.edu.lexus.facebookrecipies.lib.di.LibsModule;
import ec.edu.lexus.facebookrecipies.login.ui.LoginActivity;
import ec.edu.lexus.facebookrecipies.recipelist.di.DaggerRecipeListComponent;
import ec.edu.lexus.facebookrecipies.recipelist.di.RecipeListComponent;
import ec.edu.lexus.facebookrecipies.recipelist.di.RecipeListModule;
import ec.edu.lexus.facebookrecipies.recipelist.ui.RecipeListActivity;
import ec.edu.lexus.facebookrecipies.recipelist.ui.RecipeListView;
import ec.edu.lexus.facebookrecipies.recipelist.ui.adapters.OnItemClickListener;
import ec.edu.lexus.facebookrecipies.recipemain.di.DaggerRecipeMainComponent;
import ec.edu.lexus.facebookrecipies.recipemain.di.RecipeMainComponent;
import ec.edu.lexus.facebookrecipies.recipemain.di.RecipeMainModule;
import ec.edu.lexus.facebookrecipies.recipemain.ui.RecipeMainActivity;
import ec.edu.lexus.facebookrecipies.recipemain.ui.RecipeMainView;

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

    public void logout(){
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public RecipeMainComponent getRecipeMainComponent(RecipeMainActivity activity, RecipeMainView view) {
        return DaggerRecipeMainComponent
                .builder()
                .libsModule(new LibsModule(activity))
                .recipeMainModule(new RecipeMainModule(view))
                .build();
    }

    public RecipeListComponent getRecipeListComponent(RecipeListActivity activity, RecipeListView view, OnItemClickListener onItemClickListener) {
        return DaggerRecipeListComponent
                .builder()
                .libsModule(new LibsModule(activity))
                .recipeListModule(new RecipeListModule(view, onItemClickListener))
                .build();
    }
}
