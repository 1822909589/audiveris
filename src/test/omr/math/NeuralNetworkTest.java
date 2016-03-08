//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                               N e u r a l N e t w o r k T e s t                                //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//  Copyright © Herve Bitteur and others 2000-2016. All rights reserved.
//  This software is released under the GNU General Public License.
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package omr.math;

import omr.classifier.AbstractClassifier.Monitor;
import omr.classifier.Sample;

import omr.util.BaseTestCase;
import omr.util.Dumping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

//import org.testng.annotations.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.*;

/**
 * Class
 * <code>NeuralNetworkTest</code> performs unit tests on
 * NeuralNetwork class.
 *
 * @author Hervé Bitteur
 * @version $Id$
 */
public class NeuralNetworkTest
        extends BaseTestCase
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final double maxMSE = 0.3;

    private static NeuralNetwork nn;

    //~ Methods ------------------------------------------------------------------------------------
    //-------------------//
    // testBackupRestore //
    //-------------------//
    //@Test
    public void testBackupRestore ()
    {
        double[][] inputs = new double[][]{
            {0, 0},
            {1, 0},
            {0, 1},
            {1, 1}
        };

        double[][] desiredOutputs = new double[][]{
            {0},
            {1},
            {1},
            {0}
        };
        Monitor monitor = new MyMonitor();

        do {
            nn = createNetwork();
        } while (nn.train(inputs, desiredOutputs, monitor) > maxMSE);

        nn.dump();

        NeuralNetwork.Backup backup = nn.backup();

        // Check behavior on incompatible network
        NeuralNetwork pp = createNetwork(1, 2, 3);

        try {
            pp.restore(backup);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            checkException(expected);
        }

        pp = createNetwork();

        // Check behavior on a null backup
        try {
            pp.restore(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            checkException(expected);
        }

        // Check normal backup
        pp.restore(backup);

        assertNears("0 xor 0 should be 0", 0d, pp.run(new double[]{0, 0}, null, null)[0], 0.1d);

        assertNears("1 xor 0 should be 1", 1d, pp.run(new double[]{1, 0}, null, null)[0], 0.1d);

        assertNears("0 xor 1 should be 1", 1d, pp.run(new double[]{0, 1}, null, null)[0], 0.1d);

        assertNears("1 xor 1 should be 0", 0d, pp.run(new double[]{1, 1}, null, null)[0], 0.1d);
    }

    //-----------------//
    // testMarshalling //
    //-----------------//
    //@Test
    public void testMarshalling ()
            throws JAXBException, FileNotFoundException, IOException
    {
        double[][] inputs = new double[][]{
            {0, 0},
            {1, 0},
            {0, 1},
            {1, 1}
        };

        double[][] desiredOutputs = new double[][]{
            {0},
            {1},
            {1},
            {0}
        };
        Monitor monitor = new MyMonitor();

        do {
            nn = createNetwork();
        } while (nn.train(inputs, desiredOutputs, monitor) > maxMSE);

        nn.dump();

        Path dir = Paths.get("data/temp");
        Files.createDirectories(dir);

        Path path = dir.resolve("nn.xml");

        // Marshalling
        System.out.println("Marshalling to " + path);
        nn.marshal(new FileOutputStream(path.toFile()));
        System.out.println("Marshalled");

        // Unmarshalling
        System.out.println("Unmarshalling from " + path);
        nn = NeuralNetwork.unmarshal(new FileInputStream(path.toFile()));
        System.out.println("Unmarshalled");
        new Dumping().dump(nn);
        nn.dump();
    }

    //--------//
    // testOr //
    //--------//
    //@Test
    public void testOr ()
    {
        double[][] inputs = new double[][]{
            {0, 0},
            {1, 0},
            {0, 1},
            {1, 1}
        };

        double[][] desiredOutputs = new double[][]{
            {0},
            {1},
            {1},
            {1}
        };
        Monitor monitor = new MyMonitor();

        do {
            nn = createNetwork();
        } while (nn.train(inputs, desiredOutputs, monitor) > maxMSE);

        nn.dump();

        assertNears("0 or 0 should be 0", 0d, nn.run(new double[]{0, 0}, null, null)[0], 0.1d);

        assertNears("1 or 0 should be 1", 1d, nn.run(new double[]{1, 0}, null, null)[0], 0.1d);

        assertNears("0 or 1 should be 1", 1d, nn.run(new double[]{0, 1}, null, null)[0], 0.1d);

        assertNears("1 or 1 should be 1", 1d, nn.run(new double[]{1, 1}, null, null)[0], 0.1d);
    }

    //---------//
    // testXOr //
    //---------//
    //@Test
    public void testXOr ()
    {
        double[][] inputs = new double[][]{
            {0, 0},
            {1, 0},
            {0, 1},
            {1, 1}
        };

        double[][] desiredOutputs = new double[][]{
            {0},
            {1},
            {1},
            {0}
        };
        Monitor monitor = new MyMonitor();

        do {
            nn = createNetwork();
        } while (nn.train(inputs, desiredOutputs, monitor) > maxMSE);

        nn.dump();

        assertNears("0 xor 0 should be 0", 0d, nn.run(new double[]{0, 0}, null, null)[0], 0.1d);

        assertNears("1 xor 0 should be 1", 1d, nn.run(new double[]{1, 0}, null, null)[0], 0.1d);

        assertNears("0 xor 1 should be 1", 1d, nn.run(new double[]{0, 1}, null, null)[0], 0.1d);

        assertNears("1 xor 1 should be 0", 0d, nn.run(new double[]{1, 1}, null, null)[0], 0.1d);
    }

    //---------------//
    // createNetwork //
    //---------------//
    private NeuralNetwork createNetwork (int inputSize,
                                         int hiddenSize,
                                         int outputSize)
    {
        double amplitude = 0.5;
        double learningRate = 0.25;
        double momentum = 0.25;
        double maxError = 0.02;
        int epochs = 500000;

        String[] iLabels = new String[inputSize];

        for (int i = 0; i < inputSize; i++) {
            iLabels[i] = "in-" + i;
        }

        String[] oLabels = new String[outputSize];

        for (int i = 0; i < outputSize; i++) {
            oLabels[i] = "out-" + i;
        }

        return new NeuralNetwork(
                inputSize,
                hiddenSize,
                outputSize,
                amplitude,
                iLabels,
                oLabels,
                learningRate,
                momentum,
                maxError,
                epochs);
    }

    private NeuralNetwork createNetwork ()
    {
        return createNetwork(2, 2, 1);
    }

    //~ Inner Classes ------------------------------------------------------------------------------
    //-----------//
    // MyMonitor //
    //-----------//
    private static class MyMonitor
            implements Monitor
    {
        //~ Methods --------------------------------------------------------------------------------

        @Override
        public void epochEnded (int epochIndex,
                                double mse)
        {
            if ((epochIndex % 10000) == 0) {
                System.out.println("epochEnded." + " epochIndex=" + epochIndex + " mse=" + mse);

                // Test for convergence
                if (epochIndex > 100000) {
                    nn.stop();
                }
            }
        }

        @Override
        public void sampleProcessed (Sample sample)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void trainingStarted (final int epochIndex,
                                     final int epochMax,
                                     final double mse)
        {
            System.out.println("trainingStarted." + " epochIndex=" + epochIndex + " mse=" + mse);
        }
    }
}
