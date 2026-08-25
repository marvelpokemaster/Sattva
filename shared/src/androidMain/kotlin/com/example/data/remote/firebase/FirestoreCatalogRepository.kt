package com.example.data.remote.firebase

import android.util.Log
import com.example.data.remote.firebase.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreCatalogRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : CatalogRepository {

    private val TAG = "FirestoreCatalogRepo"

    override fun observePujas(): Flow<List<FirestorePuja>> = callbackFlow {
        val registration: ListenerRegistration = firestore.collection("pujas")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing pujas: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val pujas = snapshot.documents.mapNotNull { it.toObject(FirestorePuja::class.java) }
                    trySend(pujas)
                }
            }
        awaitClose { registration.remove() }
    }

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

    override fun observeGaushalas(): Flow<List<FirestoreGaushala>> = callbackFlow {
        val registration: ListenerRegistration = firestore.collection("gaushalas")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing gaushalas: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val gaushalas = snapshot.documents.mapNotNull { it.toObject(FirestoreGaushala::class.java) }
                    trySend(gaushalas)
                }
            }
        awaitClose { registration.remove() }
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

    override suspend fun getAnimals(gaushalaId: String?): Result<List<FirestoreAnimal>> {
        return try {
            val query = if (gaushalaId != null) {
                firestore.collection("animals").whereEqualTo("gaushalaId", gaushalaId)
            } else {
                firestore.collection("animals")
            }
            val snapshot = query.get().await()
            val animals = snapshot.documents.mapNotNull { it.toObject(FirestoreAnimal::class.java) }
            Result.success(animals)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get animals from Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    override fun observeAnimals(gaushalaId: String?): Flow<List<FirestoreAnimal>> = callbackFlow {
        val query = if (gaushalaId != null) {
            firestore.collection("animals").whereEqualTo("gaushalaId", gaushalaId)
        } else {
            firestore.collection("animals")
        }
        val registration: ListenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error observing animals: ${error.message}")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val animals = snapshot.documents.mapNotNull { it.toObject(FirestoreAnimal::class.java) }
                trySend(animals)
            }
        }
        awaitClose { registration.remove() }
    }

    override fun observeWelfareUpdates(gaushalaId: String?, animalId: String?): Flow<List<FirestoreWelfareUpdate>> = callbackFlow {
        var query: com.google.firebase.firestore.Query = firestore.collection("welfare_updates")
        if (gaushalaId != null) {
            query = query.whereEqualTo("gaushalaId", gaushalaId)
        }
        if (animalId != null) {
            query = query.whereEqualTo("animalId", animalId)
        }
        val registration: ListenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error observing welfare updates: ${error.message}")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val updates = snapshot.documents.mapNotNull { it.toObject(FirestoreWelfareUpdate::class.java) }
                trySend(updates)
            }
        }
        awaitClose { registration.remove() }
    }

    override suspend fun getWelfareUpdates(gaushalaId: String?, animalId: String?): Result<List<FirestoreWelfareUpdate>> {
        return try {
            var query: com.google.firebase.firestore.Query = firestore.collection("welfare_updates")
            if (gaushalaId != null) {
                query = query.whereEqualTo("gaushalaId", gaushalaId)
            }
            if (animalId != null) {
                query = query.whereEqualTo("animalId", animalId)
            }
            val snapshot = query.get().await()
            val updates = snapshot.documents.mapNotNull { it.toObject(FirestoreWelfareUpdate::class.java) }
            Result.success(updates)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get welfare updates: ${e.message}")
            Result.failure(e)
        }
    }

    override fun observeDailyWisdom(): Flow<List<FirestoreDailyContent>> = callbackFlow {
        val registration: ListenerRegistration = firestore.collection("content_daily")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error observing daily content: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val content = snapshot.documents.mapNotNull { it.toObject(FirestoreDailyContent::class.java) }
                    trySend(content)
                }
            }
        awaitClose { registration.remove() }
    }

    override suspend fun getPuja(id: String): FirestorePuja? {
        return try {
            val doc = firestore.collection("pujas").document(id).get().await()
            doc.toObject(FirestorePuja::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getGaushala(id: String): FirestoreGaushala? {
        return try {
            val doc = firestore.collection("gaushalas").document(id).get().await()
            doc.toObject(FirestoreGaushala::class.java)
        } catch (e: Exception) {
            null
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

            // Seed Welfare Updates idempotently
            val welfareCollection = firestore.collection("welfare_updates")
            val existingWelfare = welfareCollection.limit(1).get().await()
            if (existingWelfare.isEmpty) {
                Log.d(TAG, "Seeding Firestore Welfare Updates collection...")
                val initialWelfare = getSeedWelfareUpdates()
                val batch = firestore.batch()
                initialWelfare.forEach { w ->
                    val docRef = welfareCollection.document(w.id)
                    batch.set(docRef, w, SetOptions.merge())
                }
                batch.commit().await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to seed initial data in Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    private fun getSeedPujas(): List<FirestorePuja> = listOf(
        FirestorePuja(
            id = "rudrabhishek_kashi",
            title = "Maha Rudrabhishek",
            specialTag = "Popular",
            templeName = "Kashi Vishwanath Temple",
            location = "Varanasi, UP",
            dateTimeStr = "Daily, 6:00 AM IST",
            durationStr = "45 Mins",
            devoteesCount = "1.2k+ Devotees",
            priceRupees = 2101,
            imageUrl = "https://images.unsplash.com/photo-1609766857041-ed402ea8069a?w=800",
            significance = "Maha Rudrabhishek removes negative planetary influences, brings peace, and invokes the divine blessings of Lord Shiva.",
            priestName = "Pt. Rameshwar Shastri",
            priestTitle = "Head Archaka",
            priestExp = "18+ Years Experience",
            priestImageUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?w=200",
            category = "Popular",
            isFeatured = true
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
            imageUrl = "https://images.unsplash.com/photo-1570042225831-d98fa7577f1e?w=800",
            missionQuote = "Providing a lifelong, loving sanctuary for abandoned and injured cows.",
            fodderPercent = 65,
            medicalPercent = 40,
            shelterPercent = 85,
            lat = 27.58,
            lng = 77.70,
            updatesCount = 3
        ),
        FirestoreGaushala(
            id = "guruvayur_devaswom_gaushala",
            name = "Guruvayur Devaswom Sanctuary",
            location = "Thrissur",
            state = "Kerala",
            trustScorePercent = 99,
            animalsRescuedCount = 320,
            transparencyTier = "Platinum Tier",
            imageUrl = "https://images.unsplash.com/photo-1546445317-29f4545e9d53?w=800",
            missionQuote = "Preserving sacred and indigenous cattle breeds of Kerala with 100% transparent community seva.",
            fodderPercent = 70,
            medicalPercent = 50,
            shelterPercent = 90,
            lat = 10.59,
            lng = 76.04,
            updatesCount = 4
        )
    )

    private fun getSeedAnimals(): List<FirestoreAnimal> = listOf(
        FirestoreAnimal(
            id = "nandi_01",
            gaushalaId = "shri_krishna_gaushala",
            name = "Nandi",
            ageStr = "3.5 Years",
            breed = "Cheruvally",
            healthStatus = "Recovering",
            healthDescription = "Fractured left leg, currently on splint physiotherapy and herbal paste dressing.",
            imageUrl = "https://images.unsplash.com/photo-1546445317-29f4545e9d53?w=800",
            story = "Rescued from a severe highway collision near Mathura.",
            monthlyGoalRupees = 5000,
            raisedRupees = 3250,
            isUrgent = true
        ),
        FirestoreAnimal(
            id = "gauri_vechur",
            gaushalaId = "guruvayur_devaswom_gaushala",
            name = "Gauri",
            ageStr = "2 Years",
            breed = "Vechur",
            healthStatus = "Healthy",
            healthDescription = "Indigenous miniature breed under genetic conservation protocol.",
            imageUrl = "https://images.unsplash.com/photo-1570042225831-d98fa7577f1e?w=800",
            story = "Rescued during a distress distress relocation. Now thriving under daily ayurvedic care.",
            monthlyGoalRupees = 4000,
            raisedRupees = 4000,
            isUrgent = false
        ),
        FirestoreAnimal(
            id = "surabhi_kasaragod",
            gaushalaId = "guruvayur_devaswom_gaushala",
            name = "Surabhi",
            ageStr = "4 Years",
            breed = "Kasaragod Dwarf",
            healthStatus = "Recovering",
            healthDescription = "Severe malnutrition recovery; gaining weight steadily with Hydroponic green fodder.",
            imageUrl = "https://images.unsplash.com/photo-1527153857715-3908f2ae5e81?w=800",
            story = "Surrendered by an impoverished farmer unable to afford medical support.",
            monthlyGoalRupees = 4500,
            raisedRupees = 1800,
            isUrgent = true
        )
    )

    private fun getSeedWelfareUpdates(): List<FirestoreWelfareUpdate> = listOf(
        FirestoreWelfareUpdate(
            id = "welfare_01",
            gaushalaId = "shri_krishna_gaushala",
            animalId = "nandi_01",
            dateStr = "24 Aug 2026",
            eventType = "Medical Milestone",
            title = "Nandi began standing unassisted",
            description = "Veterinary Dr. Mohan removed the heavy cast today. Physiotherapy with warm sesame oil and turmeric paste started.",
            outcome = "Significant mobility improvement.",
            timestamp = 1787500000000L
        ),
        FirestoreWelfareUpdate(
            id = "welfare_02",
            gaushalaId = "shri_krishna_gaushala",
            animalId = null,
            dateStr = "22 Aug 2026",
            eventType = "Fodder Distribution",
            title = "1,200 kg Organic Green Fodder Received",
            description = "Community donation delivered fresh Napier and Hydroponic maize grass for 450 cattle residents.",
            outcome = "Adequate fodder reserve for 14 days.",
            timestamp = 1787300000000L
        ),
        FirestoreWelfareUpdate(
            id = "welfare_03",
            gaushalaId = "guruvayur_devaswom_gaushala",
            animalId = "surabhi_kasaragod",
            dateStr = "25 Aug 2026",
            eventType = "Medical Care",
            title = "Nutritional Supplement Protocol Initiated",
            description = "Surabhi received mineral boluses and specialized Kerala Pinnaka feed mix.",
            outcome = "Vitals stable, appetite restored.",
            timestamp = 1787600000000L
        )
    )
}
