

package com.lamfire.jmongo.internal;

import com.lamfire.jmongo.mapping.MappedClass;
import com.lamfire.jmongo.mapping.MappedField;
import com.lamfire.jmongo.mapping.Mapper;
import com.lamfire.jmongo.query.ValidationException;

import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;



public class PathTarget {
    private final String path;
    private final List<String> segments;
    private boolean validateNames = true;
    private int position;
    private Mapper mapper;
    private MappedClass context;
    private MappedClass root;
    private MappedField target;
    private boolean resolved = false;


    public PathTarget(final Mapper mapper, final MappedClass root, final String path) {
        this.root = root;
        segments = asList(path.split("\\."));
        this.mapper = mapper;
        this.path = path;
    }


    public void disableValidation() {
        resolved = false;
        validateNames = false;
    }

    private boolean hasNext() {
        return position < segments.size();
    }


    public String translatedPath() {
        if (!resolved) {
            resolve();
        }
        return JoinUtils.join(segments, '.');
    }


    public MappedField getTarget() {
        if (!resolved) {
            resolve();
        }
        return target;
    }

    String next() {
        return segments.get(position++);
    }

    private void resolve() {
        context = this.root;
        position = 0;
        MappedField field = null;
        while (hasNext()) {
            String segment = next();

            if (segment.equals("$") || segment.matches("[0-9]+")) {  // array operator
                if (!hasNext()) {
                    return;
                }
                segment = next();
            }
            field = resolveField(segment);

            if (field != null) {
                translate(field.getNameToStore());
                if (field.isMap() && hasNext()) {
                    next();  // consume the map key segment
                }
            } else {
                if (validateNames) {
                    throw new ValidationException(String.format("Could not resolve path '%s' against '%s'.", JoinUtils.join(segments, '.'),
                                                         root.getClazz().getName()));
                }
            }
        }
        target = field;
        resolved = true;
    }

    private void translate(final String nameToStore) {
        segments.set(position - 1, nameToStore);
    }

    private MappedField resolveField(final String segment) {
        MappedField mf = context.getMappedField(segment);
        if (mf == null) {
            mf = context.getMappedFieldByJavaField(segment);
        }
        if (mf == null) {
            Iterator<MappedClass> subTypes = mapper.getSubTypes(context).iterator();
            while (mf == null && subTypes.hasNext()) {
                context = subTypes.next();
                mf = resolveField(segment);
            }
        }

        if (mf != null) {
            context = mapper.getMappedClass(mf.getSubClass() != null ? mf.getSubClass() : mf.getConcreteType());
        }
        return mf;
    }

    @Override
    public String toString() {
        return String.format("PathTarget{root=%s, segments=%s, target=%s}", root.getClazz().getSimpleName(), segments, target);
    }
}
