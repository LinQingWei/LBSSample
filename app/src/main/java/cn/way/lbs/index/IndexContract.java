package cn.way.lbs.index;

import java.util.List;

import cn.way.lbs.abs.IPresenter;
import cn.way.lbs.abs.IView;

final class IndexContract {

    interface IndexView extends IView<IndexPresenter> {

        void loadFinish(List<String> indexList);
    }

    interface IndexPresenter extends IPresenter {

    }
}
