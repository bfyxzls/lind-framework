package com.lind.common.ai;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * @author lind
 * @date 2024/1/2 9:32
 * @since 1.0.0
 */
@Slf4j
public class Deeplearning4jTest {

	@SneakyThrows
	@Test
	public void testDeeplearning4j() {
		int nChannels = 1; // black & white picture, 3 if color image
		int outputNum = 10; // number of classification
		int batchSize = 64; // mini batch size for sgd
		int nEpochs = 10; // total rounds of training
		int iterations = 1; // number of iteration in each traning round
		int seed = 123; // random seed for initialize weights

		log.info("Load data....");
		DataSetIterator mnistTrain = null;
		DataSetIterator mnistTest = null;

		mnistTrain = new MnistDataSetIterator(batchSize, true, 12345);
		mnistTest = new MnistDataSetIterator(batchSize, false, 12345);

		MultiLayerConfiguration.Builder builder = new NeuralNetConfiguration.Builder().seed(seed).iterations(iterations)
				.regularization(true).l2(0.0005).learningRate(0.01)// .biasLearningRate(0.02)
				// .learningRateDecayPolicy(LearningRatePolicy.Inverse).lrPolicyDecayRate(0.001).lrPolicyPower(0.75)
				.weightInit(WeightInit.XAVIER).optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
				.updater(Updater.NESTEROVS).momentum(0.9).list().layer(0, new ConvolutionLayer.Builder(5, 5)
						// nIn and nOut specify depth. nIn here is the nChannels and nOut
						// is the number of filters to be applied
						.nIn(nChannels).stride(1, 1).nOut(20).activation("identity").build())
				.layer(1,
						new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX).kernelSize(2, 2).stride(2, 2)
								.build())
				.layer(2, new ConvolutionLayer.Builder(5, 5)
						// Note that nIn need not be specified in later layers
						.stride(1, 1).nOut(50).activation("identity").build())
				.layer(3,
						new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX).kernelSize(2, 2).stride(2, 2)
								.build())
				.layer(4, new DenseLayer.Builder().activation("relu").nOut(500).build())
				.layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).nOut(outputNum)
						.activation("softmax").build())
				.backprop(true).pretrain(false).cnnInputSize(28, 28, 1);
		// The builder needs the dimensions of the image along with the number of
		// channels. these are 28x28 images in one channel
		// new ConvolutionLayerSetup(builder,28,28,1);

		MultiLayerConfiguration conf = builder.build();
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		model.setListeners(new ScoreIterationListener(1)); // a listener which can print
															// loss function score after
															// each iteration

		for (int i = 0; i < nEpochs; ++i) {
			model.fit(mnistTrain);
			log.info("*** Completed epoch " + i + "***");

			log.info("Evaluate model....");
			Evaluation eval = new Evaluation(outputNum);
			while (mnistTest.hasNext()) {
				DataSet ds = mnistTest.next();
				INDArray output = model.output(ds.getFeatureMatrix(), false);
				eval.eval(ds.getLabels(), output);
			}
			log.info(eval.stats());
			mnistTest.reset();
		}
	}

}
