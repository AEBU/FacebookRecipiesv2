package ec.edu.lexus.facebookrecipies.recipemain.di;

import javax.inject.Singleton;

import dagger.Component;
import ec.edu.lexus.facebookrecipies.lib.base.ImageLoader;
import ec.edu.lexus.facebookrecipies.lib.di.LibsModule;
import ec.edu.lexus.facebookrecipies.recipemain.RecipeMainPresenter;

/**
 * Created by Alexis on 04/11/2017.
 */
@Singleton
@Component(modules = {RecipeMainModule.class, LibsModule.class})
public interface RecipeMainComponent {
    //void inject(RecipeMainActivity activity);
    ImageLoader getImageLoader();
    RecipeMainPresenter getPresenter();
}
