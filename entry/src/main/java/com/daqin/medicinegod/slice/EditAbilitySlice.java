package com.daqin.medicinegod.slice;

import com.daqin.medicinegod.ResourceTable;
import com.daqin.medicinegod.utils.imageControler.ImageSaver;
import com.daqin.medicinegod.utils.util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.ToastUtil;
import com.ycuwq.datepicker.date.DatePickerDialogFragment;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;


public class EditAbilitySlice extends AbilitySlice {
    Text edit_back;
    Text edit_ok;

    Image edit_img;
    boolean imgchanged = false;
    TextField edit_name;
    TextField edit_desp;

    Picker edit_otc;
    Text edit_date;
    TextField edit_usage_total;
    TextField edit_usage_time;
    TextField edit_usage_day;
    TextField edit_barcode;
    TextField edit_yu;
    TextField edit_company;
    Text edit_usage_u1;
    Text edit_usage_u2;
    Text edit_usage_u3;
    Text edit_yu_title;
    Text edit_elabel1;
    Text edit_elabel2;
    Text edit_elabel3;
    Text edit_elabel4;
    Text edit_elabel5;
    Text edit_elabel_add1;
    Text edit_elabel_add2;
    Text edit_elabel_title;
    Text edit_imgcrop;
    Text edit_imgback;
    Map<String, Object> mgDdata;
    String keyid, otc;
    List<String> elabel = new ArrayList<>();
    Text[] elabelview;
    TextField[] textFieldlist;
    int countElabel = 0;
    int newUsage_utils_1 = 0, newUsage_utils_3 = 0;
    String[] usage;
    String outdate;
    byte[] imgback = null;
    byte[] img = null;
    byte[] imgbytes = null;


    public void iniView() {
        edit_back = (Text) findComponentById(ResourceTable.Id_dtl_edit_back);
        edit_ok = (Text) findComponentById(ResourceTable.Id_dtl_edit_editok);
        edit_img = (Image) findComponentById(ResourceTable.Id_dtl_edit_img);
        edit_name = (TextField) findComponentById(ResourceTable.Id_dtl_edit_name);
        edit_usage_total = (TextField) findComponentById(ResourceTable.Id_dtl_edit_newUsage_1);
        edit_usage_time = (TextField) findComponentById(ResourceTable.Id_dtl_edit_newUsage_2);
        edit_usage_day = (TextField) findComponentById(ResourceTable.Id_dtl_edit_newUsage_3);
        edit_usage_u1 = (Text) findComponentById(ResourceTable.Id_dtl_edit_newUsage_utils_1);
        edit_usage_u2 = (Text) findComponentById(ResourceTable.Id_dtl_edit_newUsage_utils_2);
        edit_usage_u3 = (Text) findComponentById(ResourceTable.Id_dtl_edit_newUsage_utils_3);

        edit_desp = (TextField) findComponentById(ResourceTable.Id_dtl_edit_desp);
        edit_date = (Text)findComponentById(ResourceTable.Id_dtl_edit_choosedate);
        edit_otc = (Picker) findComponentById(ResourceTable.Id_dtl_edit_newOtc);
        edit_barcode = (TextField) findComponentById(ResourceTable.Id_dtl_edit_barcode);
        edit_yu = (TextField) findComponentById(ResourceTable.Id_dtl_edit_yu);
        edit_yu_title = (Text) findComponentById(ResourceTable.Id_dtl_edit_yu_title);
        edit_company = (TextField) findComponentById(ResourceTable.Id_dtl_edit_company);
        edit_elabel_add1 = (Text) findComponentById(ResourceTable.Id_dtl_edit_elabel_add1);
        edit_elabel_add2 = (Text) findComponentById(ResourceTable.Id_dtl_edit_elabel_add2);

        edit_elabel_title = (Text) findComponentById(ResourceTable.Id_dtl_edit_elabel_title);
        edit_elabel1 = (Text) findComponentById(ResourceTable.Id_dtl_edit_elabel1);
        edit_elabel2 = (Text) findComponentById(ResourceTable.Id_dtl_edit_elabel2);
        edit_elabel3 = (Text) findComponentById(ResourceTable.Id_dtl_edit_elabel3);
        edit_elabel4 = (Text) findComponentById(ResourceTable.Id_dtl_edit_elabel4);
        edit_elabel5 = (Text) findComponentById(ResourceTable.Id_dtl_edit_elabel5);
        edit_imgcrop = (Text) findComponentById(ResourceTable.Id_dtl_edit_imgcrop);
        edit_imgback = (Text) findComponentById(ResourceTable.Id_dtl_edit_imgback);

    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main_edit);
        keyid = util.PreferenceUtils.getString(getContext(), "mglocalkey");
        mgDdata = MainAbilitySlice.querySingleData(keyid);
        if (keyid == null || keyid.equals("null") || mgDdata == null) {
            //当不存在ID和KEY时打开了屏幕则关闭屏幕并展示弹窗信息
            new XPopup.Builder(getContext())
                    //.setPopupCallback(new XPopupListener())
                    .dismissOnTouchOutside(true)
                    .dismissOnBackPressed(true)
                    .isDestroyOnDismiss(true)
                    .asConfirm("错误", "药品信息不存在！或是内部发生错误！",
                            "", "好的",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                }
                            }, null, false, ResourceTable.Layout_popup_comfirm_without_cancel)
                    .show(); // 最后一个参数绑定已有布局
            terminate();
        }
        iniView();
        intclicklistener();
        iniCalendarPicker();


        textFieldlist = new TextField[]{edit_name, edit_desp, edit_usage_total, edit_usage_time, edit_usage_day, edit_company, edit_yu, edit_barcode};
        edit_img.setCornerRadius(25);
        img = (byte[]) mgDdata.get("img");
        imgback = img;
        edit_img.setPixelMap(util.byte2PixelMap(img));
        edit_name.setHint((String) mgDdata.get("name"));
        edit_desp.setHint((String) mgDdata.get("description"));
        edit_barcode.setHint((String) mgDdata.get("barcode"));
        usage = ((String) mgDdata.get("usage")).split("-");
        edit_usage_total.setHint(usage[0]);
        edit_usage_u1.setText(usage[1]);
        edit_usage_time.setHint(usage[2]);
        edit_usage_u2.setText(usage[3]);
        edit_usage_day.setHint(usage[4]);
        edit_usage_u3.setText(usage[5]);
        edit_yu.setHint((String) mgDdata.get("yu"));
        edit_company.setHint((String) mgDdata.get("company"));
        elabel.addAll(Arrays.asList(mgDdata.get("elabel").toString().split("@@")));
        countElabel = elabel.size();
        elabelview = new Text[]{edit_elabel1, edit_elabel2, edit_elabel3, edit_elabel4, edit_elabel5};
        //刷新标签显示
        refreshElabel();
    }


    private void intclicklistener() {
        edit_date.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(getContext());
                datePickerDialogFragment.setOnDateChooseListener(new DatePickerDialogFragment.OnDateChooseListener() {
                    @Override
                    public void onDateChoose(int year, int month, int day) {
                        outdate = year + "-" + month + "-1";
                        edit_date.setText("已选:"+outdate);
                    }
                });
                datePickerDialogFragment.show();
            }
        });
        edit_imgcrop.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (imgbytes == null) {
                    //如果没选择图片则编辑本身
                    Intent intent = new Intent();
                    Operation operation = new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName(getBundleName())
                            .withAbilityName("com.daqin.medicinegod.ImageControlAbility")
                            .build();
                    intent.setOperation(operation);
                    ImageSaver.getInstance().setByte(util.pixelMap2byte(edit_img.getPixelMap()));
                    System.out.println("开始传输");
                    startAbilityForResult(intent, 101);
                } else {
                    Intent intent = new Intent();
                    Operation operation = new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName(getBundleName())
                            .withAbilityName("com.daqin.medicinegod.ImageControlAbility")
                            .build();
                    intent.setOperation(operation);
                    ImageSaver.getInstance().setByte(imgbytes);
                    System.out.println("开始传输");
                    startAbilityForResult(intent, 101);
                }
            }
        });
        edit_imgback.setClickedListener(component -> {
            edit_img.setPixelMap(util.byte2PixelMap(imgback));
            img = null;
            imgbytes = null;
        });
        edit_ok.setClickedListener(component -> {
            boolean canEdit;
            String name, desp, otctmp, barcodetmp = "", usageall, usa1, usa2, usa3, yu, company;
            StringBuilder label = new StringBuilder();
            name = (edit_name.getText().length() == 0 || edit_name.getText().equals(" ") || edit_name.getText().equals(edit_name.getHint())) ? edit_name.getHint() : edit_name.getText();
            desp = (edit_desp.getText().length() == 0 || edit_desp.getText().equals(" ") || edit_desp.getText().equals(edit_desp.getHint())) ? edit_desp.getHint() : edit_desp.getText();
            otctmp = (edit_otc.getDisplayedData()[edit_otc.getValue()].equals("OTC(非处方药)-绿") ? "OTC-G" : ((edit_otc.getDisplayedData()[edit_otc.getValue()].equals("OTC(非处方药)-红") ? "OTC-R" : (edit_otc.getDisplayedData()[edit_otc.getValue()].equals("RX(处方药)") ? "Rx" : "none"))));
            if (edit_barcode.getText().equals("") || edit_barcode.getText().equals(" ")) {
                barcodetmp = edit_barcode.getHint();
                canEdit = true;
            } else {
                if (edit_barcode.getText().length() == 13) {
                    barcodetmp = edit_barcode.getText();
                    canEdit = true;
                } else {
                    canEdit = false;
                    new XPopup.Builder(getContext())
                            //.setPopupCallback(new XPopupListener())
                            .dismissOnTouchOutside(false)
                            .dismissOnBackPressed(false)
                            .isDestroyOnDismiss(true)
                            .asConfirm("格式不正确", "条码格式不正确(13)",
                                    " ", "好", null, null, false, ResourceTable.Layout_popup_comfirm_without_cancel)
                            .show(); // 最后一个参数绑定已有布局
                }

            }
            if (outdate.equals("")){
                canEdit = false;
                new XPopup.Builder(getContext())
                        //.setPopupCallback(new XPopupListener())
                        .dismissOnTouchOutside(false)
                        .dismissOnBackPressed(false)
                        .isDestroyOnDismiss(true)
                        .asConfirm("格式不正确", "未选择时间",
                                " ", "好", null, null, false, ResourceTable.Layout_popup_comfirm_without_cancel)
                        .show(); // 最后一个参数绑定已有布局
            }

            if (canEdit) {
                usa1 = (edit_usage_total.getText().length() == 0 || edit_usage_total.getText().equals(" ") || edit_usage_total.getText().equals(edit_usage_total.getHint())) ? edit_usage_total.getHint() : edit_usage_total.getText();
                usa2 = (edit_usage_time.getText().length() == 0 || edit_usage_time.getText().equals(" ") || edit_usage_time.getText().equals(edit_usage_time.getHint())) ? edit_usage_time.getHint() : edit_usage_time.getText();
                usa3 = (edit_usage_day.getText().length() == 0 || edit_usage_day.getText().equals(" ") || edit_usage_day.getText().equals(edit_usage_day.getHint())) ? edit_usage_day.getHint() : edit_usage_day.getText();
                usageall = usa1 + "-" + edit_usage_u1.getText() + "-" + usa2 + "-" + edit_usage_u2.getText() + "-" + usa3 + "-" + edit_usage_u3.getText();
                yu = (edit_yu.getText().length() == 0 || edit_yu.getText().equals(" ") || edit_yu.getText().equals(edit_yu.getHint())) ? edit_yu.getHint() : edit_yu.getText();
                company = (edit_company.getText().length() == 0 || edit_company.getText().equals(" ") || edit_company.getText().equals(edit_company.getHint())) ? edit_company.getHint() : edit_company.getText();
                for (String s : elabel) {
                    label.append(s).append("@@");
                }

                String finalBarcodetmp = barcodetmp;
                System.out.println("输出了" + keyid + name + desp + util.getDateFromString(outdate) + outdate + otctmp + finalBarcodetmp + usageall + company + yu + label.toString().substring(0, label.length() - 2));

                new XPopup.Builder(getContext())
                        //.setPopupCallback(new XPopupListener())
                        .dismissOnTouchOutside(false)
                        .dismissOnBackPressed(false)
                        .isDestroyOnDismiss(true)
                        .asConfirm("更改确认", "您做出了更改，请您再次确认。",
                                "返回", "确认修改", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        MainAbilitySlice.update(keyid,
                                                name,
                                                img == null ? imgbytes : img,
                                                desp,
                                                outdate,
                                                otctmp,
                                                finalBarcodetmp,
                                                usageall.trim(),
                                                company,
                                                yu.trim(),
                                                label.toString()
                                        );
                                        //editok属性包括{ ok (修改完成) , none(无) }
                                        util.PreferenceUtils.putString(getContext(), "editok", "ok");
                                        terminate();
                                    }
                                }, null, false, ResourceTable.Layout_popup_comfirm_with_cancel_blueconfirm)
                        .show(); // 最后一个参数绑定已有布局
            }
        });
        edit_img.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                openGallery();
            }
        });
        edit_back.setClickedListener(component -> terminate());
        edit_elabel_add1.setClickedListener(component -> {
            new XPopup.Builder(getContext())
                    .hasStatusBarShadow(true)
                    .isDestroyOnDismiss(true)
                    .autoOpenSoftInput(true)
                    .isDarkTheme(false)
                    .setComponent(component) // 用于获取页面根容器，监听页面高度变化，解决输入法盖住弹窗的问题
                    .asInputConfirm("写入药品标签", null, null, "1-4字符",
                            new OnInputConfirmListener() {
                                @Override
                                public void onConfirm(String text) {
                                    elabelClickAdd(text);
                                }
                            }, null, ResourceTable.Layout_popup_comfirm_with_input_word)
                    .show();
        });
        edit_elabel_add2.setClickedListener(component -> {
            new XPopup.Builder(getContext())
                    .hasStatusBarShadow(true)
                    .isDestroyOnDismiss(true)
                    .autoOpenSoftInput(true)
                    .isDarkTheme(false)
                    .setComponent(component) // 用于获取页面根容器，监听页面高度变化，解决输入法盖住弹窗的问题
                    .asInputConfirm("写入药品标签", null, null, "1-4字符",
                            new OnInputConfirmListener() {
                                @Override
                                public void onConfirm(String text) {
                                    elabelClickAdd(text);
                                }
                            }, null, ResourceTable.Layout_popup_comfirm_with_input_word)
                    .show();
        });
        edit_elabel1.setClickedListener(component -> {
            new XPopup.Builder(getContext())
                    .isDestroyOnDismiss(true)
                    .isDarkTheme(false)
                    .asCenterList("选择你的操作", new String[]{"编辑", "删除"},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    elabelClickOther(position, edit_elabel1.getText());
                                }
                            })
                    .show();
        });
        edit_elabel2.setClickedListener(component -> {
            new XPopup.Builder(getContext())
                    .isDestroyOnDismiss(true)
                    .isDarkTheme(false)
                    .asCenterList("选择你的操作", new String[]{"编辑", "删除"},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    elabelClickOther(position, edit_elabel2.getText());
                                }
                            })
                    .show();
        });
        edit_elabel3.setClickedListener(component -> {
            new XPopup.Builder(getContext())
                    .isDestroyOnDismiss(true)
                    .isDarkTheme(false)
                    .asCenterList("选择你的操作", new String[]{"编辑", "删除"},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    elabelClickOther(position, edit_elabel3.getText());
                                }
                            })
                    .show();
        });
        edit_elabel4.setClickedListener(component -> {
            new XPopup.Builder(getContext())
                    .isDestroyOnDismiss(true)
                    .isDarkTheme(false)
                    .asCenterList("选择你的操作", new String[]{"编辑", "删除"},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    elabelClickOther(position, edit_elabel4.getText());
                                }
                            })
                    .show();
        });
        edit_elabel5.setClickedListener(component -> {
            new XPopup.Builder(getContext())
                    .isDestroyOnDismiss(true)
                    .isDarkTheme(false)
                    .asCenterList("选择你的操作", new String[]{"编辑", "删除"},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    elabelClickOther(position, edit_elabel5.getText());
                                }
                            })
                    .show();
        });


        edit_usage_u1.setClickedListener(component -> {
            newUsage_utils_1 += 1;
            switch (newUsage_utils_1) {
                case 1:
                    edit_usage_u1.setText("克");
                    edit_yu_title.setText("剩余余量(单位:克)");
                    break;
                case 2:
                    edit_usage_u1.setText("包");
                    edit_yu_title.setText("剩余余量(单位:包)");
                    break;
                case 3:
                    edit_usage_u1.setText("片");
                    edit_yu_title.setText("剩余余量(单位:片)");
                    newUsage_utils_1 = 0;
                    break;
            }
        });
        edit_usage_u3.setClickedListener(component -> {
            newUsage_utils_3 += 1;
            switch (newUsage_utils_3) {
                case 1:
                    edit_usage_u3.setText("时");
                    break;
                case 2:
                    edit_usage_u3.setText("天");
                    newUsage_utils_3 = 0;
                    break;
            }
        });

    }
    private void openGallery() {
        Intent intent = new Intent();
//        intent.setType("video/*");
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setParam("return-data", true);
        intent.addFlags(Intent.FLAG_NOT_OHOS_COMPONENT);
        intent.addFlags(0x00000001);
        startAbilityForResult(intent, 100);
    }
    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        super.onAbilityResult(requestCode, resultCode, resultData);
        switch (requestCode) {
            case 100:
                if (resultData != null) {
                    //取得图片路径
                    String imgpath = resultData.getUriString();
                    System.out.println("修改后的：" + imgpath);
                    //定义数据能力帮助对象
                    DataAbilityHelper helper = DataAbilityHelper.creator(getContext());
                    //定义组件资源
                    ImageSource imageSource = null;
                    FileInputStream inputStream = null;

                    try {
                        inputStream = new FileInputStream(helper.openFile(Uri.parse(imgpath), "r"));
                    } catch (DataAbilityRemoteException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //定义文件
                    FileDescriptor file = null;
                    try {
                        file = helper.openFile(Uri.parse(imgpath), "r");
                    } catch (DataAbilityRemoteException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //创建文件对象
                    imageSource = ImageSource.create(file, null);
                    //创建位图
                    PixelMap pixelMap = imageSource.createPixelmap(null);
                    edit_img.setPixelMap(pixelMap);

                    //readInputStream将inputStream转换成byte[]
                    imgbytes = util.readInputStream(inputStream);

                }
                break;
            case 101:
                if (resultData != null) {
                    if (resultData.getStringParam("cropedimage").equals("ok")) {
                        img = ImageSaver.getInstance().getByte();
                        edit_img.setPixelMap(util.byte2PixelMap(img));

                    }
                }
                break;
        }
    }

    public void elabelClickAdd(String text) {
        if (countElabel >= 5) {
            new XPopup.Builder(getContext())
                    //.setPopupCallback(new XPopupListener())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .isDestroyOnDismiss(true)
                    .asConfirm("数量受限", "已达到标签最大数量(5)",
                            " ", "好", null, null, false, ResourceTable.Layout_popup_comfirm_without_cancel)
                    .show(); // 最后一个参数绑定已有布局
            edit_elabel_add1.setVisibility(Component.HIDE);
            edit_elabel_add2.setVisibility(Component.HIDE);
        } else if (elabel.contains(text.trim())) {
            new XPopup.Builder(getContext())
                    //.setPopupCallback(new XPopupListener())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .isDestroyOnDismiss(true)
                    .asConfirm("标签受限", "您只能添加同一种标签一次",
                            " ", "好", null, null, false, ResourceTable.Layout_popup_comfirm_without_cancel)
                    .show(); // 最后一个参数绑定已有布局
        } else if (text.trim().length() == 0 || text.trim().length() >= 5) {
            new XPopup.Builder(getContext())
                    //.setPopupCallback(new XPopupListener())
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .isDestroyOnDismiss(true)
                    .asConfirm("格式受限", "您在一个标签内只能添加1到4个中文字符且不能为空",
                            " ", "好", null, null, false, ResourceTable.Layout_popup_comfirm_without_cancel)
                    .show(); // 最后一个参数绑定已有布局
        } else if (text.trim().length() > 0 && text.trim().length() < 5) {
            elabel.remove("测试标签");
            elabel.add(text.trim());
            refreshElabel();
        }

    }

    public void elabelClickOther(int position, String textInLabel) {
        //0是编辑1是删除
        switch (position) {
            case 0:
                //弹窗
                new XPopup.Builder(getContext())
                        .hasStatusBarShadow(true) // 暂无实现
                        .autoOpenSoftInput(false)
                        .isDarkTheme(false)
                        .dismissOnBackPressed(false)
                        .dismissOnTouchOutside(false)
                        .isDestroyOnDismiss(true)
                        .setComponent(edit_elabel_title) // 用于获取页面根容器，监听页面高度变化，解决输入法盖住弹窗的问题
                        .asInputConfirm("编辑标签(1-4个字符)", null, null, textInLabel,
                                new OnInputConfirmListener() {
                                    @Override
                                    public void onConfirm(String text) {
                                        if (text.trim().length() == 0 || text.trim().length() >= 5) {
                                            new XPopup.Builder(getContext())
                                                    //.setPopupCallback(new XPopupListener())
                                                    .dismissOnTouchOutside(false)
                                                    .dismissOnBackPressed(false)
                                                    .isDestroyOnDismiss(true)
                                                    .asConfirm("格式受限", "您在一个标签内只能添加1到4个中文字符且不能为空",
                                                            " ", "好", null, null, false, ResourceTable.Layout_popup_comfirm_without_cancel)
                                                    .show(); // 最后一个参数绑定已有布局
                                        } else if (elabel.contains(text.trim())) {
                                            new XPopup.Builder(getContext())
                                                    //.setPopupCallback(new XPopupListener())
                                                    .dismissOnTouchOutside(false)
                                                    .dismissOnBackPressed(false)
                                                    .isDestroyOnDismiss(true)
                                                    .asConfirm("标签受限", "您只能添加同一种标签一次",
                                                            " ", "好", null, null, false, ResourceTable.Layout_popup_comfirm_without_cancel)
                                                    .show(); // 最后一个参数绑定已有布局
                                        } else if (text.trim().length() > 0 && text.trim().length() < 5) {
                                            elabel.set(elabel.indexOf(textInLabel), text.trim());
                                            refreshElabel();
                                        }
                                    }
                                }, null, ResourceTable.Layout_popup_comfirm_with_input_word)
                        .show();
                break;
            case 1:
                //弹窗
                new XPopup.Builder(getContext())
                        //.setPopupCallback(new XPopupListener())
                        .dismissOnTouchOutside(false)
                        .dismissOnBackPressed(false)
                        .isDestroyOnDismiss(true)
                        .asConfirm("删除确认", "是否删除？",
                                "返回", "删除", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        if (countElabel == 1) {
                                            new XPopup.Builder(getContext())
                                                    //.setPopupCallback(new XPopupListener())
                                                    .dismissOnTouchOutside(false)
                                                    .dismissOnBackPressed(false)
                                                    .isDestroyOnDismiss(true)
                                                    .asConfirm("数量受限", "最少需要保留(1)个标签",
                                                            " ", "好", null, null, false, ResourceTable.Layout_popup_comfirm_without_cancel)
                                                    .show(); // 最后一个参数绑定已有布局
                                        } else {
                                            elabel.remove(textInLabel);
                                            elabel.add("测试标签");
                                            refreshElabel();
                                        }
                                    }
                                }, null, false, ResourceTable.Layout_popup_comfirm_with_cancel_redconfirm)
                        .show(); // 最后一个参数绑定已有布局


                break;
        }
    }

    public void refreshElabel() {
        //始终保持5个以防操作时出现错误
        countElabel = 0;
        //小于5则补足5，大于5则删除至5
        for (Text t : elabelview) {
            t.setText("测试标签");
            t.setVisibility(Component.HIDE);
        }
        if (elabel.size() < 5) {
            for (int i = 0; i < 5 - elabel.size(); i++) {
                elabel.add("测试标签");
            }
        } else if (elabel.size() > 5) {
            for (String s : elabel) {
                if (s.equals("测试标签")) {
                    elabel.remove("测试标签");
                }
            }
            //删除多余的之后如果还大于5，截取前5个
            if (elabel.size() > 5) {
                for (int i = 5; i < elabel.size(); i++) {
                    elabel.remove(elabel.get(i));
                }
                //小了的话加上，把持5个
            } else if (elabel.size() < 5) {
                for (int i = 0; i < 5 - elabel.size(); i++) {
                    elabel.add("测试标签");
                }
            }
        }
        //把'测试标签'的标识全部集中到最后

        int count = 0;
        for (int i = 0; i < elabel.size(); i++) {
            if (elabel.get(i).equals("测试标签")) {
                elabel.remove("测试标签");
                count++;
            }

        }
        for (int i = 0; i < count; i++) {
            elabel.add("测试标签");
        }

        //设置显示
        for (int i = 0; i < elabel.size(); i++) {
            if (!elabel.get(i).equals("测试标签")) {
                elabelview[i].setVisibility(Component.VISIBLE);
                elabelview[i].setText(elabel.get(i));
                countElabel++;
            }
        }
        edit_elabel_title.setText("药品标签(" + countElabel + "/5)");

        //设置添加按钮的显示
        if (countElabel < 3) {
            edit_elabel_add1.setVisibility(Component.VISIBLE);
            edit_elabel_add2.setVisibility(Component.HIDE);
        } else if (countElabel == 3 || countElabel == 4) {
            edit_elabel_add1.setVisibility(Component.HIDE);
            edit_elabel_add2.setVisibility(Component.VISIBLE);
        } else if (countElabel == 5) {
            edit_elabel_add1.setVisibility(Component.HIDE);
            edit_elabel_add2.setVisibility(Component.HIDE);
        }

    }

    //定义选择器
    private void iniCalendarPicker() {

        long data0 = (long) mgDdata.get("outdate");
        String dateAll = util.getStringFromDate(data0);


        System.out.println("输出了" + dateAll);
        int value = 0;
        outdate= dateAll;

        String[] otcList = new String[]{"OTC(非处方药)-红", "OTC(非处方药)-绿", "(留空)", "RX(处方药)"};
        otc = (String) mgDdata.get("otc");
        switch (otc) {
            case "none":
                value = 2;
                break;
            case "OTC-G":
                value = 1;
                break;
            case "OTC-R":
                value = 0;
                break;
            case "Rx":
                value = 3;
                break;
        }
        edit_otc.setDisplayedData(otcList);
        edit_otc.setValue(value);


    }
}
