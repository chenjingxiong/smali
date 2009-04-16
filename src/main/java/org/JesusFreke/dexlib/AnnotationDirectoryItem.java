/*
 * [The "BSD licence"]
 * Copyright (c) 2009 Ben Gruver
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.JesusFreke.dexlib;

import org.JesusFreke.dexlib.ItemType;

import java.util.ArrayList;

//TODO: fix field names in dex-format.html and submit
public class AnnotationDirectoryItem extends OffsettedItem<AnnotationDirectoryItem> {
    private final Field[] fields;

    private final ArrayList<FieldAnnotation> fieldAnnotationList = new ArrayList<FieldAnnotation>();
    private final ArrayList<MethodAnnotation> methodAnnotationList = new ArrayList<MethodAnnotation>();
    private final ArrayList<ParameterAnnotation> parameterAnnotationList = new ArrayList<ParameterAnnotation>();

    private final OffsettedItemReference<AnnotationSetItem> classAnnotations;
    private final ListSizeField annotatedFieldsCount;
    private final ListSizeField annotatedMethodsCount;
    private final ListSizeField annotatedParametersCount;
    private final FieldListField<FieldAnnotation> fieldAnnotations;
    private final FieldListField<MethodAnnotation> methodAnnotations;
    private final FieldListField<ParameterAnnotation> parameterAnnotations;

    public AnnotationDirectoryItem(final DexFile dexFile, int offset) {
        super(offset);

        fields = new Field[] {
                classAnnotations = new OffsettedItemReference<AnnotationSetItem>(dexFile.AnnotationSetsSection, new IntegerField()),
                annotatedFieldsCount = new ListSizeField(fieldAnnotationList, new IntegerField()),
                annotatedMethodsCount = new ListSizeField(methodAnnotationList, new IntegerField()),
                annotatedParametersCount = new ListSizeField(parameterAnnotationList, new IntegerField()),
                fieldAnnotations = new FieldListField<FieldAnnotation>(fieldAnnotationList) {
                    protected FieldAnnotation make() {
                        return new FieldAnnotation(dexFile);
                    }
                },
                methodAnnotations = new FieldListField<MethodAnnotation>(methodAnnotationList) {
                    protected MethodAnnotation make() {
                        return new MethodAnnotation(dexFile);
                    }
                },
                parameterAnnotations = new FieldListField<ParameterAnnotation>(parameterAnnotationList) {
                    protected ParameterAnnotation make() {
                        return new ParameterAnnotation(dexFile);
                    }
                }
        };
    }

    protected int getAlignment() {
        return 4;
    }

    protected Field[] getFields() {
        return fields;
    }

    public ItemType getItemType() {
        return ItemType.TYPE_ANNOTATIONS_DIRECTORY_ITEM;
    }

    public class FieldAnnotation extends CompositeField<FieldAnnotation> {
        private final Field[] fields;

        public FieldAnnotation(DexFile dexFile) {
            fields = new Field[] {
                    new IndexedItemReference<FieldIdItem>(dexFile.FieldIdsSection, new IntegerField()),
                    new OffsettedItemReference<AnnotationSetItem>(dexFile.AnnotationSetsSection, new IntegerField())
            };
        }

        protected Field[] getFields() {
            return fields;
        }
    }

    public class MethodAnnotation extends CompositeField<MethodAnnotation> {
        private final Field[] fields;

        public MethodAnnotation(DexFile dexFile) {
            fields = new Field[] {
                    new IndexedItemReference<MethodIdItem>(dexFile.MethodIdsSection, new IntegerField()),
                    new OffsettedItemReference<AnnotationSetItem>(dexFile.AnnotationSetsSection, new IntegerField())
            };
        }

        protected Field[] getFields() {
            return fields;
        }
    }

    public class ParameterAnnotation extends CompositeField<ParameterAnnotation> {
        private final Field[] fields;

        public ParameterAnnotation(DexFile dexFile) {
            fields = new Field[] {
                    new IndexedItemReference<MethodIdItem>(dexFile.MethodIdsSection, new IntegerField()),
                    new OffsettedItemReference<AnnotationSetItem>(dexFile.AnnotationSetsSection, new IntegerField())
            };
        }

        protected Field[] getFields() {
            return fields;
        }
    }
}
