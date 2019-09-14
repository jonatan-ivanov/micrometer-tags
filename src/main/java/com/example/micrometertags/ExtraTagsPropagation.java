package com.example.micrometertags;

import io.micrometer.core.instrument.Tag;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExtraTagsPropagation {
    private static final ThreadLocal<Set<Tag>> TAGS = ThreadLocal.withInitial(HashSet::new);

    public static void add(String key, Object value) {
        add(key, String.valueOf(value));
    }

    public static void add(String key, String value) {
        TAGS.get().add(Tag.of(key, value));
    }

    static Iterable<Tag> getTagsAndReset() {
        Set<Tag> tags = Collections.unmodifiableSet(TAGS.get());
        TAGS.remove();

        return tags;
    }
}
