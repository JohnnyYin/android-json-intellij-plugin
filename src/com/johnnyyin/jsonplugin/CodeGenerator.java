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
package com.johnnyyin.jsonplugin;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.johnnyyin.jsonplugin.typeserializers.*;

import java.util.List;


/**
 * Quite a few changes here by Dallas Gutauckis [dallas@gutauckis.com]
 */
public class CodeGenerator {
    private final PsiClass mClass;
    private final List<PsiField> mFields;
    private final TypeSerializerFactory mTypeSerializerFactory;

    public CodeGenerator(PsiClass psiClass, List<PsiField> fields) {
        mClass = psiClass;
        mFields = fields;

        this.mTypeSerializerFactory = new ChainSerializerFactory(
                new BundleSerializerFactory(),
                new DateSerializerFactory(),
                new EnumerationSerializerFactory(),
                new PrimitiveTypeSerializerFactory(),
                new PrimitiveArraySerializerFactory(),
                new PrimitiveTypeArraySerializerFactory(),
                new ParcelableSerializerFactory(),
                new ListSerializerFactory(),
                new SerializableSerializerFactory()
        );
    }

    private String generateToJson(List<PsiField> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("public JSONObject toJson() {\n");
        sb.append("     try {\n");
        sb.append("         JSONObject json = new JSONObject();\n");
        for (PsiField field : fields) {
            String name = field.getName();
            sb.append("json.put(\"").append(name).append("\", ").append(name).append(");");
        }
        sb.append("         return json;\n");
        sb.append("     } catch (JSONException e) {\n");
        sb.append("         e.printStackTrace();\n");
        sb.append("         return null;\n");
        sb.append("     }\n");
        sb.append("}\n");
        return sb.toString();
    }

    private String generateFromJson(List<PsiField> fields) {
        StringBuilder sb = new StringBuilder();
        String clsName = mClass.getName();
        if (clsName == null || clsName.length() < 1) {
            return "";
        }
        sb.append("public void fromJson(String jsonStr) {\n");
        sb.append("     try {\n");
        sb.append("         JSONObject json = new JSONObject(jsonStr);\n");
        for (PsiField field : fields) {
            sb.append(getSerializerForType(field).readValue(field, "json"));
        }
        sb.append("     } catch (JSONException e) {\n");
        sb.append("         e.printStackTrace();\n");
        sb.append("     }\n");
        sb.append("}\n");
        return sb.toString();
    }

    private TypeSerializer getSerializerForType(PsiField field) {
        return mTypeSerializerFactory.getSerializer(field.getType());
    }

    public void generate() {
        Project project = mClass.getProject();
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);

        removeExistingParcelableImplementation(mClass);

        // Method for writing to the parcel
        PsiMethod toJsonMethod = elementFactory.createMethodFromText(generateToJson(mFields), mClass);
        PsiMethod fromJsonMethod = elementFactory.createMethodFromText(generateFromJson(mFields), mClass);

        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);

        // Shorten all class references
        styleManager.shortenClassReferences(mClass.addBefore(toJsonMethod, mClass.getLastChild()));
        styleManager.shortenClassReferences(mClass.addBefore(fromJsonMethod, mClass.getLastChild()));

        // import
        PsiJavaFile psiJavaFile = (PsiJavaFile) mClass.getContainingFile();
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        PsiClass clsJSONObject = javaPsiFacade.findClass("org.json.JSONObject", GlobalSearchScope.allScope(project));
        PsiClass clsJSONException = javaPsiFacade.findClass("org.json.JSONException", GlobalSearchScope.allScope(project));
        if (clsJSONObject != null)
            styleManager.addImport(psiJavaFile, clsJSONObject);
        if (clsJSONException != null)
            styleManager.addImport(psiJavaFile, clsJSONException);
    }

    /**
     * Strips the
     *
     * @param psiClass
     */
    private void removeExistingParcelableImplementation(PsiClass psiClass) {
        // Look for an existing CREATOR and remove it
        findAndRemoveMethod(psiClass, "toJson");
        findAndRemoveMethod(psiClass, "fromJson", "java.lang.String");
    }


    private static void findAndRemoveMethod(PsiClass clazz, String methodName, String... arguments) {
        // Maybe there's an easier way to do this with mClass.findMethodBySignature(), but I'm not an expert on Psi*
        PsiMethod[] methods = clazz.findMethodsByName(methodName, false);

        for (PsiMethod method : methods) {
            PsiParameterList parameterList = method.getParameterList();

            if (parameterList.getParametersCount() == arguments.length) {
                boolean shouldDelete = true;

                PsiParameter[] parameters = parameterList.getParameters();

                for (int i = 0; i < arguments.length; i++) {
                    if (!parameters[i].getType().getCanonicalText().equals(arguments[i])) {
                        shouldDelete = false;
                    }
                }

                if (shouldDelete) {
                    method.delete();
                }
            }
        }
    }
}
