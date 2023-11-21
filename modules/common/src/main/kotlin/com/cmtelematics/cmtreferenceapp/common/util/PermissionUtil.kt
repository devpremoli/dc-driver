package com.cmtelematics.cmtreferenceapp.common.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.cmtelematics.cmtreferenceapp.common.mapper.toPermissions
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType

fun isPermissionAllowed(context: Context, permissionType: PermissionType): Boolean =
    permissionType.toPermissions().all { checkSelfPermission(context, it) }

fun isPartiallyAllowed(context: Context, permissionType: PermissionType): Boolean =
    permissionType.toPermissions().any { checkSelfPermission(context, it) }

private fun checkSelfPermission(context: Context, permission: String) =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
