package com.developer.cryptokotlin.ui.fragments.assetDetails

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.developer.cryptokotlin.MyApplication
import com.developer.cryptokotlin.R
import com.developer.cryptokotlin.connect.models.AssetObject
import com.developer.cryptokotlin.connect.response.TimeSeriesResponse
import com.developer.cryptokotlin.connect.services.ApiService
import com.developer.cryptokotlin.databinding.FragmentAssetDetailsBinding
import com.developer.cryptokotlin.utils.Constants
import com.developer.cryptokotlin.utils.Utils
import com.developer.cryptokotlin.utils.customViews.MyMarkerView
import com.developer.cryptokotlin.utils.customViews.TitleTextView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import javax.inject.Inject
import kotlin.collections.ArrayList

class AssetDetailsFragment : Fragment(), OnChartValueSelectedListener {

    @Inject
    lateinit var apiService: ApiService

    private var asset: AssetObject? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentAssetDetailsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_asset_details,
            container,
            false
        )
        initView(binding)
        return binding.root
    }

    private fun initView(binding: FragmentAssetDetailsBinding) {
        asset = arguments?.getSerializable("asset") as AssetObject?

        initMarketData(binding)
        initOfficialLinks(binding)
        initProfile(binding)
        initChart(binding)

        iniViewModel(binding);
    }

    private fun iniViewModel(binding: FragmentAssetDetailsBinding) {

        val viewModelFactory = AssetDetailsViewModelFactory(
            apiService,
            asset?.symbol!!, Utils.getToday(), Utils.getBeginDate(Utils.getToday())
        )
        val assetDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(
            AssetDetailsViewModel::class.java
        )

        binding.assetDetailsViewModel = assetDetailsViewModel

        assetDetailsViewModel.data.observe(viewLifecycleOwner, Observer {

            if (null != it) {
                initTimeSeries(it, binding)
            }
        })
    }

    private fun initChart(binding: FragmentAssetDetailsBinding) {
        binding.lineChart.description.isEnabled = false
        binding.lineChart.setTouchEnabled(true)
        binding.lineChart.setOnChartValueSelectedListener(this)
        binding.lineChart.setDrawGridBackground(false)
        binding.lineChart.axisLeft.setDrawGridLines(false)
        binding.lineChart.xAxis.setDrawGridLines(false)
        val mv = MyMarkerView(requireContext(), R.layout.custom_marker_view)
        mv.chartView = binding.lineChart
        binding.lineChart.marker = mv
        binding.lineChart.isDragEnabled = true
        binding.lineChart.setScaleEnabled(true)
        binding.lineChart.setPinchZoom(true)
        binding.lineChart.axisRight.isEnabled = false
    }

    private fun initProfile(binding: FragmentAssetDetailsBinding) {
        binding.textViewOverview.text = Utils.fromHtml(
            asset!!.profile!!.general!!.overview!!.project_details
        )
        binding.textViewTagLine.text = Utils.fromHtml(
            asset!!.profile!!.general!!.overview!!.tagline
        )
    }

    @SuppressLint("SetTextI18n")
    private fun initMarketData(binding: FragmentAssetDetailsBinding) {
        binding.textViewName.text = asset!!.name
        binding.textViewPrice.text =
            "$" + Utils.formatBalance(asset!!.metrics!!.market_data!!.price_usd)
        binding.textViewStatus.text =
            Utils.formatBalance(asset!!.metrics!!.market_data!!.percent_change_usd_last_24_hours) + "%"
        Glide.with(requireContext()).load(Constants.ASSET_IMAGES + asset!!.id + "/128.png").into(
            binding.imageView
        )

        if (asset!!.metrics!!.market_data!!.percent_change_usd_last_24_hours > 0) {
            binding.textViewStatus.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.profit_color
                )
            )
        } else {
            binding.textViewStatus.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
        }
    }

    private fun initOfficialLinks(binding: FragmentAssetDetailsBinding) {
        if (null != asset!!.profile!!.general!!.overview!!.official_links) {
            for (i in asset!!.profile!!.general!!.overview!!.official_links!!.indices) {
                val name: String = asset!!.profile!!.general!!.overview!!.official_links!![i].name!!
                val link: String = asset!!.profile!!.general!!.overview!!.official_links!![i].link!!
                val title = "<a href='$link'>$name</a>"
                binding.linearLayoutLinks.addView(TitleTextView(title, null, requireContext()))
            }
        }
    }

    private fun initTimeSeries(data: TimeSeriesResponse, binding: FragmentAssetDetailsBinding) {
        setData(data.values, binding)
        binding.lineChart.animateX(500)
        binding.lineChart.legend.isEnabled = false
        val xAxis: XAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(getDate(data.values!!))
    }

    private fun getDate(values: List<List<Float>>): ArrayList<String> {
        var label = ArrayList<String>()

        for (i in values.indices) {
            label.add(Utils.parseEpoch(values[i][0].toLong(), "dd-MM"));

        }

        return label
    }

    private fun setData(timeValues: List<List<Float>>?, binding: FragmentAssetDetailsBinding) {
        val values = ArrayList<Entry>()
        for (i in timeValues!!.indices) {
            val `val` = timeValues[i][1]
            values.add(Entry(i.toFloat(), `val`))
        }
        val set1: LineDataSet
        if (binding.lineChart.data != null &&
            binding.lineChart.data.dataSetCount > 0
        ) {
            set1 = binding.lineChart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.setDrawCircles(false)
            set1.setDrawValues(false)
            set1.notifyDataSetChanged()
            set1.axisDependency = YAxis.AxisDependency.RIGHT
            binding.lineChart.data.notifyDataChanged()
            binding.lineChart.notifyDataSetChanged()
        } else {
            set1 = LineDataSet(values, "")
            set1.axisDependency = YAxis.AxisDependency.RIGHT
            set1.setDrawCircles(false)
            set1.setDrawValues(false)
            set1.setDrawIcons(false)
            set1.setDrawCircleHole(false)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            set1.valueTextSize = 9f
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.setDrawFilled(true)
            set1.color = ContextCompat.getColor(requireContext(), R.color.white)
            set1.fillFormatter =
                IFillFormatter { _: ILineDataSet?, _: LineDataProvider? ->
                    binding.lineChart.axisLeft.axisMinimum
                }
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_red)
            set1.fillDrawable = drawable
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1)
            val data = LineData(dataSets)
            binding.lineChart.data = data
            //            binding.lineChart.invalidate();
//            binding.lineChart.setVisibleXRangeMaximum(10);
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MyApplication).appComponent.fragmentComponent()
            .create().inject(this)
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }
}