package ec.edu.lexus.facebookrecipies.recipemain;

import ec.edu.lexus.facebookrecipies.entities.Recipe;
import ec.edu.lexus.facebookrecipies.recipemain.events.RecipeMainEvent;
import ec.edu.lexus.facebookrecipies.recipemain.ui.RecipeMainView;

/**
 * Created by Alexis on 25/10/2017.
 */

public interface RecipeMainPresenter {
    void onCreate();
    void onDestroy();

    void dismissRecipe();
    void getNextRecipe();
    void saveRecipe(Recipe recipe);
    void onEventMainThread(RecipeMainEvent event);

    RecipeMainView getView();
}
