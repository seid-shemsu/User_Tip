package com.izhar.usertip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        View view = LayoutInflater.from(context).inflate(R.layout.single_tip, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Game game = games.get(position);
        holder.home.setText(game.getHome());
        holder.away.setText(game.getAway());
        holder.tip.setText(game.getTip_type() + " : " + game.getTip());
        holder.date.setText(game.getDate());
        holder.league.setText(game.getLeague());
        holder.odd.setText("odd : " + game.getOdd());
        holder.time.setText(game.getTime());
        if (game.getStatus().equalsIgnoreCase("won")){
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.won));
        }
        else if (game.getStatus().equalsIgnoreCase("lost")){
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setRotation(45.0f);
            holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.lost));
        }
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView home, away, tip, date, league, odd, time;
        ImageView status;
        public Holder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            home = itemView.findViewById(R.id.home);
            away = itemView.findViewById(R.id.away);
            tip = itemView.findViewById(R.id.tip);
            date = itemView.findViewById(R.id.date);
            league = itemView.findViewById(R.id.league);
            odd = itemView.findViewById(R.id.odd);
            time = itemView.findViewById(R.id.time);
        }
    }
}
