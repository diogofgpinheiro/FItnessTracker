package com.diogo.fitnesstracker.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.diogo.fitnesstracker.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Alimento extends AppCompatActivity {

    private PieChart graficoAlimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimento);

        graficoAlimento = findViewById(R.id.graficoAlimento);

        graficoAlimento.setUsePercentValues(true);
        graficoAlimento.setRotationEnabled(false);
        graficoAlimento.getDescription().setEnabled(false);
        graficoAlimento.setExtraOffsets(0,10,0,5);

        graficoAlimento.setDragDecelerationEnabled(false);
        graficoAlimento.setCenterText("1000\ncal");
        graficoAlimento.setCenterTextSize(12f);
        graficoAlimento.setDrawHoleEnabled(true);
        graficoAlimento.setHoleColor(Color.WHITE);
        graficoAlimento.setHoleRadius(60f);
        graficoAlimento.setEntryLabelColor(this.getResources().getColor(R.color.colorPrimary));
        graficoAlimento.setDrawEntryLabels(false);
        graficoAlimento.getLegend().setEnabled(false);

        ArrayList<PieEntry> valoresGrafico = new ArrayList<>();

        valoresGrafico.add(new PieEntry(48,"Carboidratos"));
        valoresGrafico.add(new PieEntry(27,"Proteinas"));
        valoresGrafico.add(new PieEntry(25,"Gorduras"));

        PieDataSet dataSet = new PieDataSet(valoresGrafico,"g dos macronutrientes");
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        graficoAlimento.setData(data);
    }
}
