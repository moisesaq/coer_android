package moises.com.appcoer.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import moises.com.appcoer.R;
import moises.com.appcoer.model.login.User;
import moises.com.appcoer.ui.CoerApplication;

public class UserGuide {

    private static final String USER_GUIDE = User.class.getSimpleName();
    private AppCompatActivity activity;

    private UserGuide(AppCompatActivity activity1){
        activity = activity1;
    }

    public static UserGuide getInstance(AppCompatActivity activity){
        return new UserGuide(activity);
    }

    public void showStageWithView(@NonNull StageGuide stageGuide,@NonNull View view, CallBack callBack){
        //showTapTargetView(view, stageGuide.getTitle(), stageGuide.getDescription(), callBack);
        if(!isGuideSeen(stageGuide)){
            showTapTargetView(view, stageGuide.getTitle(), stageGuide.getDescription(), callBack);
            guideSeen(stageGuide, true);
        }
    }

    public void showStageWithToolbar(@NonNull StageGuide stageGuide,@NonNull Toolbar toolbar, CallBack callBack){
        //showTapTargetToolbar(toolbar, stageGuide.getTitle(), stageGuide.getDescription(), callBack);
        if(!isGuideSeen(stageGuide)){
            showTapTargetToolbar(toolbar, stageGuide.getTitle(), stageGuide.getDescription(), callBack);
            guideSeen(stageGuide, true);
        }
    }

    private void showTapTargetView(View view, String title, String description, final CallBack callBack){
        TapTargetView.showFor(activity,
                TapTarget.forView(view, title, description)
                        .outerCircleColor(R.color.colorAccent)
                        .targetCircleColor(R.color.colorPrimaryLight)
                        .dimColor(android.R.color.white)
                        .transparentTarget(false)
                        .tintTarget(false)
                        .icon(AppCompatResources.getDrawable(activity, R.drawable.svg_lock))
                        .drawShadow(false)
                        .cancelable(true)
                        .targetRadius(60),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        if(callBack != null)
                            callBack.onUserGuideOnClick();
                    }
                });
    }

    private void showTapTargetToolbar(Toolbar toolbar, String title, String description, final CallBack callBack){
        //TapTarget.forToolbarNavigationIcon(toolbar, "This is the back button", sassyDesc).id(1)
        TapTargetView.showFor(activity,
                TapTarget.forToolbarNavigationIcon(toolbar, title, description).id(1)
                        .outerCircleColor(R.color.colorAccent)
                        .targetCircleColor(R.color.colorPrimaryLight)
                        .dimColor(android.R.color.white)
                        .transparentTarget(false)
                        .tintTarget(false)
                        .icon(AppCompatResources.getDrawable(activity, R.drawable.svg_plus))
                        .drawShadow(false)
                        .cancelable(true)
                        .targetRadius(60),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        if(callBack != null)
                            callBack.onUserGuideOnClick();
                    }
                });
    }

    public void resetUserGuide(){
        guideSeen(StageGuide.STAGE_1, false);
        guideSeen(StageGuide.STAGE_2, false);
    }

    private void guideSeen(StageGuide stageGuide, boolean status){
        SharedPreferences preferences = CoerApplication.getContext().getSharedPreferences(USER_GUIDE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(stageGuide.getStage(), status);
        editor.apply();
    }

    private boolean isGuideSeen(StageGuide stageGuide){
        SharedPreferences preferences = CoerApplication.getContext().getSharedPreferences(USER_GUIDE, Context.MODE_PRIVATE);
        return preferences.getBoolean(stageGuide.getStage(), false);
    }

    public interface CallBack{
        void onUserGuideOnClick();
    }

    public enum StageGuide {
        STAGE_1("stage1", CoerApplication.getContext().getString(R.string.guide_stage_1), CoerApplication.getContext().getString(R.string.guide_stage_description_1)),
        STAGE_2("stage2", CoerApplication.getContext().getString(R.string.guide_stage_2), CoerApplication.getContext().getString(R.string.guide_stage_description_2)),
        STAGE_3("stage3", "Title stage 3", "Description stage 3"),
        STAGE_4("stage4", "Title stage 4", "Description stage 4");

        private String stage, title, description;

        StageGuide(String stage, String title, String description){
            this.stage = stage;
            this.title = title;
            this.description = description;
        }

        public String getStage() {
            return stage;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
}
