package moises.com.appcoer.ui.login.resetPassword;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.view.InputTextView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordDialog extends DialogFragment implements View.OnClickListener{

    public static final String TAG = ResetPasswordDialog.class.getSimpleName();

    private InputTextView mEmail;

    public static ResetPasswordDialog newInstance(){
        return new ResetPasswordDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.title_reset_password);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_reset_password, null);
        mEmail = (InputTextView) view.findViewById(R.id.itv_email);

        Button cancel = (Button)view.findViewById(R.id.b_cancel);
        cancel.setOnClickListener(this);
        Button restore = (Button)view.findViewById(R.id.b_restore);
        restore.setOnClickListener(this);
        dialog.setView(view);

        return dialog.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_cancel:
                this.dismiss();
                break;
            case R.id.b_restore:
                if(mEmail.isEmailValid()){
                    resetPassword(mEmail.getText());
                }
                break;
        }
    }

    private void resetPassword(String email){
        Utils.showToastMessage(getString(R.string.sending));
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<ResponseBody> call = apiClient.resetPassword(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Utils.showToastMessage(getString(R.string.message_reset_password_successful));
                }else{
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
}
