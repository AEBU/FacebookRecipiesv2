package ec.edu.lexus.facebookrecipies.recipelist;

/**
 * Created by Alexis on 10/11/2017.
 */

public class RecipeListInteractorImpl implements RecipeListInteractor {
    RecipeListRepository repository;

    public RecipeListInteractorImpl(RecipeListRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        repository.getSaveRecipes();
    }
//va a ir a traer a los elementos de la BD

}
