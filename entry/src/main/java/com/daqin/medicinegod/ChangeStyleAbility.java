package com.daqin.medicinegod;

import com.daqin.medicinegod.slice.ChangeStyleAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ChangeStyleAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ChangeStyleAbilitySlice.class.getName());

    }
}
