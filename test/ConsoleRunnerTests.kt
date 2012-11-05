package spek.test

import kotlin.test.assertNotNull
import org.junit.Test as test
import java.util.ArrayList
import kotlin.test.assertEquals
import spek.ConsoleTextListener
import spek.Listener
import spek.MultipleListenerNotifier
import spek.SpecificationRunner

public class ConsoleRunnerTests {

    test fun tempTest() {

        val textOutput = ConsoleTextListener()
        val listeners = ArrayList<Listener>()
        val multipleNotifiers = MultipleListenerNotifier(listeners)

        listeners.add(textOutput)
        val specRunner = SpecificationRunner(multipleNotifiers)
        try {
            specRunner.runSpecsInFolder("samples")
        } finally {
            specRunner.close()
        }


    }



}