package ec.edu.lexus.facebookrecipies.recipelist;

import ec.edu.lexus.facebookrecipies.entities.Recipe;

/**
 * Podia tner 3 interactuadores uno para Leer, otro para Actualizar, otro para Eliminar
 * Created by Alexis on 10/11/2017.
 */

public class StoredRecipesInteractorImpl implements StoredRecipesInteractor{
    RecipeListRepository repository;

    public StoredRecipesInteractorImpl(RecipeListRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executeUpdate(Recipe recipe) {
        repository.updateRecipe(recipe);
    }

    @Override
    public void executeDelete(Recipe recipe) {
        repository.removeRecipe(recipe);
    }
//va a ir a traer a los elementos de la BD, pero este va a tener un metodo para actualizar y otro para borrar
}
