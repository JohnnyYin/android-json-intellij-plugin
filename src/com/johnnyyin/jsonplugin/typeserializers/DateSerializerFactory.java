package com.johnnyyin.jsonplugin.typeserializers;

import com.intellij.psi.PsiType;
import com.johnnyyin.jsonplugin.typeserializers.serializers.DateSerializer;

/**
 * Custom serializer factory for Date objects
 *
 * @author Dallas Gutauckis [dallas@gutauckis.com]
 */
public class DateSerializerFactory implements TypeSerializerFactory {
    private final DateSerializer mSerializer;

    public DateSerializerFactory() {
        mSerializer = new DateSerializer();
    }

    @Override
    public TypeSerializer getSerializer(PsiType psiType) {
        if ("java.util.Date".equals(psiType.getCanonicalText())) {
            return mSerializer;
        }

        return null;
    }
}
