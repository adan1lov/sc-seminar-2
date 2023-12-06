package ru.hse.bank.dao

import ru.hse.bank.entity.AccountEntity
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Paths
import kotlin.io.path.Path


class FileSystemAccountDao : AccountDao {

    companion object {
        private val OSpath = Paths.get("").toAbsolutePath().toString()
        private val DIR_WITH_ACCOUNTS = OSpath + "/src/main/kotlin/ru/hse/bank/accounts"
    }

    override fun findAccountByName(name: String): AccountEntity {
        val file = Path(DIR_WITH_ACCOUNTS, name).toFile()
        val file_sum : Long

        if (!file.exists()) {
            throw Exception("No accounts with this name")
        }

        val lines = file.bufferedReader().readLines()
        file_sum = lines[0].toLong()
        return AccountEntity(name, file_sum)
    }

    override fun increaseAccountSumByName(name: String, sum: Long): AccountEntity {
        val file = Path(DIR_WITH_ACCOUNTS, name).toFile()
        var file_sum : Long

        if (file.exists()) {
            val lines = file.bufferedReader().readLines()
            file_sum = lines[0].toLong()
            file_sum += sum
            file.delete()
        } else {
            throw Exception("No accounts with this name")
        }

        val isFileCreated : Boolean = file.createNewFile()
        file.writeText("${file_sum}", Charset.defaultCharset())
        return AccountEntity(name, file_sum)
    }

    override fun saveAccount(accountEntity: AccountEntity): Boolean {
        val file = Path(DIR_WITH_ACCOUNTS, accountEntity.name).toFile()

        if (file.exists()) {
            file.delete()
        }

        val isFileCreated : Boolean = file.createNewFile()
        file.writeText("${accountEntity.sum}", Charset.defaultCharset())

        return file.exists()
    }

    override fun transfer(nameFrom: String, nameTo: String, sum: Long)
    : Pair<AccountEntity, AccountEntity> {
        return transfer(findAccountByName(nameFrom), findAccountByName(nameTo), sum)
    }

    override fun transfer(accountEntityFrom: AccountEntity, accountEntityTo: AccountEntity, sum: Long)
    : Pair<AccountEntity, AccountEntity> {
        var newAccountEntityFrom : AccountEntity
        var newAccountEntityTo : AccountEntity

        try {
            newAccountEntityFrom = increaseAccountSumByName(accountEntityFrom.name, -sum)
        } catch (e : Exception) {
            throw Exception("not enough balance")
        }

        try {
            newAccountEntityTo = increaseAccountSumByName(accountEntityTo.name, sum)
        } catch (e:Exception) {
            newAccountEntityFrom = increaseAccountSumByName(accountEntityFrom.name, sum)
            throw Exception("something goes wrong")
        }

        val logFileFrom = Path(DIR_WITH_ACCOUNTS, accountEntityFrom.name + "Log").toFile()
        val logFileTo = Path(DIR_WITH_ACCOUNTS, accountEntityTo.name + "Log").toFile()
        val allLog = Path(DIR_WITH_ACCOUNTS, "transaction").toFile()

        if (!logFileFrom.exists()) {
            logFileFrom.createNewFile()
        }

        if (!logFileTo.exists()) {
            logFileTo.createNewFile()
        }

        if (!allLog.exists()) {
            allLog.createNewFile()
        }

        logFileFrom.appendText("Send ${sum} ${newAccountEntityTo.name}\n")
        logFileTo.appendText("Get ${sum} ${newAccountEntityFrom.name}\n")
        allLog.appendText("Transaction ${sum} ${newAccountEntityFrom.name} ${newAccountEntityTo.name}\n")

        return Pair(newAccountEntityFrom, newAccountEntityTo)
    }
}
