package com.example.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.`data`.model.Puja
import kotlin.Boolean
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
public class PujaDao_Impl(
  __db: RoomDatabase,
) : PujaDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPuja: EntityInsertAdapter<Puja>

  private val __updateAdapterOfPuja: EntityDeleteOrUpdateAdapter<Puja>
  init {
    this.__db = __db
    this.__insertAdapterOfPuja = object : EntityInsertAdapter<Puja>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `pujas` (`id`,`title`,`specialTag`,`templeName`,`location`,`dateTimeStr`,`durationStr`,`devoteesCount`,`priceRupees`,`imageUrl`,`significance`,`priestName`,`priestTitle`,`priestExp`,`priestImageUrl`,`category`,`isFeatured`,`isBooked`,`bookedGotra`,`bookedDevoteeName`,`bookedDate`,`isBookmarked`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Puja) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.specialTag)
        statement.bindText(4, entity.templeName)
        statement.bindText(5, entity.location)
        statement.bindText(6, entity.dateTimeStr)
        statement.bindText(7, entity.durationStr)
        statement.bindText(8, entity.devoteesCount)
        statement.bindLong(9, entity.priceRupees.toLong())
        statement.bindText(10, entity.imageUrl)
        statement.bindText(11, entity.significance)
        statement.bindText(12, entity.priestName)
        statement.bindText(13, entity.priestTitle)
        statement.bindText(14, entity.priestExp)
        statement.bindText(15, entity.priestImageUrl)
        statement.bindText(16, entity.category)
        val _tmp: Int = if (entity.isFeatured) 1 else 0
        statement.bindLong(17, _tmp.toLong())
        val _tmp_1: Int = if (entity.isBooked) 1 else 0
        statement.bindLong(18, _tmp_1.toLong())
        statement.bindText(19, entity.bookedGotra)
        statement.bindText(20, entity.bookedDevoteeName)
        statement.bindText(21, entity.bookedDate)
        val _tmp_2: Int = if (entity.isBookmarked) 1 else 0
        statement.bindLong(22, _tmp_2.toLong())
      }
    }
    this.__updateAdapterOfPuja = object : EntityDeleteOrUpdateAdapter<Puja>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `pujas` SET `id` = ?,`title` = ?,`specialTag` = ?,`templeName` = ?,`location` = ?,`dateTimeStr` = ?,`durationStr` = ?,`devoteesCount` = ?,`priceRupees` = ?,`imageUrl` = ?,`significance` = ?,`priestName` = ?,`priestTitle` = ?,`priestExp` = ?,`priestImageUrl` = ?,`category` = ?,`isFeatured` = ?,`isBooked` = ?,`bookedGotra` = ?,`bookedDevoteeName` = ?,`bookedDate` = ?,`isBookmarked` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Puja) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.specialTag)
        statement.bindText(4, entity.templeName)
        statement.bindText(5, entity.location)
        statement.bindText(6, entity.dateTimeStr)
        statement.bindText(7, entity.durationStr)
        statement.bindText(8, entity.devoteesCount)
        statement.bindLong(9, entity.priceRupees.toLong())
        statement.bindText(10, entity.imageUrl)
        statement.bindText(11, entity.significance)
        statement.bindText(12, entity.priestName)
        statement.bindText(13, entity.priestTitle)
        statement.bindText(14, entity.priestExp)
        statement.bindText(15, entity.priestImageUrl)
        statement.bindText(16, entity.category)
        val _tmp: Int = if (entity.isFeatured) 1 else 0
        statement.bindLong(17, _tmp.toLong())
        val _tmp_1: Int = if (entity.isBooked) 1 else 0
        statement.bindLong(18, _tmp_1.toLong())
        statement.bindText(19, entity.bookedGotra)
        statement.bindText(20, entity.bookedDevoteeName)
        statement.bindText(21, entity.bookedDate)
        val _tmp_2: Int = if (entity.isBookmarked) 1 else 0
        statement.bindLong(22, _tmp_2.toLong())
        statement.bindText(23, entity.id)
      }
    }
  }

  public override suspend fun insertPujas(pujas: List<Puja>): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfPuja.insert(_connection, pujas)
  }

  public override suspend fun updatePuja(puja: Puja): Unit = performSuspending(__db, false, true) {
      _connection ->
    __updateAdapterOfPuja.handle(_connection, puja)
  }

  public override fun getAllPujas(): Flow<List<Puja>> {
    val _sql: String = "SELECT * FROM pujas"
    return createFlow(__db, false, arrayOf("pujas")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSpecialTag: Int = getColumnIndexOrThrow(_stmt, "specialTag")
        val _columnIndexOfTempleName: Int = getColumnIndexOrThrow(_stmt, "templeName")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfDateTimeStr: Int = getColumnIndexOrThrow(_stmt, "dateTimeStr")
        val _columnIndexOfDurationStr: Int = getColumnIndexOrThrow(_stmt, "durationStr")
        val _columnIndexOfDevoteesCount: Int = getColumnIndexOrThrow(_stmt, "devoteesCount")
        val _columnIndexOfPriceRupees: Int = getColumnIndexOrThrow(_stmt, "priceRupees")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfSignificance: Int = getColumnIndexOrThrow(_stmt, "significance")
        val _columnIndexOfPriestName: Int = getColumnIndexOrThrow(_stmt, "priestName")
        val _columnIndexOfPriestTitle: Int = getColumnIndexOrThrow(_stmt, "priestTitle")
        val _columnIndexOfPriestExp: Int = getColumnIndexOrThrow(_stmt, "priestExp")
        val _columnIndexOfPriestImageUrl: Int = getColumnIndexOrThrow(_stmt, "priestImageUrl")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfIsFeatured: Int = getColumnIndexOrThrow(_stmt, "isFeatured")
        val _columnIndexOfIsBooked: Int = getColumnIndexOrThrow(_stmt, "isBooked")
        val _columnIndexOfBookedGotra: Int = getColumnIndexOrThrow(_stmt, "bookedGotra")
        val _columnIndexOfBookedDevoteeName: Int = getColumnIndexOrThrow(_stmt, "bookedDevoteeName")
        val _columnIndexOfBookedDate: Int = getColumnIndexOrThrow(_stmt, "bookedDate")
        val _columnIndexOfIsBookmarked: Int = getColumnIndexOrThrow(_stmt, "isBookmarked")
        val _result: MutableList<Puja> = mutableListOf()
        while (_stmt.step()) {
          val _item: Puja
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSpecialTag: String
          _tmpSpecialTag = _stmt.getText(_columnIndexOfSpecialTag)
          val _tmpTempleName: String
          _tmpTempleName = _stmt.getText(_columnIndexOfTempleName)
          val _tmpLocation: String
          _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          val _tmpDateTimeStr: String
          _tmpDateTimeStr = _stmt.getText(_columnIndexOfDateTimeStr)
          val _tmpDurationStr: String
          _tmpDurationStr = _stmt.getText(_columnIndexOfDurationStr)
          val _tmpDevoteesCount: String
          _tmpDevoteesCount = _stmt.getText(_columnIndexOfDevoteesCount)
          val _tmpPriceRupees: Int
          _tmpPriceRupees = _stmt.getLong(_columnIndexOfPriceRupees).toInt()
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpSignificance: String
          _tmpSignificance = _stmt.getText(_columnIndexOfSignificance)
          val _tmpPriestName: String
          _tmpPriestName = _stmt.getText(_columnIndexOfPriestName)
          val _tmpPriestTitle: String
          _tmpPriestTitle = _stmt.getText(_columnIndexOfPriestTitle)
          val _tmpPriestExp: String
          _tmpPriestExp = _stmt.getText(_columnIndexOfPriestExp)
          val _tmpPriestImageUrl: String
          _tmpPriestImageUrl = _stmt.getText(_columnIndexOfPriestImageUrl)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpIsFeatured: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFeatured).toInt()
          _tmpIsFeatured = _tmp != 0
          val _tmpIsBooked: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsBooked).toInt()
          _tmpIsBooked = _tmp_1 != 0
          val _tmpBookedGotra: String
          _tmpBookedGotra = _stmt.getText(_columnIndexOfBookedGotra)
          val _tmpBookedDevoteeName: String
          _tmpBookedDevoteeName = _stmt.getText(_columnIndexOfBookedDevoteeName)
          val _tmpBookedDate: String
          _tmpBookedDate = _stmt.getText(_columnIndexOfBookedDate)
          val _tmpIsBookmarked: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsBookmarked).toInt()
          _tmpIsBookmarked = _tmp_2 != 0
          _item =
              Puja(_tmpId,_tmpTitle,_tmpSpecialTag,_tmpTempleName,_tmpLocation,_tmpDateTimeStr,_tmpDurationStr,_tmpDevoteesCount,_tmpPriceRupees,_tmpImageUrl,_tmpSignificance,_tmpPriestName,_tmpPriestTitle,_tmpPriestExp,_tmpPriestImageUrl,_tmpCategory,_tmpIsFeatured,_tmpIsBooked,_tmpBookedGotra,_tmpBookedDevoteeName,_tmpBookedDate,_tmpIsBookmarked)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getPujaById(id: String): Flow<Puja?> {
    val _sql: String = "SELECT * FROM pujas WHERE id = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("pujas")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSpecialTag: Int = getColumnIndexOrThrow(_stmt, "specialTag")
        val _columnIndexOfTempleName: Int = getColumnIndexOrThrow(_stmt, "templeName")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfDateTimeStr: Int = getColumnIndexOrThrow(_stmt, "dateTimeStr")
        val _columnIndexOfDurationStr: Int = getColumnIndexOrThrow(_stmt, "durationStr")
        val _columnIndexOfDevoteesCount: Int = getColumnIndexOrThrow(_stmt, "devoteesCount")
        val _columnIndexOfPriceRupees: Int = getColumnIndexOrThrow(_stmt, "priceRupees")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfSignificance: Int = getColumnIndexOrThrow(_stmt, "significance")
        val _columnIndexOfPriestName: Int = getColumnIndexOrThrow(_stmt, "priestName")
        val _columnIndexOfPriestTitle: Int = getColumnIndexOrThrow(_stmt, "priestTitle")
        val _columnIndexOfPriestExp: Int = getColumnIndexOrThrow(_stmt, "priestExp")
        val _columnIndexOfPriestImageUrl: Int = getColumnIndexOrThrow(_stmt, "priestImageUrl")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfIsFeatured: Int = getColumnIndexOrThrow(_stmt, "isFeatured")
        val _columnIndexOfIsBooked: Int = getColumnIndexOrThrow(_stmt, "isBooked")
        val _columnIndexOfBookedGotra: Int = getColumnIndexOrThrow(_stmt, "bookedGotra")
        val _columnIndexOfBookedDevoteeName: Int = getColumnIndexOrThrow(_stmt, "bookedDevoteeName")
        val _columnIndexOfBookedDate: Int = getColumnIndexOrThrow(_stmt, "bookedDate")
        val _columnIndexOfIsBookmarked: Int = getColumnIndexOrThrow(_stmt, "isBookmarked")
        val _result: Puja?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSpecialTag: String
          _tmpSpecialTag = _stmt.getText(_columnIndexOfSpecialTag)
          val _tmpTempleName: String
          _tmpTempleName = _stmt.getText(_columnIndexOfTempleName)
          val _tmpLocation: String
          _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          val _tmpDateTimeStr: String
          _tmpDateTimeStr = _stmt.getText(_columnIndexOfDateTimeStr)
          val _tmpDurationStr: String
          _tmpDurationStr = _stmt.getText(_columnIndexOfDurationStr)
          val _tmpDevoteesCount: String
          _tmpDevoteesCount = _stmt.getText(_columnIndexOfDevoteesCount)
          val _tmpPriceRupees: Int
          _tmpPriceRupees = _stmt.getLong(_columnIndexOfPriceRupees).toInt()
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpSignificance: String
          _tmpSignificance = _stmt.getText(_columnIndexOfSignificance)
          val _tmpPriestName: String
          _tmpPriestName = _stmt.getText(_columnIndexOfPriestName)
          val _tmpPriestTitle: String
          _tmpPriestTitle = _stmt.getText(_columnIndexOfPriestTitle)
          val _tmpPriestExp: String
          _tmpPriestExp = _stmt.getText(_columnIndexOfPriestExp)
          val _tmpPriestImageUrl: String
          _tmpPriestImageUrl = _stmt.getText(_columnIndexOfPriestImageUrl)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpIsFeatured: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFeatured).toInt()
          _tmpIsFeatured = _tmp != 0
          val _tmpIsBooked: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsBooked).toInt()
          _tmpIsBooked = _tmp_1 != 0
          val _tmpBookedGotra: String
          _tmpBookedGotra = _stmt.getText(_columnIndexOfBookedGotra)
          val _tmpBookedDevoteeName: String
          _tmpBookedDevoteeName = _stmt.getText(_columnIndexOfBookedDevoteeName)
          val _tmpBookedDate: String
          _tmpBookedDate = _stmt.getText(_columnIndexOfBookedDate)
          val _tmpIsBookmarked: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsBookmarked).toInt()
          _tmpIsBookmarked = _tmp_2 != 0
          _result =
              Puja(_tmpId,_tmpTitle,_tmpSpecialTag,_tmpTempleName,_tmpLocation,_tmpDateTimeStr,_tmpDurationStr,_tmpDevoteesCount,_tmpPriceRupees,_tmpImageUrl,_tmpSignificance,_tmpPriestName,_tmpPriestTitle,_tmpPriestExp,_tmpPriestImageUrl,_tmpCategory,_tmpIsFeatured,_tmpIsBooked,_tmpBookedGotra,_tmpBookedDevoteeName,_tmpBookedDate,_tmpIsBookmarked)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getBookedPujas(): Flow<List<Puja>> {
    val _sql: String = "SELECT * FROM pujas WHERE isBooked = 1 ORDER BY dateTimeStr ASC"
    return createFlow(__db, false, arrayOf("pujas")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSpecialTag: Int = getColumnIndexOrThrow(_stmt, "specialTag")
        val _columnIndexOfTempleName: Int = getColumnIndexOrThrow(_stmt, "templeName")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfDateTimeStr: Int = getColumnIndexOrThrow(_stmt, "dateTimeStr")
        val _columnIndexOfDurationStr: Int = getColumnIndexOrThrow(_stmt, "durationStr")
        val _columnIndexOfDevoteesCount: Int = getColumnIndexOrThrow(_stmt, "devoteesCount")
        val _columnIndexOfPriceRupees: Int = getColumnIndexOrThrow(_stmt, "priceRupees")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfSignificance: Int = getColumnIndexOrThrow(_stmt, "significance")
        val _columnIndexOfPriestName: Int = getColumnIndexOrThrow(_stmt, "priestName")
        val _columnIndexOfPriestTitle: Int = getColumnIndexOrThrow(_stmt, "priestTitle")
        val _columnIndexOfPriestExp: Int = getColumnIndexOrThrow(_stmt, "priestExp")
        val _columnIndexOfPriestImageUrl: Int = getColumnIndexOrThrow(_stmt, "priestImageUrl")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfIsFeatured: Int = getColumnIndexOrThrow(_stmt, "isFeatured")
        val _columnIndexOfIsBooked: Int = getColumnIndexOrThrow(_stmt, "isBooked")
        val _columnIndexOfBookedGotra: Int = getColumnIndexOrThrow(_stmt, "bookedGotra")
        val _columnIndexOfBookedDevoteeName: Int = getColumnIndexOrThrow(_stmt, "bookedDevoteeName")
        val _columnIndexOfBookedDate: Int = getColumnIndexOrThrow(_stmt, "bookedDate")
        val _columnIndexOfIsBookmarked: Int = getColumnIndexOrThrow(_stmt, "isBookmarked")
        val _result: MutableList<Puja> = mutableListOf()
        while (_stmt.step()) {
          val _item: Puja
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSpecialTag: String
          _tmpSpecialTag = _stmt.getText(_columnIndexOfSpecialTag)
          val _tmpTempleName: String
          _tmpTempleName = _stmt.getText(_columnIndexOfTempleName)
          val _tmpLocation: String
          _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          val _tmpDateTimeStr: String
          _tmpDateTimeStr = _stmt.getText(_columnIndexOfDateTimeStr)
          val _tmpDurationStr: String
          _tmpDurationStr = _stmt.getText(_columnIndexOfDurationStr)
          val _tmpDevoteesCount: String
          _tmpDevoteesCount = _stmt.getText(_columnIndexOfDevoteesCount)
          val _tmpPriceRupees: Int
          _tmpPriceRupees = _stmt.getLong(_columnIndexOfPriceRupees).toInt()
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpSignificance: String
          _tmpSignificance = _stmt.getText(_columnIndexOfSignificance)
          val _tmpPriestName: String
          _tmpPriestName = _stmt.getText(_columnIndexOfPriestName)
          val _tmpPriestTitle: String
          _tmpPriestTitle = _stmt.getText(_columnIndexOfPriestTitle)
          val _tmpPriestExp: String
          _tmpPriestExp = _stmt.getText(_columnIndexOfPriestExp)
          val _tmpPriestImageUrl: String
          _tmpPriestImageUrl = _stmt.getText(_columnIndexOfPriestImageUrl)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpIsFeatured: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFeatured).toInt()
          _tmpIsFeatured = _tmp != 0
          val _tmpIsBooked: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsBooked).toInt()
          _tmpIsBooked = _tmp_1 != 0
          val _tmpBookedGotra: String
          _tmpBookedGotra = _stmt.getText(_columnIndexOfBookedGotra)
          val _tmpBookedDevoteeName: String
          _tmpBookedDevoteeName = _stmt.getText(_columnIndexOfBookedDevoteeName)
          val _tmpBookedDate: String
          _tmpBookedDate = _stmt.getText(_columnIndexOfBookedDate)
          val _tmpIsBookmarked: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsBookmarked).toInt()
          _tmpIsBookmarked = _tmp_2 != 0
          _item =
              Puja(_tmpId,_tmpTitle,_tmpSpecialTag,_tmpTempleName,_tmpLocation,_tmpDateTimeStr,_tmpDurationStr,_tmpDevoteesCount,_tmpPriceRupees,_tmpImageUrl,_tmpSignificance,_tmpPriestName,_tmpPriestTitle,_tmpPriestExp,_tmpPriestImageUrl,_tmpCategory,_tmpIsFeatured,_tmpIsBooked,_tmpBookedGotra,_tmpBookedDevoteeName,_tmpBookedDate,_tmpIsBookmarked)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun bookPuja(
    id: String,
    gotra: String,
    name: String,
    date: String,
  ) {
    val _sql: String =
        "UPDATE pujas SET isBooked = 1, bookedGotra = ?, bookedDevoteeName = ?, bookedDate = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gotra)
        _argIndex = 2
        _stmt.bindText(_argIndex, name)
        _argIndex = 3
        _stmt.bindText(_argIndex, date)
        _argIndex = 4
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun toggleBookmark(id: String, isBookmarked: Boolean) {
    val _sql: String = "UPDATE pujas SET isBookmarked = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (isBookmarked) 1 else 0
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
