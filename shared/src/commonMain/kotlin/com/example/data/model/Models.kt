package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pujas")
data class Puja(
    @PrimaryKey val id: String,
    val title: String,
    val specialTag: String = "",
    val templeName: String,
    val location: String,
    val dateTimeStr: String,
    val durationStr: String,
    val devoteesCount: String,
    val priceRupees: Int,
    val imageUrl: String,
    val significance: String,
    val priestName: String,
    val priestTitle: String,
    val priestExp: String,
    val priestImageUrl: String,
    val category: String = "Upcoming", // Upcoming, Popular, By Temple, Special
    val isFeatured: Boolean = false,
    val isBooked: Boolean = false,
    val bookedGotra: String = "",
    val bookedDevoteeName: String = "",
    val bookedDate: String = "",
    val isBookmarked: Boolean = false
)

@Entity(tableName = "gaushalas")
data class Gaushala(
    @PrimaryKey val id: String,
    val name: String,
    val location: String,
    val state: String,
    val trustScorePercent: Int,
    val animalsRescuedCount: Int,
    val transparencyTier: String, // Gold Tier, Silver Tier, Platinum Tier
    val imageUrl: String,
    val missionQuote: String,
    val fodderPercent: Int,
    val medicalPercent: Int,
    val shelterPercent: Int,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val isSupported: Boolean = false,
    val updatesCount: Int = 2
)

@Entity(tableName = "animal_residents")
data class AnimalResident(
    @PrimaryKey val id: String,
    val gaushalaId: String,
    val name: String,
    val ageStr: String,
    val healthStatus: String, // Recovering, Healthy, Critical Care
    val healthDescription: String,
    val imageUrl: String,
    val story: String,
    val monthlyGoalRupees: Int,
    val raisedRupees: Int,
    val isUrgent: Boolean = false,
    val isFavorite: Boolean = false,
    val breed: String = "Desi"
) {
    val fundedPercent: Int
        get() = if (monthlyGoalRupees > 0) ((raisedRupees.toDouble() / monthlyGoalRupees) * 100).toInt().coerceIn(0, 100) else 0

    val neededRupees: Int
        get() = (monthlyGoalRupees - raisedRupees).coerceAtLeast(0)
}

data class WelfareUpdate(
    val id: String = "",
    val gaushalaId: String = "",
    val animalId: String? = null,
    val dateStr: String = "",
    val eventType: String = "General", // "Medical", "Feeding", "Rescue", "Shelter", "Milestone"
    val title: String = "",
    val description: String = "",
    val outcome: String? = null,
    val mediaUrl: String? = null,
    val timestamp: Long = 0L
)

@Entity(tableName = "seva_contributions")
data class SevaContribution(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val targetType: String, // "GAUSHALA" or "ANIMAL"
    val targetName: String,
    val amountRupees: Int,
    val sevaCategory: String, // "Fodder", "Medical", "Shelter", "Custom"
    val dateStr: String,
    val timestamp: Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
) {
    val category: String
        get() = sevaCategory
}

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "",
    val name: String = "Devotee",
    val location: String = "Mumbai, India",
    val gotra: String = "Kashyapa",
    val nakshatra: String = "Rohini",
    val rashi: String = "Vrishabha (Taurus)",
    val avatarUrl: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuADMpwdiC3G39nZ2nCVMn1bu8eDkd-3LPnmDVmZE5DcpABkusRUlmUrb0Yx7s99SDwQmdZ7t-XryMbqL_ttMe9qKUMbq6bOB7gPdteFOZh3_SMUSOW0SsgLeVBFvVZbZ86bvDC_ev_sM0xGJ4QXFLAPVGss6lH21rGPoWe5zKFrE7iMlHp4IH8Gl5AF2ouLuKdTNYZsa-XeXUZMEbCYEOJibs5pF9e99L6H_IfWGWiZRYLNK3NrkAc",
    val pujasCount: Int = 12,
    val animalsCount: Int = 5,
    val totalContributedRupees: Int = 5200
) {
    val city: String
        get() = location

    val animalsSupportedCount: Int
        get() = animalsCount
}

data class FamilyMember(
    val name: String,
    val relation: String,
    val gotra: String,
    val nakshatra: String = "Rohini"
)

data class DailyWisdom(
    val quote: String,
    val source: String,
    val commentary: String,
    val sanskritShloka: String = ""
)

data class PanchangInfo(
    val tithi: String,
    val nakshatra: String,
    val paksha: String,
    val auspiciousTiming: String,
    val rahuKaal: String
)

data class ChatMessage(
    val id: String = kotlin.random.Random.nextLong().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),
    val isStreaming: Boolean = false
)
