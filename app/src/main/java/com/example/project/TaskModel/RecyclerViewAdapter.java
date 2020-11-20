package com.example.project.TaskModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.project.ChildModel.ChildManager;
import com.example.project.R;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private TaskManager taskManager = TaskManager.getInstance();
    private ChildManager childManager = ChildManager.getInstance();
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


        //int avatarId = childManager.getChildAvatarId(position);
        //imageView.setImageResource(avatarId);

        //use the icon of the child responsible here   Glide.with(context).asBitmap().load(lensIcons.get(position)).into(holder.icon);

        holder.childIcon.setImageResource(taskList.get(position).getAvatarId());
        holder.taskName.setText(taskListName.get(position));
        holder.taskAssigned.setText("Presently assigned to "+taskListChildAssigned.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context viewContext = v.getContext();
                //Intent editTaskData = EditTaskActivity.makeLaunchIntent(viewContext, position);
                //context.startActivity(editTaskData);
                //Start the expand task activity here ^ and have a button which allows you to edit the activity.
            }
        });
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
