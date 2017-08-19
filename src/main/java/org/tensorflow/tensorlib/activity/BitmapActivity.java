package org.tensorflow.tensorlib.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.TypedValue;

import org.tensorflow.tensorlib.R;
import org.tensorflow.tensorlib.classifier.Classifier;
import org.tensorflow.tensorlib.classifier.ClassifierType;
import org.tensorflow.tensorlib.classifier.TensorFlowImageClassifier;
import org.tensorflow.tensorlib.env.BorderedText;
import org.tensorflow.tensorlib.env.Logger;
import org.tensorflow.tensorlib.fragment.BitmapFragment;
import org.tensorflow.tensorlib.view.ResultsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sabine on 7/16/17.
 */

public class BitmapActivity extends Activity {

    Fragment fragment;
    private ResultsView resultsView;
    private static final Logger LOGGER = new Logger();
    private BorderedText borderedText;

    private static final float TEXT_SIZE_DIP = 10;
    Bitmap croppedBitmap;
    List<Classifier.Recognition> results;
    Handler handler;
    HandlerThread handlerThread;
    ClassifierType classifierType;
    Classifier classifier;
    int mInputSize;
    ProgressDialog mDialog;
    //use weakrefs?
    //compress bitmap
    //

    public BitmapActivity(Context c, String type) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, c.getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);
        //choose either paintings or inception classifier at this point

        if (type.equals(ClassifierType.CLASSIFIER_RETRAINED.name())) {
            classifierType = ClassifierType.CLASSIFIER_RETRAINED;
        } else {
            classifierType = ClassifierType.CLASSIFIER_INCEPTION;
        }
        //@todo cache the classifier
        classifier =
                TensorFlowImageClassifier.create(
                        c.getAssets(), classifierType.getModelFilename(), classifierType.getLabelFilename(), classifierType.getInputSize(),
                        classifierType.getImageMean(), classifierType.getImageStd(), classifierType.getInputName(), classifierType.getOutputName()
                );
        mInputSize = classifierType.getInputSize();


    }

    @Override
    public synchronized void onStart() {
        LOGGER.d("onStart " + this);
        super.onStart();

    }



    @Override
    public synchronized void onStop() {
        LOGGER.d("onStop " + this);
        super.onStop();
    }

    @Override
    public synchronized void onDestroy() {
        LOGGER.d("onDestroy " + this);
        super.onDestroy();
    }


    protected int getLayoutId() {
        return R.layout.bitmap_fragment;
    }

    protected void setFragment() {
        //this could be a place for diff frags to exist
        /*final Fragment fragment = CameraConnectionFragment.newInstance(
                new CameraConnectionFragment.ConnectionCallback(){
                    @Override
                    public void onPreviewSizeChosen(final Size size, final int rotation) {
                        CameraActivity.this.onPreviewSizeChosen(size, rotation);
                    }
                },
                this, getLayoutId(), getDesiredPreviewFrameSize()); */
        fragment = BitmapFragment.newInstance(getLayoutId());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void runClassifier(final Bitmap bitmap, Context c, final ResultsView rv) {
        //change this to static

        handlerThread = new HandlerThread("bitmap");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {


                final long startTime = SystemClock.uptimeMillis();
                //@todo run in bg return on main
                //final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
                //@todo
                //check if smaller first
                croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, mInputSize, mInputSize);
                //todo cache the results
                results = classifier.recognizeImage(croppedBitmap);
                if (results.size() == 0) {
                    results = new ArrayList<Classifier.Recognition>();
                    results.add(new Classifier.Recognition("", "There were no results!", 0f, null));
                }
                rv.setResults(results);

                final long lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
                croppedBitmap.recycle();
            }
        });
        //ProgressDialog d = new ProgressDialog(c);
        /*while (results == null) {
            //progress bar

            d.show();
        }*/

        //d.dismiss();

        //resultsView.setResults(results);
        // requestRender();
        //final long lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
        //  }
        // cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
        // resultsView.setResults(results);
        // requestRender();
        // computing = false;

        //  });
    }


}
