package ec.edu.lexus.facebookrecipies.recipelist.events;

import android.content.Intent;

import java.util.List;

import ec.edu.lexus.facebookrecipies.entities.Recipe;

import static com.raizlabs.android.dbflow.config.FlowLog.Level.I;

/**
 * Created by Alexis on 10/11/2017.
 */

public class RecipeListEvent {
    private int type;
    private List<Recipe> recipeList;

    public static final int READ_EVENT=0;
    public static final int UPDATE_EVENT=1;
    public static final int DELETE_EVENT=2;


    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
