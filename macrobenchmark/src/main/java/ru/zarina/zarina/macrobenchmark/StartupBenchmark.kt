package ru.zarina.zarina.macrobenchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Basic benchmark test.
 *
 * Before running this test, add <profileable> tag to application's manifest.
 */
@RunWith(AndroidJUnit4::class)
class StartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startupCold() = startup(StartupMode.COLD)

    @Test
    fun startupWarm() = startup(StartupMode.WARM)

    @Test
    fun startupHot() = startup(StartupMode.HOT)

    private fun startup(mode: StartupMode) {
        benchmarkRule.measureRepeated(
            packageName = "ru.zarina.zarina.benchmark",
            metrics = listOf(
                StartupTimingMetric(),
                FrameTimingMetric(),
            ),
            iterations = 5,
            startupMode = mode
        ) {
            pressHome()
            startActivityAndWait()
        }
    }
}
