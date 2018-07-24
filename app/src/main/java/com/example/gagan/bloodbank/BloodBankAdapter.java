package com.example.gagan.bloodbank;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.BankViewHolder>  implements Filterable {

    private List<BloodBankItem> listItems;
    private List<BloodBankItem> mOriginalValues;
    private Context context;

    public BloodBankAdapter(List<BloodBankItem> listItems, Context context) {
        this.listItems = listItems;
        this.mOriginalValues = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public  BloodBankAdapter.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_bank_item, parent, false);
        return new BloodBankAdapter.BankViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BloodBankAdapter.BankViewHolder holder, final int position) {
        final BloodBankItem listItem = listItems.get(position);
        holder.bankName.setText(listItem.getBloodBankName());
        holder.bankCity.setText(listItem.getCity());
        holder.bankState.setText(listItem.getState());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.blood_bank_profile);
                dialog.setTitle("DETAILS");
                dialog.show();
                TextView name=dialog.findViewById(R.id.name);
                TextView address=dialog.findViewById(R.id.address);
                TextView state=dialog.findViewById(R.id.state);
                TextView district=dialog.findViewById(R.id.district);
                TextView city=dialog.findViewById(R.id.city);
                TextView pincode=dialog.findViewById(R.id.pincode);
                TextView mob=dialog.findViewById(R.id.contact);
                TextView helpline=dialog.findViewById(R.id.helpline);
                TextView email=dialog.findViewById(R.id.email);
                TextView website=dialog.findViewById(R.id.website);
                TextView servicetime=dialog.findViewById(R.id.servicetime);

                name.setText("Name: "+listItem.getBloodBankName());
                address.setText("Address: "+listItem.getAddress());
                state.setText("State: "+listItem.getState());
                district.setText("District: "+listItem.getDistrict());
                city.setText("City: "+listItem.getCity());
                pincode.setText("Pincode: "+listItem.getPincode());
                mob.setText("Contact Num: "+listItem.getContactno());
                helpline.setText("Helpline Num: "+listItem.getHelpline());
                email.setText("Email: "+listItem.getEmail());
                website.setText("Website: "+listItem.getWebsite());
                servicetime.setText("Service Time: "+listItem.getServicetime());

            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                try {
                    listItems = (ArrayList<BloodBankItem>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                try {
                    ArrayList<BloodBankItem> FilteredArrList = new ArrayList();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList(listItems); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String data = new Gson().toJson(mOriginalValues.get(i));
                            if (data.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(mOriginalValues.get(i));
                            }
                        }
                        // set the Filtered result to returnl
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return results;
            }
        };
        return filter;
    }


    public class BankViewHolder extends RecyclerView.ViewHolder
    {
        public TextView bankName;
        public TextView bankCity;
        public TextView bankState;
        LinearLayout linearLayout;

        public BankViewHolder(View itemView) {
            super(itemView);

            bankName= (TextView)itemView.findViewById(R.id.bank_name);
            bankCity=(TextView)itemView.findViewById(R.id.bank_city);
            bankState=(TextView)itemView.findViewById(R.id.bank_state);
            linearLayout = itemView.findViewById(R.id.bloodbank_linearlayout);

        }


    }}
