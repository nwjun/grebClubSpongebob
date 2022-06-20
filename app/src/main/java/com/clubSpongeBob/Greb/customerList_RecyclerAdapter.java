package com.clubSpongeBob.Greb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;

//public class customerList_RecyclerAdapter extends FirebaseRecyclerAdapter<Customer, customerList_RecyclerAdapter.MyViewHolder> {
//    private Context context;
//    private ArrayList<Customer> cusList ;
//    public customerList_RecyclerAdapter(
//            @NonNull FirebaseRecyclerOptions<Customer> options, Context context, ArrayList<Customer> cusList)
//    {
//        super(options);
//        this.context = context;
//        this.cusList = cusList;
//    }
//
//
//    // Function to bind the view in Card view(here
//    // "person.xml") iwth data in
//    // model class(here "person.class")
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Customer model) {
//        holder.name.setText(model.getName());
//        holder.capacity.setText(model.getCapacity());
//        holder.eat.setText(model.getEat());
//        holder.start.setText(model.getLocation());
//        holder.destination.setText(model.getDestination());
//        int status = model.getStatus();
//        switch(status){
//            case 1:
//                holder.customerStatus.setImageResource(R.drawable.ic_baseline_incomplete_circle_24);
//                break;
//            case 2:
//                holder.customerStatus.setImageResource(R.drawable.ic_baseline_access_time_filled_24);
//                break;
//            case 3:
//                holder.customerStatus.setImageResource(R.drawable.ic_baseline_emoji_transportation_24);
//                break;
//            case 4:
//                holder.customerStatus.setImageResource(R.drawable.ic_baseline_check_circle_24);
//                break;
//            default:
//        }
//    }
//
//
//    // Function to tell the class about the Card view (here
//    // "person.xml")in
//    // which the data will be shown
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.customer_list,parent,false);
//        return new MyViewHolder(view) ;
//    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        ImageView customerStatus;
//        TextView name,capacity,eat,start,destination;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            customerStatus = itemView.findViewById((R.id.customerStatus));
//            name = itemView.findViewById((R.id.driverName));
//            capacity= itemView.findViewById((R.id.capacity));
//            eat = itemView.findViewById((R.id.eat));
//            start = itemView.findViewById((R.id.startingPoint));
//            destination = itemView.findViewById((R.id.destination));
//        }
//    }
//}

public class customerList_RecyclerAdapter extends RecyclerView.Adapter<customerList_RecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Customer> cusList ;

    public customerList_RecyclerAdapter(Context context, ArrayList<Customer> cusList){
        this.context = context;
        this.cusList = cusList;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.customer_list,parent,false);
        return new MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Customer model = cusList.get(position);
        holder.name.setText("Name: " +model.getName());
        holder.capacity.setText("Capacity: "+String.valueOf(model.getCapacity()));
        holder.eat.setText("Estimated Arrival Time: "+model.getEat());
        holder.start.setText("Starting point: "+model.getLocation());
        holder.destination.setText("Destination: "+model.getDestination());
        int status = model.getStatus();

        switch(status){
            case 1:
                holder.customerStatus.setImageResource(R.drawable.ic_baseline_incomplete_circle_24);
                break;
            case 2:
                holder.customerStatus.setImageResource(R.drawable.ic_baseline_access_time_filled_24);
                break;
            case 3:
                holder.customerStatus.setImageResource(R.drawable.ic_baseline_emoji_transportation_24);
                break;
            case 4:
                holder.customerStatus.setImageResource(R.drawable.ic_baseline_check_circle_24);
                break;
            default:
        }
    }

    @Override
    public int getItemCount() {
        System.out.println(cusList.size()+" hifaobaobocabfoa");
        return cusList.size();
    }

    public void setData(List<Customer> list) {
        if (cusList != null) cusList.clear();
        if (cusList == null) cusList = new ArrayList<>();
        cusList.addAll(list);
        notifyDataSetChanged();

    }

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView customerStatus;
    TextView name,capacity,eat,start,destination;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        customerStatus = itemView.findViewById((R.id.customerStatus));
        name = itemView.findViewById((R.id.driverName));
        capacity= itemView.findViewById((R.id.capacity));
        eat = itemView.findViewById((R.id.eat));
        start = itemView.findViewById((R.id.startingPoint));
        destination = itemView.findViewById((R.id.destination));

    }
}


}