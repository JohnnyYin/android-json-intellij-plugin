package com.johnnyyin.jsonplugin.typeserializers.serializers;

import com.intellij.psi.PsiField;
import com.johnnyyin.jsonplugin.typeserializers.TypeSerializer;

public class ParcelableArraySerializer implements TypeSerializer {
    @Override
    public String writeValue(PsiField field, String parcel, String flags) {
        return parcel + ".writeTypedArray(this." + field.getName() + ", flags);";
    }

    @Override
    public String readValue(PsiField field, String parcel) {
        final String fieldName = field.getName();
        return fieldName + " = " + parcel + ".createTypedArray(" + field.getType().getDeepComponentType().getCanonicalText() + ".CREATOR);";
    }
}
