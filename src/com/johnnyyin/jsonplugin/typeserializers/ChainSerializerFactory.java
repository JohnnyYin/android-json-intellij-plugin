/*
 * Copyright (C) 2013 Michał Charmas (http://blog.charmas.pl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.johnnyyin.jsonplugin.typeserializers;

import com.intellij.psi.PsiType;
import com.johnnyyin.jsonplugin.typeserializers.serializers.ParcelableObjectSerializer;

import java.util.Arrays;
import java.util.List;

public class ChainSerializerFactory implements TypeSerializerFactory {

    private final List<TypeSerializerFactory> factories;

    public ChainSerializerFactory(TypeSerializerFactory... factories) {
        this.factories = Arrays.asList(factories);
    }

    @Override
    public TypeSerializer getSerializer(PsiType psiType) {
        for (TypeSerializerFactory factory : factories) {
            TypeSerializer serializer = factory.getSerializer(psiType);
            if (serializer != null) {
                return serializer;
            }
        }
        return new ParcelableObjectSerializer();
    }
}
