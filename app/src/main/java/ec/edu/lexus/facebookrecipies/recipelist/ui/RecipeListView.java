package ec.edu.lexus.facebookrecipies.recipelist.ui;

import java.util.List;

import ec.edu.lexus.facebookrecipies.entities.Recipe;

/**
 * Created by Alexis on 10/11/2017.
 */

public interface RecipeListView {
    void setRecipes(List<Recipe> data);
    void recipeUpdated();
    void recipeDeleted(Recipe recipe);


}
