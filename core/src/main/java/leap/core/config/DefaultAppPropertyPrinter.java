/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package leap.core.config;

import leap.lang.Strings;
import leap.lang.logging.LogUtils;
import leap.lang.resource.Resource;
import leap.lang.text.PrintFormat;

import java.io.PrintWriter;
import java.util.*;

public class DefaultAppPropertyPrinter implements AppPropertyPrinter {

    protected static final String COLUMN_SEPARATOR = " ";

    protected boolean printSystem = true;

    public boolean isPrintSystem() {
        return printSystem;
    }

    public void setPrintSystem(boolean printSystem) {
        this.printSystem = printSystem;
    }

    @Override
    public void printProperties(Collection<AppProperty> props, PrintWriter writer) {
        PrintInfo info = new PrintInfo();

        prepare(props, info);

        PrintFormat nameFormat   = new PrintFormat(info.maxNameLength,   PrintFormat.JUST_LEFT);
        PrintFormat valueFormat  = new PrintFormat(info.maxValueLength,  PrintFormat.JUST_LEFT);
        PrintFormat sourceFormat = new PrintFormat(info.maxSourceLength, PrintFormat.JUST_LEFT);

        printHeader(writer, nameFormat, valueFormat, sourceFormat);

        info.props.forEach((p) -> printProperty(writer, nameFormat, valueFormat, sourceFormat, p));
    }

    protected void prepare(Collection<AppProperty> props, PrintInfo info) {
        props.forEach(p -> {

            if(!printSystem && p.isSystem()) {
                return;
            }

            PrintProperty pp = toPrintProperty(p);

            info.maxNameLength   = Math.max(info.maxNameLength,   pp.name.length());
            info.maxValueLength  = Math.max(info.maxValueLength,  pp.value.length());
            info.maxSourceLength = Math.max(info.maxSourceLength, pp.source.length());

            info.props.add(pp);
        });
    }

    protected void printHeader(PrintWriter writer, PrintFormat nameFormat, PrintFormat valueFormat, PrintFormat sourceFormat){
        StringBuilder header = new StringBuilder();

        header.append(nameFormat.format("NAME")).append(COLUMN_SEPARATOR);
        header.append(valueFormat.format("VALUE")).append(COLUMN_SEPARATOR);
        header.append(sourceFormat.format("SOURCE"));

        writer.println(header.toString());

        StringBuilder line = new StringBuilder();
        line.append(Strings.repeat('-', nameFormat.maxChars())).append(COLUMN_SEPARATOR)
                .append(Strings.repeat('-', valueFormat.maxChars())).append(COLUMN_SEPARATOR)
                .append(Strings.repeat('-', sourceFormat.maxChars()));

        writer.println(line.toString());
    }

    protected void printProperty(PrintWriter writer, PrintFormat nameFormat, PrintFormat valueFormat, PrintFormat sourceFormat, PrintProperty p){
        StringBuilder sb = new StringBuilder();

        sb.append(nameFormat.format(p.name)).append(COLUMN_SEPARATOR);
        sb.append(valueFormat.format(p.value)).append(COLUMN_SEPARATOR);
        sb.append(sourceFormat.format(p.source));

        writer.println(sb.toString());
    }

    protected PrintProperty toPrintProperty(AppProperty p) {
        PrintProperty pp = new PrintProperty();

        pp.name  = p.getName();
        pp.value = Strings.replace(Strings.abbreviate(p.getUnprocessedValue(), 30, ".."),"\n","\\n");

        if(null == p.getSource()) {
            pp.source = "(n/a)";
        }else if(p.getSource() instanceof Resource) {
            pp.source = LogUtils.getUrl((Resource)p.getSource());
        }else if(p.getSource() instanceof Class) {
            pp.source = ((Class) p.getSource()).getSimpleName();
        }else if(p.getSource() instanceof String) {
            pp.source = (String)p.getSource();
        }else {
            pp.source = p.getSource().getClass().getSimpleName();
        }

        pp.source = Strings.right(pp.source, 65);
        pp.system = p.isSystem();

        return pp;
    }

    protected static final class PrintInfo {
        int maxNameLength;
        int maxValueLength;
        int maxSourceLength;
        Set<PrintProperty> props = new TreeSet<>(new PrintPropertyComparator());
    }

    protected static final class PrintProperty {
        String  name;
        String  value;
        String  source;
        boolean system;
    }

    protected static final class PrintPropertyComparator implements Comparator<PrintProperty> {
        @Override
        public int compare(PrintProperty o1, PrintProperty o2) {
            if(o1.system && !o2.system) {
                return -1;
            }else if(o2.system && !o1.system) {
                return 1;
            }

            return String.CASE_INSENSITIVE_ORDER.compare(o1.name, o2.name);
        }
    }
}