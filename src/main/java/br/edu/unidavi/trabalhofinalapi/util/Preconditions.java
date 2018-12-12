package br.edu.unidavi.trabalhofinalapi.util;

import br.edu.unidavi.trabalhofinalapi.domain.exception.EntityNotFoundException;
import br.edu.unidavi.trabalhofinalapi.domain.exception.InvalidArgumentException;
import br.edu.unidavi.trabalhofinalapi.domain.exception.NullArgumentException;

import java.util.Objects;

/**
 * Created by Davi on 11/12/18.
 */
public interface Preconditions {

    static <T> T checkNotNull(T value, String message) {
        if (Objects.isNull(value)) {
            throw new NullArgumentException(message);
        }
        return value;
    }

    static <T> T checkEntityNotFound(T value, String message) {
        if (Objects.isNull(value)) {
            throw new EntityNotFoundException(message);
        }
        return value;
    }

    static void checkArgument(boolean valid, String message) {
        if (!valid) {
            throw new InvalidArgumentException(message);
        }
    }
}
