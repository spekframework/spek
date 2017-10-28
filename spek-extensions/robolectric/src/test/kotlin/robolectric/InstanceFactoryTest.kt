package robolectric

import android.app.Activity
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.robolectric.Robolectric
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.internal.SdkEnvironment
import org.robolectric.manifest.AndroidManifest
import org.spekframework.spek2.CreateWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.it
import org.spekframework.spek2.lifecycle.InstanceFactory
import org.spekframework.spek2.robolectric.RobolectricInstanceFactory
import org.spekframework.spek2.robolectric.RobolectricLifeCycleListener
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

class RobolectricInstanceFactoryTest : AbstractSpekRuntimeTest() {

    object SampleInstanceFactoryTest : InstanceFactory by RobolectricInstanceFactory

    @Test
    fun testUsingRobolectric() {
        @CreateWith(SampleInstanceFactoryTest::class)
        class RobolectricSpek(val sdkEnvironment: SdkEnvironment, val config: Config, val androidManifest: AndroidManifest ): Spek({
            var activity: Activity? = null
            registerListener(RobolectricLifeCycleListener(sdkEnvironment, config, androidManifest))
            beforeEachTest {
                println("hello")

                activity = Robolectric.buildActivity(Activity::class.java).get()
                println(activity)
            }
            it("should work") {
                println("world")

                val shadowActivity = Shadows.shadowOf(activity)

                println(shadowActivity.isFinishing)
                assertFalse(false)
            }
        })

        val recorder = executeTestsForClass(RobolectricSpek::class)
        println(recorder.testStartedCount)
        println(recorder.testSuccessfulCount)
        assertThat(recorder.testSuccessfulCount, equalTo(1))
    }
}
