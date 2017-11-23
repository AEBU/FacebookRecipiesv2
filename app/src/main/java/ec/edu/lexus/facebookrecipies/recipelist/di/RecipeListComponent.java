package ec.edu.lexus.facebookrecipies.recipelist.di;

import javax.inject.Singleton;

import dagger.Component;
import ec.edu.lexus.facebookrecipies.lib.base.ImageLoader;
import ec.edu.lexus.facebookrecipies.lib.di.LibsModule;
import ec.edu.lexus.facebookrecipies.recipelist.RecipeListPresenter;
import ec.edu.lexus.facebookrecipies.recipelist.ui.adapters.RecipesAdapter;
import ec.edu.lexus.facebookrecipies.recipemain.RecipeMainPresenter;

/**
 * Created by Alexis on 04/11/2017.
 */
@Singleton
@Component(modules = {RecipeListModule.class, LibsModule.class})
public interface RecipeListComponent {
    RecipesAdapter getAdapter();
    RecipeListPresenter getPresenter();
}
