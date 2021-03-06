package com.bs.demo.myapplication.ui.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.demo.myapplication.R;
import com.bs.demo.myapplication.bean.Lsinfo;
import com.bs.demo.myapplication.bean.SrInfo;
import com.bs.demo.myapplication.common.BaseActivity;
import com.bs.demo.myapplication.utils.GlobalValue;
import com.bs.demo.myapplication.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class ColActivity extends BaseActivity {

    private TextView tv_sr;

    private ColumnChartView columnChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_col);
        initView();
        setToolbarTitle("主要产品数量对比图");
        load("出库");


    }



    private void load(String type) {
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.addParams("bq",type);
        httpUtil.addParams("type","0");
        httpUtil.sendGetRequest("GetLS", new HttpUtil.Callback() {
            @Override
            public void onSuccess(String json) {
                List<Lsinfo> b = new Gson().fromJson(json, new TypeToken<List<Lsinfo>>() {
                }.getType());
                setChartViewData(columnChartView,b);

            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
    }

    private void initView() {
        columnChartView = (ColumnChartView) findViewById(R.id.lcv);
        tv_sr = (TextView) findViewById(R.id.tv_sr);

    }


    public  void setChartViewData(ColumnChartView chart, List<Lsinfo> b) {
        //底部标题
        List<String> title = new ArrayList<>();
        //颜色值
        List<Integer> color = new ArrayList<>();
        //X、Y轴值list
        List<AxisValue> axisXValues = new ArrayList<>();


        for (Lsinfo srInfo:b){
            title.add(srInfo.getKcInfo().getHh());
        }


        //颜色值
        color.add(Color.parseColor("#85B74F"));
        color.add(Color.parseColor("#ff0000"));

        //所有的柱子
        List<Column> columns = new ArrayList<>();


        //对每个集合的柱子进行遍历
        for (int i = 0; i < title.size(); i++) {


            //单个柱子
            Column column = new Column();
            List<SubcolumnValue> mPointValues = new ArrayList<>();
            //设置X轴的柱子所对应的属性名称(底部文字)
            axisXValues.add(new AxisValue(i).setLabel(title.get(i)));
            //显示几个小柱子 这里为3
            //值的大小、颜色
            mPointValues.add(new SubcolumnValue(Float.parseFloat(b.get(i).getSl()), color.get(0)));
            mPointValues.add(new SubcolumnValue(Float.parseFloat(b.get(i).getKcInfo().getSl()), color.get(1)));
            //将每个属性得列全部添加到List中
            //添加了7个大柱子Column,单个大柱子里面mPointValues大小为3（自行调整)
            //是否显示每个柱子的标签
            column.setHasLabels(true);
            //设置每个柱子的Lable是否选中，为false，表示不用选中，一直显示在柱子上
            column.setHasLabelsOnlyForSelected(false);

            column.setValues(mPointValues);

            columns.add(column);
        }


        //设置Columns添加到Data中
        ColumnChartData columnData = new ColumnChartData(columns);


        //底部
        Axis axisBottom = new Axis(axisXValues);
        //是否显示X轴的网格线
        axisBottom.setHasLines(false);
        //分割线颜色
        axisBottom.setLineColor(Color.parseColor("#ff0000"));
        //字体颜色
        axisBottom.setTextColor(Color.parseColor("#666666"));
        //字体大小
        axisBottom.setTextSize(10);
        //底部文字
        axisBottom.setName("主要产品出库数量");
        //每个柱子的便签是否倾斜着显示
        axisBottom.setHasTiltedLabels(true);
        //距离各标签之间的距离,包括离Y轴间距 (0-32之间)
        axisBottom.setMaxLabelChars(10);
        //设置是否自动生成轴对象,自动适应表格的范围(设置之后底部标题变成0-5)
        //axisBottom.setAutoGenerated(true);
        axisBottom.setHasSeparationLine(true);
        //设置x轴在底部显示
        columnData.setAxisXBottom(axisBottom);

        //左边  属性与上面一致
        Axis axisLeft = new Axis();
        axisLeft.setHasLines(false);
        axisLeft.setName("数量");
        axisLeft.setHasTiltedLabels(true);
        axisLeft.setTextColor(Color.parseColor("#666666"));
        columnData.setAxisYLeft(axisLeft);

        //设置数据标签的字体大小
        //data.setValueLabelTextSize(12);
        //设置数据标签的字体颜色
        //data.setValueLabelsTextColor(Color.WHITE);
        //设置数据背景是否跟随节点颜色
        //data.setValueLabelBackgroundAuto(true);
        //设置是否有数据背景  是否跟随columvalue的颜色变化
        // data.setValueLabelBackgroundEnabled(true);
        //设置坐标点旁边的文字背景(...)
        //data.setValueLabelBackgroundColor(Color.YELLOW);
        //axisBottom.setMaxLabelChars(5);
        //设置组与组之间的间隔比率,取值范围0-1,1表示组与组之间不留任何间隔
        columnData.setFillRatio(0.7f);
        chart.setInteractive(false);
        //最后将所有值显示在View中
        chart.setColumnChartData(columnData);
    }
}
