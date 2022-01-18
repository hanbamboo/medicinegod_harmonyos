package com.daqin.medicinegod;

import com.lxj.xpopup.impl.FullScreenPopupView;
import ohos.agp.components.Component;
import ohos.agp.components.ScrollView;
import ohos.app.Context;

/**
 * Description: 自定义全屏弹窗
 * Create by lxj, at 2019/3/12
 *
 */
public class MedicineDetailPopup extends FullScreenPopupView {
    public MedicineDetailPopup(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_popup_medicine_detail;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ScrollView s = (ScrollView)findComponentById(ResourceTable.Id_detail_scrollview);
        s.setScrolledListener(new ScrolledListener() {
            @Override
            public void onContentScrolled(Component component, int i, int i1, int i2, int i3) {
                if (i1<=-300){
                    MedicineDetailPopup.super.dismiss();
                }
            }
        });
        s.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                MedicineDetailPopup.super.dismiss();
            }
        });
    }
}