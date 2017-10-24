package ec.edu.lexus.facebookrecipies.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ec.edu.lexus.facebookrecipies.entities.Recipe;

/**
 * Created by Alexis on 24/10/2017.
 */
public class RecipeSearchResponse {

    private int count;
    private List<Recipe> recipes;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Recipe getFirstRecipe(){
        Recipe first = null;
        if (!recipes.isEmpty()) {
            first = recipes.get(0);
        }
        return first;
    }
}
