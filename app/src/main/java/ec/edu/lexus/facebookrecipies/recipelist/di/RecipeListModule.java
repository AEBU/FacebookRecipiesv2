package ec.edu.lexus.facebookrecipies.recipelist.di;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ec.edu.lexus.facebookrecipies.api.RecipeClient;
import ec.edu.lexus.facebookrecipies.api.RecipeService;
import ec.edu.lexus.facebookrecipies.entities.Recipe;
import ec.edu.lexus.facebookrecipies.lib.base.EventBus;
import ec.edu.lexus.facebookrecipies.lib.base.ImageLoader;
import ec.edu.lexus.facebookrecipies.recipelist.RecipeListInteractor;
import ec.edu.lexus.facebookrecipies.recipelist.RecipeListInteractorImpl;
import ec.edu.lexus.facebookrecipies.recipelist.RecipeListPresenter;
import ec.edu.lexus.facebookrecipies.recipelist.RecipeListPresenterImpl;
import ec.edu.lexus.facebookrecipies.recipelist.RecipeListRepository;
import ec.edu.lexus.facebookrecipies.recipelist.RecipeListRepositoryImpl;
import ec.edu.lexus.facebookrecipies.recipelist.StoredRecipesInteractor;
import ec.edu.lexus.facebookrecipies.recipelist.StoredRecipesInteractorImpl;
import ec.edu.lexus.facebookrecipies.recipelist.ui.RecipeListView;
import ec.edu.lexus.facebookrecipies.recipelist.ui.adapters.OnItemClickListener;
import ec.edu.lexus.facebookrecipies.recipelist.ui.adapters.RecipesAdapter;
import ec.edu.lexus.facebookrecipies.recipemain.GetNextRecipeInteractor;
import ec.edu.lexus.facebookrecipies.recipemain.GetNextRecipeInteractorImpl;
import ec.edu.lexus.facebookrecipies.recipemain.RecipeMainPresenter;
import ec.edu.lexus.facebookrecipies.recipemain.RecipeMainPresenterImpl;
import ec.edu.lexus.facebookrecipies.recipemain.RecipeMainRepository;
import ec.edu.lexus.facebookrecipies.recipemain.RecipeMainRepositoryImpl;
import ec.edu.lexus.facebookrecipies.recipemain.SaveRecipeInteractor;
import ec.edu.lexus.facebookrecipies.recipemain.SaveRecipeInteractorImpl;
import ec.edu.lexus.facebookrecipies.recipemain.ui.RecipeMainView;

/**
 * Created by Alexis on 04/11/2017.
 */
@Module
public class RecipeListModule {
    RecipeListView view;
    OnItemClickListener clickListener;

    public RecipeListModule(RecipeListView view, OnItemClickListener clickListener) {
        this.view = view;
        this.clickListener = clickListener;
    }

    @Provides @Singleton
    RecipeListView providesRecipeMainView() {
        return this.view;
    }

    @Provides
    @Singleton
    RecipeListPresenter providesRecipeListPresenter(EventBus eventBus, RecipeListView view, RecipeListInteractor listInteractor, StoredRecipesInteractor storedRecipesInteractor) {
        return new RecipeListPresenterImpl(eventBus, view, listInteractor, storedRecipesInteractor);
    }

    @Provides @Singleton
    RecipeListInteractor providesRecipeListInteractor(RecipeListRepository repository) {
        return new RecipeListInteractorImpl(repository);
    }

    @Provides @Singleton
    StoredRecipesInteractor providesStoredRecipesInteractor(RecipeListRepository repository) {
        return new StoredRecipesInteractorImpl(repository);
    }

    @Provides @Singleton
    RecipeListRepository providesRecipeListRepository(EventBus eventBus) {
        return new RecipeListRepositoryImpl(eventBus);
    }

    @Provides @Singleton
    RecipesAdapter providesRecipesAdapter(List<Recipe> recipeList, ImageLoader imageLoader, OnItemClickListener onItemClickListener) {
        return new RecipesAdapter(recipeList,imageLoader,onItemClickListener);
    }

    @Provides @Singleton
    OnItemClickListener providesOnItemClickListener() {
        return this.clickListener;
    }

    @Provides @Singleton
    List<Recipe> providesEmptyList() {
        return new ArrayList<Recipe>();
    }

}
