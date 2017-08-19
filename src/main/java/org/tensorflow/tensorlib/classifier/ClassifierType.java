package org.tensorflow.tensorlib.classifier;

/**
 * Created by sabine on 8/2/17.
 */

public enum ClassifierType {
    CLASSIFIER_RETRAINED("file:///android_asset/rounded_graph.pb", "file:///android_asset/retrained_labels.txt", 299, 128, 128f, "Mul", "final_result"),
    CLASSIFIER_INCEPTION("file:///android_asset/tensorflow_inception_graph.pb", "file:///android_asset/imagenet_comp_graph_label_strings.txt", 224, 117, 1, "input", "output");


    private final int inputSize;
    private final int imageMean;
    private final float imageStd;
    private final String inputName;
    private final String outputName;
    private final String modelFilename;
    private final String labelFilename;

    ClassifierType(
            String modelFilename,
            String labelFilename,
            int inputSize,
            int imageMean,
            float imageStd,
            String inputName,
            String outputName) {
        this.modelFilename = modelFilename;
        this.labelFilename = labelFilename;
        this.inputSize = inputSize;
        this.imageMean = imageMean;
        this.imageStd = imageStd;
        this.inputName = inputName;
        this.outputName = outputName;

    }

    public int getImageMean() {
        return imageMean;
    }

    public float getImageStd() {
        return imageStd;
    }

    public String getInputName() {
        return inputName;
    }

    public String getOutputName() {
        return outputName;
    }

    public String getModelFilename() {
        return modelFilename;
    }

    public String getLabelFilename() {
        return labelFilename;
    }

    public int getInputSize() {

        return inputSize;
    }

    public String toString() {
        return this.name();
    }
}
