package ec.edu.lexus.facebookrecipies.recipelist;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.Arrays;
import java.util.List;

import ec.edu.lexus.facebookrecipies.entities.Recipe;
import ec.edu.lexus.facebookrecipies.lib.base.EventBus;
import ec.edu.lexus.facebookrecipies.recipelist.events.RecipeListEvent;

/**
 * Created by Alexis on 10/11/2017.
 */

public class RecipeListRepositoryImpl implements RecipeListRepository{
    EventBus eventBus;

    public RecipeListRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getSaveRecipes() {
        //esto se puede pasar a una clase RecipeConsultHelper, o DBREcipeHelper
//        FlowCursorList<Recipe> storedRecipes = new FlowCursorList<Recipe>(true, Recipe.class);
//        storedRecipes.close();
        List<Recipe> recipes = new Select().from(Recipe.class).queryList();
        post(RecipeListEvent.READ_EVENT,recipes);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        recipe.update();
        post();
    }

    @Override
    public void removeRecipe(Recipe recipe) {
        recipe.delete();
        post(RecipeListEvent.DELETE_EVENT, Arrays.asList(recipe));
    }

    private void  post(int type, List<Recipe> recipes){
        RecipeListEvent event=new RecipeListEvent();
        event.setType(type);
        event.setRecipeList(recipes);
        eventBus.post(event);
    }

    private void post(){
        post(RecipeListEvent.UPDATE_EVENT,null);
    }
}
