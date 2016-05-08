package com.johnnyyin.jsonplugin.typeserializers.serializers;

import com.intellij.psi.PsiField;
import com.johnnyyin.jsonplugin.typeserializers.TypeSerializer;

/**
 * Serializer for types implementing Parcelable
 *
 * @author Dallas Gutauckis [dallas@gutauckis.com]
 */
public class ParcelableObjectSerializer implements TypeSerializer {
    @Override
    public String writeValue(PsiField field, String parcel, String flags) {
        return parcel + ".writeParcelable(this." + field.getName() + ", " + flags + ");";
    }

    @Override
    public String readValue(PsiField field, String parcel) {
        final String fieldName = field.getName();
        return fieldName + " = " + parcel + ".opt(\"" + fieldName + "\");";
    }
}
