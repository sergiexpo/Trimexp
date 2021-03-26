package com.neoland.trimexp.entities

import androidx.room.Embedded


data class UserWithExperience (
    @Embedded var experience: Experience,
    @Embedded var user : User

        )