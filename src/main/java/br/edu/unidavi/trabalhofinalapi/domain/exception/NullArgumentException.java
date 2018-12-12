package br.edu.unidavi.trabalhofinalapi.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Davi on 30/11/18.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NullArgumentException extends NullPointerException {

    public NullArgumentException(String message) {
        super(message);
    }
}
