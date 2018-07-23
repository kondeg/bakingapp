package baking.kondeg.udacity.edu.app.fragments;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import baking.kondeg.udacity.edu.app.R;
import baking.kondeg.udacity.edu.app.data.PreparationInstruction;
import baking.kondeg.udacity.edu.app.fragments.DetailFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PreparationInstruction} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> {

    private final List<PreparationInstruction> mValues;
    private final ListItemClickListener mListener;
    View view;

    public DetailRecyclerViewAdapter(List<PreparationInstruction> items, ListItemClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(List<PreparationInstruction> steps, int clickedItemIndex);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_detail_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.stepDescription.setText(mValues.get(position).getShortDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListItemClick(mValues,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public final View mView;
        public final TextView stepDescription;
        public CardView cardView;
        PreparationInstruction mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            stepDescription = (TextView) view.findViewById(R.id.stepDescription);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mListener.onListItemClick(mValues,clickedPosition);
        }
    }
}
