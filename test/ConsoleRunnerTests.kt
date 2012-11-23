package spek.test

import kotlin.test.assertNotNull
import org.junit.Test as test
import java.util.ArrayList
import kotlin.test.assertEquals
import spek.PlainTextListener
import spek.Listener
import spek.MultipleListenerNotifier
import spek.SpecificationRunner

public class ConsoleRunnerTests {

    test fun tempTest() {

        val textOutput = PlainTextListener(ConsoleDevice())
        val htmlOutput = HTMLListener(ConsoleDevice())
        val listeners = ArrayList<Listener>()
        val multipleNotifiers = MultipleListenerNotifier(listeners)

        listeners.add(textOutput)
        listeners.add(htmlOutput)
        val specRunner = SpecificationRunner(multipleNotifiers)
        try {
            specRunner.runSpecsInFolder("samples")
        } finally {
            specRunner.close()
        }


    }



}