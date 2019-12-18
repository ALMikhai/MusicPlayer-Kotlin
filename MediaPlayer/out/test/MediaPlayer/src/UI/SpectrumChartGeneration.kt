package UI

import javafx.scene.chart.BarChart
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis

class SpectrumChartGeneration {
    companion object{
        fun init() : BarChart<String, Number>{
            val xAxis = CategoryAxis()
            xAxis.animated = false
            xAxis.isTickLabelsVisible = false
            xAxis.isTickMarkVisible = false
            xAxis.isAutoRanging = true

            val yAxis = NumberAxis()
            yAxis.animated = false
            yAxis.isTickLabelsVisible = false
            yAxis.isTickMarkVisible = false
            yAxis.isMinorTickVisible = false
            yAxis.isAutoRanging = false
            yAxis.upperBound = 50.0
            yAxis.lowerBound = 0.0

            var chart = BarChart(xAxis, yAxis)

            chart.animated = false
            chart.isLegendVisible = false
            chart.isAlternativeColumnFillVisible = false
            chart.isAlternativeRowFillVisible = false
            chart.isHorizontalGridLinesVisible = false
            chart.isHorizontalZeroLineVisible = false

            return chart
        }
    }
}