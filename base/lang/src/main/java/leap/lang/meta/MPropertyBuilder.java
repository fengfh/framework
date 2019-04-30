/*
 * Copyright 2015 the original author or authors.
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
package leap.lang.meta;

import leap.lang.beans.BeanProperty;

public class MPropertyBuilder extends MNamedWithDescBuilder<MProperty> {

    protected MType        type;
    protected BeanProperty beanProperty;
    protected boolean      identity;
    protected boolean      unique;
    protected Boolean      required;
    protected String       defaultValue;
    protected String[]     enumValues;
    protected boolean      fixedLength;
    protected Integer      length;
    protected Integer      precision;
    protected Integer      scale;
    protected boolean      discriminator;
    protected Boolean      selectable;
    protected Boolean      aggregatable;
    protected Boolean      groupable;
    protected Boolean      creatable;
    protected Boolean      updatable;
    protected Boolean      sortable;
    protected Boolean      filterable;
    protected boolean      reference;

    public MType getType() {
        return type;
    }

    public void setType(MType type) {
        this.type = type;
    }

    public BeanProperty getBeanProperty() {
        return beanProperty;
    }

    public void setBeanProperty(BeanProperty beanProperty) {
        this.beanProperty = beanProperty;
    }

    public boolean isIdentity() {
        return identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String[] getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(String[] enumValues) {
        this.enumValues = enumValues;
    }

    public boolean isFixedLength() {
        return fixedLength;
    }

    public void setFixedLength(boolean fixedLength) {
        this.fixedLength = fixedLength;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public boolean isDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(boolean discriminator) {
        this.discriminator = discriminator;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public Boolean getAggregatable() {
        return aggregatable;
    }

    public void setAggregatable(Boolean aggregatable) {
        this.aggregatable = aggregatable;
    }

    public Boolean getGroupable() {
        return groupable;
    }

    public void setGroupable(Boolean groupable) {
        this.groupable = groupable;
    }

    public Boolean getCreatable() {
        return creatable;
    }

    public void setCreatable(Boolean creatable) {
        this.creatable = creatable;
    }

    public Boolean getUpdatable() {
        return updatable;
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public Boolean getFilterable() {
        return filterable;
    }

    public void setFilterable(Boolean filterable) {
        this.filterable = filterable;
    }

    public boolean isReference() {
        return reference;
    }

    public void setReference(boolean reference) {
        this.reference = reference;
    }

    @Override
    public MProperty build() {
        return new MProperty(name, title, summary, description, type, beanProperty, identity, unique, required,
                             defaultValue, enumValues, fixedLength, length, precision, scale,
                             discriminator, selectable, aggregatable, groupable,
                             creatable, updatable, sortable, filterable, reference);
    }

}
