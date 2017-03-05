package mobi.tarantino.stub.auto.feature.dashboard.services.fuelInfoCard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

    private List<FuelInfo> data = new ArrayList<>();

    private Context context;

    public FuelInfoListAdapter(Context context) {
        this.context = context;
    }

    private static float parseCost(String cost) {
        return Float.parseFloat(cost.replace(',', '.'));
    }

    @Override
    public FuelInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.services_fuel_info_card_list_item, parent, false);
        return new FuelInfoViewHolder(v);
    }

    public void setData(List<FuelInfo> data) {
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
    public void onBindViewHolder(FuelInfoViewHolder holder, int position) {
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

    private void colorizeHolder(FuelInfoViewHolder holder, boolean showArrow, int color, int
            trendImageRes,
                                float fuelCostDifferent) {
        holder.trendImage.setVisibility(showArrow ? View.VISIBLE : View.GONE);
        holder.trendImage.setImageResource(trendImageRes);
        holder.costDifferent.setTextColor(color);
        holder.currentCost.setTextColor(color);
        holder.trendImage.setColorFilter(color);
        holder.costDifferent.setText(String.format("%04.2f", Math.abs(fuelCostDifferent)));
        holder.costDifferent.setTextColor(color);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FuelInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date_textView)
        TextView date;

        @BindView(R.id.current_cost_textView)
        TextView currentCost;

        @BindView(R.id.cost_different_textView)
        TextView costDifferent;

        @BindView(R.id.trend_imageView)
        ImageView trendImage;

        public FuelInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
