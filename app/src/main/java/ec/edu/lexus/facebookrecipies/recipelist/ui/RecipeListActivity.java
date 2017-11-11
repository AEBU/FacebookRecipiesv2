package ec.edu.lexus.facebookrecipies.recipelist.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.edu.lexus.facebookrecipies.R;
import ec.edu.lexus.facebookrecipies.recipelist.RecipeListPresenter;

public class RecipeListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    //RecipesAdapter adapter;
    RecipeListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        setupToolbar();
        setupRecyclerView();
        setupInjection();
        
    }

    private void setupInjection() {
    }

    private void setupRecyclerView() {
    }

    private void setupToolbar() {
    }
}
