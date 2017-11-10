package ec.edu.lexus.facebookrecipies.recipelist;

import ec.edu.lexus.facebookrecipies.entities.Recipe;

/**
 * Podia tner 3 interactuadores uno para Leer, otro para Actualizar, otro para Eliminar
 * Created by Alexis on 10/11/2017.
 */

public interface StoredRecipesInteractor {
//va a ir a traer a los elementos de la BD, pero este va a tener un metodo para actualizar y otro para borrar
    void executeUpdate(Recipe recipe);
    void executeDelete(Recipe recipe);
}
