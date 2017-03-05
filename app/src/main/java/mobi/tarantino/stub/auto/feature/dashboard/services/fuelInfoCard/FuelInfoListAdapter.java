package mobi.tarantino.stub.auto.feature.dashboard.services.fuelInfoCard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.model.additionalData.pojo.FuelPrices.FuelInfo;

import static mobi.tarantino.stub.auto.R.drawable.ic_arrow_down;
import static mobi.tarantino.stub.auto.R.drawable.ic_arrow_up;

/**

 */
public class FuelInfoListAdapter extends RecyclerView.Adapter<FuelInfoListAdapter
        .FuelInfoViewHolder> {

    @NonNull
    private List<FuelInfo> data = new ArrayList<>();

    private Context context;

    public FuelInfoListAdapter(Context context) {
        this.context = context;
    }

    private static float parseCost(@NonNull String cost) {
        return Float.parseFloat(cost.replace(',', '.'));
    }

    @NonNull
    @Override
    public FuelInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.services_fuel_info_card_list_item, parent, false);
        return new FuelInfoViewHolder(v);
    }

    public void setData(@NonNull List<FuelInfo> data) {
        removeData();
        this.data.addAll(data);
        if (data.size() > 0) {
            notifyItemRangeInserted(0, data.size());
        }
    }

    public void removeData() {
        if (data.size() > 0) {
            notifyItemRangeRemoved(0, data.size());
        }
        data.clear();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull FuelInfoViewHolder holder, int position) {
        FuelInfo fuelInfo = data.get(position);
        holder.date.setText(fuelInfo.getDate());
        Resources resources = context.getResources();


        holder.currentCost.setText(resources.getString(R.string.fuelPrice, parseCost(fuelInfo
                .getCost())));

        float fuelCostDifferent = parseCost(fuelInfo.getTrend());
        if (fuelCostDifferent > 0) {
            colorizeHolder(holder, true, ResourcesCompat.getColor(resources, R.color.pink, null),
                    ic_arrow_up, fuelCostDifferent);
        } else {
            if (fuelCostDifferent == 0) {
                colorizeHolder(holder, false, ResourcesCompat.getColor(resources, R.color.green,
                        null), ic_arrow_down, fuelCostDifferent);
            } else {
                colorizeHolder(holder, true, ResourcesCompat.getColor(resources, R.color.green,
                        null),
                        ic_arrow_down, fuelCostDifferent);
            }
        }
    }

    private void colorizeHolder(@NonNull FuelInfoViewHolder holder, boolean showArrow, int color,
                                int
                                        trendImageRes,
                                float fuelCostDifferent) {
        holder.trendImage.setVisibility(showArrow ? View.VISIBLE : View.GONE);
        holder.trendImage.setImageResource(trendImageRes);
        holder.costDifferent.setTextColor(color);
        holder.currentCost.setTextColor(color);
        holder.trendImage.setColorFilter(color);
        float abs = Math.abs(fuelCostDifferent);
        holder.costDifferent.setText(String.format(Locale.getDefault(), "%04.2f", abs));
        holder.costDifferent.setTextColor(color);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FuelInfoViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.date_textView)
        TextView date;

        @Nullable
        @BindView(R.id.current_cost_textView)
        TextView currentCost;

        @Nullable
        @BindView(R.id.cost_different_textView)
        TextView costDifferent;

        @Nullable
        @BindView(R.id.trend_imageView)
        ImageView trendImage;

        public FuelInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
