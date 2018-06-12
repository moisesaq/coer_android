package moises.com.appcoer.ui.login;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moises.com.appcoer.R;
import moises.com.appcoer.api.DataContract;
import moises.com.appcoer.model.login.User;

public class LoginPresenter implements LoginContract.Presenter{

    private final LoginContract.View loginView;
    private final DataContract dataManager;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public LoginPresenter(LoginContract.View loginView, DataContract dataManager){
        this.loginView = loginView;
        this.dataManager = dataManager;
        loginView.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onFragmentStarted() {
        //TODO Here check network status
    }

    @Override
    public void startLogin() {
        if(loginView.areUsernameAndPasswordValid())
            compositeDisposable.add(getStartLogin());
    }

    private Disposable getStartLogin(){
        loginView.showLoading(true);
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        return dataManager.login(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::success, this::failed);
    }

    private void success(User user){
        loginView.showLoading(false);
        loginView.loginSuccess(user);
    }

    private void failed(Throwable throwable){
        loginView.showLoading(false);
        loginView.showLoginError(loginView.getFragment().getString(R.string.message_something_went_wrong));
    }
}
