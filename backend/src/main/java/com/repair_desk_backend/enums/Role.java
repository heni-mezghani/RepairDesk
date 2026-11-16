package com.repair_desk_backend.enums;

/**
 * The two roles a user of RepairDesk can have.
 *
 * <p>Spring Security expects authorities to be prefixed with "ROLE_". We store the
 * raw enum name here (e.g. "ADMINISTRATEUR") and add the "ROLE_" prefix later, in
 * {@code CustomUserDetailsService}, when we build the Spring {@code UserDetails}.</p>
 */
public enum Role {

    /** A workshop technician: handles the day-to-day repair work. */
    TECHNICIEN,

    /** An administrator: full access, including user management. */
    ADMINISTRATEUR
}
