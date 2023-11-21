package com.cmtelematics.cmtreferenceapp.common.model

/**
 * Domain representation of Android specific permission states (Undetermined, Allowed, TrapDoor).
 */
enum class PermissionState {

    /**
     * Represents a [Undetermined] type [PermissionState].
     *
     * [Undetermined] state means that all of the followings should be true:
     *
     * * User did not see the system permission prompt dialog yet,
     * * A given [PermissionType] is in the default system permission state (Denied).
     */
    Undetermined,

    /**
     * Represents an [Allowed] type [PermissionState].
     *
     * [Allowed] state means that all of the followings should be true:
     *
     * * User has enabled (or allowed) the system permission for a given [PermissionType].
     */
    Allowed,

    /**
     * Represents an [PartiallyAllowed] type [PermissionState].
     *
     * [PartiallyAllowed] state means that all of the followings should be true:
     *
     * * In case, when a [PermissionType] contains more than one system permissions,
     *   and User has enabled (or allowed) at least one system permission for a given [PermissionType].
     */
    PartiallyAllowed,

    /**
     * Represents a [TrapDoor] type [PermissionState].
     *
     * [TrapDoor] state means that all of the followings should be true:
     *
     * * User has seen (at least once) the system permission prompt dialog,
     * * User has selected 'Deny & don't ask again' on the system permission prompt dialog.
     */
    TrapDoor
}
