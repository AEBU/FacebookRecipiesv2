package ec.edu.lexus.facebookrecipies.recipemain;

import java.util.Random;

/**
 * Created by Alexis on 25/10/2017.
 */

public class GetNextRecipeInteractorImpl implements GetNextRecipeInteractor{

    RecipeMainRepository repository;

    public GetNextRecipeInteractorImpl(RecipeMainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        int recipePage=new Random().nextInt(RecipeMainRepository.RECIPE_RANGE);
        repository.setRecipePage(recipePage);
        repository.getNextRecipe();
    }
}
