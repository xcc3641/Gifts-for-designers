package com.android.hugo.giftforstylist;


import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RelativeLayout relativeLayout;
    private EditText editTextHEX;
    private EditText editTextRGB;
    private TextView textView;
    private StringBuffer sb = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);//去掉信息栏

        setContentView(R.layout.activity_main);

        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "font/Inconsolata.otf");
        Typeface tvFontFace = Typeface.createFromAsset(getAssets(), "font/sheepsansbold.ttf");
        initView();
        editTextHEX.setTypeface(fontFace);
        editTextRGB.setTypeface(fontFace);
        textView.setTypeface(tvFontFace);
        focusOnHEX();
        focusOnRGB();

    }


    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_backgroud);
        editTextHEX = (EditText) findViewById(R.id.et_hex);
        editTextRGB = (EditText) findViewById(R.id.et_rgb);
        textView = (TextView) findViewById(R.id.tv_slogn);

    }

    private void focusOnHEX() {
        RxTextView.textChanges(editTextHEX)
                .subscribeOn(AndroidSchedulers.mainThread())
                .debounce(300, TimeUnit.MICROSECONDS)

                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        return charSequence.toString();
                    }
                })

                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.length() > 4;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (s.length() == 6) {
                            setBackground(s, 0);
                            changeHEXtoRGB(s);
                        } else {
                            setBackground("56abe4", 0);
                            editTextRGB.setText("");
                        }
                    }
                })
        ;

    }

    private void focusOnRGB() {
        RxTextView.textChanges(editTextRGB)
                .debounce(300, TimeUnit.MICROSECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        String[] temps = charSequence.toString().split(",");
                        return temps.length == 3;
                    }
                })
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        Log.d(TAG, "已有3个,进行下一步");
                    }
                })
                .map(new Func1<CharSequence, String[]>() {
                    @Override
                    public String[] call(CharSequence charSequence) {
                        return charSequence.toString().split(",");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "completed");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String[] strings) {
                        int red = Integer.valueOf(strings[0]);
                        int green = Integer.valueOf(strings[1]);
                        int blue = Integer.valueOf(strings[2]);
                        int temp = Color.rgb(red, green, blue);
                        Log.d(TAG, "temp:" + temp);
                        relativeLayout.setBackgroundColor(temp);

                    }
                })

        ;


    }

    private void changeHEXtoRGB(String s) {

        int red = Integer.parseInt(String.valueOf(s.charAt(0)) + s.charAt(1), 16);
        int green = Integer.parseInt(String.valueOf(s.charAt(2)) + s.charAt(3), 16);
        int blue = Integer.parseInt(String.valueOf(s.charAt(4)) + s.charAt(5), 16);
        Log.d(TAG, red + " " + green + " " + blue);
        editTextRGB.setText("(" + red + "," + green + "," + blue + ")");

    }


    /**
     * @param color
     * @param mark  0:HEX 1:RGB
     */
    public void setBackground(String color, int mark) {
        int i = 0;
        ValueAnimator paramInteger;
        Drawable localDrawable = this.relativeLayout.getBackground();
        if ((localDrawable instanceof ColorDrawable))
            i = ((ColorDrawable) localDrawable).getColor();

        if (mark == 0) {
            paramInteger = ValueAnimator.ofObject(new ArgbEvaluator(), i, Color
                    .parseColor("#" + color));
        } else {
            paramInteger = ValueAnimator.ofObject(new ArgbEvaluator(), i, mark);
        }


        paramInteger.setDuration(500L);
        paramInteger.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                           public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator) {
                                               relativeLayout.setBackgroundColor((Integer)
                                                       paramAnonymousValueAnimator.getAnimatedValue());
                                           }
                                       }

        );
        paramInteger.start();
    }
}
