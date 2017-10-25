package ec.edu.lexus.facebookrecipies.recipemain;


import ec.edu.lexus.facebookrecipies.entities.Recipe;

/**
 * Created by Alexis on 25/10/2017.
 */

public interface RecipeMainRepository {
    public final static int COUNT = 1;
    public final static String RECENT_SORT = "r";
    public final static int RECIPE_RANGE = 100000;

    void getNextRecipe();
    void saveRecipe(Recipe recipe);
    void setRecipePage(int recipePage);
}
