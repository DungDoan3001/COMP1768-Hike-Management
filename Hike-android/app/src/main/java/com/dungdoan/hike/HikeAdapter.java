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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HolderHike> {
    private Context context;
    private ArrayList<HikeModel> hikeArrayList;
    DbHelper dbHelper;

    public HikeAdapter(Context context, ArrayList<HikeModel> hikeArrayList) {
        this.context = context;
        this.hikeArrayList = hikeArrayList;

        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public HolderHike onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hike_data, parent, false);

        return new HolderHike(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderHike holder, int position) {
        HikeModel hikeModel = hikeArrayList.get(position);
        String id = hikeModel.getID();
        String name = hikeModel.getName();
        String location = hikeModel.getLocation();
        String date = hikeModel.getDate();
        String length = hikeModel.getLength();
        String parking = hikeModel.getParking();
        String level = hikeModel.getLevel();
        String description = hikeModel.getDescription();
        String image = hikeModel.getImage();
        String createdAt = hikeModel.getCreated_At();
        String updatedAd = hikeModel.getUpdated_At();

        holder.hikeName.setText(name);
        holder.location.setText(location);
        holder.date.setText(date);
        holder.level.setText(level);

        if(level.trim().toLowerCase().equals("easy")) {
            holder.level.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else if(level.trim().toLowerCase().equals("intermediate")) {
            holder.level.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        } else if(level.trim().toLowerCase().equals("hard")) {
            holder.level.setTextColor(ContextCompat.getColor(context, R.color.red));
        };
        holder.level.setText(level);

        if(image.toString().equals("null")) {
            holder.imageView.setImageResource(R.drawable.image_vector);
        } else {
            holder.imageView.setImageURI(Uri.parse(image));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, hike_details.class);
                intent.putExtra("HIKE_ID", id);
                context.startActivity(intent);
            }
        });

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(id, name, location, date, length, parking, level, description, image, createdAt, updatedAd);
            }
        });
    }

    private void showMoreDialog(String ID,
                                String name,
                                String location,
                                String date,
                                String length,
                                String parking,
                                String level,
                                String description,
                                String image,
                                String createdAt,
                                String updatedAt) {
        String[] options = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    Intent intent = new Intent(context, hike_add_and_update.class);

                    intent.putExtra("ID", ID);
                    intent.putExtra("NAME", name);
                    intent.putExtra("LOCATION", location);
                    intent.putExtra("DATE", date);
                    intent.putExtra("LENGTH", length);
                    intent.putExtra("PARKING", parking);
                    intent.putExtra("LEVEL", level);
                    intent.putExtra("DESCRIPTION", description);
                    intent.putExtra("IMAGE", image);
                    intent.putExtra("CREATED_AT", createdAt);
                    intent.putExtra("UPDATED_AT", updatedAt);
                    intent.putExtra("IS_EDIT_MODE", true);

                    context.startActivity(intent);
                } else if (which == 1) {
                    dbHelper.deleteHike(ID);
                    Toast.makeText(context, String.format("Delete hike: %s successfully!", name), Toast.LENGTH_SHORT).show();
                    ((MainActivity)context).onResume();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return hikeArrayList.size();
    }

    class HolderHike extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView hikeName,location, date, level;
        ImageButton moreBtn;

        public HolderHike(@NonNull View itemView) {
            super(itemView);

            hikeName = itemView.findViewById(R.id.hike_item_name);
            location = itemView.findViewById(R.id.hike_item_location);
            date = itemView.findViewById(R.id.hike_item_date);
            level = itemView.findViewById(R.id.hike_item_level);
            moreBtn = itemView.findViewById(R.id.hike_item_more_btn);
            imageView = itemView.findViewById(R.id.hike_item_image);
        }
    }
}

