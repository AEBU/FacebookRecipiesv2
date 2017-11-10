package ec.edu.lexus.facebookrecipies.recipelist;

import ec.edu.lexus.facebookrecipies.entities.Recipe;

/**
 * Created by Alexis on 10/11/2017.
 */

public interface RecipeListRepository {
    void getSaveRecipes();
    void updateRecipe(Recipe recipe);
    void removeRecipe(Recipe recipe);
}
