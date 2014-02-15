package net.blmarket.rforest

/**
 * Generic Predictor provides predict.
 *
 * Created by blmarket on 2014. 2. 15..
 */
trait Predictor {
  def predict(features: Array[Double]): Option[Double]
}
