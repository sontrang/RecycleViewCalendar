package vuonuomit.task;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {

    RecyclerView recyclerView;
    MyRecyclerViewAdapter myRecyclerAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar calendar;
    String format = "MMM yyyy";
    SimpleDateFormat fm;
    TextView tvMonthYear;
    ItemViewClickListener listener;
    View mContentView;

    RecyclerView.OnScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        tvMonthYear = (TextView) findViewById(R.id.tvMonthYear);

        calendar = Calendar.getInstance();
        fm = new SimpleDateFormat(format);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        setDayDate(calendar);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDayDate(calendar);
            }
        };

        tvMonthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    void setTextMonthYear() {
        tvMonthYear.setText(fm.format(calendar.getTime()));
    }

    void setDayDate(final Calendar calendar) {

        myRecyclerAdapter = new MyRecyclerViewAdapter(calendar);

        recyclerView.setAdapter(myRecyclerAdapter);

        setTextMonthYear();
        ((LinearLayoutManager) mLayoutManager).scrollToPosition(myRecyclerAdapter.START);
        listener = new ItemViewClickListener() {
            @Override
            public void itemViewClicked(final View view, int position, long time) {
                setUpdateView(view);
                recyclerView.smoothScrollBy(-recyclerView.getLayoutManager().getWidth() / 2 + (view.getWidth() / 2 + view.getLeft()), 0);
//                ((LinearLayoutManager) mLayoutManager).scrollToPositionWithOffset(position, recyclerView.getLayoutManager().getWidth() / 2 - v.getWidth() / 2);
                tvMonthYear.setText(fm.format(time));
            }
        };
        myRecyclerAdapter.setListener(listener);


    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recycler, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                View view = findCenterView(lm);
                setUpdateView(view);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recycler, int newState) {
                super.onScrollStateChanged(recycler, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                    View view = findCenterView(lm);
                    recyclerView.smoothScrollBy((view.getWidth() / 2 + view.getLeft()) - recyclerView.getLayoutManager().getWidth() / 2, 0);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    private View findCenterView(LinearLayoutManager lm) {
        int minDistance = 0;
        View returnView = null, view;
        boolean notFound = true;

        for (int i = lm.findFirstVisibleItemPosition(); i <= lm.findLastVisibleItemPosition() && notFound; i++) {
            view = lm.findViewByPosition(i);
            if (view != null) {
                int center = (view.getLeft() + view.getRight()) / 2;

                int leastDifference = Math.abs(lm.getWidth() / 2 - center);
                if (leastDifference <= minDistance || i == lm.findFirstVisibleItemPosition()) {
                    MyRecyclerViewAdapter.DayDateHolder holder =
                            (MyRecyclerViewAdapter.DayDateHolder) recyclerView.findContainingViewHolder(view);
                    tvMonthYear.setText(fm.format(myRecyclerAdapter.getItem(holder.getAdapterPosition())));

                    minDistance = leastDifference;
                    returnView = view;
                } else {
                    notFound = false;
                }
            }
        }
        return returnView;
    }

    public interface ItemViewClickListener {
        void itemViewClicked(View v, int position, long time);
    }


    void updateView(View view, boolean isSelected) {
        if (view == null) {
            return;
        }
        View tvDayOfMonth = view.findViewById(R.id.tvDayOfMonth);
        if (isSelected && tvDayOfMonth instanceof TextView) {
            ((TextView) tvDayOfMonth).setTextAppearance(MainActivity.this, R.style.SelectedTextStyle);
        } else {
            ((TextView) tvDayOfMonth).setTextAppearance(MainActivity.this, R.style.PrimaryTextStyle);
        }

    }

    public void setUpdateView(View v) {
        updateView(mContentView, false);
        mContentView = v;
        updateView(mContentView, true);
    }


}
