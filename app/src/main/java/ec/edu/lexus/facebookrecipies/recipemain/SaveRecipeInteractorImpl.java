package ec.edu.lexus.facebookrecipies.recipemain;

import java.util.Random;

import ec.edu.lexus.facebookrecipies.entities.Recipe;

/**
 * Created by Alexis on 25/10/2017.
 */

public class SaveRecipeInteractorImpl implements SaveRecipeInteractor{
    RecipeMainRepository repository;

    public SaveRecipeInteractorImpl(RecipeMainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Recipe recipe) {
        repository.saveRecipe(recipe);
    }
}
