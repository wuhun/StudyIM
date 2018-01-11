package studyim.cn.edu.cafa.studyim.activity.login;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.main.MainActivity;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.db.DBManager;
import studyim.cn.edu.cafa.studyim.model.BaseModel;
import studyim.cn.edu.cafa.studyim.model.GroupModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getSPUtil;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
    }

    private void init() {
        if (!TextUtils.isEmpty(getSPUtil().getTokens())) {
            goToMain();
        } else {
            goToLogin();
        }
    }

    private void goToMain() {
        initGroupList();
        jumpToActivity(MainActivity.class);
        WelcomeActivity.this.finish();
    }

    /** 初始化群组信息 */
    private void initGroupList() {
        HttpUtil.getGroupList(null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                BaseModel<GroupModel> model = MyApplication.getGson().fromJson(result, new TypeToken<BaseModel<GroupModel>>(){}.getType());
                if(model != null && model.getCode() == 1 && response.isSuccessful()) {
                    List<GroupModel> groups = model.getResult();
                    String before = WuhunDataTool.isNullString(model.getBefore()) ? Constant.HOME_URL : model.getBefore();
                    DBManager.getmInstance().saveGroups(groups, before);
                }
            }
        });
    }

    private void goToLogin(){
        jumpToActivity(LoginActivity.class);
        WelcomeActivity.this.finish();
    }
}
