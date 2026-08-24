package com.example.data.remote.firebase

import android.util.Log
import com.example.data.model.AnimalResident
import com.example.data.model.DailyWisdom
import com.example.data.model.Gaushala
import com.example.data.model.PanchangInfo
import com.example.data.model.Puja
import com.example.data.remote.firebase.model.FirestoreAnimal
import com.example.data.remote.firebase.model.FirestoreDailyContent
import com.example.data.remote.firebase.model.FirestoreGaushala
import com.example.data.remote.firebase.model.FirestorePuja
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface FirestoreCatalogRepository {
    suspend fun getPujas(): Result<List<FirestorePuja>>
    suspend fun getGaushalas(): Result<List<FirestoreGaushala>>
    suspend fun getAnimals(): Result<List<FirestoreAnimal>>
    suspend fun getDailyContent(dateKey: String): Result<FirestoreDailyContent?>
    suspend fun seedInitialDataIfEmpty(): Result<Unit>
}

class DefaultFirestoreCatalogRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : FirestoreCatalogRepository {

    private val TAG = "FirestoreCatalogRepo"

    override suspend fun getPujas(): Result<List<FirestorePuja>> {
        return try {
            val snapshot = firestore.collection("pujas").get().await()
            val pujas = snapshot.documents.mapNotNull { it.toObject(FirestorePuja::class.java) }
            Result.success(pujas)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get pujas from Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getGaushalas(): Result<List<FirestoreGaushala>> {
        return try {
            val snapshot = firestore.collection("gaushalas").get().await()
            val gaushalas = snapshot.documents.mapNotNull { it.toObject(FirestoreGaushala::class.java) }
            Result.success(gaushalas)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get gaushalas from Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getAnimals(): Result<List<FirestoreAnimal>> {
        return try {
            val snapshot = firestore.collection("animals").get().await()
            val animals = snapshot.documents.mapNotNull { it.toObject(FirestoreAnimal::class.java) }
            Result.success(animals)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get animals from Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getDailyContent(dateKey: String): Result<FirestoreDailyContent?> {
        return try {
            val doc = firestore.collection("content_daily").document(dateKey).get().await()
            Result.success(doc.toObject(FirestoreDailyContent::class.java))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get daily content from Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun seedInitialDataIfEmpty(): Result<Unit> {
        return try {
            // Seed Pujas idempotently using deterministic document IDs
            val pujasCollection = firestore.collection("pujas")
            val existingPujas = pujasCollection.limit(1).get().await()
            if (existingPujas.isEmpty) {
                Log.d(TAG, "Seeding Firestore Pujas collection...")
                val initialPujas = getSeedPujas()
                val batch = firestore.batch()
                initialPujas.forEach { puja ->
                    val docRef = pujasCollection.document(puja.id)
                    batch.set(docRef, puja, SetOptions.merge())
                }
                batch.commit().await()
            }

            // Seed Gaushalas idempotently
            val gaushalasCollection = firestore.collection("gaushalas")
            val existingGaushalas = gaushalasCollection.limit(1).get().await()
            if (existingGaushalas.isEmpty) {
                Log.d(TAG, "Seeding Firestore Gaushalas collection...")
                val initialGaushalas = getSeedGaushalas()
                val batch = firestore.batch()
                initialGaushalas.forEach { g ->
                    val docRef = gaushalasCollection.document(g.id)
                    batch.set(docRef, g, SetOptions.merge())
                }
                batch.commit().await()
            }

            // Seed Animals idempotently
            val animalsCollection = firestore.collection("animals")
            val existingAnimals = animalsCollection.limit(1).get().await()
            if (existingAnimals.isEmpty) {
                Log.d(TAG, "Seeding Firestore Animals collection...")
                val initialAnimals = getSeedAnimals()
                val batch = firestore.batch()
                initialAnimals.forEach { a ->
                    val docRef = animalsCollection.document(a.id)
                    batch.set(docRef, a, SetOptions.merge())
                }
                batch.commit().await()
            }

            // Seed today's content_daily
            val todayKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val contentDoc = firestore.collection("content_daily").document(todayKey)
            val existingContent = contentDoc.get().await()
            if (!existingContent.exists()) {
                val daily = FirestoreDailyContent(dateKey = todayKey)
                contentDoc.set(daily, SetOptions.merge()).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.w(TAG, "Firestore seeding skipped or encountered error (e.g. offline): ${e.message}")
            Result.failure(e)
        }
    }

    private fun getSeedPujas(): List<FirestorePuja> = listOf(
        FirestorePuja(
            id = "maha_rudrabhishek",
            title = "Maha Rudrabhishek",
            specialTag = "Maha Shivratri Special",
            templeName = "Kashi Vishwanath Temple, Varanasi",
            location = "Kashi Vishwanath Temple, Varanasi",
            dateTimeStr = "Mar 8, 5:30 AM IST",
            durationStr = "2.5 Hours",
            devoteesCount = "12.5k Devotees attending",
            priceRupees = 2501,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAOpqdNo42gJqDM8nBf0S-Hb22Q9tRGrWdjusk87abF3ywTcszYbUaaDwRjZ_O_q-L2aYmVY-Bl7IMXpzwqEvGnZrvr_f4OS3kecYgV_4HmNM5kAN2eXQHxpTzuQkJap3ol57kWh_-GN5ta8UgbQmOSYA96-mD2TrmVWP_V5LifI_9TKkIUiULJzAOIdNlzp0WzaSFwk-yWZdM0YCjvUY3f_9hnSGknRRk3soO_gznmp0zCUtITN40",
            significance = "The Maha Rudrabhishek is a highly potent Vedic ritual dedicated to Lord Shiva. It involves bathing the Shiva Lingam with sacred offerings like milk, honey, ghee, and Gangajal while chanting the powerful Rudra Suktam. This sacred practice is believed to remove deeply rooted negative karmas, bring immense peace, and foster spiritual growth and material prosperity.",
            priestName = "Pt. Rameshwar Shastri",
            priestTitle = "Chief Priest, Kashi Vishwanath Mandir Trust",
            priestExp = "30+ Years Experience in Vedic Rituals",
            priestImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCHeevcOISsYI1rtQI5r8he_R9P2uJpKBOE6VUu71jI4ra8jF_28mBO-Th5BeXpeoLCWVh8tdSW6fTSRM4vmeqtmyiEQFOL3Rj5XkEm-La-YtrVpfVVgm8s0OuDVrHUtLLRWCPmvl8waWJle8wE8Lsu4CtrdD83EwAYMfuou2oRSJBi4Hf_hrFFZz3RAS2xginOAPp8jIWO-KzgIgwN_YUtk9JAZrRo6GYyfAIya7ov45LgiyCWw-4",
            category = "Upcoming",
            isFeatured = true
        ),
        FirestorePuja(
            id = "navagraha_shanti",
            title = "Navagraha Shanti",
            specialTag = "Popular",
            templeName = "Trimbakeshwar Temple, Nashik",
            location = "Trimbakeshwar Temple, Nashik",
            dateTimeStr = "Every Tuesday, 7:00 AM IST",
            durationStr = "3 Hours",
            devoteesCount = "1.2k Devotees",
            priceRupees = 1801,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCiOZCDJREiux2pK9I8vnrVJmJjpfKehH4pRC27fdQurVc07SxuFCEIS1H3auIdRI75sUxZZN9UvnHmIM2UrmRAteABz_DE0McuuXxH5FhZM1eT4O50vKSWTb09cOrX5mPt80vzwpcJLb_kvyJAExWO_YQcmgDDI2TNquH06hGPQzSlLkqfbOPesT2lZ9lojhwGhsHP2UdRtpxmm5QO0KButOKeFwrkU55MqcNJzdOOBbpOm8LocEE",
            significance = "Navagraha Shanti homa balances planetary energies, removes astrological doshas, and invites harmony, good health, and professional prosperity into your household.",
            priestName = "Acharya Vidyadhar Guruji",
            priestTitle = "Vedacharya, Trimbakeshwar Jyotirlinga",
            priestExp = "25+ Years Experience",
            priestImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCHeevcOISsYI1rtQI5r8he_R9P2uJpKBOE6VUu71jI4ra8jF_28mBO-Th5BeXpeoLCWVh8tdSW6fTSRM4vmeqtmyiEQFOL3Rj5XkEm-La-YtrVpfVVgm8s0OuDVrHUtLLRWCPmvl8waWJle8wE8Lsu4CtrdD83EwAYMfuou2oRSJBi4Hf_hrFFZz3RAS2xginOAPp8jIWO-KzgIgwN_YUtk9JAZrRo6GYyfAIya7ov45LgiyCWw-4",
            category = "Popular"
        ),
        FirestorePuja(
            id = "satyanarayan_katha",
            title = "Satyanarayan Katha",
            specialTag = "Special",
            templeName = "Online Sankalp",
            location = "Online Sankalp",
            dateTimeStr = "Purnima (Full Moon), 6:00 PM",
            durationStr = "1.5 Hours",
            devoteesCount = "850 Devotees",
            priceRupees = 1101,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBi_JP9tQ0dNw2yhnlQ6gkauFsmbIsm1MhGOXi7rgOJX60mw1G_grFCmSfeuKSbSuV4zONzJNiAY3XSrnud8PulQgQEzgEdqw2vUtDH-zWUQqx0rtbIk2qeZOIEVncp0u1XqK5FN-R2ezMS0sJYt4e5vtmB365bFwitV6sC61SJqO-atI47GbbuDIcnnUbcPFXRIba0COGXLicnttd-jpY95lUSneYIMQFoxwh_qH11v42ByUWka3U",
            significance = "Sri Satyanarayan Puja is performed to seek the blessings of Lord Vishnu for domestic happiness, resolution of hurdles, and fulfillment of auspicious wishes.",
            priestName = "Pt. Hariprasad Joshi",
            priestTitle = "Vedic Scholar",
            priestExp = "20+ Years Experience",
            priestImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCHeevcOISsYI1rtQI5r8he_R9P2uJpKBOE6VUu71jI4ra8jF_28mBO-Th5BeXpeoLCWVh8tdSW6fTSRM4vmeqtmyiEQFOL3Rj5XkEm-La-YtrVpfVVgm8s0OuDVrHUtLLRWCPmvl8waWJle8wE8Lsu4CtrdD83EwAYMfuou2oRSJBi4Hf_hrFFZz3RAS2xginOAPp8jIWO-KzgIgwN_YUtk9JAZrRo6GYyfAIya7ov45LgiyCWw-4",
            category = "By Temple"
        )
    )

    private fun getSeedGaushalas(): List<FirestoreGaushala> = listOf(
        FirestoreGaushala(
            id = "shri_krishna_gaushala",
            name = "Shri Krishna Gaushala",
            location = "Vrindavan",
            state = "Uttar Pradesh",
            trustScorePercent = 98,
            animalsRescuedCount = 450,
            transparencyTier = "Gold Tier",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB-rVXkCb9UZsqmd1VN9FZaoCFYEKr0UYrWaDyMz4G5cu0RqLrysF4jCD-jPZip07NznS7G4GYt5pMurcrhIP1Vn2VqtcMgctuUSfzpu0OAA-Ipdvk4r9D_XwkMJ2K5aTB-vzNLlPL27fnJi7NDA6l0tt9JgyHfOldp3b_fgayEo2iuSVb83FYpJkpPx7mpsuLChQSxvGKg3ruya1Ct_DI0pSA5A4pV9313MQnmkwstgP8clw8ThPQ",
            missionQuote = "Providing a lifelong, loving sanctuary for abandoned and injured cows. We believe every being deserves dignity, medical care, and a peaceful environment to thrive.",
            fodderPercent = 65,
            medicalPercent = 40,
            shelterPercent = 85,
            lat = 27.58,
            lng = 77.70,
            updatesCount = 2
        ),
        FirestoreGaushala(
            id = "gopashtami_sanctuary",
            name = "Gopashtami Sanctuary",
            location = "Pushkar",
            state = "Rajasthan",
            trustScorePercent = 94,
            animalsRescuedCount = 310,
            transparencyTier = "Silver Tier",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC93W4mZ41E9rZ6R2jQp6aY88Ew6mH3wXoK6B6cZ0JgM7nL7vA2v9zN6yC8p4rX2mK9vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1",
            missionQuote = "Dedicated to the rescue and organic care of native indigenous Gir and Tharparkar cow breeds across the sacred Pushkar desert region.",
            fodderPercent = 50,
            medicalPercent = 55,
            shelterPercent = 70,
            lat = 26.49,
            lng = 74.55,
            updatesCount = 3
        ),
        FirestoreGaushala(
            id = "ganga_surabhi_dham",
            name = "Ganga Surabhi Dham",
            location = "Rishikesh",
            state = "Uttarakhand",
            trustScorePercent = 99,
            animalsRescuedCount = 620,
            transparencyTier = "Gold Tier",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB-rVXkCb9UZsqmd1VN9FZaoCFYEKr0UYrWaDyMz4G5cu0RqLrysF4jCD-jPZip07NznS7G4GYt5pMurcrhIP1Vn2VqtcMgctuUSfzpu0OAA-Ipdvk4r9D_XwkMJ2K5aTB-vzNLlPL27fnJi7NDA6l0tt9JgyHfOldp3b_fgayEo2iuSVb83FYpJkpPx7mpsuLChQSxvGKg3ruya1Ct_DI0pSA5A4pV9313MQnmkwstgP8clw8ThPQ",
            missionQuote = "Holistic rehabilitation combining natural Himalayan herbal treatments, fresh mountain grazing, and daily spiritual aarti for Gomata.",
            fodderPercent = 75,
            medicalPercent = 60,
            shelterPercent = 90,
            lat = 30.08,
            lng = 78.26,
            updatesCount = 5
        )
    )

    private fun getSeedAnimals(): List<FirestoreAnimal> = listOf(
        FirestoreAnimal(
            id = "nandi_01",
            gaushalaId = "shri_krishna_gaushala",
            name = "Nandi",
            ageStr = "3.5 Years",
            healthStatus = "Recovering",
            healthDescription = "Fractured left leg, currently on splint physiotherapy & daily herbal poultice.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA9kQ_J56K6b-67zQh6yM0GvJ7n8yC_8rT3pLmK6jN2v9bA4xZ7c8vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL",
            story = "Rescued from a severe highway collision near Mathura. Thanks to timely veterinary attention and daily care by loving volunteers, Nandi is now standing on his own and walking gently with support.",
            monthlyGoalRupees = 5000,
            raisedRupees = 3250,
            isUrgent = true
        ),
        FirestoreAnimal(
            id = "surabhi_02",
            gaushalaId = "shri_krishna_gaushala",
            name = "Surabhi",
            ageStr = "5 Years",
            healthStatus = "Healthy",
            healthDescription = "Active, gentle mother cow with a calf; thrives on green grass & jaggery.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD3pZ1nL8vA4xZ7c8vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM",
            story = "Surabhi was brought to the sanctuary after losing her way during a storm. She has since given birth to a healthy calf, Gopal, and is one of the friendliest residents in the sanctuary.",
            monthlyGoalRupees = 4000,
            raisedRupees = 4000,
            isUrgent = false
        ),
        FirestoreAnimal(
            id = "kamadhenu_03",
            gaushalaId = "ganga_surabhi_dham",
            name = "Gauri",
            ageStr = "8 Years",
            healthStatus = "Critical Care",
            healthDescription = "Elderly cow with severe cataract & joint arthritis requiring continuous medical care.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB_9yC8p4rX2mK9vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1_vN6tY7uB3c8zV2mJ9qP1_vN6tY",
            story = "An elder Gomata rescued from abandonment. She receives special soft boiled mash, ayurvedic oil joint massages, and round-the-clock shelter attendant support.",
            monthlyGoalRupees = 6500,
            raisedRupees = 2100,
            isUrgent = true
        )
    )
}
