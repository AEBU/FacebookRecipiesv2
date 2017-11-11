package ec.edu.lexus.facebookrecipies.recipelist.ui.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.SendButton;
import com.facebook.share.widget.ShareButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.edu.lexus.facebookrecipies.R;
import ec.edu.lexus.facebookrecipies.entities.Recipe;
import ec.edu.lexus.facebookrecipies.lib.base.ImageLoader;

/**
 * Created by Alexis on 11/11/2017.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private List<Recipe> recipeList;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;

    public RecipesAdapter(List<Recipe> recipeList, ImageLoader imageLoader, OnItemClickListener onItemClickListener) {
        this.recipeList = recipeList;
        this.imageLoader = imageLoader;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_stored_recipes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        imageLoader.load(holder.imgRecipe,currentRecipe.getImageURL());
        holder.txtRecipeName.setText(currentRecipe.getTitle());
        holder.imgFav.setTag(currentRecipe.isFavorite());//perimite poner un objeto asociado a un lemento, en este caso el elemento es el bot'on de favorito, con esto voy a ver a la hora de hacer la prueba eque valor tiene el HOLDER
        if (currentRecipe.isFavorite()){
            holder.imgFav.setImageResource(android.R.drawable.btn_star_big_on);
        }else   holder.imgFav.setImageResource(android.R.drawable.btn_star_big_off);

        holder.setOnItemClickListener(currentRecipe,onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void setRecipes(List<Recipe> recipe) {
        this.recipeList = recipe;
        notifyDataSetChanged();
    }

    public void removeRecipe(Recipe recipe) {
        recipeList.remove(recipe);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgRecipe)
        ImageView imgRecipe;
        @BindView(R.id.txtRecipeName)
        TextView txtRecipeName;
        @BindView(R.id.imgFav)
        ImageButton imgFav;
        @BindView(R.id.imgDelete)
        ImageButton imgDelete;
        @BindView(R.id.fbShare)
        ShareButton fbShare;
        @BindView(R.id.fbSend)
        SendButton fbSend;

        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            ButterKnife.bind(this,view);
        }

        public void setOnItemClickListener(final Recipe currentRecipe, final OnItemClickListener onItemClickListener) {
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(currentRecipe);
                }
            });
            imgFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(currentRecipe);
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onDeleteClick(currentRecipe);
                }
            });

            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(currentRecipe.getSourceURL()))
                    //.setQuote()
                    .build();
            fbShare.setShareContent(content);
            fbSend.setShareContent(content);
        }




    }
}
