package io.novafoundation.nova.common.utils

import io.novafoundation.nova.common.R

class AvatarUtils {

    val avatars = listOf(
        R.drawable.ic_default_profile_avatar_1,
        R.drawable.ic_default_profile_avatar_2,
        R.drawable.ic_default_profile_avatar_3,
        R.drawable.ic_default_profile_avatar_4,
        R.drawable.ic_default_profile_avatar_5,
        R.drawable.ic_default_profile_avatar_6,
        R.drawable.ic_default_profile_avatar_7,
        R.drawable.ic_default_profile_avatar_8,
        R.drawable.ic_default_profile_avatar_9,
        R.drawable.ic_default_profile_avatar_10
    ).map { it.toString() }

    fun randomAvatar(): String {
        val index = (avatars.indices).random()
        return avatars[index]
    }
}
