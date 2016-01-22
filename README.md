# Gifts-for-designers
---
# Introduction
一个为设计师设计的APP( ´_ゝ`)一个特别简单项目
 
主要用来练习Rx，当然这个适合初入Rx的看看。
 
自己参考的教程[给 Android 开发者的 RxJava 详解](http://gank.io/post/560e15be2dca930e00da1083)
 
Github地址：[Gifts-for-designers](https://github.com/xcc3641/Gifts-for-designers)
# Usage
( ´_ゝ`) 好像这个项目没有很多代码需要解释，不过我还是整理一份
 
#### 自定义字体
```
//      字体
Typeface fontFace = Typeface.createFromAsset(getAssets(),
"font/Inconsolata.otf");
Typeface tvFontFace = Typeface.createFromAsset(getAssets(), "font/Lobster.ttf");
editTextHEX.setTypeface(fontFace);
editTextRGB.setTypeface(fontFace);
textView.setTypeface(tvFontFace);
```

#### Rx监听输入
```
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
```
 
#### 改变背景颜色（有个过渡效果）
```
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
```
 
#### 将16进制转化成RGB
```
    private void changeToRGB(String s) {
        int red = Integer.parseInt(String.valueOf(s.charAt(0)) + s.charAt(1), 16);
        int green = Integer.parseInt(String.valueOf(s.charAt(2)) + s.charAt(3), 16);
        int blue = Integer.parseInt(String.valueOf(s.charAt(4)) + s.charAt(5), 16);

        Log.d(TAG, red + " " + green + " " + blue);
        editTextRGB.setText("(" + red + "," + green + "," + blue + ")");

    }
```


# Todo&Issues
- [x] ~~搭配色算法推荐~~
- [ ] 搭配色展示（RecyclerView）
- [ ] 目前只做了16进制颜色，RGB有些逻辑问题
- [x] ~~删除过快会有卡顿~~
- [ ] 颜色保存列表

# 截图
![](http://xcc3641.qiniudn.com/app-pixlr.jpg)

