package com.example.l999;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_statistics);

        int AugTrainings = getIntent().getIntExtra("AugTrainings", 0);
        int SeptTrainings = getIntent().getIntExtra("SeptTrainings", 0);
        int OctTrainings = getIntent().getIntExtra("OctTrainings", 0);
        int NovTrainings = getIntent().getIntExtra("NovTrainings", 0);
        int DecTrainings = getIntent().getIntExtra("DecTrainings", 0);
        int IanTrainings = getIntent().getIntExtra("IanTrainings", 0);
        int FebTrainings = getIntent().getIntExtra("FebTrainings", 0);
        int MarTrainings = getIntent().getIntExtra("MarTrainings", 0);
        int AprTrainings = getIntent().getIntExtra("AprTrainings", 0);
        int MayTrainings = getIntent().getIntExtra("MayTrainings", 0);
        int IunTrainings = getIntent().getIntExtra("IunTrainings", 0);
        int IulTrainings = getIntent().getIntExtra("IulTrainings", 0);


        BarChart chart = findViewById(R.id.barChart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, IanTrainings));
        entries.add(new BarEntry(1, FebTrainings));
        entries.add(new BarEntry(2, MarTrainings));
        entries.add(new BarEntry(3, AprTrainings));
        entries.add(new BarEntry(4, MayTrainings));
        entries.add(new BarEntry(5, IunTrainings));
        entries.add(new BarEntry(6, IulTrainings));
        entries.add(new BarEntry(7, AugTrainings));
        entries.add(new BarEntry(8, SeptTrainings));
        entries.add(new BarEntry(9, OctTrainings));
        entries.add(new BarEntry(10, NovTrainings));
        entries.add(new BarEntry(11, DecTrainings));


        int[] topIndices = new int[3];
        int[] topValues = new int[3];
        Arrays.fill(topValues, Integer.MIN_VALUE);

        for (int i = 0; i < entries.size(); i++) {
            int value = (int) entries.get(i).getY();

            for (int j = 0; j < topValues.length; j++) {
                if (value > topValues[j]) {
                    for (int k = topValues.length - 1; k > j; k--) {
                        topValues[k] = topValues[k - 1];
                        topIndices[k] = topIndices[k - 1];
                    }
                    topValues[j] = value;
                    topIndices[j] = i;
                    break;
                }
            }
        }

        int largestIndex = topIndices[0];

        BarDataSet dataSet = new BarDataSet(entries, "Total antrenamente");
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            if (i == largestIndex) {
                colors.add(Color.RED);
            } else {
                int indexInTop = Arrays.binarySearch(topIndices, i);
                if (indexInTop >= 0) {
                    if (indexInTop == 1) {
                        colors.add(Color.YELLOW);
                    } else if (indexInTop == 2) {
                        colors.add(Color.GREEN);
                    }
                } else {
                    colors.add(Color.BLUE);
                }
            }
        }
        dataSet.setColors(colors);

        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(entries.size());
        xAxis.setLabelRotationAngle(45);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonths()));

        chart.invalidate();
    }

    private List<String> getMonths() {
        List<String> months = new ArrayList<>();
        months.add("AUG");
        months.add("SEPT");
        months.add("OCT");
        months.add("NOI");
        months.add("DEC");
        months.add("IAN");
        months.add("FEB");
        months.add("MAR");
        months.add("APR");
        months.add("MAI");
        months.add("IUN");
        months.add("IUL");

        return months;
    }
}