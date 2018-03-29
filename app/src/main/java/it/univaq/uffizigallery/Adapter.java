package it.univaq.uffizigallery;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Riccardo on 23/03/2018.
 */


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private JSONArray data;

    public Adapter(JSONArray data){
        this.data = data;
        if(this.data == null) this.data = new JSONArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_checkpoint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        JSONObject item = data.optJSONObject(position);
        if(item == null) return;

        holder.title.setText(item.optString("nome", "- - -"));
        //holder.subtitle.setText(item.optString("tipo", "- - -"));
        holder.subtitle.setText("Tipo: " + item.optString("tipo", "- - -"));

    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, subtitle;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adapter_title);
            subtitle = itemView.findViewById(R.id.adapter_subtitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            JSONObject checkpoint = data.optJSONObject(getAdapterPosition());
            Intent intent = new Intent(v.getContext(), CheckpointHubActivity.class);
            intent.putExtra("checkpoint", checkpoint.toString());
            v.getContext().startActivity(intent);
        }
    }
}
