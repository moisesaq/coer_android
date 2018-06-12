package moises.com.appcoer.ui.login.forgotPassword;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiService;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.customviews.InputTextView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordDialog extends DialogFragment {

    public static final String TAG = ForgotPasswordDialog.class.getSimpleName();

    private Unbinder unbinder;

    @BindView(R.id.itv_email)
    protected InputTextView itvEmail;

    public static ForgotPasswordDialog newInstance() {
        return new ForgotPasswordDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.title_reset_password);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_forgot_password, null);
        unbinder = ButterKnife.bind(this, view);
        dialog.setView(view);

        return dialog.create();
    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClick() {
        dismiss();
    }

    @OnClick(R.id.btn_restore)
    public void onRestoreClick() {
        if (itvEmail.isEmailValid())
            resetPassword(itvEmail.getText());
    }

    private void resetPassword(String email) {
        Utils.showToastMessage(getString(R.string.sending));
        ApiService apiClient = RestApiAdapter.getInstance().startConnection();
        Call<ResponseBody> call = apiClient.resetPassword(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Utils.showToastMessage(getString(R.string.message_reset_password_successful));
                } else {
                    Utils.showToastMessage(getString(R.string.message_reset_password_failed));
                }
                dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.showToastMessage(getString(R.string.message_reset_password_failed));
                dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
