package com.example.project.TaskActivities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.project.R;
import com.example.project.TaskModel.Task;
import com.example.project.TaskModel.TaskManager;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private TaskManager taskManager = TaskManager.getInstance();
    private ArrayList<Task> taskList = taskManager.getTaskArrayList();
    private ArrayList<String> taskListName = new ArrayList<>();
    private ArrayList<String> taskListChildAssigned = new ArrayList<>();

    private static final String TAG = "RecycleAdapterClass";
    private Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    //the following functions recycles the viewholder and puts the items into the positions that they need to be put into
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setTaskDetails(){
        for(Task t:taskList){
            taskListName.add(t.getTaskName());
            taskListChildAssigned.add(String.valueOf(t.getTheAssignedChildId()));
            Log.d(TAG, "Task name is: "+taskListName);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        setTaskDetails();
        Log.d(TAG, "onBindViewHolder: called"); //helps us identify where we failed, if we ever do in the process

        //Glide.with(context).asBitmap().load(lensIcons.get(position)).into(holder.icon); Icon here
        holder.taskName.setText(taskListName.get(position));
        holder.taskAssigned.setText("Presently assigned to "+taskListChildAssigned.get(position));

        /*holder.parentLayout.setOnClickListener(v -> {
            Context viewContext = v.getContext();
                Intent calculateLensData = CalculateLensValues.makeLaunchIntent(viewContext, manager.returnLensFromIndex(position).getLensName(), manager.returnLensFromIndex(position).getAperture(), manager.returnLensFromIndex(position).getFocalLength());
                context.startActivity(calculateLensData);

        });*/
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Size is: "+taskManager.getTaskLength());
        return taskManager.getTaskLength();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView childIcon;
        TextView taskName;
        TextView taskAssigned;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            childIcon = itemView.findViewById(R.id.childIcon);
            taskName = itemView.findViewById(R.id.taskName);
            taskAssigned = itemView.findViewById(R.id.taskAssigned);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }

    }
}
