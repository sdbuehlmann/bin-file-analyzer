package ch.puzzle.devtre.tools.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CastHelper {
    public static <T> Optional<T> tryCast(Class<T> clazz, Object candidate) {
        if (candidate.getClass().isAssignableFrom(clazz)) {
            try {
                return Optional.of((T)candidate);
            } catch (ClassCastException classCastException) {
                // do nothing
            }
        }

        return Optional.empty();
    }
}