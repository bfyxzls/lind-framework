package com.lind.common.ai;

import org.junit.jupiter.api.Test;

/**
 * 余弦相似度.
 *
 * @author lind
 * @date 2023/12/29 10:17
 * @since 1.0.0
 */
public class CosineSimilarity {

	public static double dotProduct(double[] vectorA, double[] vectorB) {
		double dotProduct = 0;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
		}
		return dotProduct;
	}

	public static double magnitude(double[] vector) {
		double sum = 0;
		for (double v : vector) {
			sum += v * v;
		}
		return Math.sqrt(sum);
	}

	public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
		double dotProduct = dotProduct(vectorA, vectorB);
		double magnitudeA = magnitude(vectorA);
		double magnitudeB = magnitude(vectorB);
		return dotProduct / (magnitudeA * magnitudeB);
	}

	@Test
	public void testCosineSimilarity() {
		double[] vectorA = { 2, 1, 0, 2, 3 };
		double[] vectorB = { 1, 1, 2, 1, 1 };

		double similarity = cosineSimilarity(vectorA, vectorB);
		System.out.println("Cosine Similarity: " + similarity);
	}

}
