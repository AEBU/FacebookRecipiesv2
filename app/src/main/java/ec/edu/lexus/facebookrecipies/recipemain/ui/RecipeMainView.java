package ec.edu.lexus.facebookrecipies.recipemain.ui;

import ec.edu.lexus.facebookrecipies.entities.Recipe;

/**
 * Created by Alexis on 25/10/2017.
 */

public interface RecipeMainView {
    void showProgress();
    void hideProgress();
    void showUIElements();
    void hideUIElements();
    void saveAnimation();
    void dismissAnimation();

    void onRecipeSaved();
    void setRecipe(Recipe recipe);
    void onGetRecipeError(String error);
}
