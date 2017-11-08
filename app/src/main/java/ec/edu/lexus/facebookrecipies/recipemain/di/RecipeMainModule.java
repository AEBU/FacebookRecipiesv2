package ec.edu.lexus.facebookrecipies.recipemain.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ec.edu.lexus.facebookrecipies.api.RecipeClient;
import ec.edu.lexus.facebookrecipies.api.RecipeService;
import ec.edu.lexus.facebookrecipies.lib.base.EventBus;
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
public class RecipeMainModule {
    RecipeMainView view;

    public RecipeMainModule(RecipeMainView view) {
        this.view = view;
    }
    @Provides @Singleton
    RecipeMainView provideRecipeMainView() {
        return this.view;
    }

    @Provides
    @Singleton
    RecipeMainPresenter provideRecipeMainPresenter(EventBus eventBus, RecipeMainView view, SaveRecipeInteractor save, GetNextRecipeInteractor getNext) {
        return new RecipeMainPresenterImpl(eventBus, view, save, getNext);
    }

    @Provides @Singleton
    SaveRecipeInteractor provideSaveRecipeInteractor(RecipeMainRepository repository) {
        return new SaveRecipeInteractorImpl(repository);
    }

    @Provides @Singleton
    GetNextRecipeInteractor provideGetNextRecipeInteractor(RecipeMainRepository repository) {
        return new GetNextRecipeInteractorImpl(repository);
    }

    @Provides @Singleton
    RecipeMainRepository provideRecipeMainRepository(EventBus eventBus, RecipeService service) {
        return new RecipeMainRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    RecipeService provideRecipeService() {
        RecipeClient client = new RecipeClient();
        return client.getRecipeService();
    }
}
