package ec.edu.lexus.facebookrecipies.lib.di;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ec.edu.lexus.facebookrecipies.lib.GlideImageLoader;
import ec.edu.lexus.facebookrecipies.lib.GreenRobotEventBus;
import ec.edu.lexus.facebookrecipies.lib.base.EventBus;
import ec.edu.lexus.facebookrecipies.lib.base.ImageLoader;

import static android.R.attr.fragment;

/**
 * Pongo mis implementaciones para los módulos que necesito, cascada a través del cual voy devolviendo dependencias
 * De esta Forma estoy proveyendo las dependencias de las librerías, si yo uso un Glide o GrenRobotEvents tendría que especificar
 * un LibsComponent ahí explicando como lo voy a inyectar ya sea con un método generico inject especificando la clase que lo va usar
 * o métodos que dicen como obtengo estos elementos
 * Created by Alexis on 26/09/2017.
 */

@Module
public class LibsModule {
    private Activity activity;

    public LibsModule() {
    }

    public LibsModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    EventBus providesEventBus(org.greenrobot.eventbus.EventBus eventBus) {
        return new GreenRobotEventBus(eventBus);
    }

    @Provides
    @Singleton
    org.greenrobot.eventbus.EventBus providesLibraryEventBus() {
        return  org.greenrobot.eventbus.EventBus.getDefault();
    }



    @Provides
    @Singleton
    ImageLoader providesImageLoader(RequestManager requestManager) {
        return new GlideImageLoader(requestManager);
    }

    @Provides
    @Singleton
    RequestManager providesRequestManager( Activity activity) {
        return Glide.with(activity);
    }


    @Provides
    @Singleton
    Activity providesActivity() {
        return this.activity;
    }
}
