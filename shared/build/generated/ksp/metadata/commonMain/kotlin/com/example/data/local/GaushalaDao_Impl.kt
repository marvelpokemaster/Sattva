package com.example.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.`data`.model.AnimalResident
import com.example.`data`.model.Gaushala
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class GaushalaDao_Impl(
  __db: RoomDatabase,
) : GaushalaDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfGaushala: EntityInsertAdapter<Gaushala>

  private val __insertAdapterOfAnimalResident: EntityInsertAdapter<AnimalResident>

  private val __updateAdapterOfGaushala: EntityDeleteOrUpdateAdapter<Gaushala>
  init {
    this.__db = __db
    this.__insertAdapterOfGaushala = object : EntityInsertAdapter<Gaushala>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `gaushalas` (`id`,`name`,`location`,`state`,`trustScorePercent`,`animalsRescuedCount`,`transparencyTier`,`imageUrl`,`missionQuote`,`fodderPercent`,`medicalPercent`,`shelterPercent`,`lat`,`lng`,`isSupported`,`updatesCount`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Gaushala) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.location)
        statement.bindText(4, entity.state)
        statement.bindLong(5, entity.trustScorePercent.toLong())
        statement.bindLong(6, entity.animalsRescuedCount.toLong())
        statement.bindText(7, entity.transparencyTier)
        statement.bindText(8, entity.imageUrl)
        statement.bindText(9, entity.missionQuote)
        statement.bindLong(10, entity.fodderPercent.toLong())
        statement.bindLong(11, entity.medicalPercent.toLong())
        statement.bindLong(12, entity.shelterPercent.toLong())
        statement.bindDouble(13, entity.lat)
        statement.bindDouble(14, entity.lng)
        val _tmp: Int = if (entity.isSupported) 1 else 0
        statement.bindLong(15, _tmp.toLong())
        statement.bindLong(16, entity.updatesCount.toLong())
      }
    }
    this.__insertAdapterOfAnimalResident = object : EntityInsertAdapter<AnimalResident>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `animal_residents` (`id`,`gaushalaId`,`name`,`ageStr`,`healthStatus`,`healthDescription`,`imageUrl`,`story`,`monthlyGoalRupees`,`raisedRupees`,`isUrgent`,`isFavorite`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AnimalResident) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.gaushalaId)
        statement.bindText(3, entity.name)
        statement.bindText(4, entity.ageStr)
        statement.bindText(5, entity.healthStatus)
        statement.bindText(6, entity.healthDescription)
        statement.bindText(7, entity.imageUrl)
        statement.bindText(8, entity.story)
        statement.bindLong(9, entity.monthlyGoalRupees.toLong())
        statement.bindLong(10, entity.raisedRupees.toLong())
        val _tmp: Int = if (entity.isUrgent) 1 else 0
        statement.bindLong(11, _tmp.toLong())
        val _tmp_1: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(12, _tmp_1.toLong())
      }
    }
    this.__updateAdapterOfGaushala = object : EntityDeleteOrUpdateAdapter<Gaushala>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `gaushalas` SET `id` = ?,`name` = ?,`location` = ?,`state` = ?,`trustScorePercent` = ?,`animalsRescuedCount` = ?,`transparencyTier` = ?,`imageUrl` = ?,`missionQuote` = ?,`fodderPercent` = ?,`medicalPercent` = ?,`shelterPercent` = ?,`lat` = ?,`lng` = ?,`isSupported` = ?,`updatesCount` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Gaushala) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.location)
        statement.bindText(4, entity.state)
        statement.bindLong(5, entity.trustScorePercent.toLong())
        statement.bindLong(6, entity.animalsRescuedCount.toLong())
        statement.bindText(7, entity.transparencyTier)
        statement.bindText(8, entity.imageUrl)
        statement.bindText(9, entity.missionQuote)
        statement.bindLong(10, entity.fodderPercent.toLong())
        statement.bindLong(11, entity.medicalPercent.toLong())
        statement.bindLong(12, entity.shelterPercent.toLong())
        statement.bindDouble(13, entity.lat)
        statement.bindDouble(14, entity.lng)
        val _tmp: Int = if (entity.isSupported) 1 else 0
        statement.bindLong(15, _tmp.toLong())
        statement.bindLong(16, entity.updatesCount.toLong())
        statement.bindText(17, entity.id)
      }
    }
  }

  public override suspend fun insertGaushalas(gaushalas: List<Gaushala>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfGaushala.insert(_connection, gaushalas)
  }

  public override suspend fun insertAnimals(animals: List<AnimalResident>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfAnimalResident.insert(_connection, animals)
  }

  public override suspend fun updateGaushala(gaushala: Gaushala): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfGaushala.handle(_connection, gaushala)
  }

  public override fun getAllGaushalas(): Flow<List<Gaushala>> {
    val _sql: String = "SELECT * FROM gaushalas"
    return createFlow(__db, false, arrayOf("gaushalas")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfState: Int = getColumnIndexOrThrow(_stmt, "state")
        val _columnIndexOfTrustScorePercent: Int = getColumnIndexOrThrow(_stmt, "trustScorePercent")
        val _columnIndexOfAnimalsRescuedCount: Int = getColumnIndexOrThrow(_stmt,
            "animalsRescuedCount")
        val _columnIndexOfTransparencyTier: Int = getColumnIndexOrThrow(_stmt, "transparencyTier")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfMissionQuote: Int = getColumnIndexOrThrow(_stmt, "missionQuote")
        val _columnIndexOfFodderPercent: Int = getColumnIndexOrThrow(_stmt, "fodderPercent")
        val _columnIndexOfMedicalPercent: Int = getColumnIndexOrThrow(_stmt, "medicalPercent")
        val _columnIndexOfShelterPercent: Int = getColumnIndexOrThrow(_stmt, "shelterPercent")
        val _columnIndexOfLat: Int = getColumnIndexOrThrow(_stmt, "lat")
        val _columnIndexOfLng: Int = getColumnIndexOrThrow(_stmt, "lng")
        val _columnIndexOfIsSupported: Int = getColumnIndexOrThrow(_stmt, "isSupported")
        val _columnIndexOfUpdatesCount: Int = getColumnIndexOrThrow(_stmt, "updatesCount")
        val _result: MutableList<Gaushala> = mutableListOf()
        while (_stmt.step()) {
          val _item: Gaushala
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLocation: String
          _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          val _tmpState: String
          _tmpState = _stmt.getText(_columnIndexOfState)
          val _tmpTrustScorePercent: Int
          _tmpTrustScorePercent = _stmt.getLong(_columnIndexOfTrustScorePercent).toInt()
          val _tmpAnimalsRescuedCount: Int
          _tmpAnimalsRescuedCount = _stmt.getLong(_columnIndexOfAnimalsRescuedCount).toInt()
          val _tmpTransparencyTier: String
          _tmpTransparencyTier = _stmt.getText(_columnIndexOfTransparencyTier)
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpMissionQuote: String
          _tmpMissionQuote = _stmt.getText(_columnIndexOfMissionQuote)
          val _tmpFodderPercent: Int
          _tmpFodderPercent = _stmt.getLong(_columnIndexOfFodderPercent).toInt()
          val _tmpMedicalPercent: Int
          _tmpMedicalPercent = _stmt.getLong(_columnIndexOfMedicalPercent).toInt()
          val _tmpShelterPercent: Int
          _tmpShelterPercent = _stmt.getLong(_columnIndexOfShelterPercent).toInt()
          val _tmpLat: Double
          _tmpLat = _stmt.getDouble(_columnIndexOfLat)
          val _tmpLng: Double
          _tmpLng = _stmt.getDouble(_columnIndexOfLng)
          val _tmpIsSupported: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsSupported).toInt()
          _tmpIsSupported = _tmp != 0
          val _tmpUpdatesCount: Int
          _tmpUpdatesCount = _stmt.getLong(_columnIndexOfUpdatesCount).toInt()
          _item =
              Gaushala(_tmpId,_tmpName,_tmpLocation,_tmpState,_tmpTrustScorePercent,_tmpAnimalsRescuedCount,_tmpTransparencyTier,_tmpImageUrl,_tmpMissionQuote,_tmpFodderPercent,_tmpMedicalPercent,_tmpShelterPercent,_tmpLat,_tmpLng,_tmpIsSupported,_tmpUpdatesCount)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getGaushalaById(id: String): Flow<Gaushala?> {
    val _sql: String = "SELECT * FROM gaushalas WHERE id = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("gaushalas")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfState: Int = getColumnIndexOrThrow(_stmt, "state")
        val _columnIndexOfTrustScorePercent: Int = getColumnIndexOrThrow(_stmt, "trustScorePercent")
        val _columnIndexOfAnimalsRescuedCount: Int = getColumnIndexOrThrow(_stmt,
            "animalsRescuedCount")
        val _columnIndexOfTransparencyTier: Int = getColumnIndexOrThrow(_stmt, "transparencyTier")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfMissionQuote: Int = getColumnIndexOrThrow(_stmt, "missionQuote")
        val _columnIndexOfFodderPercent: Int = getColumnIndexOrThrow(_stmt, "fodderPercent")
        val _columnIndexOfMedicalPercent: Int = getColumnIndexOrThrow(_stmt, "medicalPercent")
        val _columnIndexOfShelterPercent: Int = getColumnIndexOrThrow(_stmt, "shelterPercent")
        val _columnIndexOfLat: Int = getColumnIndexOrThrow(_stmt, "lat")
        val _columnIndexOfLng: Int = getColumnIndexOrThrow(_stmt, "lng")
        val _columnIndexOfIsSupported: Int = getColumnIndexOrThrow(_stmt, "isSupported")
        val _columnIndexOfUpdatesCount: Int = getColumnIndexOrThrow(_stmt, "updatesCount")
        val _result: Gaushala?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLocation: String
          _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          val _tmpState: String
          _tmpState = _stmt.getText(_columnIndexOfState)
          val _tmpTrustScorePercent: Int
          _tmpTrustScorePercent = _stmt.getLong(_columnIndexOfTrustScorePercent).toInt()
          val _tmpAnimalsRescuedCount: Int
          _tmpAnimalsRescuedCount = _stmt.getLong(_columnIndexOfAnimalsRescuedCount).toInt()
          val _tmpTransparencyTier: String
          _tmpTransparencyTier = _stmt.getText(_columnIndexOfTransparencyTier)
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpMissionQuote: String
          _tmpMissionQuote = _stmt.getText(_columnIndexOfMissionQuote)
          val _tmpFodderPercent: Int
          _tmpFodderPercent = _stmt.getLong(_columnIndexOfFodderPercent).toInt()
          val _tmpMedicalPercent: Int
          _tmpMedicalPercent = _stmt.getLong(_columnIndexOfMedicalPercent).toInt()
          val _tmpShelterPercent: Int
          _tmpShelterPercent = _stmt.getLong(_columnIndexOfShelterPercent).toInt()
          val _tmpLat: Double
          _tmpLat = _stmt.getDouble(_columnIndexOfLat)
          val _tmpLng: Double
          _tmpLng = _stmt.getDouble(_columnIndexOfLng)
          val _tmpIsSupported: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsSupported).toInt()
          _tmpIsSupported = _tmp != 0
          val _tmpUpdatesCount: Int
          _tmpUpdatesCount = _stmt.getLong(_columnIndexOfUpdatesCount).toInt()
          _result =
              Gaushala(_tmpId,_tmpName,_tmpLocation,_tmpState,_tmpTrustScorePercent,_tmpAnimalsRescuedCount,_tmpTransparencyTier,_tmpImageUrl,_tmpMissionQuote,_tmpFodderPercent,_tmpMedicalPercent,_tmpShelterPercent,_tmpLat,_tmpLng,_tmpIsSupported,_tmpUpdatesCount)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSupportedGaushalas(): Flow<List<Gaushala>> {
    val _sql: String = "SELECT * FROM gaushalas WHERE isSupported = 1"
    return createFlow(__db, false, arrayOf("gaushalas")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfState: Int = getColumnIndexOrThrow(_stmt, "state")
        val _columnIndexOfTrustScorePercent: Int = getColumnIndexOrThrow(_stmt, "trustScorePercent")
        val _columnIndexOfAnimalsRescuedCount: Int = getColumnIndexOrThrow(_stmt,
            "animalsRescuedCount")
        val _columnIndexOfTransparencyTier: Int = getColumnIndexOrThrow(_stmt, "transparencyTier")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfMissionQuote: Int = getColumnIndexOrThrow(_stmt, "missionQuote")
        val _columnIndexOfFodderPercent: Int = getColumnIndexOrThrow(_stmt, "fodderPercent")
        val _columnIndexOfMedicalPercent: Int = getColumnIndexOrThrow(_stmt, "medicalPercent")
        val _columnIndexOfShelterPercent: Int = getColumnIndexOrThrow(_stmt, "shelterPercent")
        val _columnIndexOfLat: Int = getColumnIndexOrThrow(_stmt, "lat")
        val _columnIndexOfLng: Int = getColumnIndexOrThrow(_stmt, "lng")
        val _columnIndexOfIsSupported: Int = getColumnIndexOrThrow(_stmt, "isSupported")
        val _columnIndexOfUpdatesCount: Int = getColumnIndexOrThrow(_stmt, "updatesCount")
        val _result: MutableList<Gaushala> = mutableListOf()
        while (_stmt.step()) {
          val _item: Gaushala
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLocation: String
          _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          val _tmpState: String
          _tmpState = _stmt.getText(_columnIndexOfState)
          val _tmpTrustScorePercent: Int
          _tmpTrustScorePercent = _stmt.getLong(_columnIndexOfTrustScorePercent).toInt()
          val _tmpAnimalsRescuedCount: Int
          _tmpAnimalsRescuedCount = _stmt.getLong(_columnIndexOfAnimalsRescuedCount).toInt()
          val _tmpTransparencyTier: String
          _tmpTransparencyTier = _stmt.getText(_columnIndexOfTransparencyTier)
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpMissionQuote: String
          _tmpMissionQuote = _stmt.getText(_columnIndexOfMissionQuote)
          val _tmpFodderPercent: Int
          _tmpFodderPercent = _stmt.getLong(_columnIndexOfFodderPercent).toInt()
          val _tmpMedicalPercent: Int
          _tmpMedicalPercent = _stmt.getLong(_columnIndexOfMedicalPercent).toInt()
          val _tmpShelterPercent: Int
          _tmpShelterPercent = _stmt.getLong(_columnIndexOfShelterPercent).toInt()
          val _tmpLat: Double
          _tmpLat = _stmt.getDouble(_columnIndexOfLat)
          val _tmpLng: Double
          _tmpLng = _stmt.getDouble(_columnIndexOfLng)
          val _tmpIsSupported: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsSupported).toInt()
          _tmpIsSupported = _tmp != 0
          val _tmpUpdatesCount: Int
          _tmpUpdatesCount = _stmt.getLong(_columnIndexOfUpdatesCount).toInt()
          _item =
              Gaushala(_tmpId,_tmpName,_tmpLocation,_tmpState,_tmpTrustScorePercent,_tmpAnimalsRescuedCount,_tmpTransparencyTier,_tmpImageUrl,_tmpMissionQuote,_tmpFodderPercent,_tmpMedicalPercent,_tmpShelterPercent,_tmpLat,_tmpLng,_tmpIsSupported,_tmpUpdatesCount)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllAnimals(): Flow<List<AnimalResident>> {
    val _sql: String = "SELECT * FROM animal_residents"
    return createFlow(__db, false, arrayOf("animal_residents")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGaushalaId: Int = getColumnIndexOrThrow(_stmt, "gaushalaId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfAgeStr: Int = getColumnIndexOrThrow(_stmt, "ageStr")
        val _columnIndexOfHealthStatus: Int = getColumnIndexOrThrow(_stmt, "healthStatus")
        val _columnIndexOfHealthDescription: Int = getColumnIndexOrThrow(_stmt, "healthDescription")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfStory: Int = getColumnIndexOrThrow(_stmt, "story")
        val _columnIndexOfMonthlyGoalRupees: Int = getColumnIndexOrThrow(_stmt, "monthlyGoalRupees")
        val _columnIndexOfRaisedRupees: Int = getColumnIndexOrThrow(_stmt, "raisedRupees")
        val _columnIndexOfIsUrgent: Int = getColumnIndexOrThrow(_stmt, "isUrgent")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _result: MutableList<AnimalResident> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnimalResident
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGaushalaId: String
          _tmpGaushalaId = _stmt.getText(_columnIndexOfGaushalaId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpAgeStr: String
          _tmpAgeStr = _stmt.getText(_columnIndexOfAgeStr)
          val _tmpHealthStatus: String
          _tmpHealthStatus = _stmt.getText(_columnIndexOfHealthStatus)
          val _tmpHealthDescription: String
          _tmpHealthDescription = _stmt.getText(_columnIndexOfHealthDescription)
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpStory: String
          _tmpStory = _stmt.getText(_columnIndexOfStory)
          val _tmpMonthlyGoalRupees: Int
          _tmpMonthlyGoalRupees = _stmt.getLong(_columnIndexOfMonthlyGoalRupees).toInt()
          val _tmpRaisedRupees: Int
          _tmpRaisedRupees = _stmt.getLong(_columnIndexOfRaisedRupees).toInt()
          val _tmpIsUrgent: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsUrgent).toInt()
          _tmpIsUrgent = _tmp != 0
          val _tmpIsFavorite: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp_1 != 0
          _item =
              AnimalResident(_tmpId,_tmpGaushalaId,_tmpName,_tmpAgeStr,_tmpHealthStatus,_tmpHealthDescription,_tmpImageUrl,_tmpStory,_tmpMonthlyGoalRupees,_tmpRaisedRupees,_tmpIsUrgent,_tmpIsFavorite)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAnimalsByGaushala(gaushalaId: String): Flow<List<AnimalResident>> {
    val _sql: String = "SELECT * FROM animal_residents WHERE gaushalaId = ?"
    return createFlow(__db, false, arrayOf("animal_residents")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gaushalaId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGaushalaId: Int = getColumnIndexOrThrow(_stmt, "gaushalaId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfAgeStr: Int = getColumnIndexOrThrow(_stmt, "ageStr")
        val _columnIndexOfHealthStatus: Int = getColumnIndexOrThrow(_stmt, "healthStatus")
        val _columnIndexOfHealthDescription: Int = getColumnIndexOrThrow(_stmt, "healthDescription")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfStory: Int = getColumnIndexOrThrow(_stmt, "story")
        val _columnIndexOfMonthlyGoalRupees: Int = getColumnIndexOrThrow(_stmt, "monthlyGoalRupees")
        val _columnIndexOfRaisedRupees: Int = getColumnIndexOrThrow(_stmt, "raisedRupees")
        val _columnIndexOfIsUrgent: Int = getColumnIndexOrThrow(_stmt, "isUrgent")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _result: MutableList<AnimalResident> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnimalResident
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGaushalaId: String
          _tmpGaushalaId = _stmt.getText(_columnIndexOfGaushalaId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpAgeStr: String
          _tmpAgeStr = _stmt.getText(_columnIndexOfAgeStr)
          val _tmpHealthStatus: String
          _tmpHealthStatus = _stmt.getText(_columnIndexOfHealthStatus)
          val _tmpHealthDescription: String
          _tmpHealthDescription = _stmt.getText(_columnIndexOfHealthDescription)
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpStory: String
          _tmpStory = _stmt.getText(_columnIndexOfStory)
          val _tmpMonthlyGoalRupees: Int
          _tmpMonthlyGoalRupees = _stmt.getLong(_columnIndexOfMonthlyGoalRupees).toInt()
          val _tmpRaisedRupees: Int
          _tmpRaisedRupees = _stmt.getLong(_columnIndexOfRaisedRupees).toInt()
          val _tmpIsUrgent: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsUrgent).toInt()
          _tmpIsUrgent = _tmp != 0
          val _tmpIsFavorite: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp_1 != 0
          _item =
              AnimalResident(_tmpId,_tmpGaushalaId,_tmpName,_tmpAgeStr,_tmpHealthStatus,_tmpHealthDescription,_tmpImageUrl,_tmpStory,_tmpMonthlyGoalRupees,_tmpRaisedRupees,_tmpIsUrgent,_tmpIsFavorite)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAnimalById(id: String): Flow<AnimalResident?> {
    val _sql: String = "SELECT * FROM animal_residents WHERE id = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("animal_residents")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGaushalaId: Int = getColumnIndexOrThrow(_stmt, "gaushalaId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfAgeStr: Int = getColumnIndexOrThrow(_stmt, "ageStr")
        val _columnIndexOfHealthStatus: Int = getColumnIndexOrThrow(_stmt, "healthStatus")
        val _columnIndexOfHealthDescription: Int = getColumnIndexOrThrow(_stmt, "healthDescription")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfStory: Int = getColumnIndexOrThrow(_stmt, "story")
        val _columnIndexOfMonthlyGoalRupees: Int = getColumnIndexOrThrow(_stmt, "monthlyGoalRupees")
        val _columnIndexOfRaisedRupees: Int = getColumnIndexOrThrow(_stmt, "raisedRupees")
        val _columnIndexOfIsUrgent: Int = getColumnIndexOrThrow(_stmt, "isUrgent")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _result: AnimalResident?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGaushalaId: String
          _tmpGaushalaId = _stmt.getText(_columnIndexOfGaushalaId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpAgeStr: String
          _tmpAgeStr = _stmt.getText(_columnIndexOfAgeStr)
          val _tmpHealthStatus: String
          _tmpHealthStatus = _stmt.getText(_columnIndexOfHealthStatus)
          val _tmpHealthDescription: String
          _tmpHealthDescription = _stmt.getText(_columnIndexOfHealthDescription)
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpStory: String
          _tmpStory = _stmt.getText(_columnIndexOfStory)
          val _tmpMonthlyGoalRupees: Int
          _tmpMonthlyGoalRupees = _stmt.getLong(_columnIndexOfMonthlyGoalRupees).toInt()
          val _tmpRaisedRupees: Int
          _tmpRaisedRupees = _stmt.getLong(_columnIndexOfRaisedRupees).toInt()
          val _tmpIsUrgent: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsUrgent).toInt()
          _tmpIsUrgent = _tmp != 0
          val _tmpIsFavorite: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp_1 != 0
          _result =
              AnimalResident(_tmpId,_tmpGaushalaId,_tmpName,_tmpAgeStr,_tmpHealthStatus,_tmpHealthDescription,_tmpImageUrl,_tmpStory,_tmpMonthlyGoalRupees,_tmpRaisedRupees,_tmpIsUrgent,_tmpIsFavorite)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getUrgentAnimals(): Flow<List<AnimalResident>> {
    val _sql: String = "SELECT * FROM animal_residents WHERE isUrgent = 1"
    return createFlow(__db, false, arrayOf("animal_residents")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGaushalaId: Int = getColumnIndexOrThrow(_stmt, "gaushalaId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfAgeStr: Int = getColumnIndexOrThrow(_stmt, "ageStr")
        val _columnIndexOfHealthStatus: Int = getColumnIndexOrThrow(_stmt, "healthStatus")
        val _columnIndexOfHealthDescription: Int = getColumnIndexOrThrow(_stmt, "healthDescription")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfStory: Int = getColumnIndexOrThrow(_stmt, "story")
        val _columnIndexOfMonthlyGoalRupees: Int = getColumnIndexOrThrow(_stmt, "monthlyGoalRupees")
        val _columnIndexOfRaisedRupees: Int = getColumnIndexOrThrow(_stmt, "raisedRupees")
        val _columnIndexOfIsUrgent: Int = getColumnIndexOrThrow(_stmt, "isUrgent")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _result: MutableList<AnimalResident> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnimalResident
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGaushalaId: String
          _tmpGaushalaId = _stmt.getText(_columnIndexOfGaushalaId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpAgeStr: String
          _tmpAgeStr = _stmt.getText(_columnIndexOfAgeStr)
          val _tmpHealthStatus: String
          _tmpHealthStatus = _stmt.getText(_columnIndexOfHealthStatus)
          val _tmpHealthDescription: String
          _tmpHealthDescription = _stmt.getText(_columnIndexOfHealthDescription)
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpStory: String
          _tmpStory = _stmt.getText(_columnIndexOfStory)
          val _tmpMonthlyGoalRupees: Int
          _tmpMonthlyGoalRupees = _stmt.getLong(_columnIndexOfMonthlyGoalRupees).toInt()
          val _tmpRaisedRupees: Int
          _tmpRaisedRupees = _stmt.getLong(_columnIndexOfRaisedRupees).toInt()
          val _tmpIsUrgent: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsUrgent).toInt()
          _tmpIsUrgent = _tmp != 0
          val _tmpIsFavorite: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp_1 != 0
          _item =
              AnimalResident(_tmpId,_tmpGaushalaId,_tmpName,_tmpAgeStr,_tmpHealthStatus,_tmpHealthDescription,_tmpImageUrl,_tmpStory,_tmpMonthlyGoalRupees,_tmpRaisedRupees,_tmpIsUrgent,_tmpIsFavorite)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markSupported(id: String) {
    val _sql: String = "UPDATE gaushalas SET isSupported = 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun contributeToAnimal(id: String, amount: Int) {
    val _sql: String = "UPDATE animal_residents SET raisedRupees = raisedRupees + ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, amount.toLong())
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun toggleFavorite(id: String, isFav: Boolean) {
    val _sql: String = "UPDATE animal_residents SET isFavorite = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (isFav) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
