package com.dungdoan.hike;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.HolderObservation> {
    private Context context;
    private ArrayList<ObservationModel> observationArrayList;
    DbHelper dbHelper;

    public ObservationAdapter(Context context, ArrayList<ObservationModel> observationArrayList) {
        this.context = context;
        this.observationArrayList = observationArrayList;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public HolderObservation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.observation_data, parent, false);
        return new HolderObservation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderObservation holder, int position) {
        ObservationModel model = observationArrayList.get(position);
        String id = model.getID();
        String name = model.getName();
        String time = model.getTime();
        String image = model.getImage();
        String comment = model.getComment();
        String createdAt = model.getCreated_At();
        String updatedAT = model.getUpdated_At();
        String hikeID = model.getHikeID();

        holder.name.setText(name);
        holder.time.setText(time);
        holder.comment.setText(comment);

        if(image.equals("null")) {
            holder.imageView.setImageResource(R.drawable.image_vector);
        } else {
            holder.imageView.setImageURI(Uri.parse(image));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, observation_details.class);
                intent.putExtra("OBSERVATION_ID", id);
                context.startActivity(intent);
            }
        });

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(id, name, comment, time, image, createdAt, updatedAT, hikeID);
            }
        });
    }

    private void showMoreDialog(String id,
                                String name,
                                String comment,
                                String time,
                                String image,
                                String createdAt,
                                String updatedAt,
                                String hikeID) {
        String[] options = {"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    Intent intent = new Intent(context, observation_add_and_update.class);
                    intent.putExtra("ID", id);
                    intent.putExtra("NAME", name);
                    intent.putExtra("COMMENT", comment);
                    intent.putExtra("TIME", time);
                    intent.putExtra("IMAGE", image);
                    intent.putExtra("CREATED_AT", createdAt);
                    intent.putExtra("UPDATED_AT", updatedAt);
                    intent.putExtra("HIKE_ID", hikeID);
                    intent.putExtra("IS_EDIT_MODE", true);
                    context.startActivity(intent);
                } else if (which == 1) {
                    dbHelper.deleteObservation(id);
                    Toast.makeText(context, "Delete Observation: " + name + "Successfully!", Toast.LENGTH_SHORT).show();
                    ((hike_details)context).onResume();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return observationArrayList.size();
    }

    class HolderObservation extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name, time , comment;
        ImageButton moreBtn;

        public HolderObservation(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.observation_name);
            time = itemView.findViewById(R.id.observation_time);
            comment = itemView.findViewById(R.id.observation_comment);
            imageView = itemView.findViewById(R.id.observation_input_image);
            moreBtn = itemView.findViewById(R.id.hike_item_more_btn);
        }
    }
}
