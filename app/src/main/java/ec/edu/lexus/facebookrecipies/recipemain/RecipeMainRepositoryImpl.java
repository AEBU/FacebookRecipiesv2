package ec.edu.lexus.facebookrecipies.recipemain;



import java.util.Random;

import ec.edu.lexus.facebookrecipies.BuildConfig;
import ec.edu.lexus.facebookrecipies.api.RecipeSearchResponse;
import ec.edu.lexus.facebookrecipies.api.RecipeService;
import ec.edu.lexus.facebookrecipies.entities.Recipe;
import ec.edu.lexus.facebookrecipies.lib.base.EventBus;
import ec.edu.lexus.facebookrecipies.recipemain.events.RecipeMainEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alexis on 25/10/2017.
 */

public class RecipeMainRepositoryImpl implements RecipeMainRepository{
    private int recipePage;
    private EventBus eventBus;
    private RecipeService recipeService;

    public RecipeMainRepositoryImpl(int recipePage, EventBus eventBus, RecipeService recipeService) {
        this.recipePage = recipePage;
        this.eventBus = eventBus;
        this.recipeService = recipeService;
    }

    @Override
    public void getNextRecipe() {
        Call<RecipeSearchResponse> call = recipeService.search(BuildConfig.FOOD_API_KEY, RECENT_SORT, COUNT, recipePage);
        Callback<RecipeSearchResponse> callback=new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                if (response.isSuccessful()) {
                    RecipeSearchResponse recipeSearchResponse = response.body();
                    if (recipeSearchResponse.getCount() == 0){
                        setRecipePage(new Random().nextInt(RECIPE_RANGE));
                        getNextRecipe();
                    } else {
                        Recipe recipe = recipeSearchResponse.getFirstRecipe();
                        if (recipe != null) {
                            post(recipe);
                        } else {
                            post(response.message());
                        }
                    }
                } else {
                    post(response.message());
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
                post(t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);
        // o podemos hacr call.enqueque(new Callback<SearchResponse> ...
    }

    @Override
    public void saveRecipe(Recipe recipe) {
        //nos apoyamos en el ORM para poder guardar directamente como implemenat Dbflow pues tenemos estas alternativas
        recipe.save();
        post();

    }

    @Override
    public void setRecipePage(int recipePage) {
        this.recipePage=recipePage;
    }

    private void post(String error,int type,Recipe recipe){
        RecipeMainEvent event=new RecipeMainEvent();
        event.setError(error);
        event.setRecipe(recipe);
        event.setType(type);
    }
    private void post(String error){
        post(error,RecipeMainEvent.NEXT_EVENT,null);
    }
    private void post(Recipe recipe){
        post(null,RecipeMainEvent.NEXT_EVENT,recipe);
    }
    private void post(){
        post(null,RecipeMainEvent.SAVE_EVENT,null);
    }

}
