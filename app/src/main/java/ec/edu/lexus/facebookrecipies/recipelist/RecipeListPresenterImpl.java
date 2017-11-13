package ec.edu.lexus.facebookrecipies.recipelist;

import org.greenrobot.eventbus.Subscribe;

import ec.edu.lexus.facebookrecipies.entities.Recipe;
import ec.edu.lexus.facebookrecipies.lib.base.EventBus;
import ec.edu.lexus.facebookrecipies.recipelist.events.RecipeListEvent;
import ec.edu.lexus.facebookrecipies.recipelist.ui.RecipeListView;

/**
 * Created by Alexis on 10/11/2017.
 */

public class RecipeListPresenterImpl implements RecipeListPresenter {

    private EventBus eventBus;
    private RecipeListView view;
    private RecipeListInteractor recipeListInteractor;
    private StoredRecipesInteractor storedRecipesInteractor;

    public RecipeListPresenterImpl(EventBus eventBus, RecipeListView view, RecipeListInteractor recipeListInteractor, StoredRecipesInteractor storedRecipesInteractor) {
        this.eventBus = eventBus;
        this.view = view;
        this.recipeListInteractor = recipeListInteractor;
        this.storedRecipesInteractor = storedRecipesInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view=null;
    }

    @Override
    public void getRecipes() {
        recipeListInteractor.execute();
    }

    @Override
    public void removeRecipe(Recipe recipe) {
        storedRecipesInteractor.executeDelete(recipe);
    }

    @Override
    public void toggleFavorite(Recipe recipe) {
        boolean fav=recipe.isFavorite();
        recipe.setFavorite(!fav);
        storedRecipesInteractor.executeUpdate(recipe);
    }

    @Override
    @Subscribe
    public void onEventMainThread(RecipeListEvent event) {
        if (view!=null){
            switch (event.getType()){
                case RecipeListEvent.READ_EVENT:
                    view.setRecipes(event.getRecipeList());
                    break;
                case RecipeListEvent.UPDATE_EVENT:
                    view.recipeUpdated();
                    break;
                case RecipeListEvent.DELETE_EVENT:
                    Recipe recipe= event.getRecipeList().get(0);
                    view.recipeDeleted(recipe);
                    break;
            }
        }
    }

    @Override
    public RecipeListView getView() {
        return this.view;
    }
}
