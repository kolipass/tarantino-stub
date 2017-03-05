package mobi.tarantino.stub.auto.feature.dashboard.driverAssistance;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.analytics.Reporter;
import mobi.tarantino.stub.auto.model.additionalData.pojo.DriverAssistanceInfo;

/**

 */
public class DriverAssistanceInfoAdapter extends RecyclerView.Adapter<DriverAssistanceInfoAdapter
        .DriverAssistanceViewHolder> {

    private Context context;
    private List<DriverAssistanceInfo> data = new ArrayList<>();
    private AnalyticReporter analyticReporter;

    public DriverAssistanceInfoAdapter(Context context) {
        this.context = context;
        initAnalyticReporter();
    }

    private void initAnalyticReporter() {
        analyticReporter = MobiApplication.get(context)
                .getComponentContainer()
                .getAnalyticComponent()
                .provideAnalyticReporter();
    }

    @Override
    public DriverAssistanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout
                .drivers_assistance_info_item, parent, false);
        DriverAssistanceViewHolder viewHolder = new DriverAssistanceViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<DriverAssistanceInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DriverAssistanceViewHolder holder, int position) {
        DriverAssistanceInfo info = data.get(position);
        holder.phone.setText(info.getPhoneNumber());
        holder.title.setText(info.getTitle());
    }

    public class DriverAssistanceViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        TextView title;
        TextView phone;

        public DriverAssistanceViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.title_textView);
            phone = (TextView) itemView.findViewById(R.id.phone_textView);
        }

        @Override
        public void onClick(View v) {
            DriverAssistanceInfo info = data.get(getAdapterPosition());
            Uri phoneData = Uri.parse("tel:" + info.getPhoneNumber());
            Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL, phoneData);
            context.startActivity(phoneCallIntent);
            analyticReporter.phoneCallEvent(Reporter.SCREEN_DRIVER_ASSISTANCE, info.getTitle());
        }
    }
}
