package ec.edu.lexus.facebookrecipies.recipelist;

import ec.edu.lexus.facebookrecipies.entities.Recipe;
import ec.edu.lexus.facebookrecipies.recipelist.events.RecipeListEvent;
import ec.edu.lexus.facebookrecipies.recipelist.ui.RecipeListView;

/**
 * Created by Alexis on 10/11/2017.
 */

public interface RecipeListPresenter {

    void onCreate();
    void onDestroy();
    void getRecipes();
    void removeRecipe(Recipe recipe);

    void toggleFavorite(Recipe recipe);
    void onEventMainThread(RecipeListEvent event);
    //como test
    RecipeListView getView();

}
