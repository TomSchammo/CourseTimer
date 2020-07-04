package com.tomschammo.coursetimer.HelperClasses;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tomschammo.coursetimer.R;
import com.tomschammo.coursetimer.UserEvents.Session;


import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Adapter for the Recycler View. <br>
 *
 * Handles the list items for the history list specifically and provides functionality for each item in the list. <br>
 *
 * Check out {@link com.tomschammo.coursetimer.Fragments.HistoryFragment} for functionality about the list in general.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    @NonNull
    private ArrayList<Session> dataSet;

    private SQLHelper sqlHelper;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView clientName;
        TextView sessionDate;
        TextView moneyEarned;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv);
            clientName = itemView.findViewById(R.id.client_name);
            sessionDate = itemView.findViewById(R.id.session_date);
            moneyEarned = itemView.findViewById(R.id.money_earned);

            itemView.setOnClickListener(view -> {
                /*
                    This does nothing for now, maybe adding new features in future versions,
                    like opening person in contacts, or opening a view that allows to add and
                    display more information about that person, like phone number, etc.

                    Could also sync information with contacts...
                 */
            });
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View t = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view, viewGroup, false);

        sqlHelper = new SQLHelper(viewGroup.getContext());

        return new ViewHolder(t);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.moneyEarned.setText((dataSet.get(i).getEarnings() + sqlHelper.getCurrency()));
        viewHolder.sessionDate.setText(dataSet.get(i).getDate());
        viewHolder.clientName.setText(dataSet.get(i).getClient());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
