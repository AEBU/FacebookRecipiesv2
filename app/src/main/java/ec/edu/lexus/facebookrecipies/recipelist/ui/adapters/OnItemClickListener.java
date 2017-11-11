package ec.edu.lexus.facebookrecipies.recipelist.ui.adapters;

import ec.edu.lexus.facebookrecipies.entities.Recipe;

/**
 * Created by Alexis on 11/11/2017.
 */

public interface OnItemClickListener {
    void onFavClick(Recipe recipe);
    void onItemClick(Recipe recipe);
    void onDeleteClick(Recipe recipe);

}
