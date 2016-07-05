package vuonuomit.task;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DayDateHolder> {

    static int MAX_VALUE = 331;
    static int START = 301;
    Calendar calendar;

    MainActivity.ItemViewClickListener listener;


    public MyRecyclerViewAdapter(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setListener(MainActivity.ItemViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public DayDateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_date_item, null);
        return new DayDateHolder(v);
    }

    @Override
    public void onBindViewHolder(final DayDateHolder holder, final int position) {

        holder.tvDayOfMonth.setTextColor(Color.GRAY);
        holder.tvDayOfMonth.setTextSize(22);
        if (position == START) {
            listener.itemViewClicked(holder.itemView, position, getItem(position));
        }
        Calendar itemCalendar = getCalendar(position);
        holder.tvDayOfMonth.setText("" + itemCalendar.get(itemCalendar.DAY_OF_MONTH));
        if (equals(itemCalendar, Calendar.getInstance())) {
            holder.tvDayOfWeek.setText("Today");
        } else {
            holder.tvDayOfWeek.setText(itemCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US));
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public long getItem(int position) {
        return getCalendar(position).getTimeInMillis();
    }

    @Override
    public int getItemCount() {
        return MAX_VALUE;
    }

    private boolean equals(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private Calendar getCalendar(int position) {
        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DAY_OF_YEAR, position - START);
        return cal;
    }

    /*
              -----------------------------------------
   */
    public class DayDateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDayOfMonth, tvDayOfWeek;


        public DayDateHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setSelected(false);
            itemView.setClickable(true);

            tvDayOfMonth = (TextView) itemView.findViewById(R.id.tvDayOfMonth);
            tvDayOfWeek = (TextView) itemView.findViewById(R.id.tvDayOfWeek);
        }

        @Override
        public void onClick(View v) {
            listener.itemViewClicked(v, this.getLayoutPosition(), getItem(this.getLayoutPosition()));
        }


    }
}
