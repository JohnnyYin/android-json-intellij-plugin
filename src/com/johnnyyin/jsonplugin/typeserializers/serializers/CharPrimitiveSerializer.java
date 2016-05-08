package com.johnnyyin.jsonplugin.typeserializers.serializers;

import com.intellij.psi.PsiField;
import com.johnnyyin.jsonplugin.typeserializers.TypeSerializer;

public class CharPrimitiveSerializer implements TypeSerializer {
    @Override
    public String writeValue(PsiField field, String parcel, String flags) {
        return parcel + ".writeInt(" + field.getName() + ");";
    }

    @Override
    public String readValue(PsiField field, String parcel) {
        final String fieldName = field.getName();
        return fieldName + " = (char) " + parcel + ".optInt(\"" + fieldName + "\");";
    }
}
