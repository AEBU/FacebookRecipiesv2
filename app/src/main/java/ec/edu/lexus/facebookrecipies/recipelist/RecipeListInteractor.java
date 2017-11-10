package ec.edu.lexus.facebookrecipies.recipelist;

import ec.edu.lexus.facebookrecipies.entities.Recipe;
import ec.edu.lexus.facebookrecipies.recipelist.events.RecipeListEvent;
import ec.edu.lexus.facebookrecipies.recipelist.ui.RecipeListView;

/**
 * Created by Alexis on 10/11/2017.
 */

public interface RecipeListInteractor {
//va a ir a traer a los elementos de la BD
    void execute();
}
