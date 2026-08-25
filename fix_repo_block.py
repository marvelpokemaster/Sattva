import re
with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "r") as f:
    content = f.read()

# Replace the block manually
replacement = """
            // Fetch Pujas from Firestore & update Room cache
            val pujasRes = catalogRepo.getPujas()
            if (pujasRes.isSuccess) {
                val firestorePujas = pujasRes.getOrNull() ?: emptyList()
                if (firestorePujas.isNotEmpty()) {
                    val currentRoomPujas = pujaDao.getAllPujas().firstOrNull() ?: emptyList()
                    val mappedPujas = firestorePujas.map { fp ->
                        val existing = currentRoomPujas.find { it.id == fp.id }
                        fp.toPuja(
                            isBookmarked = existing?.isBookmarked ?: false,
                            isBooked = existing?.isBooked ?: false,
                            bookedDate = existing?.bookedDate ?: ""
                        )
                    }
                    pujaDao.insertPujas(mappedPujas)
                }
            }

            // Fetch Gaushalas & update Room cache
            val gaushalasRes = catalogRepo.getGaushalas()
            if (gaushalasRes.isSuccess) {
                val firestoreGaushalas = gaushalasRes.getOrNull() ?: emptyList()
                if (firestoreGaushalas.isNotEmpty()) {
                    val currentRoomGaushalas = gaushalaDao.getAllGaushalas().firstOrNull() ?: emptyList()
                    val mappedGaushalas = firestoreGaushalas.map { fg ->
                        val existing = currentRoomGaushalas.find { it.id == fg.id }
                        fg.toGaushala(isSupported = existing?.isSupported ?: false)
                    }
                    gaushalaDao.insertGaushalas(mappedGaushalas)
                }
            }

            // Fetch Animals & update Room cache
            val animalsRes = catalogRepo.getAnimals()
            if (animalsRes.isSuccess) {
                val firestoreAnimals = animalsRes.getOrNull() ?: emptyList()
                if (firestoreAnimals.isNotEmpty()) {
                    val currentRoomAnimals = gaushalaDao.getAllAnimals().firstOrNull() ?: emptyList()
                    val mappedAnimals = firestoreAnimals.map { fa ->
                        val existing = currentRoomAnimals.find { it.id == fa.id }
                        fa.toAnimalResident(isFavorite = existing?.isFavorite ?: false)
                    }
                    gaushalaDao.insertAnimals(mappedAnimals)
                }
            }
"""

content = re.sub(
    r"// Fetch Pujas from Firestore.*?gaushalaDao\.insertAnimals\(mappedAnimals\)\n                }\n            }",
    replacement.strip(),
    content,
    flags=re.DOTALL
)

with open("shared/src/commonMain/kotlin/com/example/data/repository/SattvaRepository.kt", "w") as f:
    f.write(content)
