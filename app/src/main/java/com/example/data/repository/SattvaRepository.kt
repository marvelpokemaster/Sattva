package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.AnimalResident
import com.example.data.model.DailyWisdom
import com.example.data.model.FamilyMember
import com.example.data.model.Gaushala
import com.example.data.model.PanchangInfo
import com.example.data.model.Puja
import com.example.data.model.SevaContribution
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class SattvaRepository(private val database: AppDatabase) {

    private val pujaDao = database.pujaDao()
    private val gaushalaDao = database.gaushalaDao()
    private val sevaDao = database.sevaDao()
    private val userProfileDao = database.userProfileDao()

    // Pujas Flow
    val allPujas: Flow<List<Puja>> = pujaDao.getAllPujas()
    val bookedPujas: Flow<List<Puja>> = pujaDao.getBookedPujas()

    fun getPujaById(id: String): Flow<Puja?> = pujaDao.getPujaById(id)

    suspend fun bookPuja(id: String, gotra: String, name: String, date: String) {
        pujaDao.bookPuja(id, gotra, name, date)
        userProfileDao.incrementPujaCount()
    }

    suspend fun togglePujaBookmark(id: String, isBookmarked: Boolean) {
        pujaDao.toggleBookmark(id, isBookmarked)
    }

    // Gaushalas Flow
    val allGaushalas: Flow<List<Gaushala>> = gaushalaDao.getAllGaushalas()
    val supportedGaushalas: Flow<List<Gaushala>> = gaushalaDao.getSupportedGaushalas()
    fun getGaushalaById(id: String): Flow<Gaushala?> = gaushalaDao.getGaushalaById(id)

    // Animals Flow
    val allAnimals: Flow<List<AnimalResident>> = gaushalaDao.getAllAnimals()
    val urgentAnimals: Flow<List<AnimalResident>> = gaushalaDao.getUrgentAnimals()
    fun getAnimalsByGaushala(gaushalaId: String): Flow<List<AnimalResident>> = gaushalaDao.getAnimalsByGaushala(gaushalaId)
    fun getAnimalById(id: String): Flow<AnimalResident?> = gaushalaDao.getAnimalById(id)

    suspend fun contributeToAnimal(animalId: String, amount: Int, animalName: String, category: String) {
        gaushalaDao.contributeToAnimal(animalId, amount)
        val contribution = SevaContribution(
            title = "Seva for $animalName",
            targetType = "ANIMAL",
            targetName = animalName,
            amountRupees = amount,
            sevaCategory = category,
            dateStr = "Today"
        )
        sevaDao.insertContribution(contribution)
        userProfileDao.addContribution(amount)
    }

    suspend fun contributeToGaushala(gaushalaId: String, amount: Int, gaushalaName: String, category: String) {
        gaushalaDao.markSupported(gaushalaId)
        val contribution = SevaContribution(
            title = "Sanctuary Support: $gaushalaName",
            targetType = "GAUSHALA",
            targetName = gaushalaName,
            amountRupees = amount,
            sevaCategory = category,
            dateStr = "Today"
        )
        sevaDao.insertContribution(contribution)
        userProfileDao.addContribution(amount)
    }

    suspend fun toggleAnimalFavorite(id: String, isFav: Boolean) {
        gaushalaDao.toggleFavorite(id, isFav)
    }

    // Seva History
    val allContributions: Flow<List<SevaContribution>> = sevaDao.getAllContributions()
    val sevaCount: Flow<Int> = sevaDao.getSevaCount()
    val totalContributed: Flow<Int> = sevaDao.getTotalAmountContributed()

    // Profile
    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfile()

    suspend fun updateSpiritualIdentity(gotra: String, nakshatra: String, rashi: String) {
        userProfileDao.updateSpiritualIdentity(gotra, nakshatra, rashi)
    }

    // Static / Daily Vedic Content
    fun getTodayPanchang(): PanchangInfo {
        return PanchangInfo(
            tithi = "Shukla Paksha Dashami",
            nakshatra = "Rohini Nakshatra",
            paksha = "Shukla",
            auspiciousTiming = "Abhijit Muhurta: 11:58 AM - 12:48 PM",
            rahuKaal = "04:30 PM - 06:00 PM"
        )
    }

    fun getTodayWisdom(): DailyWisdom {
        return DailyWisdom(
            quote = "Karma-yoga is a supreme secret indeed.",
            source = "Bhagavad Gita (Chapter 2, Verse 50)",
            commentary = "One who is engaged in devotional service rids himself of both good and bad actions even in this life. Therefore strive for Yoga, which is the art of all work.",
            sanskritShloka = "बुद्धियुक्तो जहातीह उभे सुकृतदुष्कृते । तस्माद्योगाय युज्यस्व योगः कर्मसु कौशलम् ॥"
        )
    }

    fun getDefaultFamilyMembers(): List<FamilyMember> {
        return listOf(
            FamilyMember(name = "Priya Desai", relation = "Spouse", gotra = "Kashyapa Gotra"),
            FamilyMember(name = "Rohan Desai", relation = "Son", gotra = "Kashyapa Gotra"),
            FamilyMember(name = "Meera Desai", relation = "Daughter", gotra = "Kashyapa Gotra")
        )
    }

    suspend fun seedInitialDataIfEmpty() {
        val existingPujas = pujaDao.getAllPujas().firstOrNull()
        if (existingPujas.isNullOrEmpty()) {
            val initialPujas = listOf(
                Puja(
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
                Puja(
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
                Puja(
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
                ),
                Puja(
                    id = "maha_mrityunjaya_past",
                    title = "Maha Mrityunjaya Havan",
                    specialTag = "Completed",
                    templeName = "Kashi Vishwanath Temple, Varanasi",
                    location = "Kashi Vishwanath Temple, Varanasi",
                    dateTimeStr = "Oct 12, 6:00 AM IST",
                    durationStr = "3 Hours",
                    devoteesCount = "3.4k Devotees",
                    priceRupees = 3100,
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBSmeJ1uJipFCYDSP4OEsK4XmSIpDRwHlNUckoF-Pk7nZva0St3zCsQ80glWljZA77QGRxW1oEqxAS9D7BNCSJUiytuPO3KuX3TY-8WzduDj0tTxIJkJvOdxuBEDOAcVTJGI3mPp8KwcWZ5uTi0s811eH6plBUs1fD6FtE2qN65IUVtYwwpN0nqmNck-bCBW-63aoYeS1gssRScax3MYLyIzw83Unznc8Wns320xUrJurWNqzSKgWw",
                    significance = "Consecrated Maha Mrityunjaya Japa and Havan invoking Mahadev for healing, longevity, and liberation from fear and distress.",
                    priestName = "Pt. Rameshwar Shastri",
                    priestTitle = "Chief Priest",
                    priestExp = "30+ Years",
                    priestImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCHeevcOISsYI1rtQI5r8he_R9P2uJpKBOE6VUu71jI4ra8jF_28mBO-Th5BeXpeoLCWVh8tdSW6fTSRM4vmeqtmyiEQFOL3Rj5XkEm-La-YtrVpfVVgm8s0OuDVrHUtLLRWCPmvl8waWJle8wE8Lsu4CtrdD83EwAYMfuou2oRSJBi4Hf_hrFFZz3RAS2xginOAPp8jIWO-KzgIgwN_YUtk9JAZrRo6GYyfAIya7ov45LgiyCWw-4",
                    isBooked = true,
                    bookedDate = "Oct 12",
                    bookedGotra = "Kashyapa",
                    bookedDevoteeName = "Arjun Desai"
                ),
                Puja(
                    id = "rudrabhishek_upcoming",
                    title = "Rudrabhishek",
                    specialTag = "Upcoming",
                    templeName = "Trimbakeshwar, Nashik",
                    location = "Trimbakeshwar, Nashik",
                    dateTimeStr = "Nov 24, 7:00 AM IST",
                    durationStr = "2 Hours",
                    devoteesCount = "2.1k Devotees",
                    priceRupees = 2100,
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAph87WGYFEDvfZFOaXiKZeeXbvyPh-59nkVFtwsuIgmCzayD8hSm99DcuvzZ1HEooDHuj-P6PvIApqDVSMbNqJDti-bmWEUeaXu3NQCydHu_eME5n5kgvfvvUh8_Fs7mpf2N-ci7rawgylMFq5AVNtkX1DSpY_w0VDSxc0HKuUsRJWHsfoTOZ8pl_iSNT6D_KqQ8tuNVjDUd05lBoaYIDt6lDhjIVHZhVU-NGe4mYNiF5bfW5zCs4",
                    significance = "Sacred Abhishekam to Lord Shiva at Trimbakeshwar for family peace and spiritual blessings.",
                    priestName = "Acharya Vidyadhar Guruji",
                    priestTitle = "Chief Priest",
                    priestExp = "25+ Years",
                    priestImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCHeevcOISsYI1rtQI5r8he_R9P2uJpKBOE6VUu71jI4ra8jF_28mBO-Th5BeXpeoLCWVh8tdSW6fTSRM4vmeqtmyiEQFOL3Rj5XkEm-La-YtrVpfVVgm8s0OuDVrHUtLLRWCPmvl8waWJle8wE8Lsu4CtrdD83EwAYMfuou2oRSJBi4Hf_hrFFZz3RAS2xginOAPp8jIWO-KzgIgwN_YUtk9JAZrRo6GYyfAIya7ov45LgiyCWw-4",
                    isBooked = true,
                    bookedDate = "Nov 24",
                    bookedGotra = "Kashyapa",
                    bookedDevoteeName = "Arjun Desai"
                )
            )
            pujaDao.insertPujas(initialPujas)
        }

        val existingGaushalas = gaushalaDao.getAllGaushalas().firstOrNull()
        if (existingGaushalas.isNullOrEmpty()) {
            val initialGaushalas = listOf(
                Gaushala(
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
                    isSupported = true,
                    updatesCount = 2
                ),
                Gaushala(
                    id = "gopashtami_sanctuary",
                    name = "Gopashtami Sanctuary",
                    location = "Pushkar",
                    state = "Rajasthan",
                    trustScorePercent = 95,
                    animalsRescuedCount = 210,
                    transparencyTier = "Silver Tier",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAhtH9A3gDjYr-ftOb3kTrFbrGPgXrxRvh-gCjpRMZJ1kgS6HXEXhOmjyixwr_Hifj5Is1Kx_mhtwHvTDmRAU_ErDRcAiiXGN7fPWm7qZCDKfw5Gqa_jFmQvL3N5hbJTLJtLahVaJ5u9gc0lglEmuECf7g4vsFxl4EkTaA8d5eT5MydY5yci9d4-uYaqWOiUBjp59HcR5Z--_eaVD8yQWAm9ulZ77M8cm9WdqxxJsCpsmkTdGlZiRU",
                    missionQuote = "Dedicated to the rehabilitation and emergency medical response for street animals and stray bovine breeds across the Pushkar valley.",
                    fodderPercent = 55,
                    medicalPercent = 30,
                    shelterPercent = 70,
                    lat = 26.48,
                    lng = 74.55,
                    isSupported = false
                ),
                Gaushala(
                    id = "nandini_welfare_trust",
                    name = "Nandini Welfare Trust",
                    location = "Rishikesh",
                    state = "Uttarakhand",
                    trustScorePercent = 99,
                    animalsRescuedCount = 850,
                    transparencyTier = "Gold Tier",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBS1NX39Mwy-YZKSxQD_67ksilpz7Sz5taA1JJhtRFGUpDc11va68-pVJ6cYc_AKtPWozyhSbAqb5qWbaEnZlgyBTxPfd-LXdrUd6WtpKjbXMiUNojrn6dMm8M4CKDtweN0HebCrSnPYdx05ncEAlzO9weYfQeZdtKvQHg4IdN07wxQtHABtDiZm-YXKOCYkI9OJghPU7vNlmgRgYuZvDzLzpV6lcyxlV4ATQUGIYsAmcZP--Kf1Hw",
                    missionQuote = "Himalayan foothills sanctuary committed to organic Vedic farming, indigenous Gir cow protection, and free healthcare for local animal populations.",
                    fodderPercent = 80,
                    medicalPercent = 65,
                    shelterPercent = 90,
                    lat = 30.08,
                    lng = 78.26,
                    isSupported = false
                ),
                Gaushala(
                    id = "govardhan_eco_village",
                    name = "Govardhan Eco Village",
                    location = "Palghar",
                    state = "Maharashtra",
                    trustScorePercent = 97,
                    animalsRescuedCount = 320,
                    transparencyTier = "Platinum Tier",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCHve6TDONNi667wklA5U-HxxIdd33TZVX9UMwOFSSd1GeVQ3pm-eUqlsz1V_c3OBybT6plXq_UpV2nXdIP0PKuRNE-HHttjseeeFughd3myLSapbVAEqfco8UcdAbdJBQbCLNLzGyGedt7fkspakaevUUxZ1waFkVVXCvOvVOO59YUC7rRCkcxtwIKSy_--DlEk6UU-goWTA_veuyVVu7lvz3khAAuHywK-Qv_PBwqF3mkkjg_pd0",
                    missionQuote = "Holistic sustainable cow care sanctuary emphasizing natural grazing, cruelty-free environment, and zero-chemical natural remedies.",
                    fodderPercent = 75,
                    medicalPercent = 50,
                    shelterPercent = 80,
                    lat = 19.69,
                    lng = 72.76,
                    isSupported = true,
                    updatesCount = 1
                )
            )
            gaushalaDao.insertGaushalas(initialGaushalas)

            val initialAnimals = listOf(
                AnimalResident(
                    id = "nandi",
                    gaushalaId = "shri_krishna_gaushala",
                    name = "Nandi",
                    ageStr = "3 yrs • Recovering",
                    healthStatus = "Recovering",
                    healthDescription = "Gaining weight steadily. Current focus on mobility rehabilitation and nutritional supplements.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAPDU7KDFzmVHRSaSXuJPOCMZzeKEijZrobJjw14TmN_Io5Eq8RQ6j_uYhPTe8BI1d2YGouL5MioooDQZ65q-elS775_73SGpFV8nyau8d3sRq126sya3ZXMNGml2nLEsW9P17MbNOt7cakvRjY8Co7FI8SOpR1JZqTGghOSUbuC0F3eLQu5v_PsKNYX7BvW5mCum_uODY9a2ATwb2Rp2qTfJo39B9GNQpB1KHVU8BlslRP9_blEXo",
                    story = "Rescued from an abandoned dairy facility where he was left without adequate nutrition, Nandi arrived at the sanctuary weak but spirited. His initial weeks required intensive care, but his resilience has been inspiring. He has formed a close bond with the older cows and spends his afternoons resting in the shade of the banyan tree.",
                    monthlyGoalRupees = 15000,
                    raisedRupees = 9000,
                    isUrgent = true
                ),
                AnimalResident(
                    id = "gauri",
                    gaushalaId = "shri_krishna_gaushala",
                    name = "Gauri",
                    ageStr = "5 yrs • Serene",
                    healthStatus = "Healthy",
                    healthDescription = "Provide a month of premium fodder and shelter maintenance for Gauri.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuACqH_gQKbdJBoU02l1o7efkk3TYgMIJ85CKZy969WdF7whWLpuVATtKypusqagb3d9BkO2A_EHWkgE14sMctuagcWL2WXFJuZn3AJR6M64mE5p1rKUrYbIMvTTn4A-lDgOQaipCAeqYwAiUdL2OXVOE3s81PzpcFGsA8akYkdMnG9whu4LFF0vuasEw_WSCpm0P6FyxXfkVhIhF8DiCpNrNj3nylCeTOA0GVWVEvQ25kxpOBNKITc",
                    story = "Rescued 2 years ago. Loves jaggery, fresh grass, and gentle head scratches from visiting devotees.",
                    monthlyGoalRupees = 5000,
                    raisedRupees = 1250,
                    isUrgent = false
                ),
                AnimalResident(
                    id = "krishna_calf",
                    gaushalaId = "shri_krishna_gaushala",
                    name = "Krishna",
                    ageStr = "8 mos • Playful",
                    healthStatus = "Healthy",
                    healthDescription = "A playful calf who was brought in safely after being lost.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuArMWfCSO74IGaqW9MXUQEhC_gdQI-hyHuaBSfYIp3LOYCHON-iiHIK_RKGJoLP7fq6LD1ftKvwXwsanKSyLV8NUXllr4mlr8ZjTiQuuEpvzoclJqbgGMbC--sQ9-wSDKikxIQVTlFjMMaY9Uxuv1gioCqjC108prfGsiX8iEIKiOzGgplEq5yz6eC0j5xDYlBVSwGWJ5smDeo2Bj-8NSuNq33CX0kLla0Zz3iFcoDn1nmiYg3HoiU",
                    story = "Found wandering near the edge of the sacred Govardhan hill. Now growing strong with joyful energy.",
                    monthlyGoalRupees = 6000,
                    raisedRupees = 4200,
                    isUrgent = false
                )
            )
            gaushalaDao.insertAnimals(initialAnimals)
        }

        val existingProfile = userProfileDao.getUserProfile().firstOrNull()
        if (existingProfile == null) {
            userProfileDao.insertProfile(UserProfile())
        }
    }
}
