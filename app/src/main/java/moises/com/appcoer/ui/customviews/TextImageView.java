package moises.com.appcoer.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import moises.com.appcoer.R;
import moises.com.appcoer.global.SavedState;

public class TextImageView extends LinearLayout implements View.OnClickListener{

    private ImageView imageView;
    private TextView textView1, textView2, textView3;
    private ImageButton imageButton;
    private ProgressBar progressBar;

    private OnTextImageViewListener onTextImageViewListener;

    public TextImageView(Context context) {
        super(context);
        setupView();
    }

    public TextImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TextImageView);

        Drawable background = typedArray.getDrawable(R.styleable.TextImageView_android_background);
        setBackground(background);

        Drawable icon = typedArray.getDrawable(R.styleable.TextImageView_iconImage);
        setIconImage(icon);

        String text1 = typedArray.getString(R.styleable.TextImageView_text1);
        setText1(text1);

        String text2 = typedArray.getString(R.styleable.TextImageView_text2);
        setText2(text2);

        String text3 = typedArray.getString(R.styleable.TextImageView_text3);
        setText3(text3);

        int color = typedArray.getColor(R.styleable.TextImageView_android_textColor, 0);
        setTextColor(color);

        int maxLines = typedArray.getInt(R.styleable.TextImageView_android_maxLines, 1);
        setTextViewMaxLines(maxLines);

        int styleText = typedArray.getInt(R.styleable.TextImageView_android_textStyle, 0);
        setTextViewStyle(styleText);

        float textSize = typedArray.getFloat(R.styleable.TextImageView_android_textSize, 0);
        setTextSize(textSize);

        Drawable iconButton = typedArray.getDrawable(R.styleable.TextImageView_iconButton);
        setIconButton(iconButton);

        typedArray.recycle();
    }

    private void setupView(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_image_text, this, true);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void setOnTextImageViewListener(OnTextImageViewListener listener){
        this.onTextImageViewListener = listener;
    }

    public void setIconImage(Drawable image){
        if(image != null){
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(image);
        }
    }

    public void setIconImage(int image){
        if(image > 0){
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(image);
        }
    }

    private void setIconButton(Drawable image){
        if(image != null){
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setImageDrawable(image);
        }
    }

    public void setIconButton(int icon){
        if(icon > 0){
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setImageResource(icon);
        }
    }

    public void setText1(String text){
        if(text != null)
            textView1.setText(text);
    }

    public String getText1(){
        return textView1.getText().toString();
    }

    public void setText2(String text){
        if(text != null && !text.isEmpty()){
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(text);
        }else{
            textView2.setVisibility(View.GONE);
        }

    }

    public void setText3(String text){
        if(text != null && !text.isEmpty()){
            textView3.setVisibility(View.VISIBLE);
            textView3.setText(text);
        }else{
            textView3.setVisibility(View.GONE);
        }
    }

    public void setTextColor(int color){
        if(color > 0){
            textView1.setTextColor(getColor(color));
            textView2.setTextColor(getColor(color));
            textView3.setTextColor(getColor(color));
        }
    }

    private void setTextViewMaxLines(int lines){
        textView1.setMaxLines(lines);
    }

    public void setTextViewStyle(int style){
        if(style > 0)
            textView1.setTypeface(null, style);
    }

    public void setTextSize(float size){
        if(size > 0)
            textView1.setTextSize(size);
    }

    @Override
    public void onClick(View view) {
        if(onTextImageViewListener != null)
            onTextImageViewListener.onEditClick(getId());
    }

    public ImageButton getImageButtonAction(){
        return imageButton;
    }

    public interface OnTextImageViewListener {
        void onEditClick(int id);
    }

    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        progressBar.setVisibility(View.GONE);
    }

    /*SAVE STATE OF THE VIEWS*/
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.childrenStates = new SparseArray();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(ss.childrenStates);
        }
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).restoreHierarchyState(ss.childrenStates);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    private int getColor(int id){
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(getContext(), id);
        } else {
            return getContext().getResources().getColor(id);
        }
    }
}
