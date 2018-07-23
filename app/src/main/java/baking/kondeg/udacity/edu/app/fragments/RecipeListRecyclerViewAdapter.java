package baking.kondeg.udacity.edu.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import baking.kondeg.udacity.edu.app.R;
import baking.kondeg.udacity.edu.app.activities.DetailActivity;
import baking.kondeg.udacity.edu.app.data.PreparationInstruction;
import baking.kondeg.udacity.edu.app.data.Recipe;
import baking.kondeg.udacity.edu.app.fragments.RecipeListFragment.OnListFragmentInteractionListener;
import baking.kondeg.udacity.edu.app.utils.EndpointValues;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Recipe} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RecipeListRecyclerViewAdapter extends RecyclerView.Adapter<RecipeListRecyclerViewAdapter.MyViewHolder> {

    private final List<Recipe> mValues;
    //private final OnItemClickListener mListener;
    private final String LOG_TAG = RecipeListRecyclerViewAdapter.class.getSimpleName();


    public RecipeListRecyclerViewAdapter(List<Recipe> items) {
        mValues = items;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_recipe, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        //viewHolder.onItemClickListener = mListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Recipe recipe = mValues.get(position);
        Context context = holder.itemView.getContext();
        holder.titleTextView.setText(recipe.getName());
        if (holder.photoImageView!=null && !recipe.getImage().isEmpty()) {
            Picasso.with(context).load(recipe.getImage()).into(holder.photoImageView);
        }
        holder.recipe = recipe;

        //holder.viewButton.setTag(recipe);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;
        public TextView titleTextView;
        public ImageView photoImageView;
        public CardView cardView;
        public Recipe recipe;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            cardView = (CardView) view.findViewById(R.id.cardView);
            titleTextView = (TextView) view.findViewById(R.id.title);
            photoImageView = (ImageView) view.findViewById(R.id.photoImageView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mView.getContext(), DetailActivity.class);
                    intent.putExtra(EndpointValues.selectedRecipe, recipe);
                    mView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mView.getContext(), (recipe.getName()), Toast.LENGTH_SHORT).show();
        }
    }

}
