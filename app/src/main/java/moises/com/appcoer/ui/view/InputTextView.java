package moises.com.appcoer.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import moises.com.appcoer.R;
import moises.com.appcoer.global.SavedState;

public class InputTextView extends LinearLayout{
    private static final int MIN_TEXT_LINE = 1;

    @BindView(R.id.image_view) protected ImageView imageView;
    @BindView(R.id.text_input_layout) protected TextInputLayout textInputLayout;
    @BindView(R.id.edit_text) protected EditText editText;
    @BindView(R.id.image_button) protected ImageButton imageButton;

    private Callback callback;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public InputTextView(Context context) {
        super(context);
        setUpView();
    }

    public InputTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpView();
        initialize(attrs);
    }

    private void setUpView() {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_input_text, this, true);
        ButterKnife.bind(this, view);
        compositeDisposable.add(getSubscriptionFromEditText());
        textInputLayout.setErrorEnabled(true);
    }

    private Disposable getSubscriptionFromEditText() {
        return RxTextView
                .textChanges(editText)
                .subscribe(charSequence -> textInputLayout.setError(null));
    }

    private void initialize(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.InputTextView);

        int id = typedArray.getInt(R.styleable.InputTextView_android_id, 0);
        setIdEditText(id);

        Drawable imageIcon = typedArray.getDrawable(R.styleable.InputTextView_iconImage);
        setImageIcon(imageIcon);

        boolean errorEnabled = typedArray.getBoolean(R.styleable.InputTextView_errorEnabled, false);
        setErrorEnabled(errorEnabled);

        boolean maxLengthEnabled = typedArray.getBoolean(R.styleable.InputTextView_maxLengthEnabled, false);
        setMaxLengthEnabled(maxLengthEnabled);

        int maxLength = typedArray.getInteger(R.styleable.InputTextView_maxLength, 0);
        setMaxLength(maxLength);

        int inputType = typedArray.getInt(R.styleable.InputTextView_android_inputType, InputType.TYPE_NULL);
        setInputType(inputType);

        int lines = typedArray.getInt(R.styleable.InputTextView_android_lines, 1);
        setLines(lines);

        String text = typedArray.getString(R.styleable.InputTextView_android_text);
        setText(text);

        String hint = typedArray.getString(R.styleable.InputTextView_hint);
        setHint(hint);

        boolean enabled = typedArray.getBoolean(R.styleable.InputTextView_android_enabled, true);
        setEnabled(enabled);

        Drawable iconAction = typedArray.getDrawable(R.styleable.InputTextView_iconAction);
        setImageIconAction(iconAction);

        typedArray.recycle();
    }

    public void onDestroy() { compositeDisposable.dispose(); }

    /**
     * Getters & Setters
     */
    public String getText(){
        return editText.getText().toString();
    }

    public void setImageIcon(Drawable imageIcon){
        if(imageIcon != null){
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(imageIcon);
        }
    }

    public void setImageIconAction(Drawable imageIcon){
        if(imageIcon != null){
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setImageDrawable(imageIcon);
        }
    }

    public void setText(@NonNull String text){
        editText.setText(text);
    }

    public void setHint(@NonNull String text){ textInputLayout.setHint(text); }

    public void setError(@NonNull String error){
        textInputLayout.setError(error);
    }

    public void setInputType(int inputType){
        editText.setInputType(inputType);
    }

    public void setErrorEnabled(boolean enabled){
        textInputLayout.setErrorEnabled(enabled);
    }

    public void setMaxLengthEnabled(boolean enabled){
        textInputLayout.setCounterEnabled(enabled);
    }

    public void setMaxLength(int maxLength){
        textInputLayout.setCounterMaxLength(maxLength);
    }

    public void setLines(int lines) {
        if(lines >= MIN_TEXT_LINE)
            setLinesAttributes(lines);
    }

    private void setLinesAttributes(int lines) {
        editText.setLines(lines);
        editText.setGravity(Gravity.START|Gravity.TOP);
    }

    @Override
    public void setEnabled(boolean value){ editText.setEnabled(value); }

    @Override
    public void setFocusable(boolean focusable){
        editText.setFocusable(focusable);
    }

    public void clearField(){ editText.getText().clear(); }

    public void setIdEditText(int id){
        this.editText.setId(id);
    }

    @OnClick(R.id.image_button)
    public void onClick(View view) {
        callback.onActionIconClick(this);
    }

    /**
     * Public Checks
     */
    public boolean isTextValid() {
        try {
            return checkEmptyAndLengthAndReturn();
        } catch (ExceptionInvalidInput exception) {
            return manageErrorAndReturn(exception);
        }
    }

    public boolean isPasswordValid(){
        try {
            return checkEmptyAndLengthAndReturn();
        } catch (ExceptionInvalidInput exception) {
            return manageErrorAndReturn(exception);
        }
    }

    public boolean isPhoneValid() {
        try {
            return checkEmptyAndPhoneAndReturn();
        } catch (ExceptionInvalidInput exception) {
            return manageErrorAndReturn(exception);
        }
    }

    public boolean isEmailValid() {
        try {
            return checkEmptyAndEmailAndReturn();
        } catch (ExceptionInvalidInput exception) {
            return manageErrorAndReturn(exception);
        }
    }

    private boolean checkEmptyAndLengthAndReturn() throws ExceptionInvalidInput {
        String text = editText.getText().toString();
        checkEmpty(text);
        checkLength(text);
        return true;
    }

    private boolean checkEmptyAndPhoneAndReturn() throws ExceptionInvalidInput{
        String phone = editText.getText().toString().trim();
        checkEmpty(phone);
        checkPhone(phone);
        return true;
    }

    private boolean checkEmptyAndEmailAndReturn() throws ExceptionInvalidInput {
        String email = editText.getText().toString().trim();
        checkEmpty(email);
        checkEmail(email);
        return true;
    }

    private boolean manageErrorAndReturn(ExceptionInvalidInput exception) {
        clearField();
        setError(exception.getMessage());
        return false;
    }

    private void checkEmpty(String text) throws ExceptionInvalidInput {
        if(text.isEmpty())
            throw new ExceptionInvalidInput(getStringToShow(R.string.is_empty));
    }

    private void checkLength(String text) throws ExceptionInvalidInput {
        if(textInputLayout.isCounterEnabled() && textIsGreaterThanMax(text))
            throw new ExceptionInvalidInput(getStringToShow(R.string.is_invalid));
    }

    private boolean textIsGreaterThanMax(String text) {
        return text.length() >= textInputLayout.getCounterMaxLength();
    }

    private void checkPhone(String phone) throws ExceptionInvalidInput {
        if(!Patterns.PHONE.matcher(phone).matches())
            throw new ExceptionInvalidInput(getStringToShow(R.string.is_invalid));
    }

    private void checkEmail(String email) throws ExceptionInvalidInput {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            throw new ExceptionInvalidInput(getStringToShow(R.string.is_invalid));
    }

    private String getStringToShow(int resourceId) {
        return textInputLayout.getHint() + " " + getResources().getString(resourceId);
    }

    public void addCallback(Callback callback){
        this.callback = callback;
    }

    public EditText getEditText(){
        return editText;
    }

    public interface Callback{
        void onActionIconClick(View view);
    }

    /**
     * Save State
    * */
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.childrenStates = new SparseArray();
        return saveHierarchyStateOfEachChild(ss);
    }

    private SavedState saveHierarchyStateOfEachChild(SavedState ss) {
        for(int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(ss.childrenStates);
        }

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        restoreHierarchyStateOfEachChild(ss);
    }

    private void restoreHierarchyStateOfEachChild(SavedState ss) {
        for(int i = 0; i < getChildCount(); i++) {
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
}