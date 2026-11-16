package com.repair_desk_backend.exception;

/**
 * Throw this when an entity asked for by id (or any lookup) does not exist.
 *
 * <p>{@code GlobalExceptionHandler} maps it to an HTTP 404 response, so services and
 * controllers can simply do:</p>
 * <pre>
 *   clientRepository.findById(id)
 *       .orElseThrow(() -&gt; new ResourceNotFoundException("Client introuvable : " + id));
 * </pre>
 *
 * <p>It extends {@link RuntimeException} so you are not forced to declare it with
 * {@code throws}.</p>
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
