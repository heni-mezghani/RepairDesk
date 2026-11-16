/**
 * Base URL of the Spring Boot backend.
 *
 * The backend runs on port 8089 (see application.properties) and all endpoints live
 * under /api. If you deploy elsewhere, change this one line.
 *
 * TODO (intern): for a real deployment, move this into Angular environment files
 * (src/environments/) instead of a constant.
 */
export const API_BASE_URL = 'http://192.168.0.214:8089/api';