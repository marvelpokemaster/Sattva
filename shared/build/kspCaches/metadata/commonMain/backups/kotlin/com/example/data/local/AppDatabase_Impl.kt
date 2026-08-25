package com.example.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _pujaDao: Lazy<PujaDao> = lazy {
    PujaDao_Impl(this)
  }

  private val _gaushalaDao: Lazy<GaushalaDao> = lazy {
    GaushalaDao_Impl(this)
  }

  private val _sevaDao: Lazy<SevaDao> = lazy {
    SevaDao_Impl(this)
  }

  private val _userProfileDao: Lazy<UserProfileDao> = lazy {
    UserProfileDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "801accc0a3d79fd498de36146363cc3c", "04cbb4916fb225f138f8625abf9803da") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `pujas` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `specialTag` TEXT NOT NULL, `templeName` TEXT NOT NULL, `location` TEXT NOT NULL, `dateTimeStr` TEXT NOT NULL, `durationStr` TEXT NOT NULL, `devoteesCount` TEXT NOT NULL, `priceRupees` INTEGER NOT NULL, `imageUrl` TEXT NOT NULL, `significance` TEXT NOT NULL, `priestName` TEXT NOT NULL, `priestTitle` TEXT NOT NULL, `priestExp` TEXT NOT NULL, `priestImageUrl` TEXT NOT NULL, `category` TEXT NOT NULL, `isFeatured` INTEGER NOT NULL, `isBooked` INTEGER NOT NULL, `bookedGotra` TEXT NOT NULL, `bookedDevoteeName` TEXT NOT NULL, `bookedDate` TEXT NOT NULL, `isBookmarked` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `gaushalas` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `location` TEXT NOT NULL, `state` TEXT NOT NULL, `trustScorePercent` INTEGER NOT NULL, `animalsRescuedCount` INTEGER NOT NULL, `transparencyTier` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `missionQuote` TEXT NOT NULL, `fodderPercent` INTEGER NOT NULL, `medicalPercent` INTEGER NOT NULL, `shelterPercent` INTEGER NOT NULL, `lat` REAL NOT NULL, `lng` REAL NOT NULL, `isSupported` INTEGER NOT NULL, `updatesCount` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `animal_residents` (`id` TEXT NOT NULL, `gaushalaId` TEXT NOT NULL, `name` TEXT NOT NULL, `ageStr` TEXT NOT NULL, `healthStatus` TEXT NOT NULL, `healthDescription` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `story` TEXT NOT NULL, `monthlyGoalRupees` INTEGER NOT NULL, `raisedRupees` INTEGER NOT NULL, `isUrgent` INTEGER NOT NULL, `isFavorite` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `seva_contributions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `targetType` TEXT NOT NULL, `targetName` TEXT NOT NULL, `amountRupees` INTEGER NOT NULL, `sevaCategory` TEXT NOT NULL, `dateStr` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `location` TEXT NOT NULL, `gotra` TEXT NOT NULL, `nakshatra` TEXT NOT NULL, `rashi` TEXT NOT NULL, `avatarUrl` TEXT NOT NULL, `pujasCount` INTEGER NOT NULL, `animalsCount` INTEGER NOT NULL, `totalContributedRupees` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '801accc0a3d79fd498de36146363cc3c')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `pujas`")
        connection.execSQL("DROP TABLE IF EXISTS `gaushalas`")
        connection.execSQL("DROP TABLE IF EXISTS `animal_residents`")
        connection.execSQL("DROP TABLE IF EXISTS `seva_contributions`")
        connection.execSQL("DROP TABLE IF EXISTS `user_profile`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsPujas: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPujas.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("specialTag", TableInfo.Column("specialTag", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("templeName", TableInfo.Column("templeName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("location", TableInfo.Column("location", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("dateTimeStr", TableInfo.Column("dateTimeStr", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("durationStr", TableInfo.Column("durationStr", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("devoteesCount", TableInfo.Column("devoteesCount", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("priceRupees", TableInfo.Column("priceRupees", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("imageUrl", TableInfo.Column("imageUrl", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("significance", TableInfo.Column("significance", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("priestName", TableInfo.Column("priestName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("priestTitle", TableInfo.Column("priestTitle", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("priestExp", TableInfo.Column("priestExp", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("priestImageUrl", TableInfo.Column("priestImageUrl", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("category", TableInfo.Column("category", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("isFeatured", TableInfo.Column("isFeatured", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("isBooked", TableInfo.Column("isBooked", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("bookedGotra", TableInfo.Column("bookedGotra", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("bookedDevoteeName", TableInfo.Column("bookedDevoteeName", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("bookedDate", TableInfo.Column("bookedDate", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPujas.put("isBookmarked", TableInfo.Column("isBookmarked", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPujas: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPujas: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPujas: TableInfo = TableInfo("pujas", _columnsPujas, _foreignKeysPujas,
            _indicesPujas)
        val _existingPujas: TableInfo = read(connection, "pujas")
        if (!_infoPujas.equals(_existingPujas)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |pujas(com.example.data.model.Puja).
              | Expected:
              |""".trimMargin() + _infoPujas + """
              |
              | Found:
              |""".trimMargin() + _existingPujas)
        }
        val _columnsGaushalas: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsGaushalas.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("location", TableInfo.Column("location", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("state", TableInfo.Column("state", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("trustScorePercent", TableInfo.Column("trustScorePercent", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("animalsRescuedCount", TableInfo.Column("animalsRescuedCount",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("transparencyTier", TableInfo.Column("transparencyTier", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("imageUrl", TableInfo.Column("imageUrl", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("missionQuote", TableInfo.Column("missionQuote", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("fodderPercent", TableInfo.Column("fodderPercent", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("medicalPercent", TableInfo.Column("medicalPercent", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("shelterPercent", TableInfo.Column("shelterPercent", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("lat", TableInfo.Column("lat", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("lng", TableInfo.Column("lng", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("isSupported", TableInfo.Column("isSupported", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGaushalas.put("updatesCount", TableInfo.Column("updatesCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysGaushalas: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesGaushalas: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoGaushalas: TableInfo = TableInfo("gaushalas", _columnsGaushalas,
            _foreignKeysGaushalas, _indicesGaushalas)
        val _existingGaushalas: TableInfo = read(connection, "gaushalas")
        if (!_infoGaushalas.equals(_existingGaushalas)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |gaushalas(com.example.data.model.Gaushala).
              | Expected:
              |""".trimMargin() + _infoGaushalas + """
              |
              | Found:
              |""".trimMargin() + _existingGaushalas)
        }
        val _columnsAnimalResidents: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAnimalResidents.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("gaushalaId", TableInfo.Column("gaushalaId", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("ageStr", TableInfo.Column("ageStr", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("healthStatus", TableInfo.Column("healthStatus", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("healthDescription", TableInfo.Column("healthDescription",
            "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("imageUrl", TableInfo.Column("imageUrl", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("story", TableInfo.Column("story", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("monthlyGoalRupees", TableInfo.Column("monthlyGoalRupees",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("raisedRupees", TableInfo.Column("raisedRupees", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("isUrgent", TableInfo.Column("isUrgent", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAnimalResidents.put("isFavorite", TableInfo.Column("isFavorite", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAnimalResidents: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAnimalResidents: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoAnimalResidents: TableInfo = TableInfo("animal_residents", _columnsAnimalResidents,
            _foreignKeysAnimalResidents, _indicesAnimalResidents)
        val _existingAnimalResidents: TableInfo = read(connection, "animal_residents")
        if (!_infoAnimalResidents.equals(_existingAnimalResidents)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |animal_residents(com.example.data.model.AnimalResident).
              | Expected:
              |""".trimMargin() + _infoAnimalResidents + """
              |
              | Found:
              |""".trimMargin() + _existingAnimalResidents)
        }
        val _columnsSevaContributions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSevaContributions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSevaContributions.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSevaContributions.put("targetType", TableInfo.Column("targetType", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSevaContributions.put("targetName", TableInfo.Column("targetName", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSevaContributions.put("amountRupees", TableInfo.Column("amountRupees", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSevaContributions.put("sevaCategory", TableInfo.Column("sevaCategory", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSevaContributions.put("dateStr", TableInfo.Column("dateStr", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSevaContributions.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSevaContributions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSevaContributions: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSevaContributions: TableInfo = TableInfo("seva_contributions",
            _columnsSevaContributions, _foreignKeysSevaContributions, _indicesSevaContributions)
        val _existingSevaContributions: TableInfo = read(connection, "seva_contributions")
        if (!_infoSevaContributions.equals(_existingSevaContributions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |seva_contributions(com.example.data.model.SevaContribution).
              | Expected:
              |""".trimMargin() + _infoSevaContributions + """
              |
              | Found:
              |""".trimMargin() + _existingSevaContributions)
        }
        val _columnsUserProfile: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUserProfile.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("location", TableInfo.Column("location", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("gotra", TableInfo.Column("gotra", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("nakshatra", TableInfo.Column("nakshatra", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("rashi", TableInfo.Column("rashi", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("avatarUrl", TableInfo.Column("avatarUrl", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("pujasCount", TableInfo.Column("pujasCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("animalsCount", TableInfo.Column("animalsCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("totalContributedRupees", TableInfo.Column("totalContributedRupees",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUserProfile: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUserProfile: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUserProfile: TableInfo = TableInfo("user_profile", _columnsUserProfile,
            _foreignKeysUserProfile, _indicesUserProfile)
        val _existingUserProfile: TableInfo = read(connection, "user_profile")
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |user_profile(com.example.data.model.UserProfile).
              | Expected:
              |""".trimMargin() + _infoUserProfile + """
              |
              | Found:
              |""".trimMargin() + _existingUserProfile)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "pujas", "gaushalas",
        "animal_residents", "seva_contributions", "user_profile")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(PujaDao::class, PujaDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(GaushalaDao::class, GaushalaDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SevaDao::class, SevaDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UserProfileDao::class, UserProfileDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun pujaDao(): PujaDao = _pujaDao.value

  public override fun gaushalaDao(): GaushalaDao = _gaushalaDao.value

  public override fun sevaDao(): SevaDao = _sevaDao.value

  public override fun userProfileDao(): UserProfileDao = _userProfileDao.value
}
