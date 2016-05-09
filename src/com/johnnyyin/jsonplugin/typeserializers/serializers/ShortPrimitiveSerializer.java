package com.johnnyyin.jsonplugin.typeserializers.serializers;

import com.intellij.psi.PsiField;

import com.johnnyyin.jsonplugin.typeserializers.TypeSerializer;

public class ShortPrimitiveSerializer implements TypeSerializer {

    @Override
    public String writeValue(PsiField field, String parcel, String flags) {
        return parcel + ".writeInt(this." + field.getName() + ");";
    }

    @Override
    public String readValue(PsiField field, String parcel) {
        final String fieldName = field.getName();
        return fieldName + " = (short) " + parcel + ".optInt(\"" + fieldName + "\");";
    }
}