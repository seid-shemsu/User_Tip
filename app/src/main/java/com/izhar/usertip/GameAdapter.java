package com.izhar.usertip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.Holder> {
    Context context;
    List<Game> games;

    public GameAdapter(Context context, List<Game> games){
        this.context = context;
        this.games = games;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_game, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Game game = games.get(position);
        holder.home.setText(game.getHome());
        holder.away.setText(game.getAway());
        holder.status.setText(game.getStatus());
        holder.tip_type.setText(game.getTip_type());
        holder.tip.setText(game.getTip());
        holder.date.setText(game.getDate());
        holder.league.setText(game.getLeague());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView home, away, status, tip, tip_type, date, league;
        public Holder(@NonNull View itemView) {
            super(itemView);
            home = itemView.findViewById(R.id.home);
            away = itemView.findViewById(R.id.away);
            status = itemView.findViewById(R.id.status);
            tip = itemView.findViewById(R.id.tip);
            tip_type = itemView.findViewById(R.id.tip_type);
            date = itemView.findViewById(R.id.date);
            league = itemView.findViewById(R.id.league);
        }
    }
}
