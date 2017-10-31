package ec.edu.lexus.facebookrecipies.recipemain;

import com.raizlabs.android.dbflow.list.IFlowCursorIterator;

import org.greenrobot.eventbus.Subscribe;

import ec.edu.lexus.facebookrecipies.entities.Recipe;
import ec.edu.lexus.facebookrecipies.lib.base.EventBus;
import ec.edu.lexus.facebookrecipies.recipemain.events.RecipeMainEvent;
import ec.edu.lexus.facebookrecipies.recipemain.ui.RecipeMainView;

/**
 * Created by Alexis on 31/10/2017.
 */

public class RecipeMainImpl implements RecipeMainPresenter {
    private EventBus eventBus;
    private RecipeMainView view;
    SaveRecipeInteractor saveRecipeInteractor;
    GetNextRecipeInteractor getNextRecipeInteractor;

    public RecipeMainImpl(EventBus eventBus, RecipeMainView view, SaveRecipeInteractor saveRecipeInteractor, GetNextRecipeInteractor getNextRecipeInteractor) {
        this.eventBus = eventBus;
        this.view = view;
        this.saveRecipeInteractor = saveRecipeInteractor;
        this.getNextRecipeInteractor = getNextRecipeInteractor;
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
    public void dismissRecipe() {
        if (view!=null){
         view.dismissAnimation();
        }
        getNextRecipe();
    }

    @Override
    public void getNextRecipe() {
        if (view!=null){
            view.hideUIElements();
            view.showProgress();
        }
        getNextRecipeInteractor.execute();
    }

    @Override
    public void saveRecipe(Recipe recipe) {
        if (view!=null){
        view.saveAnimation();
            view.hideUIElements();
            view.showProgress();
        }
        saveRecipeInteractor.execute(recipe);
    }

    @Override
    @Subscribe
    public void onEventMainThread(RecipeMainEvent event) {
        if (view!=null){
            String error=event.getError();
            if (error!=null){
                view.hideProgress();
                view.onGetRecipeError(error);
            }else {
                if (event.getType()==RecipeMainEvent.NEXT_EVENT){
                    view.setRecipe(event.getRecipe());
                }else if (event.getType()==RecipeMainEvent.SAVE_EVENT) {
                    view.onRecipeSaved();
                    getNextRecipeInteractor.execute();
                }
            }
        }
    }

    @Override
    public void imageReady() {
        if (view!=null){
            view.hideProgress();
            view.showUIElements();
        }
    }

    @Override
    public void imageError(String error) {
        if (view!=null){
            view.onGetRecipeError(error);
        }
    }

    @Override
    public RecipeMainView getView() {
        return this.view;
    }
}
