package com.example.data.remote.firebase.model

import com.example.data.model.AnimalResident
import com.example.data.model.DailyWisdom
import com.example.data.model.FamilyMember
import com.example.data.model.Gaushala
import com.example.data.model.PanchangInfo
import com.example.data.model.Puja
import com.example.data.model.SevaContribution
import com.example.data.model.UserProfile
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Firestore DTO for `users/{userId}` document
 */
@IgnoreExtraProperties
data class FirestoreUser(
    @DocumentId val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val avatarUrl: String = "",
    val city: String = "Mumbai, India",
    val gotra: String = "Kashyapa",
    val nakshatra: String = "Rohini",
    val rashi: String = "Vrishabha (Taurus)",
    val pujasCount: Int = 0,
    val animalsCount: Int = 0,
    val totalContributedRupees: Int = 0,
    val fcmToken: String = "",
    val preferences: Map<String, Boolean> = mapOf(
        "panchangNotifications" to true,
        "pujaReminders" to true,
        "monthlySevaReminders" to true
    ),
    @ServerTimestamp val createdAt: Date? = null,
    @ServerTimestamp val updatedAt: Date? = null
) {
    fun toUserProfile(): UserProfile {
        return UserProfile(
            id = uid.ifEmpty { "primary_user" },
            name = displayName.ifEmpty { "Devotee" },
            location = city.ifEmpty { "Mumbai, India" },
            gotra = gotra.ifEmpty { "Kashyapa" },
            nakshatra = nakshatra.ifEmpty { "Rohini" },
            rashi = rashi.ifEmpty { "Vrishabha (Taurus)" },
            avatarUrl = avatarUrl.ifEmpty { "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200" },
            pujasCount = pujasCount,
            animalsCount = animalsCount,
            totalContributedRupees = totalContributedRupees
        )
    }
}

/**
 * Firestore DTO for `users/{userId}/family_members/{memberId}`
 */
@IgnoreExtraProperties
data class FirestoreFamilyMember(
    @DocumentId val id: String = "",
    val name: String = "",
    val relation: String = "Family",
    val gotra: String = "Kashyapa Gotra",
    val nakshatra: String = "Rohini",
    @ServerTimestamp val createdAt: Date? = null
) {
    fun toFamilyMember(): FamilyMember {
        return FamilyMember(
            name = name,
            relation = relation,
            gotra = gotra,
            nakshatra = nakshatra
        )
    }
}

/**
 * Firestore DTO for `users/{userId}/bookmarks/{targetId}`
 */
@IgnoreExtraProperties
data class FirestoreBookmark(
    @DocumentId val targetId: String = "",
    val targetType: String = "PUJA", // "PUJA" or "ANIMAL" or "GAUSHALA"
    @ServerTimestamp val createdAt: Date? = null
)

/**
 * Firestore DTO for `pujas/{pujaId}`
 */
@IgnoreExtraProperties
data class FirestorePuja(
    @DocumentId val id: String = "",
    val title: String = "",
    val specialTag: String = "",
    val templeName: String = "",
    val location: String = "",
    val dateTimeStr: String = "",
    val durationStr: String = "",
    val devoteesCount: String = "",
    val priceRupees: Int = 0,
    val imageUrl: String = "",
    val significance: String = "",
    val priestName: String = "",
    val priestTitle: String = "",
    val priestExp: String = "",
    val priestImageUrl: String = "",
    val category: String = "Upcoming",
    @get:PropertyName("isFeatured") val isFeatured: Boolean = false,
    @ServerTimestamp val createdAt: Date? = null
) {
    fun toPuja(isBookmarked: Boolean = false, isBooked: Boolean = false, bookedDate: String = ""): Puja {
        return Puja(
            id = id,
            title = title,
            specialTag = specialTag,
            templeName = templeName,
            location = location,
            dateTimeStr = dateTimeStr,
            durationStr = durationStr,
            devoteesCount = devoteesCount,
            priceRupees = priceRupees,
            imageUrl = imageUrl,
            significance = significance,
            priestName = priestName,
            priestTitle = priestTitle,
            priestExp = priestExp,
            priestImageUrl = priestImageUrl,
            category = category,
            isFeatured = isFeatured,
            isBooked = isBooked,
            bookedDate = bookedDate,
            isBookmarked = isBookmarked
        )
    }
}

/**
 * Firestore DTO for `gaushalas/{gaushalaId}`
 */
@IgnoreExtraProperties
data class FirestoreGaushala(
    @DocumentId val id: String = "",
    val name: String = "",
    val location: String = "",
    val state: String = "",
    val trustScorePercent: Int = 95,
    val animalsRescuedCount: Int = 0,
    val transparencyTier: String = "Gold Tier",
    val imageUrl: String = "",
    val missionQuote: String = "",
    val fodderPercent: Int = 60,
    val medicalPercent: Int = 30,
    val shelterPercent: Int = 80,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val updatesCount: Int = 2,
    @ServerTimestamp val createdAt: Date? = null
) {
    fun toGaushala(isSupported: Boolean = false): Gaushala {
        return Gaushala(
            id = id,
            name = name,
            location = location,
            state = state,
            trustScorePercent = trustScorePercent,
            animalsRescuedCount = animalsRescuedCount,
            transparencyTier = transparencyTier,
            imageUrl = imageUrl,
            missionQuote = missionQuote,
            fodderPercent = fodderPercent,
            medicalPercent = medicalPercent,
            shelterPercent = shelterPercent,
            lat = lat,
            lng = lng,
            isSupported = isSupported,
            updatesCount = updatesCount
        )
    }
}

/**
 * Firestore DTO for `animals/{animalId}`
 */
@IgnoreExtraProperties
data class FirestoreAnimal(
    @DocumentId val id: String = "",
    val gaushalaId: String = "",
    val name: String = "",
    val ageStr: String = "",
    val healthStatus: String = "Healthy",
    val healthDescription: String = "",
    val imageUrl: String = "",
    val story: String = "",
    val monthlyGoalRupees: Int = 5000,
    val raisedRupees: Int = 0,
    @get:PropertyName("isUrgent") val isUrgent: Boolean = false,
    @ServerTimestamp val createdAt: Date? = null
) {
    fun toAnimalResident(isFavorite: Boolean = false): AnimalResident {
        return AnimalResident(
            id = id,
            gaushalaId = gaushalaId,
            name = name,
            ageStr = ageStr,
            healthStatus = healthStatus,
            healthDescription = healthDescription,
            imageUrl = imageUrl,
            story = story,
            monthlyGoalRupees = monthlyGoalRupees,
            raisedRupees = raisedRupees,
            isUrgent = isUrgent,
            isFavorite = isFavorite
        )
    }
}

/**
 * Firestore DTO for `users/{userId}/puja_bookings/{bookingId}`
 */
@IgnoreExtraProperties
data class FirestorePujaBooking(
    @DocumentId val bookingId: String = "",
    val pujaId: String = "",
    val pujaTitle: String = "",
    val templeName: String = "",
    val devoteeName: String = "",
    val gotra: String = "",
    val nakshatra: String = "",
    val intentWish: String = "",
    val aiGeneratedSankalpa: String = "",
    val amountRupees: Int = 0,
    val bookingDateStr: String = "",
    val scheduledDateStr: String = "",
    val status: String = "PENDING", // PENDING, CONFIRMED, COMPLETED, CANCELLED
    val paymentStatus: String = "PENDING", // PENDING, PAID, REFUNDED (Client only writes PENDING)
    val prasadStatus: String = "PROCESSING",
    val proofVideoUrl: String? = null,
    val proofPhotoUrls: List<String> = emptyList(),
    @ServerTimestamp val timestamp: Date? = null
)

/**
 * Firestore DTO for `users/{userId}/seva_contributions/{contributionId}`
 */
@IgnoreExtraProperties
data class FirestoreSevaContribution(
    @DocumentId val contributionId: String = "",
    val title: String = "",
    val targetType: String = "GAUSHALA", // "GAUSHALA" or "ANIMAL"
    val targetId: String = "",
    val targetName: String = "",
    val amountRupees: Int = 0,
    val sevaCategory: String = "Fodder",
    val dateStr: String = "",
    val paymentStatus: String = "PENDING", // PENDING, PAID (Client creates as PENDING)
    @ServerTimestamp val timestamp: Date? = null
) {
    fun toSevaContribution(): SevaContribution {
        return SevaContribution(
            id = 0,
            title = title,
            targetType = targetType,
            targetName = targetName,
            amountRupees = amountRupees,
            sevaCategory = sevaCategory,
            dateStr = dateStr,
            timestamp = timestamp?.time ?: System.currentTimeMillis()
        )
    }
}

/**
 * Firestore DTO for `content_daily/{dateKey}`
 */
@IgnoreExtraProperties
data class FirestoreDailyContent(
    @DocumentId val dateKey: String = "",
    val tithi: String = "Shukla Paksha Dashami",
    val nakshatra: String = "Rohini Nakshatra",
    val paksha: String = "Shukla",
    val auspiciousTiming: String = "Abhijit Muhurta: 11:58 AM - 12:48 PM",
    val rahuKaal: String = "04:30 PM - 06:00 PM",
    val wisdomQuote: String = "Karma-yoga is a supreme secret indeed.",
    val wisdomSource: String = "Bhagavad Gita (Chapter 2, Verse 50)",
    val wisdomCommentary: String = "One who is engaged in devotional service rids himself of both good and bad actions even in this life. Therefore strive for Yoga, which is the art of all work.",
    val wisdomSanskritShloka: String = "बुद्धियुक्तो जहातीह उभे सुकृतदुष्कृते । तस्माद्योगाय युज्यस्व योगः कर्मसु कौशलम् ॥",
    @ServerTimestamp val createdAt: Date? = null
) {
    fun toPanchangInfo(): PanchangInfo {
        return PanchangInfo(
            tithi = tithi,
            nakshatra = nakshatra,
            paksha = paksha,
            auspiciousTiming = auspiciousTiming,
            rahuKaal = rahuKaal
        )
    }

    fun toDailyWisdom(): DailyWisdom {
        return DailyWisdom(
            quote = wisdomQuote,
            source = wisdomSource,
            commentary = wisdomCommentary,
            sanskritShloka = wisdomSanskritShloka
        )
    }
}
