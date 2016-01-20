package com.android.hugo.giftforstylist;


import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RelativeLayout relativeLayout;
    private EditText editTextHEX;
    private EditText editTextRGB;
    private TextView textView;
    private RecyclerView recyclerView;
    private RecycerlViewAdapter adapter;
    private ArrayList<Integer> colors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);//去掉信息栏

        setContentView(R.layout.activity_main);

        initView();
        focusOnHEX();
//        focusOnRGB();


    }


    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_backgroud);
        editTextHEX = (EditText) findViewById(R.id.et_hex);
        editTextRGB = (EditText) findViewById(R.id.et_rgb);
        textView = (TextView) findViewById(R.id.tv_slogn);
        recyclerView = (RecyclerView) findViewById(R.id.recycerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


//      字体
        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "font/Inconsolata.otf");
        Typeface tvFontFace = Typeface.createFromAsset(getAssets(), "font/Lobster.ttf");
        editTextHEX.setTypeface(fontFace);
        editTextRGB.setTypeface(fontFace);
        textView.setTypeface(tvFontFace);
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
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (s.length() == 5) {
                            setBackground("56abe4", 0);
                            editTextRGB.setText("");
                        }
                        if (!colors.isEmpty()) {
                            colors.clear();
                            adapter.notifyDataSetChanged();
                        }

                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.length() > 5;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                        setBackground(s, 0);
//                        addColorAdapter(s);
                        changeToRGB(s);

                    }
                })
        ;

    }

//    private void focusOnRGB() {
//        RxTextView.textChanges(editTextRGB)
//                .debounce(300, TimeUnit.MICROSECONDS)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .filter(new Func1<CharSequence, Boolean>() {
//                    @Override
//                    public Boolean call(CharSequence charSequence) {
//                        String[] temps = charSequence.toString().split(",");
//                        return temps.length == 3;
//                    }
//                })
//                .doOnNext(new Action1<CharSequence>() {
//                    @Override
//                    public void call(CharSequence charSequence) {
//                        Log.d(TAG, "已有3个,进行下一步");
//                    }
//                })
//                .map(new Func1<CharSequence, String[]>() {
//                    @Override
//                    public String[] call(CharSequence charSequence) {
//                        return charSequence.toString().split(",");
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String[]>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "completed");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String[] strings) {
//                        int red = Integer.valueOf(strings[0]);
//                        int green = Integer.valueOf(strings[1]);
//                        int blue = Integer.valueOf(strings[2]);
//                        int temp = Color.rgb(red, green, blue);
//                        Log.d(TAG, "temp:" + temp);
//                        relativeLayout.setBackgroundColor(temp);
//
//                    }
//                })
//
//        ;
//
//
//    }

    private void changeToRGB(String s) {
        int red = Integer.parseInt(String.valueOf(s.charAt(0)) + s.charAt(1), 16);
        int green = Integer.parseInt(String.valueOf(s.charAt(2)) + s.charAt(3), 16);
        int blue = Integer.parseInt(String.valueOf(s.charAt(4)) + s.charAt(5), 16);

        Log.d(TAG, red + " " + green + " " + blue);
        editTextRGB.setText("(" + red + "," + green + "," + blue + ")");

    }

    /**
     * 想做一个颜色推荐用recyclerView展示
     *
     * @param s
     */
    private void addColorAdapter(final String s) {

        Observable observable = Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(1);
                        subscriber.onNext(2);
                        subscriber.onNext(3);
                        subscriber.onNext(4);
                        subscriber.onCompleted();
                    }
                });

        Subscriber subscriber = new Subscriber() {
            int red;
            int green;
            int blue;

            @Override
            public void onStart() {
                super.onStart();
                red = Integer.parseInt(String.valueOf(s.charAt(0)) + s.charAt(1), 16);
                green = Integer.parseInt(String.valueOf(s.charAt(2)) + s.charAt(3), 16);
                blue = Integer.parseInt(String.valueOf(s.charAt(4)) + s.charAt(5), 16);

                Log.d(TAG, red + " " + green + " " + blue);
                editTextRGB.setText("(" + red + "," + green + "," + blue + ")");

            }

            @Override
            public void onCompleted() {
                adapter = new RecycerlViewAdapter(MainActivity.this, colors);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNext(Object o) {
                Random random = new Random();
                int mixRed = random.nextInt(256);
                int mixGreen = random.nextInt(256);
                int mixBlue = random.nextInt(256);

                mixRed = (red + mixRed) / 2;
                mixGreen = (green + mixGreen) / 2;
                mixBlue = (blue + mixBlue) / 2;
                int mixColer = Color.rgb(mixRed, mixGreen, mixBlue);
                colors.add(mixColer);

            }

            @Override
            public void onError(Throwable e) {

            }
        };

        observable.subscribe(subscriber);
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
